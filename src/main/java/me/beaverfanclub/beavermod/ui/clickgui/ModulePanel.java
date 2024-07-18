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

package me.beaverfanclub.beavermod.ui.clickgui;

import me.beaverfanclub.beavermod.module.Module;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class ModulePanel {

    public final Module module;
    public boolean expanded = false;
    public int top, bottom;
    public int bindPos;
    public final List<SettingPanel> settingPanels;

    public ModulePanel(Module module) {
        this.module = module;
        settingPanels = module.getSettings().stream()
                .collect(ArrayList::new,
                        (settingPanels, setting) -> settingPanels.add(new SettingPanel(setting)),
                        List::addAll);
    }

    public String getBindText() {
        if (ClickGui.keybindListener == this) {
            return "Listening...";
        }

        String keyName = Keyboard.getKeyName(module.getKey());
        if (keyName != null) return keyName;

        return "NONE";
    }

}
