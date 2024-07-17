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

package me.beavermod.module.impl.visual;

import me.beavermod.module.Module;
import me.beavermod.module.setting.impl.BooleanSetting;

public class NoRender extends Module {

    private final BooleanSetting hurtCamera = new BooleanSetting("Hurt Camera", "Prevents your screen from shaking when getting hit", true);

    public NoRender() {
        super("No Render", "Prevents some things from rendering", Category.VISUAL);
        addSettings(hurtCamera);
    }

    public boolean hurtCamera() {
        return this.isEnabled() && this.hurtCamera.get();
    }

}
