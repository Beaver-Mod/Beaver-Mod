/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.ui.clickgui;

import me.beavermod.module.Module;
import me.beavermod.module.ModuleManager;

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
