/*
 * This file is modified from Raven bS <https://github.com/Strangerrrs/Raven-bS>
 *
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.util;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

public class Reflection {

    // This class can be used to access private fields and methods

    public static Field leftClickCounter;
    public static Field rightClickDelayTimer;

    public static void init() {
        leftClickCounter = ReflectionHelper.findField(Minecraft.class, "field_71429_W", "leftClickCounter");
        if (leftClickCounter != null) {
            leftClickCounter.setAccessible(true);
        }

        rightClickDelayTimer = ReflectionHelper.findField(Minecraft.class, "field_71467_ac", "rightClickDelayTimer");
        if (rightClickDelayTimer != null) {
            rightClickDelayTimer.setAccessible(true);
        }
    }

}
