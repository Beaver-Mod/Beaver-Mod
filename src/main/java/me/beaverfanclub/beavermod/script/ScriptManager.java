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

package me.beaverfanclub.beavermod.script;

import me.beaverfanclub.beavermod.util.minecraft.ChatUtil;
import me.beaverfanclub.beavermod.script.api.PlayerScriptApi;
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
            ChatUtil.error("%sLua Error: %s", error.getMessage());
        } catch (Exception exception) {
            ChatUtil.error("%sJVM Error: %s", exception.getMessage());
        }
    }

}
