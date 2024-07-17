/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.module.impl.visual;

import me.beavermod.module.Module;
import me.beavermod.module.setting.impl.BooleanSetting;
import me.beavermod.module.setting.impl.EnumSetting;
import me.beavermod.module.setting.impl.SeperatorSetting;
import me.beavermod.ui.hud.Hud;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class HudMod extends Module {

    public final BooleanSetting enableNotifications = new BooleanSetting("Enable Notifications", "Enables notifications", true);
    public final EnumSetting<NotificationSound> notificationSound = new EnumSetting<>("Notification Sound", "Sound to play when you get a notification", NotificationSound.POP);

    public HudMod() {
        super("HUD", "Heads Up Display", Category.VISUAL);
        addSettings(
                new SeperatorSetting("Notifications"),
                enableNotifications, notificationSound
        );

        toggle(true);
    }

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent event) {
        Hud.draw(event);
    }

    public enum NotificationSound {
        POP(new ResourceLocation("random.pop")),
        DING(new ResourceLocation("random.orb")),
        NONE(null);

        public final ResourceLocation location;

        NotificationSound(ResourceLocation location) {
            this.location = location;
        }
    }
}
