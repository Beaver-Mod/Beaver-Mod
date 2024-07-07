package me.beavermod.module.setting;

import com.google.gson.JsonObject;

public abstract class Setting<T> {

    public final String name;
    public final String description;


    public Setting(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract T get();

    public abstract void set(T value);

    public abstract void parseString(String str);

    public abstract void serialize(JsonObject object);
    public abstract void deserialize(JsonObject object);


}
