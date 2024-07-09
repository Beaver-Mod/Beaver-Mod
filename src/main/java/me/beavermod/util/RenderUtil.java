/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.util;

import net.minecraft.client.renderer.GlStateManager;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil {

    public static void drawCircle(float x, float y, float radius, int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        glColor4f(red, green, blue, alpha);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glPushMatrix();
        glLineWidth(1.0F);
        glBegin(GL_POLYGON);

        for(int i = 0; i <= 360; i++) {
            glVertex2d(x + Math.sin(i * Math.PI / 180.0) * radius, y + Math.cos(i * Math.PI / 180.0) * radius);
        }

        glEnd();
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LINE_SMOOTH);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

    }

    public static void drawCircleRect(float left, float top, float right, float bottom, float radius, int color) {
        drawCircleRect(left, top, right, bottom, radius, 4, color);
    }

    public static void drawCircleRect(float left, float top, float right, float bottom, float radius, int diff, int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        glColor4f(red, green, blue, alpha);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glPushMatrix();
        glLineWidth(1.0F);
        glBegin(GL_POLYGON);

        glVertex2d(right, bottom - radius);
        glVertex2d(right, top + radius);

        for (int i = 0; i <= 90; i += diff) {
            double x = right - radius + Math.cos(Math.toRadians(i)) * radius;
            double y = top + radius + Math.sin(Math.toRadians(i)) * radius;
            glVertex2d(x, y);
        }

        glVertex2d(right - radius, top);
        glVertex2d(left + radius, top);

        for (int i = 90; i <= 180; i += diff) {
            double x = left + radius + Math.cos(Math.toRadians(i)) * radius;
            double y = top + radius + Math.sin(Math.toRadians(i)) * radius;
            glVertex2d(x, y);
        }

        glVertex2d(left, top + radius);
        glVertex2d(left, bottom - radius);

        for (int i = 180; i <= 270; i += diff) {
            double x = left + radius + Math.cos(Math.toRadians(i)) * radius;
            double y = bottom - radius + Math.sin(Math.toRadians(i)) * radius;
            glVertex2d(x, y);
        }

        glVertex2d(left + radius, bottom);
        glVertex2d(right - radius, bottom);

        for (int i = 270; i <= 360; i += diff) {
            double x = right - radius + Math.cos(Math.toRadians(i)) * radius;
            double y = bottom - radius + Math.sin(Math.toRadians(i)) * radius;
            glVertex2d(x, y);
        }

        glVertex2d(right, bottom - radius);

        glEnd();
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LINE_SMOOTH);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void glColor(final int red, final int green, final int blue, final int alpha) {
        GlStateManager.color(red / 255F, green / 255F, blue / 255F, alpha / 255F);
    }

    public static void glColor(final int color) {
        int alpha = color >> 24 & 0xFF;
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;

        glColor(red, green, blue, alpha);
    }

}
