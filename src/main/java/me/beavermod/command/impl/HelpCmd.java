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

package me.beavermod.command.impl;

import me.beavermod.command.Command;
import me.beavermod.command.CommandManager;
import me.beavermod.util.minecraft.ChatUtil;

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
