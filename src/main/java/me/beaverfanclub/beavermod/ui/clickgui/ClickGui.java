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

package me.beaverfanclub.beavermod.ui.clickgui;

import me.beaverfanclub.beavermod.module.setting.impl.*;
import me.beaverfanclub.beavermod.module.Module;
import me.beaverfanclub.beavermod.util.minecraft.ChatUtil;
import me.beaverfanclub.beavermod.ui.font.FontManager;
import me.beaverfanclub.beavermod.ui.font.Fonts;
import me.beaverfanclub.beavermod.ui.font.TTFFontRenderer;
import me.beaverfanclub.beavermod.util.minecraft.render.RenderUtil;
import me.beaverfanclub.beavermod.util.minecraft.render.ShaderProgram;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

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
    public static ModulePanel keybindListener = null;

    public static String toolTip = null;

    public static ShaderProgram colorPickerShader;

    public static void init() {
        font = FontManager.getFont(Fonts.ARIAL_18);
        headerFont = FontManager.getFont(Fonts.ARIAL_24);
        titleFont = FontManager.getFont(Fonts.ARIAL_36);

        colorPickerShader = new ShaderProgram(new ResourceLocation("beaver/shader/fragment/color_interpolation.frag"));

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
        selectedPanel = null;
        keybindListener = null;
    }

    @Override
    public void drawScreen(int mouseX2, int mouseY2, float partialTicks) {
        mouseX = mouseX2 * mc.gameSettings.guiScale;
        mouseY = mouseY2 * mc.gameSettings.guiScale;

        float scale = (float)mc.gameSettings.guiScale;
        float inverseScale = 1.0F / scale;

        toolTip = null;

        CategoryPanel categoryPanel = categoryPanels.get(selectedCategory);

        if (keybindListener != null && keybindListener.module.category != selectedCategory) {
            keybindListener = null;
        }

        categoryPanel.offset -= Mouse.getDWheel() / 5;
        if (categoryPanel.offset <= 0) {
            categoryPanel.offset = 0;
        }

        GlStateManager.pushMatrix();
        GlStateManager.scale(inverseScale, inverseScale, inverseScale);
        glEnable(GL_SCISSOR_TEST);
        RenderUtil.scissor(left / scale, top / scale + 0.5F, WIDTH / scale, HEIGHT / scale);

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

                // Key Bind
                modulePanel.bindPos = y;
                String bindName = modulePanel.getBindText();

                drawRect(moduleLeft + 16, y, right - 16, y + 32, 0xFF444444);
                RenderUtil.drawCircleRect(right - 34 - fontWidth(font, bindName), y + 14 - Math.round(fontHeight(font) / 2.0F), right - 30, y + 18 + Math.round(fontHeight(font) / 2.0F), 4, 0xFF222222);
                drawString(font, "Key Bind", moduleLeft + 32, y + 16 - fontHeight(font) / 2, -1, true);
                drawString(font, bindName, right - 32 - fontWidth(font, bindName), y + 16 - fontHeight(font) / 2, -1, true);

                y += 32;

                for (SettingPanel panel : modulePanel.settingPanels) {
                    y = panel.bottom = drawSetting(panel, y);
                }
                RenderUtil.color(-1);
            } else if (keybindListener == modulePanel) {
                keybindListener = null;
            }

            y += 8;
            modulePanel.bottom = y - 4;
        }

        glDisable(GL_SCISSOR_TEST);

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

            RenderUtil.drawCircleRect(right - 80, y + 4, right - 32, y + 28, 12, 0xFF222222);
            RenderUtil.drawCircle(right - (setting.get() ? 44 : 68), y + 16, 10, setting.get() ? 0xFF55FFFF : 0xFFAAAAAA);

            drawString(font, setting.displayName, moduleLeft + 32, y + 16 - fontHeight(font) / 2, -1, true);
            if (mouseIntersecting(mouseX, mouseY,
                    moduleLeft + 32,
                    y + 16  - fontHeight(font) / 2,
                    moduleLeft + 32 + fontWidth(font, setting.displayName),
                    y + 16  - fontHeight(font) / 2 + fontHeight(font))) {
                toolTip = setting.description;
            }

            return y + 32;

        } else if (panel.setting instanceof NumberSetting<?>) {
            NumberSetting<?> setting = (NumberSetting<?>)panel.setting;

            drawRect(left + categoriesWidth + 16, y, right - 16, y + 32, 0xFF444444);
            drawString(font, setting.displayName, moduleLeft + 32, y + 16 - fontHeight(font) / 2, -1, true);
            if (mouseIntersecting(mouseX, mouseY,
                    moduleLeft + 32,
                    y + 16  - fontHeight(font) / 2,
                    moduleLeft + 32 + fontWidth(font, setting.displayName),
                    y + 16  - fontHeight(font) / 2 + fontHeight(font))) {
                toolTip = setting.description;
            }

            RenderUtil.drawCircleRect(right - 32 - SLIDER_WIDTH, y + 14, right - 32, y + 18, 2, 0xFFAAAAAA);
            RenderUtil.drawCircle(right - 32 - SLIDER_WIDTH + (int)(setting.getPercent() * SLIDER_WIDTH), y + 16, 8, 0xFF55FFFF);

            String text = setting.getDisplayValue();
            RenderUtil.drawCircleRect(right - 50 - SLIDER_WIDTH - fontWidth(font, text), y + 14 - fontHeight(font) / 2.0F, right - 46 - SLIDER_WIDTH, y + 18 + fontHeight(font) / 2.0F, 4,  0xFF222222);
            drawString(font, text, right - 48 - SLIDER_WIDTH - fontWidth(font, text), y + 16 - fontHeight(font) / 2, -1, true);

            return y + 32;

        } else if (panel.setting instanceof EnumSetting<?>) {
            EnumSetting<?> setting = (EnumSetting<?>)panel.setting;

            drawRect(left + categoriesWidth + 16, y, right - 16, y + 32, 0xFF444444);
            drawString(font, setting.displayName, moduleLeft + 32, y + 16 - fontHeight(font) / 2, -1, true);
            if (mouseIntersecting(mouseX, mouseY,
                    moduleLeft + 32,
                    y + 16  - fontHeight(font) / 2,
                    moduleLeft + 32 + fontWidth(font, setting.displayName),
                    y + 16  - fontHeight(font) / 2 + fontHeight(font))) {
                toolTip = setting.description;
            }

            RenderUtil.drawCircleRect(right - 34 - fontWidth(font, setting.getDisplayValue()), y + 14 - Math.round(fontHeight(font) / 2.0F), right - 30, y + 18 + Math.round(fontHeight(font) / 2.0F), 2, 0xFF222222);
            drawString(font, setting.getDisplayValue(), right - 32 - fontWidth(font, setting.getDisplayValue()), y + 16 - fontHeight(font) / 2, -1, true);

            return y + 32;

        } else if (panel.setting instanceof ColorSetting) {
            ColorSetting setting = (ColorSetting)panel.setting;

            drawRect(left + categoriesWidth + 16, y, right - 16, y + 32, 0xFF444444);
            drawString(font, setting.displayName, moduleLeft + 32, y + 16 - fontHeight(font) / 2, -1, true);
            if (mouseIntersecting(mouseX, mouseY,
                    moduleLeft + 32,
                    y + 16  - fontHeight(font) / 2,
                    moduleLeft + 32 + fontWidth(font, setting.displayName),
                    y + 16  - fontHeight(font) / 2 + fontHeight(font))) {
                toolTip = setting.description;
            }

            RenderUtil.drawCircleRect(right - 48, y + 8, right - 32, y + 24, 4, setting.getRgb());
            y += 32;

            if (panel.expanded) {

                // Draw shit here (it doesn't work...)

                y += 112;
            }

            return y;

        } else if (panel.setting instanceof SeperatorSetting) {

            drawRect(left + categoriesWidth + 16, y, right - 16, y + 40, 0xFF444444);
            RenderUtil.drawCircleRect(moduleLeft + 24, y + 34, right - 24, y + 38, 2, 0xFFAAAAAA);
            drawString(font, panel.setting.displayName, moduleLeft + 32, y + 12, 0xFF55FFFF, true);

            return y + 40;
        }

        return y;
    }

    @Override
    public void mouseClicked(int mouseX2, int mouseY2, int mouseButton) {
        mouseX = mouseX2 * mc.gameSettings.guiScale;
        mouseY = mouseY2 * mc.gameSettings.guiScale;

        // No need to check + you can probably click on invisible objects
        if (mouseY < top || mouseY > bottom) return;

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

                          String bindName = modulePanel.getBindText();
                          if (mouseIntersecting(mouseX, mouseY, right - 34 - fontWidth(font, bindName), modulePanel.bindPos + 14 - Math.round(fontHeight(font) / 2.0F), right - 30, modulePanel.bindPos + 18 + Math.round(fontHeight(font) / 2.0F))) {
                              if (keybindListener == modulePanel) {
                                  keybindListener = null;
                              } else {
                                  keybindListener = modulePanel;
                              }
                          }

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

                              } else if (panel.setting instanceof ColorSetting) {
                                  if (mouseIntersecting(mouseX, mouseY, right - 48, panel.top + 8, right - 32, panel.top + 24)) {
                                      panel.expanded = !panel.expanded;
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
        int mouseX = mouseX2 * mc.gameSettings.guiScale;
        int mouseY = mouseY2 * mc.gameSettings.guiScale;

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

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keybindListener != null) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                keybindListener.module.setKey(Keyboard.KEY_NONE);
            } else {
                keybindListener.module.setKey(keyCode);
            }
            keybindListener = null;
        } else {
            super.keyTyped(typedChar, keyCode);
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
        RenderUtil.color(-1);
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