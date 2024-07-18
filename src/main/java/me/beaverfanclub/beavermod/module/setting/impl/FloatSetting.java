package me.beaverfanclub.beavermod.module.setting.impl;

import com.google.gson.JsonObject;
import me.beaverfanclub.beavermod.module.setting.util.IChanged;

import java.text.DecimalFormat;

public class FloatSetting extends NumberSetting<Float> {

    private float value;
    public final float min, max;
    public DecimalFormat format;

    public FloatSetting(String name, String description, float min, float max, float defaultValue, DecimalFormat format, IChanged onChanged) {
        super(name, description, onChanged);

        if (min > max) throw new IllegalArgumentException("Minimum value must be smaller than maximum value");

        this.min = min;
        this.max = max;
        this.value = defaultValue;
        this.format = format;
    }

    public FloatSetting(String name, String description, float min, float max, float defaultValue) {
        this(name, description, min, max, defaultValue, DEFAULT_DECIMAL_FORMAT, null);
    }

    @Override
    public Float get() {
        return value;
    }

    @Override
    public void set(Float value) {
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
            this.value = Float.parseFloat(str);
        } catch (NumberFormatException ignored) {}
    }

    @Override
    public String getDisplayValue() {
        return format.format(value);
    }

    @Override
    public void serialize(JsonObject object) {
        object.addProperty(this.name, this.value);
    }

    @Override
    public void deserialize(JsonObject object) {
        this.value = object.get(this.name).getAsFloat();
    }

    @Override
    public Float getRange() {
        return max - min;
    }

    @Override
    public float getPercent() {
        return value / (max - min);
    }
}
