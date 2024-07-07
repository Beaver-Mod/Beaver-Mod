package me.beavermod.module.setting.impl;

import com.google.gson.JsonObject;
import me.beavermod.module.setting.Setting;

public class BooleanSetting extends Setting<Boolean> {

    private boolean value;

    public BooleanSetting(String name, String description, boolean defaultValue) {
        super(name, description);
        this.value = defaultValue;
    }

    @Override
    public Boolean get() {
        return value;
    }

    @Override
    public void set(Boolean value) {
        this.value = value;
    }

    @Override
    public void parseString(String str) {
        this.value = str.equalsIgnoreCase("true") || str.equalsIgnoreCase("1");
    }

    @Override
    public void serialize(JsonObject object) {
        object.addProperty(name, value);
    }

    @Override
    public void deserialize(JsonObject object) {
        this.value = object.get(name).getAsBoolean();
    }
}
