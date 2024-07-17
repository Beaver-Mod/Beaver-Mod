/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.module.impl.combat;

import me.beavermod.event.PreTickEvent;
import me.beavermod.module.Module;
import me.beavermod.util.misc.Reflection;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoClickDelay extends Module {

    public NoClickDelay() {
        super("No Click Delay", "Removes the click delay after missing a hit (1.7 attacking)", Category.COMBAT);
    }

    @SubscribeEvent
    public void onPreTick(PreTickEvent event) {
        try {
            Reflection.leftClickCounter.setInt(mc, 0);
        } catch (IllegalAccessException ignored) {}
    }

}
