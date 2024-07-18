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

package me.beaverfanclub.beavermod.command.impl;

import me.beaverfanclub.beavermod.command.Command;
import me.beaverfanclub.beavermod.util.minecraft.ChatUtil;
import me.beaverfanclub.beavermod.Beaver;
import me.beaverfanclub.beavermod.module.ModuleManager;
import me.beaverfanclub.beavermod.module.impl.other.Debugger;
import me.beaverfanclub.beavermod.ui.notification.Notification;
import me.beaverfanclub.beavermod.util.minecraft.entity.EntityUtil;
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
