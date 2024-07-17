package me.beavermod.command.impl;

import me.beavermod.Beaver;
import me.beavermod.command.Command;
import me.beavermod.module.ModuleManager;
import me.beavermod.module.impl.other.Debugger;
import me.beavermod.ui.notification.Notification;
import me.beavermod.util.minecraft.ChatUtil;
import me.beavermod.util.minecraft.entity.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.Packet;
import net.minecraft.util.Tuple;

import java.util.Objects;

public class DebugCmd extends Command {

    public DebugCmd() {
        super("Debug", "Debugging command", ".debug <args>");
    }


    @Override
    public void onCommand(String[] args, String rawCommand) {
        if (args.length < 2) {
            ChatUtil.error("Missing arguments");
            return;
        }

        String action = args[1];

        if (action.equalsIgnoreCase("packet")) {
            if (args.length < 3) {
                ChatUtil.error("Missing arguments");
                return;
            }

            try {
                int hash = Integer.parseInt(args[2]);
                Tuple<Packet, Debugger.PacketState> packet = ModuleManager.INSTANCE.get(Debugger.class).cachedPacket(hash);
                if (packet == null) {
                    ChatUtil.error("That packet was not found");
                    return;
                }
                ModuleManager.INSTANCE.get(Debugger.class).printPacketInfo(packet.getFirst(), packet.getSecond());

            } catch (NumberFormatException exception) {
                ChatUtil.error("'%s' is not a number", args[2]);
            }

        } else if (action.equalsIgnoreCase("entityinfo")) {
            if (args.length < 3) {
                ChatUtil.error("Missing arguments");
                return;
            }

            try {
                int id = Integer.parseInt(args[2]);
                Entity entity = Beaver.INSTANCE.mc.theWorld.getEntityByID(id);
                if (entity == null) {
                    ChatUtil.error("That entity was not found");
                    return;
                }
                EntityUtil.printEntityDebugInfo(entity);

            } catch (NumberFormatException exception) {
                ChatUtil.error("'%s' is not a number", args[2]);
            }

        } else if (action.equalsIgnoreCase("armorstand")) {
            EntityArmorStand armorStand = Beaver.INSTANCE.mc.theWorld
                    .getEntities(EntityArmorStand.class, Objects::nonNull)
                    .stream()
                    .min((o1, o2) -> Float.compare(o1.getDistanceToEntity(Beaver.INSTANCE.mc.thePlayer), o2.getDistanceToEntity(Beaver.INSTANCE.mc.thePlayer)))
                    .orElse(null);

            if (armorStand == null) {
                ChatUtil.error("No armor stand found nearby");
                return;
            }

            EntityUtil.printEntityDebugInfo(armorStand);

        } else if (action.equalsIgnoreCase("notification")) {
            Notification.send("Test", "This is a test notification!", Notification.Type.INFO, 5000);

        } else {
            ChatUtil.error("Invalid action: %s", action);
        }
    }
}
