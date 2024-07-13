package me.beavermod.module.impl.other;

import me.beavermod.module.Module;
import me.beavermod.module.setting.impl.BooleanSetting;
import me.beavermod.module.setting.impl.SeperatorSetting;
import me.beavermod.util.ChatUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumChatFormatting;

public class PacketDebugger extends Module {

    private final BooleanSetting c02 = new BooleanSetting("C02", "C02 Packets", false);
    private final BooleanSetting c03 = new BooleanSetting("C03", "C03-C06 Packets", false);
    private final BooleanSetting c07 = new BooleanSetting("C07", "C07 Packets", false);
    private final BooleanSetting c08 = new BooleanSetting("C08", "C08 Packets", false);
    private final BooleanSetting c09 = new BooleanSetting("C09", "C09 Packets", false);
    private final BooleanSetting c0a = new BooleanSetting("C0A", "C0A Packets", false);
    private final BooleanSetting c0b = new BooleanSetting("C0B", "C0B Packets", false);

    public PacketDebugger() {
        super("Packet Debugger", "Debugs packets (useful making new modules/scripts)", Category.OTHER);
        addSettings(
                new SeperatorSetting("Packets"),
                c02, c03, c07, c08, c09, c0a, c0b
        );
    }

    public void onSendPacket(Packet packet, PacketState state) {
        if (!this.isEnabled()) return;

        if (packet instanceof C02PacketUseEntity && c02.get()) {
            C02PacketUseEntity packetUseEntity = (C02PacketUseEntity)packet;
            ChatUtil.print("%sC02%s: %s", state.format, EnumChatFormatting.GRAY, packetUseEntity.getAction().name());

        } else if (packet instanceof C03PacketPlayer && c03.get()) {
            if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                ChatUtil.print("%sC04", state.format);
            } else if (packet instanceof C03PacketPlayer.C05PacketPlayerLook) {
                ChatUtil.print("%sC05", state.format);
            } else if (packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
                ChatUtil.print("%sC06", state.format);
            } else {
                ChatUtil.print("%sC03", state.format);
            }

        } else if (packet instanceof C07PacketPlayerDigging && c07.get()) {
            C07PacketPlayerDigging packetPlayerDigging = (C07PacketPlayerDigging)packet;
            ChatUtil.print("%sC07%s: %s", state.format, EnumChatFormatting.GRAY, packetPlayerDigging.getStatus().name());

        } else if (packet instanceof C08PacketPlayerBlockPlacement && c08.get()) {
            ChatUtil.print("%sC08", state.format);

        } else if (packet instanceof C09PacketHeldItemChange && c09.get()) {
            C09PacketHeldItemChange packetHeldItemChange = (C09PacketHeldItemChange)packet;
            ChatUtil.print("%sC09%s: %d", state.format, EnumChatFormatting.GRAY, packetHeldItemChange.getSlotId());

        } else if (packet instanceof C0APacketAnimation && c0a.get()) {
            ChatUtil.print("%sC0A", state.format);

        } else if (packet instanceof C0BPacketEntityAction && c0b.get()) {
            C0BPacketEntityAction packetEntityAction = (C0BPacketEntityAction)packet;
            ChatUtil.print("%sC0B%s: %s", state.format, EnumChatFormatting.GRAY, packetEntityAction.getAction());

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
    }

}
