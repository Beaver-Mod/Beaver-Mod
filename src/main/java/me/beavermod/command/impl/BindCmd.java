/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.command.impl;

import me.beavermod.command.Command;
import me.beavermod.module.Module;
import me.beavermod.module.ModuleManager;
import me.beavermod.util.minecraft.ChatUtil;
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
