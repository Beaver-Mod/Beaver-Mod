package me.beavermod.module.impl.visual;

import me.beavermod.module.Module;
import me.beavermod.module.setting.impl.BooleanSetting;
import me.beavermod.module.setting.impl.ColorSetting;
import me.beavermod.module.setting.impl.IntSetting;
import me.beavermod.module.setting.impl.SeperatorSetting;

import java.awt.*;

public class Esp extends Module {

    // Entities
    private final BooleanSetting showPlayers = new BooleanSetting("Players", "Show players in ESP", false);
    private final BooleanSetting showMobs = new BooleanSetting("Mobs", "Show mobs in ESP", true);
    private final BooleanSetting showItems = new BooleanSetting("Items", "Show items in ESP", false);
    private final IntSetting renderDistance = new IntSetting("Distance", "Distance an entity has to be within to render ESP", 1, 250, 100, "%d blocks", null);

    // Box
    private final BooleanSetting enableBox = new BooleanSetting("Enable Boxes", "Enables boxes", true);
    private final ColorSetting boxColor = new ColorSetting("Box Color", "Color of the boxes", Color.WHITE);

    public Esp() {
        super("ESP", "extra-sensory perception", Category.VISUAL);

        addSettings(
                new SeperatorSetting("Entities"),
                showPlayers, showMobs, showItems, renderDistance,

                new SeperatorSetting("Box"),
                enableBox, boxColor
        );
    }

}
