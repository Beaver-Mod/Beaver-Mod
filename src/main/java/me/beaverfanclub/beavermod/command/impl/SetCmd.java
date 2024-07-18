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
import me.beaverfanclub.beavermod.module.Module;
import me.beaverfanclub.beavermod.module.ModuleManager;
import me.beaverfanclub.beavermod.module.setting.Setting;
import me.beaverfanclub.beavermod.util.minecraft.ChatUtil;
import net.minecraft.util.EnumChatFormatting;

public class SetCmd extends Command {


    public SetCmd() {
        super("Set", "Change a setting", ".set <module> <setting> <value>");
    }

    @Override
    public void onCommand(String[] args, String rawCommand) {
        if (args.length < 4) {
            ChatUtil.send("%sInvalid syntax: %s", EnumChatFormatting.RED, syntax);
            return;
        }

        Module module = ModuleManager.INSTANCE.get(args[1]);
        if (module == null) {
            ChatUtil.send("%s'%s' is not a valid module.", EnumChatFormatting.RED, args[1].toLowerCase());
            return;
        }

        Setting<?> setting = module.getSetting(args[2]);
        if (setting == null) {
            ChatUtil.send("%s'%s' is not a setting in %s", EnumChatFormatting.RED, args[2].toLowerCase(), module.name);
            return;
        }

        try {
            String oldValue = setting.getDisplayValue();
            setting.parseString(args[3]);

            ChatUtil.send("%s set to: %s, previously set to: %s", setting.displayName, setting.getDisplayValue(), oldValue);
        } catch (Exception exception) {
            ChatUtil.send("%sCouldn't parse value.", EnumChatFormatting.RED);
        }
    }
}
