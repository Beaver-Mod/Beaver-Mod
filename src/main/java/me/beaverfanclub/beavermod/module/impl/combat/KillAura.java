/*
 * This file is apart of Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>
 * Copyright (C) 2024  Beaver Fan Club
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.beaverfanclub.beavermod.module.impl.combat;

import io.netty.util.internal.ThreadLocalRandom;
import me.beaverfanclub.beavermod.event.PreMotionEvent;
import me.beaverfanclub.beavermod.event.SendPacketEvent;
import me.beaverfanclub.beavermod.module.Module;
import me.beaverfanclub.beavermod.module.setting.impl.*;
import me.beaverfanclub.beavermod.module.setting.util.IChanged;
import me.beaverfanclub.beavermod.util.minecraft.PacketUtil;
import me.beaverfanclub.beavermod.util.minecraft.PlayerUtils;
import me.beaverfanclub.beavermod.util.minecraft.entity.EntityManager;
import me.beaverfanclub.beavermod.util.misc.AccessWidenerReflect;
import me.beaverfanclub.beavermod.util.misc.Clock;
import me.beaverfanclub.beavermod.util.minecraft.entity.SkyBlockEntity;
import me.beaverfanclub.beavermod.util.misc.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KillAura extends Module {

    private final EnumSetting<TargetMode> targetMode = new EnumSetting<>("Target Mode", "How to select targets", TargetMode.SINGLE);
    private final EnumSetting<TargetSorting> targetSorting = new EnumSetting<>("Target Sorting", "Which entities to prioritize", TargetSorting.DISTANCE);
    private final IntSetting switchDelay = new IntSetting("Switch Delay", "Delay between switching targets in switch mode", 0, 5000, 250, "%dms", null);
    private final BooleanSetting noFilter = new BooleanSetting("No Filter", "Disable the attack filter (for debugging purposes)", false);

    private final FloatSetting minAps = new FloatSetting("Min Attack Speed", "Minimum attack speed", 0, 20, 9, new DecimalFormat("#0.## APS"), new IChanged() {
        @Override
        public void onChanged() {
            if (minAps.get() > maxAps.get()) {
                maxAps.set(minAps.get());
            }
        }
    });

    private final FloatSetting maxAps = new FloatSetting("Max Attack Speed", "Minimum attack speed", 0, 20, 12, new DecimalFormat("#0.## APS"), new IChanged() {
        @Override
        public void onChanged() {
            if (maxAps.get() < minAps.get()) {
                minAps.set(maxAps.get());
            }
        }
    });

    private final FloatSetting range = new FloatSetting("Range", "Range to attack entities, attack packets won't be sent over 3 blocks", 0, 6, 3.5F);
    private final BooleanSetting noSwing = new BooleanSetting("No Swing", "Won't swing your arm when attacking (client side only)", false);

    private final FloatSetting rotationSmoothing = new FloatSetting("Smoothing", "Rotation smoothing", 0.0F, 100.0F, 20.0F, new DecimalFormat("#0.##%"), null);

    private final BooleanSetting autoDisable = new BooleanSetting("Auto Disable", "Disables Kill Aura when dying, joining a new lobby or teleporting", true);

    private final List<SkyBlockEntity> entities = new ArrayList<>();
    private Entity target = null;

    private float yaw, pitch;

    private final Clock attackTimer = new Clock();
    private long attackTime = 0L;
    private boolean cancelAttack = false;

    public KillAura() {
        super("Kill Aura", "Automatically attacks entities", Category.COMBAT);
        addSettings(
                new SeperatorSetting("Targets"),
                targetMode, targetSorting, switchDelay, noFilter,

                new SeperatorSetting("Attack Settings"),
                minAps, maxAps, range, noSwing,

                new SeperatorSetting("Rotations"),
                rotationSmoothing,

                new SeperatorSetting("QOL"),
                autoDisable
        );
    }

    @Override
    public void onEnabled() {
        yaw = mc.thePlayer.rotationYaw;
        pitch = mc.thePlayer.rotationPitch;
        attackTimer.reset();
        attackTime = (long)(1000.0 / (minAps.get().equals(maxAps.get()) ? minAps.get() : ThreadLocalRandom.current().nextDouble(minAps.get(), maxAps.get())));
        cancelAttack = false;
    }

    @SubscribeEvent
    public void onPreMotion(PreMotionEvent event) {
        selectTarget();
        rotate(event);
        attack();
    }

    @SubscribeEvent
    public void onSendPacket(SendPacketEvent event) {
        if (event.getPacket() instanceof C07PacketPlayerDigging) {
            cancelAttack = true;
        }
    }

    public void rotate(PreMotionEvent event) {
        float targetYaw = mc.thePlayer.rotationYaw;
        float targetPitch = mc.thePlayer.rotationPitch;

        if (target != null) {
            float[] rotationsToTarget = PlayerUtils.getRotations(target.posX, target.posY + target.getEyeHeight() - 0.4, target.posZ);
            targetYaw = rotationsToTarget[0];
            targetPitch = rotationsToTarget[1];
        }

        yaw = MathUtil.lerp(yaw, targetYaw, 1.0F - (rotationSmoothing.get() / 100.0F));
        pitch = MathUtil.lerp(pitch, targetPitch, 1.0F - (rotationSmoothing.get() / 100.0F));

        event.yaw = yaw;
        event.pitch = pitch;

    }

    public void attack() {
        try {
            if (target != null && attackTimer.hasReached(attackTime) && !cancelAttack && !mc.thePlayer.isUsingItem() && AccessWidenerReflect.curBlockDamageMP.getFloat(mc.playerController) == 0.0F) {

                if (noSwing.get()) {
                    PacketUtil.send(new C0APacketAnimation());
                } else {
                    mc.thePlayer.swingItem();
                }

                if (MathUtil.hitboxCheck(mc.thePlayer.getPositionVector().addVector(0.0F, mc.thePlayer.getEyeHeight(), 0.0F), target.getEntityBoundingBox(), yaw, pitch, 3.0F)) {
                    PacketUtil.send(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                }

                attackTimer.reset();
                attackTime = (long) (1000.0 / (minAps.get().equals(maxAps.get()) ? minAps.get() : ThreadLocalRandom.current().nextDouble(minAps.get(), maxAps.get())));
            }

            cancelAttack = false;
        } catch (IllegalAccessException | IllegalArgumentException ignored) {}
    }

    public void selectTarget() {

        if (noFilter.get()) {
            target = mc.theWorld.getEntities(EntityLivingBase.class, Objects::nonNull)
                    .stream()
                    .filter(entity -> entity != mc.thePlayer && mc.thePlayer.getDistanceToEntity(entity) <= range.get())
                    .min((o1, o2) -> Float.compare(mc.thePlayer.getDistanceToEntity(o1), mc.thePlayer.getDistanceToEntity(o2)))
                    .orElse(null);

            return;
        }

        target = null;
        entities.clear();

        for (EntityLivingBase entity : mc.theWorld.getEntities(EntityLivingBase.class, Objects::nonNull)) {
            if (entity == mc.thePlayer) continue;
            SkyBlockEntity skyBlockEntity = EntityManager.getSkyBlockEntity(entity);
            if (skyBlockEntity != null && skyBlockEntity.type == SkyBlockEntity.Type.MOB) {
                entities.add(skyBlockEntity);
            }
        }

        entities.stream()
                .filter(skyBlockEntity -> mc.thePlayer.getDistanceToEntity(skyBlockEntity.entity) <= range.get())
                .min((o1, o2) -> {
                    switch (targetSorting.get()) {

                        case DISTANCE:
                            return Float.compare(mc.thePlayer.getDistanceToEntity(o1.entity), mc.thePlayer.getDistanceToEntity(o2.entity));

                        case LEVEL:
                            return Integer.compare(o1.getLevel(), o2.getLevel());

                        case HEALTH:
                            return Integer.compare(o1.getHealth(), o2.getHealth());

                        case MAX_HEALTH:
                            return Integer.compare(o1.getMaxHealth(), o2.getMaxHealth());
                    }

                    return 0;
                })
                .ifPresent(selected -> target = selected.entity);

    }

    public enum TargetMode {
        SINGLE,
        SWITCH,
        PRIORITY,
        FLOOR
    }

    public enum TargetSorting {
        DISTANCE,
        HEALTH,
        MAX_HEALTH,
        LEVEL
    }

}
