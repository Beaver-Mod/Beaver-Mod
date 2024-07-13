/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.ui.font;

import me.beavermod.Beaver;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;

import java.awt.Font;
import java.util.HashMap;

public class FontManager {

    public static FontRenderer BIT_FONT = null;

    private static final TTFFontRenderer DEFAULT_FONT = new TTFFontRenderer(new Font("Arial", Font.PLAIN, 18));
    private static final HashMap<Fonts, TTFFontRenderer> FONTS = new HashMap<>();

    public static void init() {

        BIT_FONT = new FontRenderer(Beaver.INSTANCE.mc.gameSettings, new ResourceLocation("beaver/bit.png"), Beaver.INSTANCE.mc.renderEngine, false);
        if (Beaver.INSTANCE.mc.gameSettings.language != null) {
            BIT_FONT.setUnicodeFlag(Beaver.INSTANCE.mc.isUnicode());
            BIT_FONT.setBidiFlag(Beaver.INSTANCE.mc.getLanguageManager().isCurrentLanguageBidirectional());
        }
        ((IReloadableResourceManager)Beaver.INSTANCE.mc.getResourceManager()).registerReloadListener(BIT_FONT);

        for (Fonts font : Fonts.values()) {
            Beaver.LOGGER.info("Add Font: {} {}", font.name, font.size);
            FONTS.put(font, new TTFFontRenderer(new Font(font.name, Font.PLAIN, font.size)));
        }
    }

    public static TTFFontRenderer getFont(Fonts font) {
        return FONTS.getOrDefault(font, DEFAULT_FONT);
    }
	
}
