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

package me.beaverfanclub.beavermod.util.minecraft.render;

import me.beaverfanclub.beavermod.Beaver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil {

    private static final Minecraft mc = Beaver.INSTANCE.mc;

    // Convert 2D buffers
    private static final FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
    private static final IntBuffer viewport = BufferUtils.createIntBuffer(16);
    private static final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer projection = BufferUtils.createFloatBuffer(16);

    private static final Frustum frustum = new Frustum();

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

    public static void drawRect(double left, double top, double right, double bottom, int color)
    {
        if (left < right)
        {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        GlStateManager.color(r, g, b, a);
        worldrenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
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

    public static boolean isInViewFrustum(Entity entity) {
        return isInViewFrustum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public static boolean isInViewFrustum(TileEntity tile) {
        return isInViewFrustum(tile.getBlockType().getSelectedBoundingBox(mc.theWorld, tile.getPos()));
    }

    private static boolean isInViewFrustum(AxisAlignedBB bb) {
        Entity current = mc.getRenderViewEntity();
        frustum.setPosition(current.posX, current.posY, current.posZ);
        return frustum.isBoundingBoxInFrustum(bb);
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
