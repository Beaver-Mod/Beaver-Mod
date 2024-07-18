package me.beaverfanclub.beavermod.module.setting.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.beaverfanclub.beavermod.Beaver;
import me.beaverfanclub.beavermod.module.setting.Setting;

import java.util.ArrayList;
import java.util.List;

public class StringListSetting extends Setting<List<String>> {

    private final List<String> list = new ArrayList<>();

    public StringListSetting(String name, String description) {
        super(name, description);
    }

    @Override
    public List<String> get() {
        return list;
    }

    @Override
    public void set(List<String> value) {
        list.clear();
        list.addAll(value);
    }

    @Override
    public void parseString(String str) {
        // No...
    }

    @Override
    public String getDisplayValue() {
        return String.join(", ", list);
    }

    @Override
    public void serialize(JsonObject object) {
        object.add(name, Beaver.GSON.toJsonTree(list.toArray()));
    }

    @Override
    public void deserialize(JsonObject object) {
        list.clear();
        JsonArray array = object.getAsJsonArray(name);
        for (JsonElement element : array) {
            try {
                list.add(element.getAsString());
            } catch (NullPointerException | UnsupportedOperationException | IllegalStateException ignored) {}
        }
    }
}
