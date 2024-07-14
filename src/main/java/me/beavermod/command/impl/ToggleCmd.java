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
import me.beavermod.util.ChatUtil;
import net.minecraft.util.EnumChatFormatting;

public class ToggleCmd extends Command {

    public ToggleCmd() {
        super("Toggle", "Toggles a module", ".toggle <module>", "t");
    }

    @Override
    public void onCommand(String[] args, String rawCommand) {

        if (args.length < 2) {
            ChatUtil.send("%Invalid syntax: %s", EnumChatFormatting.RED, syntax);
            return;
        }

        Module module = ModuleManager.INSTANCE.get(args[1]);

        if (module == null) {
            ChatUtil.send("%s'%s' is not a module", EnumChatFormatting.RED, args[1].toLowerCase());
            return;
        }

        module.toggle();
        ChatUtil.send("%s %s", module.isEnabled() ? "Enabled" : "Disabled", module.name);

    }
}
