/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.mixin.impl;

import com.mojang.authlib.GameProfile;
import me.beavermod.event.PreMotionEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

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
