/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.ui.clickgui;

import me.beavermod.module.Module;
import me.beavermod.module.ModuleManager;
import me.beavermod.ui.font.FontManager;
import me.beavermod.ui.font.Fonts;
import me.beavermod.ui.font.TTFFontRenderer;
import me.beavermod.util.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class ClickGui extends GuiScreen {

    private static Module.Category selectedCategory = Module.Category.COMBAT;
    private static Module selectedModule = null;
    public static TTFFontRenderer font;
    public static TTFFontRenderer titleFont;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static int left, right, top, bottom;

    public static void init() {
        font = FontManager.getFont(Fonts.ARIAL_18);
        titleFont = FontManager.getFont(Fonts.ARIAL_24);
    }


    @Override
    public void initGui() {
        left = this.width - WIDTH / 2;
        right = this.width + WIDTH / 2;
        top = this.height - HEIGHT / 2;
        bottom = this.height + HEIGHT / 2;

        if (selectedModule == null) {
            selectedModule = ModuleManager.INSTANCE.getInCategory(selectedCategory).get(0);
        }
    }

    @Override
    public void drawScreen(int mouseX2, int mouseY2, float partialTicks) {
        int mouseX = mouseX2 * 2;
        int mouseY = mouseY2 * 2;

        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.scissor(left / 2.0F, top / 2.0F, WIDTH / 2.0F, HEIGHT / 2.0F);

        drawRect(left, top, right, bottom, 0xFF222222);
        drawRect(left, top, right, top + 8 + fontHeight(titleFont), 0xFF111111);
        drawString(font, "Beaver Mod", left + 4, top + 4, -1, true);

        int i = 0;
        for (Module.Category category : Module.Category.values()) {
            drawString(font, category.name, left + 8, top + 16 + fontHeight(titleFont) + (4 + fontHeight(titleFont)) * i, selectedCategory == category ? 0xFF55FFFF : -1, true);
            i++;
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();


    }

    @Override
    protected void mouseClicked(int mouseX2, int mouseY2, int mouseButton) throws IOException {
        int mouseX = mouseX2 * 2 - left;
        int mouseY = mouseY2 * 2 - top;

        if (mouseButton == 0) {
            int i = 0;
            for (final Module.Category category : Module.Category.values()) {
                int y = 16 + fontHeight(titleFont) + (4 + fontHeight(titleFont)) * i;
                if (mouseIntersecting(mouseX, mouseY, 8, y, 8 + fontWidth(titleFont, category.name), y + fontHeight(titleFont))) {
                    selectedCategory = category;
                    return;
                }
                i++;
            }
        }

    }

    public static void drawString(TTFFontRenderer font, String text, int x, int y, int color, boolean shadow) {
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        if (shadow) {
            font.drawStringWithShadow(text, (float)(x / 2), (float)(y / 2), color);
        } else {
            font.drawString(text, (float)(x / 2), (float)(y / 2), color);
        }
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
    }

    public static int fontWidth(TTFFontRenderer font, String str) {
        return (int)(font.getStringWidth(str) * 2.0F);
    }

    public static int fontHeight(TTFFontRenderer font) {
        return (int)(font.getHeight("I") * 2.0F);
    }

    public static boolean mouseIntersecting(int mouseX, int mouseY, int left, int top, int right, int bottom) {
        return mouseX >= left &&
                mouseX < right &&
                mouseY >= top &&
                mouseY < bottom;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
