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

package me.beaverfanclub.beavermod.ui.hud;

import me.beaverfanclub.beavermod.module.impl.visual.HudMod;
import me.beaverfanclub.beavermod.Beaver;
import me.beaverfanclub.beavermod.module.ModuleManager;
import me.beaverfanclub.beavermod.ui.notification.NotificationManager;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Hud {

    public static void init() {

    }

    public static void draw(TickEvent.RenderTickEvent event) {
        if (module().enableNotifications.get() && Beaver.INSTANCE.mc.currentScreen == null) {
            NotificationManager.draw();
        }
    }

    public static HudMod module() {
        return ModuleManager.INSTANCE.get(HudMod.class);
    }

}
