package me.beavermod.mixin.impl;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin extends Render {

    protected RendererLivingEntityMixin(RenderManager renderManager) {
        super(renderManager);
    }

}
