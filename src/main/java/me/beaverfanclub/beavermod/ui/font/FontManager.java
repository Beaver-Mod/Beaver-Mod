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

package me.beaverfanclub.beavermod.ui.font;

import me.beaverfanclub.beavermod.Beaver;
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
