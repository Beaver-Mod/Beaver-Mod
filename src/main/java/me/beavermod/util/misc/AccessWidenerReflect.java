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

package me.beavermod.util.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

// TODO: create a refmap <https://github.com/KevyPorter/Minecraft-Forge-Utils>
public class AccessWidenerReflect {

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
