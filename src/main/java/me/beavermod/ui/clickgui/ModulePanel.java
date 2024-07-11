package me.beavermod.ui.clickgui;

import me.beavermod.module.Module;

public class ModulePanel {

    public final Module module;
    public boolean expanded = false;
    public int top, bottom;

    public ModulePanel(Module module) {
        this.module = module;
    }

}
