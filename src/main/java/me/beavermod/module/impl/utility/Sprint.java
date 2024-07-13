package me.beavermod.module.impl.utility;

import me.beavermod.event.PreTickEvent;
import me.beavermod.module.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", "Makes you sprint", Category.UTILITY);
    }

    @Override
    public void onDisabled() {
        if (mc.thePlayer != null && mc.inGameHasFocus) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
        }
    }

    @SubscribeEvent
    public void onPreTick(PreTickEvent event) {
        if (mc.thePlayer != null && mc.inGameHasFocus) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
        }
    }

}
