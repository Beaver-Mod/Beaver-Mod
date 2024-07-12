/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.ui.clickgui;

import me.beavermod.module.Module;

import java.util.ArrayList;
import java.util.List;

public class ModulePanel {

    public final Module module;
    public boolean expanded = false;
    public int top, bottom;
    public final List<SettingPanel> settingPanels;

    public ModulePanel(Module module) {
        this.module = module;
        settingPanels = module.getSettings().stream()
                .collect(ArrayList::new,
                        (settingPanels, setting) -> settingPanels.add(new SettingPanel(setting)),
                        List::addAll);
    }

}
