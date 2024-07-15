/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.ui.hud;

import me.beavermod.Beaver;
import me.beavermod.event.Render2DEvent;
import me.beavermod.module.ModuleManager;
import me.beavermod.module.impl.visual.HudMod;
import me.beavermod.ui.notification.NotificationManager;

public class Hud {

    public static void init() {

    }

    public static void draw(Render2DEvent event) {
        if (module().enableNotifications.get() && Beaver.INSTANCE.mc.currentScreen == null) {
            NotificationManager.draw();
        }
    }

    public static HudMod module() {
        return ModuleManager.INSTANCE.get(HudMod.class);
    }

}
