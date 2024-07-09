/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod;

import com.google.gson.Gson;
import me.beavermod.command.CommandManager;
import me.beavermod.module.ModuleManager;
import me.beavermod.ui.font.FontManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(modid = Beaver.MOD_ID, name = Beaver.NAME, version = Beaver.VERSION, acceptedMinecraftVersions = Beaver.ACCEPTED_VERSIONS, clientSideOnly = true)
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
    public void init(FMLInitializationEvent event) {

        INSTANCE = this;

        LOGGER.info("Loading Beaver Mod");
        this.mc = Minecraft.getMinecraft();

        FontManager.init();
        ModuleManager.init();
        CommandManager.init();
    }



}
