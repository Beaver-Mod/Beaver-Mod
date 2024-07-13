package me.beavermod.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class Render2DEvent extends Event {

    public final float partialTicks;

    public Render2DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

}
