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
import me.beaverfanclub.beavermod.module.Module;
import me.beaverfanclub.beavermod.module.ModuleManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

public class BindCmd extends Command {

    public BindCmd() {
        super("Bind", "Set a key bind", ".bind add <module> <key> | .bind del <module> | .bind clear");
    }

    @Override
    public void onCommand(String[] args, String rawCommand) {
        try {

            if (args[1].equalsIgnoreCase("add")) {
                Module module = ModuleManager.INSTANCE.get(args[2]);
                if (module != null) {
                    module.setKey(Keyboard.getKeyIndex(args[3].toUpperCase()));
                    ChatUtil.send("%s was bound to %s", module.name, Keyboard.getKeyName(module.getKey()));
                } else {
                    ChatUtil.send("%sInvalid module name", EnumChatFormatting.RED);
                }
            } else if (args[1].equalsIgnoreCase("del")) {
                Module module = ModuleManager.INSTANCE.get(args[2]);
                if (module != null) {
                    module.setKey(Keyboard.KEY_NONE);
                    ChatUtil.send("Removed key-bind for", module.name);
                } else {
                    ChatUtil.send("%sInvalid module name", EnumChatFormatting.RED);
                }
            } else if (args[1].equalsIgnoreCase("clear")) {
                for (Module module : ModuleManager.INSTANCE.keySet()) {
                    module.setKey(Keyboard.KEY_NONE);
                }
                ChatUtil.send("Cleared module key-binds");
            }

        } catch (ArrayIndexOutOfBoundsException exception) {
            ChatUtil.send("%sInvalid syntax: %s", EnumChatFormatting.RED, syntax);
        }
    }
}
