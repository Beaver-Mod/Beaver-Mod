/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.module;

import me.beavermod.Beaver;
import me.beavermod.module.setting.Setting;
import me.beavermod.util.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

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
    private int key = 0;

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

    public boolean matches(String name) {
        return this.name.contains(name) || name.contains(this.name);
    }

    public void onEnabled() {}
    public void onDisabled() {}

    public enum Category {
        COMBAT("Combat"),
        UTILITY("Utility"),
        WORLD("World"),
        VISUAL("Visual"),
        OTHER("Other");


        public final String name;

        Category(String name) {
            this.name = name;
        }
    }

}
