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
