package me.beavermod.module.setting.impl;

import com.google.gson.JsonObject;
import me.beavermod.module.setting.Setting;

public class SeperatorSetting extends Setting<Object> {


    public SeperatorSetting(String name) {
        super(name, null);
    }

    @Override
    public Object get() {
        return null;
    }

    @Override
    public void parseString(String str) {

    }

    @Override
    public String getDisplayValue() {
        return null;
    }

    @Override
    public void serialize(JsonObject object) {

    }

    @Override
    public void deserialize(JsonObject object) {

    }

    @Override
    public void set(Object value) {

    }
}
