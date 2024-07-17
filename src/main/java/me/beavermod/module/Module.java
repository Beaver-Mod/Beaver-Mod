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

package me.beavermod.module;

import me.beavermod.Beaver;
import me.beavermod.module.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Module {

    protected static Minecraft mc = Beaver.INSTANCE.mc;

    public final String name;
    public final String displayName;
    public final String description;
    public final Category category;

    private boolean enabled = false;
    private int key = Keyboard.KEY_NONE;

    private final List<Setting<?>> settings = new ArrayList<>();

    protected Module(String name, String description, Category category) {
        this.name = name.replace(" ", "");
        this.displayName = name;
        this.description = description;
        this.category = category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void toggle() {
       this.toggle(!this.enabled);
    }

    public void toggle(boolean enabled) {
        this.enabled = enabled;

        if (this.enabled) {
            MinecraftForge.EVENT_BUS.register(this);
            this.onEnabled();
        } else {
            MinecraftForge.EVENT_BUS.unregister(this);
            this.onDisabled();
        }
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void addSettings(Setting<?>... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    public Setting<?> getSetting(String name) {
        return settings.stream()
                .filter(setting -> setting.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public boolean matches(String name) {
        return this.name.contains(name) || name.contains(this.name);
    }

    public void onEnabled() {}
    public void onDisabled() {}

    public enum Category {
        COMBAT("Combat"),
        MACRO("Macro"),
        UTILITY("Utility"),
        VISUAL("Visual"),
        OTHER("Other"),
        SCRIPT("Script");

        public final String name;

        Category(String name) {
            this.name = name;
        }
    }

}
