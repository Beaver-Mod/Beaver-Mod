/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.script.api;

import me.beavermod.Beaver;
import net.minecraft.client.Minecraft;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

public class PlayerScriptApi extends LuaTable {

    private final Minecraft mc;

    public PlayerScriptApi() {

        mc = Beaver.INSTANCE.mc;

        set("jump", new Jump());

    }

    public class Jump extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            mc.thePlayer.jump();
            return NIL;
        }
    }

}
