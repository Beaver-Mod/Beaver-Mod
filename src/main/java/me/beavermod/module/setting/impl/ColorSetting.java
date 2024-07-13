package me.beavermod.module.setting.impl;

import com.google.gson.JsonObject;
import me.beavermod.module.setting.Setting;

import java.awt.*;

public class ColorSetting extends Setting<Color> {

    private Color color;

    public ColorSetting(String displayName, String description) {
        super(displayName, description);
    }

    @Override
    public Color get() {
        return this.color;
    }

    @Override
    public void set(Color value) {
        this.color = value;
    }

    @Override
    public void parseString(String str) {

    }

    @Override
    public String getDisplayValue() {
        return Integer.toHexString(color.getRGB());
    }

    @Override
    public void serialize(JsonObject object) {
        object.addProperty(this.name, this.color.getRGB());
    }

    @Override
    public void deserialize(JsonObject object) {
        this.color = new Color(object.get(this.name).getAsInt());
    }
}
