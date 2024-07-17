package me.beavermod.module.impl.visual;

import me.beavermod.event.PreTickEvent;
import me.beavermod.module.Module;
import me.beavermod.module.setting.impl.BooleanSetting;
import me.beavermod.module.setting.impl.FloatSetting;
import me.beavermod.module.setting.impl.IntSetting;
import me.beavermod.module.setting.impl.SeperatorSetting;
import me.beavermod.ui.font.FontManager;
import me.beavermod.util.minecraft.entity.EntityUtil;
import me.beavermod.util.minecraft.entity.SkyBlockEntity;
import me.beavermod.util.misc.Reflection;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class NameTags extends Module {

    // Entities
    private final BooleanSetting showPlayers = new BooleanSetting("Players", "Show players", false);
    private final BooleanSetting showMobs = new BooleanSetting("Mobs", "Show mobs", true);
//    private final BooleanSetting showNpcs = new BooleanSetting("NPCs", "Show NCP's", false);
    private final IntSetting renderDistance = new IntSetting("Render t5rDistance", "Distance an entity has to be within to render their name tag", 1, 250, 100, "%d blocks", null);

    // Name Tag
    private final BooleanSetting background = new BooleanSetting("Background", "Name tag background", true);
    private final BooleanSetting scaleFix = new BooleanSetting("Scale Fix", "Removes scaling", true);
    private final BooleanSetting heldItem = new BooleanSetting("Held Item", "Show held item", true);

    public NameTags() {
        super("Name Tags", "Better name tags", Category.VISUAL);

        addSettings(
                new SeperatorSetting("Entities"),
                showPlayers, showMobs, renderDistance,

                new SeperatorSetting("Rendering"),
                background, scaleFix, heldItem
        );
    }

    @SubscribeEvent
    public void onRender2D(RenderLivingEvent.Specials.Pre<?> event) {

        if (mc.theWorld == null) {
            return;
        }

        EntityLivingBase entity = event.entity;

        if (!shouldRenderNameTag(entity)) return;
        event.setCanceled(true);

        String name = entity.getDisplayName().getFormattedText();
        FontRenderer font = FontManager.BIT_FONT;
        float scale = 1.0F / 37.5F;
        int halfWidth = font.getStringWidth(name) / 2;

        GlStateManager.pushMatrix();

        GlStateManager.translate((float)event.x, (float)event.y + entity.height + 0.5F, (float)event.z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);

        if (scaleFix.get()) {
            final float delta = Reflection.getTimer().renderPartialTicks;
            final EntityPlayer clientPlayer = mc.thePlayer;
            final double x = clientPlayer.lastTickPosX + (clientPlayer.posX - clientPlayer.lastTickPosX) * delta - (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * delta);
            final double y = clientPlayer.lastTickPosY + (clientPlayer.posY - clientPlayer.lastTickPosY) * delta - (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * delta);
            final double z = clientPlayer.lastTickPosZ + (clientPlayer.posZ - clientPlayer.lastTickPosZ) * delta - (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * delta);
            final double xyz = MathHelper.sqrt_double(x * x + y * y + z * z);
            final float scaleFactor = (float) Math.max(scale, 0.003 * xyz + 0.011);
            final float offset = (float) (-(Math.max(0.07, -0.03866143897175789 + 0.018833419308066368 * xyz - 5.270970286801457E-4 * Math.pow(xyz, 2.0) + 5.4459292186948005E-6 * Math.pow(xyz, 3.0) - 1.9360259173595296E-8 * Math.pow(xyz, 4.0)) * xyz));
            GlStateManager.scale(-scaleFactor, -scaleFactor, scaleFactor);
            GlStateManager.translate(0.0f, offset, 0.0f);
        } else {
            GlStateManager.scale(-scale, -scale, scale);
            if (entity.isSneaking()) {
                GlStateManager.translate(0.0F, 9.375F, 0.0F);
            }
        }

        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);

        if (background.get()) {
            GlStateManager.disableTexture2D();
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos(-halfWidth - 1, -1, 0.0).color(0.0F, 0.0F, 0.0F, 0.67F).endVertex();
            worldrenderer.pos(-halfWidth - 1, 8, 0.0).color(0.0F, 0.0F, 0.0F, 0.67F).endVertex();
            worldrenderer.pos(halfWidth + 1, 8, 0.0).color(0.0F, 0.0F, 0.0F, 0.67F).endVertex();
            worldrenderer.pos(halfWidth + 1, -1, 0.0).color(0.0F, 0.0F, 0.0F, 0.67F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
        }

        font.drawString(name, -halfWidth, 0, -1, true);

        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();

    }

    public boolean shouldRenderNameTag(EntityLivingBase entity) {

        String name = entity.getDisplayName().getFormattedText();

        if (entity instanceof EntityPlayer) {
            return showPlayers.get() && EntityUtil.PLAYER_REGEX.matcher(name).matches();

        } else if (entity instanceof EntityArmorStand) {
            if (showMobs.get()) {
                return EntityUtil.MOB_REGEX.matcher(name).matches() || EntityUtil.SLAYER_BOSS_REGEX.matcher(name).matches() || EntityUtil.SLAYER_MINI_BOSS_REGEX.matcher(name).matches();
            }
        }

        return false;

    }

}
