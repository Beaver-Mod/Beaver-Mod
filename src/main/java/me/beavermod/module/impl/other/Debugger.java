package me.beavermod.module.impl.other;

import com.google.common.collect.EvictingQueue;
import me.beavermod.event.SendPacketEvent;
import me.beavermod.module.Module;
import me.beavermod.module.setting.impl.BooleanSetting;
import me.beavermod.module.setting.impl.EnumSetting;
import me.beavermod.module.setting.impl.IntSetting;
import me.beavermod.module.setting.impl.SeperatorSetting;
import me.beavermod.module.setting.util.IChanged;
import me.beavermod.util.minecraft.ChatUtil;
import me.beavermod.util.minecraft.entity.EntityUtil;
import net.minecraft.event.ClickEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.Queue;

public class Debugger extends Module {

    private final BooleanSetting c00 = new BooleanSetting("C00", "C00 Packets", false);
    private final BooleanSetting c01 = new BooleanSetting("C01", "C01 Packets", false);
    private final BooleanSetting c02 = new BooleanSetting("C02", "C02 Packets", false);
    private final BooleanSetting c03 = new BooleanSetting("C03", "C03-C06 Packets", false);
    private final BooleanSetting c07 = new BooleanSetting("C07", "C07 Packets", false);
    private final BooleanSetting c08 = new BooleanSetting("C08", "C08 Packets", false);
    private final BooleanSetting c09 = new BooleanSetting("C09", "C09 Packets", false);
    private final BooleanSetting c0a = new BooleanSetting("C0A", "C0A Packets", false);
    private final BooleanSetting c0b = new BooleanSetting("C0B", "C0B Packets", false);
    private final BooleanSetting c0c = new BooleanSetting("C0C", "C0C Packets", false);
    private final BooleanSetting c0d = new BooleanSetting("C0D", "C0D Packets", false);
    private final BooleanSetting c0e = new BooleanSetting("C0E", "C0E Packets", false);
    private final BooleanSetting c0f = new BooleanSetting("C0F", "C0F Packets", false);

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
        addSettings(
                new SeperatorSetting("Packets"),
                c00, c01, c02, c03, c07, c08, c09, c0a, c0b, c0c, c0d, c0e, c0f,

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

        // TODO: find a better way to do this...
        if (packet instanceof C00PacketKeepAlive && c00.get()) {
            printPacket(packet, state);
        } if (packet instanceof C02PacketUseEntity && c02.get()) {
            printPacket(packet, state);
        } else if (packet instanceof C03PacketPlayer && c03.get()) {
            printPacket(packet, state);
        } else if (packet instanceof C07PacketPlayerDigging && c07.get()) {
            printPacket(packet, state);
        } else if (packet instanceof C08PacketPlayerBlockPlacement && c08.get()) {
            printPacket(packet, state);
        } else if (packet instanceof C09PacketHeldItemChange && c09.get()) {
            printPacket(packet, state);
        } else if (packet instanceof C0APacketAnimation && c0a.get()) {
            printPacket(packet, state);
        } else if (packet instanceof C0BPacketEntityAction && c0b.get()) {
            printPacket(packet, state);
        } else if (packet instanceof C0CPacketInput && c0c.get()) {
            printPacket(packet, state);
        } else if (packet instanceof C0DPacketCloseWindow && c0d.get()) {
            printPacket(packet, state);
        } else if (packet instanceof C0EPacketClickWindow && c0e.get()) {
            printPacket(packet, state);
        } else if (packet instanceof C0FPacketConfirmTransaction && c0f.get()) {
            printPacket(packet, state);
        }

    }

    public void printPacket(Packet packet, PacketState state) {

        String mainValue = getPacketMainValue(packet);

        StringBuilder info = new StringBuilder()
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
