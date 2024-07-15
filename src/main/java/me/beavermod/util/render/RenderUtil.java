/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.util.render;

import me.beavermod.Beaver;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil {

    // Convert 2D buffers
    private static FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
    private static IntBuffer viewport = BufferUtils.createIntBuffer(16);
    private static FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
    private static FloatBuffer projection = BufferUtils.createFloatBuffer(16);

    public static void start() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
    }

    public static void stop() {
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        glEnable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        color(Color.WHITE);
    }

    public static void startSmooth() {
        glEnable(GL_POLYGON_SMOOTH);
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_POINT_SMOOTH);
    }

    public static void endSmooth() {
        glDisable(GL_POINT_SMOOTH);
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_POLYGON_SMOOTH);
    }

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
        glBegin(GL_TRIANGLE_FAN);

        for(int i = 0; i <= 360; i++) {
            glVertex2d(x + Math.sin(i * Math.PI / 180.0) * radius, y + Math.cos(i * Math.PI / 180.0) * radius);
        }

        glEnd();
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LINE_SMOOTH);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

    }

    public static void drawOutlineCircleRect(float left, float top, float right, float bottom, float radius, float lineWidth, int color) {
        drawOutlineCircleRect(left, top, right, bottom, radius, lineWidth, 4, color);
    }

    public static void drawOutlineCircleRect(float left, float top, float right, float bottom, float radius, float lineWidth, int diff, int color) {
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
        glLineWidth(lineWidth);
        glBegin(GL_LINE_STRIP);

        glVertex2d(right, bottom - radius);
        glVertex2d(right, top + radius);

        for (int i = 0; i <= 90; i += diff) {
            double x = right - radius + Math.cos(Math.toRadians(i)) * radius;
            double y = top + radius - Math.sin(Math.toRadians(i)) * radius;
            glVertex2d(x, y);
        }

        glVertex2d(right - radius, top);
        glVertex2d(left + radius, top);

        for (int i = 90; i <= 180; i += diff) {
            double x = left + radius + Math.cos(Math.toRadians(i)) * radius;
            double y = top + radius - Math.sin(Math.toRadians(i)) * radius;
            glVertex2d(x, y);
        }

        glVertex2d(left, top + radius);
        glVertex2d(left, bottom - radius);

        for (int i = 180; i <= 270; i += diff) {
            double x = left + radius + Math.cos(Math.toRadians(i)) * radius;
            double y = bottom - radius - Math.sin(Math.toRadians(i)) * radius;
            glVertex2d(x, y);
        }

        glVertex2d(left + radius, bottom);
        glVertex2d(right - radius, bottom);

        for (int i = 270; i <= 360; i += diff) {
            double x = right - radius + Math.cos(Math.toRadians(i)) * radius;
            double y = bottom - radius - Math.sin(Math.toRadians(i)) * radius;
            glVertex2d(x, y);
        }

        glVertex2d(right, bottom - radius);

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
        glBegin(GL_TRIANGLE_FAN);

        glVertex2d(right, bottom - radius);
        glVertex2d(right, top + radius);

        for (int i = 0; i <= 90; i += diff) {
            double x = right - radius + Math.cos(Math.toRadians(i)) * radius;
            double y = top + radius - Math.sin(Math.toRadians(i)) * radius;
            glVertex2d(x, y);
        }

        glVertex2d(right - radius, top);
        glVertex2d(left + radius, top);

        for (int i = 90; i <= 180; i += diff) {
            double x = left + radius + Math.cos(Math.toRadians(i)) * radius;
            double y = top + radius - Math.sin(Math.toRadians(i)) * radius;
            glVertex2d(x, y);
        }

        glVertex2d(left, top + radius);
        glVertex2d(left, bottom - radius);

        for (int i = 180; i <= 270; i += diff) {
            double x = left + radius + Math.cos(Math.toRadians(i)) * radius;
            double y = bottom - radius - Math.sin(Math.toRadians(i)) * radius;
            glVertex2d(x, y);
        }

        glVertex2d(left + radius, bottom);
        glVertex2d(right - radius, bottom);

        for (int i = 270; i <= 360; i += diff) {
            double x = right - radius + Math.cos(Math.toRadians(i)) * radius;
            double y = bottom - radius - Math.sin(Math.toRadians(i)) * radius;
            glVertex2d(x, y);
        }

        glVertex2d(right, bottom - radius);

        glEnd();
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LINE_SMOOTH);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void color(Color color) {
        color(color.getRGB());
    }

    public static void color(final int red, final int green, final int blue, final int alpha) {
        GlStateManager.color(red / 255F, green / 255F, blue / 255F, alpha / 255F);
    }

    public static void color(final int color) {
        int alpha = color >> 24 & 0xFF;
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;

        color(red, green, blue, alpha);
    }

    public static void scissor(double x, double y, double width, double height) {
        final ScaledResolution sr = new ScaledResolution(Beaver.INSTANCE.mc);
        final double scale = sr.getScaleFactor();

        y = sr.getScaledHeight() - y;

        x *= scale;
        y *= scale;
        width *= scale;
        height *= scale;

        glScissor((int) x, (int) (y - height), (int) width, (int) height);
    }

    public static Vector3d to2D(int scaleFactor, double x, double y, double z) {

        glGetFloat(GL_MODELVIEW_MATRIX, modelView);
        glGetFloat(GL_PROJECTION_MATRIX, projection);
        glGetInteger(GL_VIEWPORT, viewport);

        return GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCoords)
                ? new Vector3d(screenCoords.get(0) / (float)scaleFactor, ((float)Display.getHeight() - screenCoords.get(1)) / (float)scaleFactor, screenCoords.get(2))
                : null;


    }

    public static Color darkenColor(Color color, int amount) {
        for (int i = 0; i < amount; i++) {
            color = color.darker();
        }

        return color;
    }


}
