package me.beavermod.mixin.impl;

import me.beavermod.module.ModuleManager;
import me.beavermod.module.impl.visual.NoRender;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin implements IResourceManagerReloadListener {

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    private void noHurtCamera(float partialTicks, CallbackInfo ci) {
        if (ModuleManager.INSTANCE.get(NoRender.class).hurtCamera()) {
            ci.cancel();
        }
    }

}
