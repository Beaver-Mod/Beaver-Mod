/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.module.setting;

import com.google.gson.JsonObject;

import java.text.DecimalFormat;

public abstract class Setting<T> {

    public static final DecimalFormat DEFAULT_DECIMAL_FORMAT = new DecimalFormat("#0.##");

    public final String name;
    public final String description;


    public Setting(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract T get();

    public abstract void set(T value);

    public abstract void parseString(String str);

    public abstract String getDisplayValue();

    public abstract void serialize(JsonObject object);
    public abstract void deserialize(JsonObject object);


}
