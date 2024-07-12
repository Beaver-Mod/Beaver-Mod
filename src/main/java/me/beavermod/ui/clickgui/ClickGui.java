/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.ui.clickgui;

import me.beavermod.module.Module;
import me.beavermod.module.setting.impl.*;
import me.beavermod.ui.font.FontManager;
import me.beavermod.ui.font.Fonts;
import me.beavermod.ui.font.TTFFontRenderer;
import me.beavermod.util.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClickGui extends GuiScreen {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static final int SLIDER_WIDTH = 200;

    public static TTFFontRenderer font;
    public static TTFFontRenderer headerFont;
    public static TTFFontRenderer titleFont;

    private static Module.Category selectedCategory = Module.Category.COMBAT;
    private static final Map<Module.Category, CategoryPanel> categoryPanels = new LinkedHashMap<>();

    public static int left, right, top, bottom;
    public static int categoriesWidth;
    public static int moduleLeft;

    public static int mouseX, mouseY;

    public static SettingPanel selectedPanel = null;

    public static String toolTip = null;

    public static void init() {
        font = FontManager.getFont(Fonts.ARIAL_18);
        headerFont = FontManager.getFont(Fonts.ARIAL_24);
        titleFont = FontManager.getFont(Fonts.ARIAL_36);

        categoriesWidth = fontWidth(titleFont, EnumChatFormatting.BOLD + "Beaver") + 12;

        for (Module.Category category : Module.Category.values()) {
            categoryPanels.put(category, new CategoryPanel(category));
        }
    }


    @Override
    public void initGui() {
        left = this.width - WIDTH / 2;
        right = this.width + WIDTH / 2;
        top = this.height - HEIGHT / 2;
        bottom = this.height + HEIGHT / 2;
        moduleLeft = left + categoriesWidth;

        toolTip = null;
    }

    @Override
    public void drawScreen(int mouseX2, int mouseY2, float partialTicks) {
        mouseX = mouseX2 * 2;
        mouseY = mouseY2 * 2;

        toolTip = null;

        CategoryPanel categoryPanel = categoryPanels.get(selectedCategory);

        categoryPanel.offset += Mouse.getDWheel() / 5;
        if (categoryPanel.offset <= 0) {
            categoryPanel.offset = 0;
        }

        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.scissor(left / 2.0F, top / 2.0F + 0.5F, WIDTH / 2.0F, HEIGHT / 2.0F);

        drawRect(left, top, moduleLeft, bottom, 0xFF111111);
        drawRect(moduleLeft, top, right, bottom, 0xFF222222);
        drawString(titleFont, EnumChatFormatting.BOLD + "Beaver", left + 6, top + 6, 0xFF55FFFF, true);

        int i = 0;
        for (Module.Category category : Module.Category.values()) {
            drawString(headerFont, category.name, left + (categoriesWidth - fontWidth(headerFont, category.name)) / 2, top + 16 + fontHeight(headerFont) * 2 + (fontHeight(headerFont) + 8) * i, selectedCategory == category ? 0xFF55FFFF : -1, true);
            i++;
        }

        int y = top + 16 - categoryPanel.offset;
        for (ModulePanel modulePanel : categoryPanel.modulePanels) {
            modulePanel.top = y;

            // Module info
            drawRect(moduleLeft + 16, y, right - 16, y + 60, 0xFF444444);
            drawString(headerFont, modulePanel.module.displayName,moduleLeft + 24, y + 8, modulePanel.module.isEnabled() ? 0xFF55FFFF : -1, true);
            drawString(font, modulePanel.module.description, moduleLeft + 24, y + 14 + fontHeight(headerFont), 0xFFAAAAAA, true);
            y += 60;

            // Settings
            if (modulePanel.expanded) {
                for (SettingPanel panel : modulePanel.settingPanels) {
                    y = drawSetting(panel, y);
                }
                RenderUtil.glColor(-1);
            }

            y += 8;
            modulePanel.bottom = y - 4;
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (toolTip != null) {
            RenderUtil.drawCircleRect(mouseX, mouseY - fontHeight(font) - 4, mouseX + fontWidth(font, toolTip) + 4, mouseY, 2, 0x77000000);
            drawString(font, toolTip, mouseX + 2, mouseY - 2 - fontHeight(font), -1, true);
        }

        GlStateManager.popMatrix();


    }

    public int drawSetting(SettingPanel panel, int y) {

        panel.top = y;

        if (panel.setting instanceof BooleanSetting) {
            BooleanSetting setting = (BooleanSetting)panel.setting;

            drawRect(left + categoriesWidth + 16, y, right - 16, y + 32, 0xFF444444);
            drawString(font, setting.name, moduleLeft + 32, y + 16 - fontHeight(font) / 2, -1, true);
            if (mouseIntersecting(mouseX, mouseY,
                    moduleLeft + 32,
                    y + 16  - fontHeight(font) / 2,
                    moduleLeft + 32 + fontWidth(font, setting.name),
                    y + 16  - fontHeight(font) / 2 + fontHeight(font))) {
                toolTip = setting.description;
            }

            RenderUtil.drawCircleRect(right - 80, y + 4, right - 32, y + 28, 12, 0xFF222222);
            RenderUtil.drawCircle(right - (setting.get() ? 44 : 68), y + 16, 10, setting.get() ? 0xFF55FFFF : 0xFFAAAAAA);

            return panel.bottom = y + 32;

        } else if (panel.setting instanceof NumberSetting<?>) {
            NumberSetting<?> setting = (NumberSetting<?>)panel.setting;

            drawRect(left + categoriesWidth + 16, y, right - 16, y + 32, 0xFF444444);
            drawString(font, setting.name, moduleLeft + 32, y + 16 - fontHeight(font) / 2, -1, true);
            if (mouseIntersecting(mouseX, mouseY,
                    moduleLeft + 32,
                    y + 16  - fontHeight(font) / 2,
                    moduleLeft + 32 + fontWidth(font, setting.name),
                    y + 16  - fontHeight(font) / 2 + fontHeight(font))) {
                toolTip = setting.description;
            }

            RenderUtil.drawCircleRect(right - 32 - SLIDER_WIDTH, y + 14, right - 32, y + 18, 2, 0xFFAAAAAA);
            RenderUtil.drawCircle(right - 32 - SLIDER_WIDTH + (int)(setting.getPercent() * SLIDER_WIDTH), y + 16, 8, 0xFF55FFFF);

            String text = setting.getDisplayValue();
            drawString(font, text, right - 48 - SLIDER_WIDTH - fontWidth(font, text), y + 16 - fontHeight(font) / 2, -1, true);

            return panel.bottom = y + 32;

        } else if (panel.setting instanceof EnumSetting<?>) {
            EnumSetting<?> setting = (EnumSetting<?>)panel.setting;

            drawRect(left + categoriesWidth + 16, y, right - 16, y + 32, 0xFF444444);
            drawString(font, setting.name, moduleLeft + 32, y + 16 - fontHeight(font) / 2, -1, true);
            if (mouseIntersecting(mouseX, mouseY,
                    moduleLeft + 32,
                    y + 16  - fontHeight(font) / 2,
                    moduleLeft + 32 + fontWidth(font, setting.name),
                    y + 16  - fontHeight(font) / 2 + fontHeight(font))) {
                toolTip = setting.description;
            }

            drawString(font, setting.getDisplayValue(), right - 32 - fontWidth(font, setting.getDisplayValue()), y + 16 - fontHeight(font) / 2, -1, true);

            return panel.bottom = y + 32;

        } else if (panel.setting instanceof SeperatorSetting) {

            drawRect(left + categoriesWidth + 16, y, right - 16, y + 40, 0xFF444444);
            RenderUtil.drawCircleRect(moduleLeft + 24, y + 34, right - 24, y + 38, 2, 0xFFAAAAAA);
            drawString(font, panel.setting.name, moduleLeft + 32, y + 12, 0xFF55FFFF, true);

            return panel.bottom = y + 40;
        }

        return panel.bottom = y;
    }

    @Override
    public void mouseClicked(int mouseX2, int mouseY2, int mouseButton) throws IOException {
        mouseX = mouseX2 * 2;
        mouseY = mouseY2 * 2;

        // No need to check + you can probably click on invisible objects
        if (mouseY < 0 || mouseY > HEIGHT) return;

        if (mouseX < left + categoriesWidth) {
            // Categories
            int i = 0;
            for (final Module.Category category : Module.Category.values()) {
                int y = top + 16 + fontHeight(headerFont) * 2 + (fontHeight(headerFont) + 8) * i;
                if (mouseIntersecting(mouseX, mouseY, left, y - 4, left + categoriesWidth, y + fontHeight(headerFont) + 4)) {
                    if (selectedCategory != category) {
                        selectedCategory = category;
                        selectedPanel = null;
                    }
                    return;
                }
                i++;
            }
        } else {
            // Modules
            for (ModulePanel modulePanel : categoryPanels.get(selectedCategory).modulePanels) {
                  if (mouseIntersecting(mouseX, mouseY, left + categoriesWidth + 16, modulePanel.top, right - 16, modulePanel.bottom)) {
                      if (!modulePanel.expanded || mouseY <= modulePanel.top + 60) {
                          if (mouseButton == 0) {
                              modulePanel.module.toggle();
                          } else if (mouseButton == 1) {
                              modulePanel.expanded = !modulePanel.expanded;
                          }
                      } else {
                            for (SettingPanel panel : modulePanel.settingPanels) {
                                if (panel.setting instanceof BooleanSetting) {
                                    BooleanSetting setting = (BooleanSetting)panel.setting;
                                    if (mouseIntersecting(mouseX, mouseY, right - 80, panel.top + 4, right - 32, panel.top + 28)) {
                                        setting.toggle();
                                    }

                                } else if (panel.setting instanceof NumberSetting<?>) {
                                    if (mouseIntersecting(mouseX, mouseY, right - 32 - SLIDER_WIDTH, panel.top + 8, right - 32, panel.top + 24)) {
                                        selectedPanel = panel;
                                    }

                                } else if (panel.setting instanceof EnumSetting<?>) {
                                    EnumSetting<?> setting = (EnumSetting<?>)panel.setting;
                                    if (mouseIntersecting(mouseX, mouseY, right - 32 - fontWidth(font, setting.getDisplayValue()), panel.top + 8, right - 32, panel.bottom + 24)) {
                                        if (mouseButton == 0) setting.cycleForwards();
                                        else if (mouseButton == 1) setting.cycleBackwards();
                                    }
                                }

                            }
                      }
                  }
            }
        }


    }

    @Override
    protected void mouseClickMove(int mouseX2, int mouseY2, int clickedMouseButton, long timeSinceLastClick) {
        int mouseX = mouseX2 * 2;
        int mouseY = mouseY2 * 2;

        if (selectedPanel != null) {
            if (selectedPanel.setting instanceof IntSetting) {
                IntSetting setting = (IntSetting)selectedPanel.setting;
                float mousePos = (float)(mouseX - right + 32 + SLIDER_WIDTH) / (float)SLIDER_WIDTH;
                if (mousePos > 1.0F) mousePos = 1.0F;
                else if (mousePos < 0.0F) mousePos = 0.0F;
                setting.set(Math.round(mousePos * setting.getRange()) + setting.min);

            } else if (selectedPanel.setting instanceof FloatSetting) {
                FloatSetting setting = (FloatSetting)selectedPanel.setting;
                float mousePos = (float)(mouseX - right + 32 + SLIDER_WIDTH) / (float)SLIDER_WIDTH;
                if (mousePos > 1.0F) mousePos = 1.0F;
                else if (mousePos < 0.0F) mousePos = 0.0F;
                setting.set(mousePos * setting.getRange() + setting.min);
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (selectedPanel != null) {
            if (selectedPanel.setting instanceof NumberSetting<?>) {
                selectedPanel = null;
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
        RenderUtil.glColor(-1);
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
