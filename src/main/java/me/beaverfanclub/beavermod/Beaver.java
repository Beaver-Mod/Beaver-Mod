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

package me.beaverfanclub.beavermod;

import com.google.gson.Gson;
import me.beaverfanclub.beavermod.command.CommandManager;
import me.beaverfanclub.beavermod.ui.clickgui.ClickGui;
import me.beaverfanclub.beavermod.ui.font.FontManager;
import me.beaverfanclub.beavermod.ui.hud.Hud;
import me.beaverfanclub.beavermod.util.misc.AccessWidenerReflect;
import me.beaverfanclub.beavermod.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(modid = Beaver.MOD_ID,
        name = Beaver.NAME,
        version = Beaver.VERSION,
        acceptedMinecraftVersions = Beaver.ACCEPTED_VERSIONS,
        clientSideOnly = true)
public class Beaver {

    public static final Logger LOGGER = LogManager.getLogger("Beaver");

    public static final String MOD_ID = "keystrokesmod";
    public static final String NAME = "Beaver";
    public static final String VERSION = "1.2";
    public static final String ACCEPTED_VERSIONS = "[1.8.9]";

    public static final Gson GSON = new Gson();

    public static Beaver INSTANCE = null;

    public Minecraft mc;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        INSTANCE = this;
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("Loading Beaver Mod");
        this.mc = Minecraft.getMinecraft();

        AccessWidenerReflect.init();
        FontManager.init();
        ModuleManager.init();
        CommandManager.init();
        ClickGui.init();
        Hud.init();

    }



}
