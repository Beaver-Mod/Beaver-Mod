package me.beavermod.module.setting.impl;

import com.google.gson.JsonObject;
import me.beavermod.module.setting.Setting;

public class IntSetting extends Setting<Integer> {

    private int value;
    public final int min, max;
    public final String format;

    public IntSetting(String name, String description, int min, int max, int defaultValue, String format) {
        super(name, description);

        if (min > max) throw new IllegalArgumentException("Minimum value must be smaller than maximum value");

        this.min = min;
        this.max = max;
        this.value = defaultValue;
        this.format = format;
    }

    public IntSetting(String name, String description, int min, int max, int defaultValue) {
        this(name, description, min, max, defaultValue, "%d");
    }

    @Override
    public Integer get() {
        return value;
    }

    @Override
    public void set(Integer value) {
        this.value = value;
        if (this.value < min) this.value = min;
        else if (this.value > max) this.value = max;
    }

    @Override
    public void parseString(String str) {
        try {
            this.value = Integer.parseInt(str);
        } catch (NumberFormatException ignored) {}
    }

    @Override
    public String getDisplayValue() {
        return String.format(format, value);
    }

    @Override
    public void serialize(JsonObject object) {
        object.addProperty(this.name, this.value);
    }

    @Override
    public void deserialize(JsonObject object) {
        this.value = object.get(this.name).getAsInt();
    }
}
