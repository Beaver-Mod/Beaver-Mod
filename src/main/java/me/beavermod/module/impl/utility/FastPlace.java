/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.module.impl.utility;

import me.beavermod.event.PreTickEvent;
import me.beavermod.module.Module;
import me.beavermod.module.setting.impl.BooleanSetting;
import me.beavermod.module.setting.impl.IntSetting;
import me.beavermod.util.Reflection;
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
            if (Reflection.rightClickDelayTimer.getInt(mc) >= delay.get()) {
                Reflection.rightClickDelayTimer.setInt(mc, 0);
            }
        } catch (IllegalAccessException ignored) {}
    }

}
