package me.beavermod.mixin.impl;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.item.EntityArmorStand;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArmorStandRenderer.class)
public abstract class ArmorStandRendererMixin extends RendererLivingEntity<EntityArmorStand> {

    public ArmorStandRendererMixin(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

}
