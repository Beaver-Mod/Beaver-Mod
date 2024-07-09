/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.module.setting.impl;

import com.google.gson.JsonObject;
import me.beavermod.module.setting.Setting;

public class BooleanSetting extends Setting<Boolean> {

    private boolean value;

    public BooleanSetting(String name, String description, boolean defaultValue) {
        super(name, description);
        this.value = defaultValue;
    }

    @Override
    public Boolean get() {
        return value;
    }

    @Override
    public void set(Boolean value) {
        this.value = value;
    }

    @Override
    public void parseString(String str) {
        this.value = str.equalsIgnoreCase("true") || str.equalsIgnoreCase("1");
    }

    @Override
    public String getDisplayValue() {
        return value ? "True" : "False";
    }

    @Override
    public void serialize(JsonObject object) {
        object.addProperty(name, value);
    }

    @Override
    public void deserialize(JsonObject object) {
        this.value = object.get(name).getAsBoolean();
    }
}
