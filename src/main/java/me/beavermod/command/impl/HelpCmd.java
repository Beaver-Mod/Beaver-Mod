/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.command.impl;

import me.beavermod.command.Command;
import me.beavermod.command.CommandManager;
import me.beavermod.util.ChatUtil;

public class HelpCmd extends Command {

    public HelpCmd() {
        super("Help", "Displays help message", ".help", "?");
    }

    @Override
    public void onCommand(String[] args, String rawCommand) {
        for (Command command : CommandManager.INSTANCE.keySet()) {
            ChatUtil.print("\247b%s\2477: %s", command.name, command.description);
        }
    }
}
