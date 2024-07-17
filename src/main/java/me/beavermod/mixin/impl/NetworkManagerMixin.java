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

package me.beavermod.mixin.impl;

import io.netty.channel.ChannelHandlerContext;
import me.beavermod.event.ReceivePacketEvent;
import me.beavermod.event.SendPacketEvent;
import me.beavermod.module.ModuleManager;
import me.beavermod.module.impl.other.Debugger;
import me.beavermod.util.minecraft.PacketUtil;
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
            ModuleManager.INSTANCE.get(Debugger.class).onSendPacketCustom(packet, Debugger.PacketState.NO_EVENT);
            return;
        }

        SendPacketEvent event = new SendPacketEvent(packet);
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled()) {
            ci.cancel();
            ModuleManager.INSTANCE.get(Debugger.class).onSendPacketCustom(packet, Debugger.PacketState.CANCELED);
        } else {
            ModuleManager.INSTANCE.get(Debugger.class).onSendPacketCustom(packet, Debugger.PacketState.NORMAL);
        }

    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void receivePacket(ChannelHandlerContext context, Packet packet, CallbackInfo ci) {

        ReceivePacketEvent event = new ReceivePacketEvent(packet);
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled()) {
            ci.cancel();
            ModuleManager.INSTANCE.get(Debugger.class).onReceivePacketCustom(packet, Debugger.PacketState.CANCELED);
        } else {
            ModuleManager.INSTANCE.get(Debugger.class).onReceivePacketCustom(packet, Debugger.PacketState.NORMAL);
        }

    }



}
