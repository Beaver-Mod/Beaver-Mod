package me.beavermod.module.impl.visual;

import me.beavermod.module.Module;
import me.beavermod.ui.clickgui.ClickGui;
import org.lwjgl.input.Keyboard;

public class ClickGuiMod extends Module {

    public ClickGuiMod() {
        super("Click GUI", "Click GUI", Category.VISUAL);
        setKey(Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnabled() {
        mc.displayGuiScreen(new ClickGui());
        this.toggle(false);
    }
}
