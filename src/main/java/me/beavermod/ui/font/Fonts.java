/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.ui.font;

public enum Fonts {

    ARIAL_18("Arial", 18),
    ARIAL_24("Arial", 24);

    public final String name;
    public final int size;

    Fonts(String name, int size) {
        this.name = name;
        this.size = size;
    }

}
