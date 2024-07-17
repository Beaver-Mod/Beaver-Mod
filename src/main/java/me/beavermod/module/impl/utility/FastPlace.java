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

package me.beavermod.module.impl.utility;

import me.beavermod.event.PreTickEvent;
import me.beavermod.module.Module;
import me.beavermod.module.setting.impl.BooleanSetting;
import me.beavermod.module.setting.impl.IntSetting;
import me.beavermod.util.misc.AccessWidenerReflect;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FastPlace extends Module {

    private final BooleanSetting blocksOnly = new BooleanSetting("Blocks", "Only works while holding blocks", true);
    private final IntSetting delay = new IntSetting("Delay", "Delay", 0, 3, 0);

    public FastPlace() {
        super("Fast Place", "Removes block place delay", Category.UTILITY);
        addSettings(blocksOnly, delay);
    }

    @SubscribeEvent
    public void onPreTick(PreTickEvent event) {
        if (blocksOnly.get()) {
            if (mc.thePlayer.getHeldItem() == null) return;
            if (!(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) return;
        }

        try {
            if (AccessWidenerReflect.rightClickDelayTimer.getInt(mc) >= delay.get()) {
                AccessWidenerReflect.rightClickDelayTimer.setInt(mc, 0);
            }
        } catch (IllegalAccessException ignored) {}
    }

}
