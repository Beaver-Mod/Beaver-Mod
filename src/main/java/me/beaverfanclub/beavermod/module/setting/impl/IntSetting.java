package me.beaverfanclub.beavermod.module.setting.impl;

import com.google.gson.JsonObject;
import me.beaverfanclub.beavermod.module.setting.util.IChanged;

public class IntSetting extends NumberSetting<Integer> {

    private int value;
    public final int min, max;
    public final String format;

    public IntSetting(String name, String description, int min, int max, int defaultValue, String format, IChanged onChange) {
        super(name, description, onChange);

        if (min > max) throw new IllegalArgumentException("Minimum value must be smaller than maximum value");

        this.min = min;
        this.max = max;
        this.value = defaultValue;
        this.format = format;
    }

    public IntSetting(String name, String description, int min, int max, int defaultValue) {
        this(name, description, min, max, defaultValue, "%d", null);
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

        if (onChanged != null) {
            onChanged.onChanged();
        }
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

    @Override
    public Integer getRange() {
        return max - min;
    }

    @Override
    public float getPercent() {
        return (float)value / (float)(max - min);
    }
}
