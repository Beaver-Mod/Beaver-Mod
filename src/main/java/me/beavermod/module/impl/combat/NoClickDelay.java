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

package me.beavermod.module.impl.combat;

import me.beavermod.event.PreTickEvent;
import me.beavermod.module.Module;
import me.beavermod.util.misc.AccessWidenerReflect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoClickDelay extends Module {

    public NoClickDelay() {
        super("No Click Delay", "Removes the click delay after missing a hit (1.7 attacking)", Category.COMBAT);
    }

    @SubscribeEvent
    public void onPreTick(PreTickEvent event) {
        try {
            AccessWidenerReflect.leftClickCounter.setInt(mc, 0);
        } catch (IllegalAccessException ignored) {}
    }

}
