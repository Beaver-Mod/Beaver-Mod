package me.beavermod.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class Render2DEvent extends Event {

    public final float delta;

    public Render2DEvent(float delta) {
        this.delta = delta;
    }

}
