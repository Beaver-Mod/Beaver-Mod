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

package me.beaverfanclub.beavermod.ui.notification;

import me.beaverfanclub.beavermod.ui.font.FontManager;
import me.beaverfanclub.beavermod.ui.font.Fonts;
import me.beaverfanclub.beavermod.ui.font.TTFFontRenderer;
import me.beaverfanclub.beavermod.util.minecraft.render.RenderUtil;
import me.beaverfanclub.beavermod.util.misc.Clock;
import me.beaverfanclub.beavermod.Beaver;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.text.DecimalFormat;

/*
 * This class and NotificationManager were both written in 2022 and probably
 * need some refactoring.
 * Feel free to rewrite them and submit a pull request!
 * - CalculusHvH
 */
public class Notification {

    public final int height, fadeTime;
    private int width;

    public final String title, message;
    public final Type type;
    public final int time;
    private final Clock timer = new Clock();

    public Notification(String title, String message, Type type, int time) {
        this.width = 0;
        this.fadeTime = 200;
        this.height = 24;

        this.title = String.format("%s%s%s %s- %%time%%", EnumChatFormatting.BOLD, title, EnumChatFormatting.RESET, EnumChatFormatting.GRAY);
        this.message = message;
        this.type = type;
        this.time = time;
        this.timer.reset();
    }

    public void draw(int y, int yOffset) {
        TTFFontRenderer fr = FontManager.getFont(Fonts.ARIAL_18);
        ScaledResolution sr = new ScaledResolution(Beaver.INSTANCE.mc);

        width = this.height + Math.round(Math.max(fr.getStringWidth(title), fr.getStringWidth(message))) + 4;

        int x = sr.getScaledWidth() - width;
        if (timer.getTime() < fadeTime) {
            x += (int) (Math.abs(fadeTime - timer.getTime()) / (fadeTime / width == 0 ? 1 : fadeTime / width));
        }
        if (this.timer.hasReached(time)) {
            x += (int) ((timer.getTime() - time) / (fadeTime / width == 0 ? 1 : fadeTime / width));
        }
        int left = x + width;

        GlStateManager.pushMatrix();

        GlStateManager.enableAlpha();
        GlStateManager.color(0.0F, 0.0F, 0.0F, 185.0F / 255.0F);
        Gui.drawRect(x, y + yOffset, left, y + this.height + 2 + yOffset, 0xAA000000);
        Gui.drawRect(x, y + this.height + yOffset, x + width, y + this.height + 2 + yOffset, RenderUtil.darkenColor(type.getColor(), 3).getRGB());
        Gui.drawRect(x, y + this.height + yOffset, (int)(x + timer.getTime() / (time / width)), y + this.height + 2 + yOffset, type.getColorInt());
        GlStateManager.color(0.0F, 0.0F, 0.0F, 0.0F);
        String titleDisplay = title.replace("%time%", (new DecimalFormat("0.00")).format((float)timeLeft() / 1000.0F));
        String messageDisplay = message.replace("%time%", (new DecimalFormat("0.00")).format((float)timeLeft() / 1000.0F));

        fr.drawStringWithShadow(titleDisplay, x + this.height, y + 3 + yOffset, 0xFFFFFFFF);
        fr.drawStringWithShadow(messageDisplay, x + this.height, y + 5 + fr.getHeight(messageDisplay) + yOffset, 0xFFAAAAAA);



        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.scale(0.5, 0.5, 0.5);
        Color color = type.getColor();
        Beaver.INSTANCE.mc.getTextureManager().bindTexture(type.resourceLocation);

        GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
        final int size = 32;
        final int offset = 4;
        Gui.drawModalRectWithCustomSizedTexture((x + offset) * 2, (y + offset + yOffset) * 2, 0, 0, size, size, size, size);
        GlStateManager.popMatrix();

        GlStateManager.popMatrix();

//		Gui.drawRect(x+2, y+2+yOffset, x+52, y+this.height-2+yOffset, type.getColor());

    }

    private float getStringWidth(String text) {
        return FontManager.getFont(Fonts.ARIAL_18).getStringWidth(text);
    }

    public boolean isFinished() {
        return timer.hasReached(time + fadeTime);
    }

    public int timePassed() {
        return (int) this.timer.getTime();
    }

    public int timeLeft() {
        return this.time - (int)this.timer.getTime();
    }

    public boolean timeHasPassed(int time) {
        return this.timer.hasReached(time);
    }

    public final int getWidth() {
        return this.width;
    }

    public enum Type {
        INFO(0xFFAAAAAA, "beaver/contexts/INFO.png"),
        SUCCESS(0xFF55FF55, "beaver/contexts/SUCCESS.png"),
        WARNING(0xFFFFFF55, "beaver/contexts/WARNING.png"),
        ERROR(0xFFFF5555, "beaver/contexts/ERROR.png");

        private final int color;
        public final ResourceLocation resourceLocation;

        Type(int color, String resourceLocation) {
            this.color = color;
            this.resourceLocation = new ResourceLocation(resourceLocation);
        }

        public int getColorInt() {
            return this.color;
        }

        public Color getColor() {
            return new Color(color);
        }
    }

    public static void send(String title, String message, Type type, int time) {
        try {
            NotificationManager.addNotification(title, message, type, time);
        } catch (NullPointerException ignored) {}
    }

}
