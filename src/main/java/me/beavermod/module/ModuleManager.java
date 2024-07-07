/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this software under the terms of the MIT license.
 */

package me.beavermod.module;

import java.util.LinkedHashMap;

public class ModuleManager extends LinkedHashMap<Module, Class<? extends Module>> {

    public static final String MODULE_PACKAGE = "me.drred96.beaver.module.impl";

    public static ModuleManager INSTANCE = null;

    public static void init() {

    }

    public void addModules() {

    }

    public void addModule(Module module) {
        this.put(module, module.getClass());
    }

}
