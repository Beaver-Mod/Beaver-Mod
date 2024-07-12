package me.beavermod.module.setting.impl;

import me.beavermod.module.setting.Setting;
import me.beavermod.module.setting.util.IChanged;

public abstract class NumberSetting<T extends Number> extends Setting<T> {

    public NumberSetting(String name, String description) {
        super(name, description);
    }

    public NumberSetting(String name, String description, IChanged onChange) {
        super(name, description, onChange);
    }

    public abstract T getRange();

    public abstract float getPercent();


}
