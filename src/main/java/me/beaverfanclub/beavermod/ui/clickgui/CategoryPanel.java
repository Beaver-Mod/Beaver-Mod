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
import me.beaverfanclub.beavermod.module.ModuleManager;

import java.util.ArrayList;
import java.util.List;

public class CategoryPanel {

    public final Module.Category category;
    public final List<ModulePanel> modulePanels;
    public int offset = 0;

    public CategoryPanel(Module.Category category) {
        this.category = category;
        modulePanels = ModuleManager.INSTANCE.getInCategory(category).stream()
                .collect(ArrayList::new,
                        (categoryModules, module) -> categoryModules.add(new ModulePanel(module)),
                        List::addAll);
    }

}
