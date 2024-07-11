/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.script;

import me.beavermod.script.api.PlayerScriptApi;
import me.beavermod.util.ChatUtil;
import net.minecraft.util.EnumChatFormatting;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;

public class ScriptManager {

    public static Globals globals = JsePlatform.standardGlobals();

    public ScriptManager() {
        globals.set("player", new PlayerScriptApi());
    }

    public void runScript(File scriptFile) {
        try {
            globals.loadfile(scriptFile.getAbsolutePath()).call();
        } catch (LuaError error) {
            ChatUtil.print("%sLua Error: %s", EnumChatFormatting.RED, error.getMessage());
        } catch (Exception exception) {
            ChatUtil.print("%sJVM Error: %s", EnumChatFormatting.RED, exception.getMessage());
        }
    }

}
