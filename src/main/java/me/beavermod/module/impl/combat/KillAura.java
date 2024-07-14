/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.module.impl.combat;

import me.beavermod.event.PreMotionEvent;
import me.beavermod.module.Module;
import me.beavermod.module.setting.impl.*;
import me.beavermod.module.setting.util.IChanged;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

public class KillAura extends Module {

    private final EnumSetting<TargetMode> targetMode = new EnumSetting<>("Target Mode", "How to select targets", TargetMode.SINGLE);
    private final EnumSetting<TargetSorting> targetSorting = new EnumSetting<>("Target Sorting", "Which entities to prioritize", TargetSorting.DISTANCE);
    private final IntSetting switchDelay = new IntSetting("Switch Delay", "Delay between switching targets in switch mode", 0, 5000, 250, "%dms", null);

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

    private final BooleanSetting autoDisable = new BooleanSetting("Auto Disable", "Disables Kill Aura when dying or joining a new lobby", true);

    private Entity target = null;

    public KillAura() {
        super("Kill Aura", "Automatically attacks entities", Category.COMBAT);
        addSettings(
                new SeperatorSetting("Targets"),
                targetMode, targetSorting, switchDelay,

                new SeperatorSetting("Attack Settings"),
                minAps, maxAps, range, noSwing,

                new SeperatorSetting("QOL"),
                autoDisable
        );
    }

    @SubscribeEvent
    public void onPreMotion(PreMotionEvent event) {

    }

    public void rotate(PreMotionEvent event) {

    }

    public enum TargetMode {
        SINGLE,
        SWITCH,
        PRIORITY
    }

    public enum TargetSorting {
        DISTANCE,
        HEALTH,
        MAX_HEALTH,
        LEVEL
    }

}
