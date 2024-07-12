package me.beavermod.module.setting.impl;

import com.google.gson.JsonObject;
import me.beavermod.module.setting.Setting;
import me.beavermod.module.setting.util.DisplayName;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EnumSetting<T extends Enum<T>> extends Setting<T> {

    private final T[] values;
    private T currentValue;


    public EnumSetting(String displayName, String description, T defaultValue) {
        super(displayName, description);
        this.values = defaultValue.getDeclaringClass().getEnumConstants();
        this.currentValue = defaultValue;
    }

    public void set(T value) {
        this.currentValue = value;
    }

    public void cycleForwards() {
        int index = currentValue.ordinal() + 1;
        if (index >= values.length) index = 0;
        currentValue = values[index];
    }

    public void cycleBackwards() {
        int index = currentValue.ordinal() - 1;
        if (index < 0) index = values.length - 1;
        currentValue = values[index];
    }

    public boolean is(T value) {
        return this.currentValue == value;
    }

    public boolean isAny(T... values) {
        for (T value : values) {
            if (this.currentValue == value) return true;
        }

        return false;
    }

    public T get() {
        return this.currentValue;
    }

    public T[] getValues() {
        return this.values;
    }

    public Map<T, String> valueNameMap() {
        Map<T, String> map = new HashMap<>();
        for (T t : values) {
            map.put(t, toDisplay(t));
        }
        return map;
    }

    @Override
    public void parseString(@NotNull String str) {
        try {
            for (T value : values) {
                if (value.name().equalsIgnoreCase(str)) {
                    currentValue = value;
                    break;
                }
            }
        } catch (NullPointerException ignored) {}
    }


    @Override
    public String getDisplayValue() {
        return toDisplay(currentValue);
    }

    @Override
    public void serialize(JsonObject object) {
        object.addProperty(this.name, this.currentValue.name());
    }

    @Override
    public void deserialize(JsonObject object) {
        try {
            String valueStr = object.get(this.name).getAsString();
            for (T value : values) {
                if (value.name().equalsIgnoreCase(valueStr)) {
                    currentValue = value;
                    break;
                }
            }
        } catch (NullPointerException | UnsupportedOperationException | IllegalStateException ignored) {}
    }


    public static <U extends Enum<?>> String toDisplay(U v) {

        try {
            if (v.getClass().getField(v.name()).isAnnotationPresent(DisplayName.class)) {
                return v.getClass().getField(v.name()).getAnnotation(DisplayName.class).value();
            }
        } catch (NoSuchFieldException | NullPointerException ignored) {}

        return toDisplay(v.name());

    }

    public static String toDisplay(String str) {
        return StringUtils.capitalize(str.replace("_", " ").toLowerCase());
    }

}
