/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.mixin.impl;

import io.netty.channel.ChannelHandlerContext;
import me.beavermod.event.ReceivePacketEvent;
import me.beavermod.event.SendPacketEvent;
import me.beavermod.module.ModuleManager;
import me.beavermod.module.impl.other.PacketDebugger;
import me.beavermod.util.PacketUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class NetworkManagerMixin {

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void onSendPacket(Packet packet, CallbackInfo ci) {

        if (PacketUtil.noEventList.contains(packet)) {
            PacketUtil.noEventList.remove(packet);
            ModuleManager.INSTANCE.get(PacketDebugger.class).onSendPacket(packet, PacketDebugger.PacketState.NO_EVENT);
            return;
        }

        SendPacketEvent event = new SendPacketEvent(packet);
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled()) {
            ci.cancel();
            ModuleManager.INSTANCE.get(PacketDebugger.class).onSendPacket(packet, PacketDebugger.PacketState.CANCELED);
        } else {
            ModuleManager.INSTANCE.get(PacketDebugger.class).onSendPacket(packet, PacketDebugger.PacketState.NORMAL);
        }

    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void receivePacket(ChannelHandlerContext context, Packet packet, CallbackInfo ci) {

        ReceivePacketEvent event = new ReceivePacketEvent(packet);
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled()) ci.cancel();

    }



}
