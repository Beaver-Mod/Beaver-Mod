/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.ui.clickgui;

import me.beavermod.module.Module;
import me.beavermod.util.RenderUtil;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui extends GuiScreen {

    private static Module.Category selectedCategory = Module.Category.COMBAT;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        RenderUtil.drawCircleRect(20, 20, this.width - 20, this.height - 20, 20, 0xFFFF0000);

    }
}
