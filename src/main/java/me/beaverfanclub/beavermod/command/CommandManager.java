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

package me.beaverfanclub.beavermod.command;

import me.beaverfanclub.beavermod.Beaver;
import me.beaverfanclub.beavermod.event.SendPacketEvent;
import me.beaverfanclub.beavermod.util.minecraft.ChatUtil;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.reflections.Reflections;

import java.util.Comparator;
import java.util.LinkedHashMap;

public class CommandManager extends LinkedHashMap<Command, Class<? extends Command>> {

    public static final String PREFIX = ".";

    public static CommandManager INSTANCE = null;

    public static void init() {
        INSTANCE = new CommandManager();
        INSTANCE.addCommands();
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    public void addCommands() {
        new Reflections("me.beaverfanclub.beavermod.command.impl")
                .getSubTypesOf(Command.class)
                .stream()
                .sorted(Comparator.comparing(Class::getSimpleName))
                .forEach(command -> {
                    try {
                        Beaver.LOGGER.info("Add Command: {}", command.getSimpleName());
                        this.put(command.newInstance(), command);
                    } catch (InstantiationException | IllegalAccessException exception) {
                        throw new RuntimeException(exception);
                    }
                });
    }

    @SubscribeEvent
    public void handleChat(SendPacketEvent event) {
        if (event.getPacket() instanceof C01PacketChatMessage) {
            String message = ((C01PacketChatMessage) event.getPacket()).getMessage();

            if (message.startsWith(PREFIX)) {

                event.setCanceled(true);
                String[] args = message.split("\\s+");

                String commandName = args[0].substring(PREFIX.length());

                for (Command command : this.keySet()) {
                    if (command.nameMatches(commandName)) {
                        try {
                            command.onCommand(args, message);
                        } catch (Exception exception) {
                            ChatUtil.send("%sError: %s", EnumChatFormatting.RED, exception.getMessage());
                        }
                        return;
                    }
                }

                ChatUtil.send("%s'%s' is not a command", EnumChatFormatting.RED, commandName.toLowerCase());

            }
        }
    }

}
