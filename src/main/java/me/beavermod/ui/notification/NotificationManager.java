/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.ui.notification;

import me.beavermod.Beaver;
import me.beavermod.module.ModuleManager;
import me.beavermod.module.impl.visual.HudMod;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class NotificationManager {

    private static final List<Notification> notifications = new ArrayList<>();

    public static void addNotification(String title, String message, Notification.Type type, int time) {
        addNotification(new Notification(title, message, type, time));
    }

    public static void addNotification(Notification notification) {
        if (!ModuleManager.INSTANCE.isEnabled(HudMod.class) || !ModuleManager.INSTANCE.get(HudMod.class).enableNotifications.get()) return;
        notifications.add(0, notification);
        try {

            ResourceLocation sound = ModuleManager.INSTANCE.get(HudMod.class).notificationSound.get().location;
            if (sound != null) {
                Beaver.INSTANCE.mc.getSoundHandler().playSound(PositionedSoundRecord.create(sound, 1));
            }
        } catch (Exception ignored) {}
    }

    public static void draw() {
        if (!ModuleManager.INSTANCE.get(HudMod.class).enableNotifications.get()) return;

        try {
            notifications.removeIf(Notification::isFinished);
        } catch (ConcurrentModificationException ignored) {}

        ScaledResolution sr = new ScaledResolution(Beaver.INSTANCE.mc);
        int count = 0;
        int offset = 0;
        try {
            for (Notification notification : notifications) {
                notification.draw(sr.getScaledHeight() - 72 - (count * 30), offset);

                int factor = notification.fadeTime / 30 == 0 ? 1 : notification.fadeTime / 30;

                if (!notification.timeHasPassed(notification.fadeTime)) {
                    offset += Math.abs(notification.fadeTime - notification.timePassed()) / factor;

                } else if (notification.timeHasPassed(notification.time)) {
                    offset += (notification.timePassed() - notification.time) / factor;
                }

                ++count;
            }
        } catch (Exception ignored) {}

    }

}
