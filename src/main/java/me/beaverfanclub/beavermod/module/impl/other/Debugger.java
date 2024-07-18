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

package me.beaverfanclub.beavermod.module.impl.other;

import com.google.common.collect.EvictingQueue;
import me.beaverfanclub.beavermod.Beaver;
import me.beaverfanclub.beavermod.event.SendPacketEvent;
import me.beaverfanclub.beavermod.module.Module;
import me.beaverfanclub.beavermod.module.setting.impl.BooleanSetting;
import me.beaverfanclub.beavermod.module.setting.impl.EnumSetting;
import me.beaverfanclub.beavermod.module.setting.impl.IntSetting;
import me.beaverfanclub.beavermod.module.setting.impl.SeperatorSetting;
import me.beaverfanclub.beavermod.module.setting.util.IChanged;
import me.beaverfanclub.beavermod.util.minecraft.ChatUtil;
import me.beaverfanclub.beavermod.util.minecraft.entity.EntityUtil;
import net.minecraft.event.ClickEvent;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Debugger extends Module {

    private final Map<Class<? extends Packet>, BooleanSetting> clientPacketSettings = new HashMap<>();
    private final Map<Class<? extends Packet>, BooleanSetting> serverPacketSettings = new HashMap<>();

    private final IntSetting cacheSize = new IntSetting("Cache Size", "Packet cache size (changing will clear the cache)", 10, 1000, 100, "%d", new IChanged() {
        @Override
        public void onChanged() {
            packetCache = EvictingQueue.create(cacheSize.get());
        }
    });

    private final BooleanSetting entityDebugger = new BooleanSetting("Entity Debugger", "Shows entity info by right clicking it", false);

    private Queue<Tuple<Packet, PacketState>> packetCache;

    public Debugger() {
        super("Debugger", "Helps with debugging", Category.OTHER);

        addSettings(new SeperatorSetting("Client Packets"));
        for (int i = 0; ; i++) {
            try {
               Packet packet = EnumConnectionState.PLAY.getPacket(EnumPacketDirection.SERVERBOUND, i);
               if (packet == null) {
                   break;
               }

               Class<? extends Packet> packetClass = packet.getClass();
                String settingName = packetClass.getSimpleName().startsWith("C") ? packetClass.getSimpleName() : String.format("C%02X", i);
                BooleanSetting setting = new BooleanSetting(settingName, packetClass.getSimpleName(), false);
               clientPacketSettings.put(packetClass, setting);
               addSettings(setting);

            } catch (IllegalAccessException | InstantiationException ignored) {}
        }

        addSettings(new SeperatorSetting("Server Packets"));
        for (int i = 0; ; i++) {
            try {
                Packet packet = EnumConnectionState.PLAY.getPacket(EnumPacketDirection.CLIENTBOUND, i);
                if (packet == null) {
                    break;
                }

                Class<? extends Packet> packetClass = packet.getClass();
                String settingName = packetClass.getSimpleName().startsWith("S") ? packetClass.getSimpleName() : String.format("S%02X", i);
                BooleanSetting setting = new BooleanSetting(settingName, packetClass.getSimpleName(), false);
                serverPacketSettings.put(packetClass, setting);
                addSettings(setting);

            } catch (IllegalAccessException | InstantiationException ignored) {}
        }


        addSettings(
                new SeperatorSetting("Entities"),
                entityDebugger
        );

        packetCache = EvictingQueue.create(cacheSize.get());
    }

    @SubscribeEvent
    public void onSendPacket(SendPacketEvent event) {
        if (entityDebugger.get()) {
            if (event.getPacket() instanceof C02PacketUseEntity) {
                C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();
                if (packet.getAction() == C02PacketUseEntity.Action.INTERACT) {
                    EntityUtil.printEntityDebugInfo(packet.getEntityFromWorld(mc.theWorld));
                }
            }
        }


    }

    // Custom method for the packet debugger so it can log canceled packets
    public void onSendPacketCustom(Packet packet, PacketState state) {
        if (!this.isEnabled()) return;

        try {
            if (clientPacketSettings.get(packet.getClass()).get()) {
                printPacket(packet, state);
            }
        } catch (NullPointerException exception) {
            Beaver.LOGGER.error("Failed to log packet", exception);
        }

    }

    public void onReceivePacketCustom(Packet packet, PacketState state) {
        if (!this.isEnabled()) return;

        try {
            if (serverPacketSettings.get(packet.getClass()).get()) {
                printPacket(packet, state);
            }
        } catch (NullPointerException exception) {
            Beaver.LOGGER.error("Failed to log packet", exception);
        }

    }


    public void printPacket(Packet packet, PacketState state) {

        String mainValue = getPacketMainValue(packet);

        StringBuilder info = new StringBuilder()
                .append(ChatUtil.SHORT_PREFIX)
                .append(state.format)
                .append(packet.getClass().getSimpleName());

        if (mainValue != null) {
            info.append(EnumChatFormatting.GRAY)
                    .append(": ")
                    .append(mainValue);
        }

        packetCache.add(new Tuple<>(packet, state));

        ChatUtil.Builder.of(info.toString())
                .setHoverEvent("View Packet Info")
                .setClickEvent(ClickEvent.Action.RUN_COMMAND, ".debug packet " + packet.hashCode())
                .send();

//        ChatUtil.print("%s%s%s: %s", state.format, EnumChatFormatting.GRAY);
    }

    public Tuple<Packet, PacketState> cachedPacket(int hashCode) {
        return packetCache.stream()
                .filter(packet -> packet.getFirst().hashCode() == hashCode)
                .findFirst()
                .orElse(null);
    }

    public String getPacketMainValue(Packet packet) {
        if (packet instanceof C02PacketUseEntity) {
            return ((C02PacketUseEntity)packet).getAction().name();
        }

        if (packet instanceof C07PacketPlayerDigging) {
            return ((C07PacketPlayerDigging)packet).getStatus().name();
        }

        if (packet instanceof C09PacketHeldItemChange) {
            return Integer.toString(((C09PacketHeldItemChange)packet).getSlotId());
        }

        if (packet instanceof C0BPacketEntityAction) {
            return ((C0BPacketEntityAction)packet).getAction().name();
        }

        if (packet instanceof C0EPacketClickWindow) {
            return Integer.toString(((C0EPacketClickWindow) packet).getSlotId());
        }

        return null;
    }

    public void printPacketInfo(Packet packet, PacketState state) {
        ChatUtil.print("%s--- Packet Info ---", EnumChatFormatting.AQUA);
        ChatUtil.print("%sClass%s: %s", EnumChatFormatting.AQUA, EnumChatFormatting.GRAY, packet.getClass().getSimpleName());
        ChatUtil.print("%sState%s: %s", EnumChatFormatting.AQUA, EnumChatFormatting.GRAY, state.getString());
        ChatUtil.print("%sFields%s: ", EnumChatFormatting.AQUA, EnumChatFormatting.GRAY);
        for (Field field : packet.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                ChatUtil.print("  %s[%s] %s%s: %s", EnumChatFormatting.AQUA, field.getType().getSimpleName(), field.getName(), EnumChatFormatting.GRAY, field.get(packet));
            } catch (IllegalAccessException | IllegalArgumentException exception) {
                ChatUtil.print("  %s[%s] %s%s: %sError", EnumChatFormatting.AQUA, field.getType().getSimpleName(), field.getName(), EnumChatFormatting.GRAY, EnumChatFormatting.RED);
            }
        }
    }

    public enum PacketState {
        NORMAL(EnumChatFormatting.GREEN),
        NO_EVENT(EnumChatFormatting.YELLOW),
        CANCELED(EnumChatFormatting.RED);

        public final EnumChatFormatting format;

        PacketState(EnumChatFormatting format) {
            this.format = format;
        }

        public String getString() {
            return String.format("%s%s", format, EnumSetting.toDisplay(this));
        }
    }

}
