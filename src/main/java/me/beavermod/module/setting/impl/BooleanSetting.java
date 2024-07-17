/*
 * This file is apart of Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>
 * Copyright (C) 2024  Beaver Fan Club
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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

    public void toggle() {
        value = !value;
    }

    @Override
    public void parseString(String str) {
        this.value = str.equalsIgnoreCase("true") || str.equalsIgnoreCase("1");
    }

    @Override
    public String getDisplayValue() {
        return value ? "True" : "False";
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
