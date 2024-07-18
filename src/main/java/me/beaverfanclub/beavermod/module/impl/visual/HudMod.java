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

package me.beaverfanclub.beavermod.module.impl.visual;

import me.beaverfanclub.beavermod.module.setting.impl.EnumSetting;
import me.beaverfanclub.beavermod.module.Module;
import me.beaverfanclub.beavermod.module.setting.impl.BooleanSetting;
import me.beaverfanclub.beavermod.module.setting.impl.SeperatorSetting;
import me.beaverfanclub.beavermod.ui.hud.Hud;
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
