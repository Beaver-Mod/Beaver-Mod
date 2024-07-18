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

package me.beaverfanclub.beavermod.mixin.impl;

import com.mojang.authlib.GameProfile;
import me.beaverfanclub.beavermod.event.JoinEvent;
import me.beaverfanclub.beavermod.event.PreMotionEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("OverwriteAuthorRequired") // annoying
@Mixin(EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin extends AbstractClientPlayer {

    @Shadow @Final public NetHandlerPlayClient sendQueue;

    @Shadow private boolean serverSprintState;

    @Shadow private boolean serverSneakState;

    @Shadow protected abstract boolean isCurrentViewEntity();

    @Shadow private double lastReportedPosX;

    @Shadow private double lastReportedPosY;

    @Shadow private double lastReportedPosZ;

    @Shadow private float lastReportedYaw;

    @Shadow private float lastReportedPitch;

    @Shadow private int positionUpdateTicks;

    public EntityPlayerSPMixin(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onJoin(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatFileWriter statFile, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new JoinEvent());
    }

    @Overwrite
    public void onUpdateWalkingPlayer() {

        PreMotionEvent event = new PreMotionEvent(this.posX, this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround);
        MinecraftForge.EVENT_BUS.post(event);

        boolean sprinting = this.isSprinting();

        if (sprinting != this.serverSprintState) {

            if (sprinting) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
            } else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }

            this.serverSprintState = sprinting;
        }

        boolean sneaking = this.isSneaking();

        if (sneaking != this.serverSneakState) {

            if (sneaking) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
            } else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }

            this.serverSneakState = sneaking;
        }

        if (this.isCurrentViewEntity()) {

            double diffX = event.x - this.lastReportedPosX;
            double diffY = event.y - this.lastReportedPosY;
            double diffZ = event.z - this.lastReportedPosZ;
            double diffYaw = event.yaw - this.lastReportedYaw;
            double diffPitch = event.pitch - this.lastReportedPitch;
            boolean hasMoved = diffX * diffX + diffY * diffY + diffZ * diffZ > 9.0E-4D || this.positionUpdateTicks >= 20;
            boolean hasRotated = diffYaw != 0.0D || diffPitch != 0.0D;

            if (this.ridingEntity == null) {

                if (hasMoved && hasRotated) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(event.x, event.y, event.z, event.yaw, event.pitch, event.onGround));
                } else if (hasMoved) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(event.x, event.y, event.z, event.onGround));
                } else if (hasRotated) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(event.yaw, event.pitch, event.onGround));
                } else {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer(event.onGround));
                }

            } else {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0D, this.motionZ, event.yaw, event.pitch, event.onGround));
                hasMoved = false;
            }

            ++this.positionUpdateTicks;

            if (hasMoved)
            {
                this.lastReportedPosX = event.x;
                this.lastReportedPosY = event.y;
                this.lastReportedPosZ = event.z;
                this.positionUpdateTicks = 0;
            }

            if (hasRotated)
            {
                this.lastReportedYaw = event.yaw;
                this.lastReportedPitch = event.pitch;
            }
        }

    }

}
