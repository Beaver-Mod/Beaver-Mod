/*
 * This file is modified from Raven bS <https://github.com/Strangerrrs/Raven-bS>
 *
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.util.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflection {

    // This class can be used to access private fields and methods

    public static Field leftClickCounter;
    public static Field rightClickDelayTimer;
    public static Field curBlockDamageMP;

    public static Method setupCameraTransform;

    public static void init() {
        getFields();
        getMethods();
    }

    public static void getFields() {
        leftClickCounter = ReflectionHelper.findField(Minecraft.class, "field_71429_W", "leftClickCounter");
        if (leftClickCounter != null) {
            leftClickCounter.setAccessible(true);
        }

        rightClickDelayTimer = ReflectionHelper.findField(Minecraft.class, "field_71467_ac", "rightClickDelayTimer");
        if (rightClickDelayTimer != null) {
            rightClickDelayTimer.setAccessible(true);
        }

        curBlockDamageMP = ReflectionHelper.findField(PlayerControllerMP.class, "field_78770_f", "curBlockDamageMP");
        if (curBlockDamageMP != null) {
            curBlockDamageMP.setAccessible(true);
        }
    }

    public static void getMethods() {

        try {
            setupCameraTransform = EntityRenderer.class.getDeclaredMethod("func_78479_a", float.class, int.class);
        } catch (NoSuchMethodException ignored) {
            try {
                setupCameraTransform = EntityRenderer.class.getDeclaredMethod("setupCameraTransform", float.class, int.class);
            } catch (NoSuchMethodException ignored1) {
            }
        }

        if (setupCameraTransform != null) {
            setupCameraTransform.setAccessible(true);
        }
    }

    public static Timer getTimer() {
        return ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "timer", "field_71428_T");
    }

}
