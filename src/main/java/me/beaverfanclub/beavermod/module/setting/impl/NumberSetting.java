package me.beaverfanclub.beavermod.module.setting.impl;

import me.beaverfanclub.beavermod.module.setting.Setting;
import me.beaverfanclub.beavermod.module.setting.util.IChanged;

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
