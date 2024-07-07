/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this software under the terms of the MIT license.
 */

package me.beavermod.module;

public abstract class Module {

    public final String name;
    public final String displayName;
    public final String description;
    public final Category category;

    private boolean enabled = false;
    private int key = 0;

    public Module(String name, String description, Category category) {
        this.name = name.replace(" ", "");
        this.displayName = name;
        this.description = description;
        this.category = category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }

    public void toggle(boolean enabled) {
        this.enabled = enabled;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public enum Category {
        COMBAT("Combat"),
        BLATANT("Blatant"),
        VISUAL("Visual");

        public final String name;

        Category(String name) {
            this.name = name;
        }
    }

}
