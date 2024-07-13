package me.beavermod.module.impl.visual;

import me.beavermod.module.Module;
import me.beavermod.module.setting.impl.BooleanSetting;

public class NoRender extends Module {

    private final BooleanSetting hurtCamera = new BooleanSetting("Hurt Camera", "Prevents your screen from shaking when getting hit", true);

    public NoRender() {
        super("No Render", "Prevents some things from rendering", Category.VISUAL);
        addSettings(hurtCamera);
    }

    public boolean hurtCamera() {
        return this.isEnabled() && this.hurtCamera.get();
    }

}
