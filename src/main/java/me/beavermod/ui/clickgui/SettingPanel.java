/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.ui.clickgui;

import me.beavermod.module.setting.Setting;

public class SettingPanel {

    public final Setting<?> setting;
    public int top, bottom;

    // Expanded flag used by the color pickers
    public boolean expanded = false;

    public SettingPanel(Setting<?> setting) {
        this.setting = setting;
    }

}
