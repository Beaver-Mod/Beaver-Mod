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

package me.beavermod.module.setting;

import com.google.gson.JsonObject;
import me.beavermod.module.setting.util.IChanged;

import java.text.DecimalFormat;

public abstract class Setting<T> {

    public static final DecimalFormat DEFAULT_DECIMAL_FORMAT = new DecimalFormat("#0.##");

    public final String name;
    public final String displayName;
    public final String description;

    protected final IChanged onChanged;

    public Setting(String displayName, String description) {
        this.name = displayName.toLowerCase().replace(' ', '-');
        this.displayName = displayName;
        this.description = description;
        onChanged = null;
    }

    public Setting(String displayName, String description, IChanged onChanged) {
        this.name = displayName.toLowerCase().replace(' ', '-');
        this.displayName = displayName;
        this.description = description;
        this.onChanged = onChanged;
    }

    public abstract T get();

    public abstract void set(T value);

    public abstract void parseString(String str);

    public abstract String getDisplayValue();

    public abstract void serialize(JsonObject object);
    public abstract void deserialize(JsonObject object);


}
