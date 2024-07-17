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

package me.beavermod.module;

import me.beavermod.Beaver;
import me.beavermod.util.minecraft.ChatUtil;
import org.reflections.Reflections;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModuleManager extends LinkedHashMap<Module, Class<? extends Module>> {

    public static ModuleManager INSTANCE = null;

    public static void init() {
        INSTANCE = new ModuleManager();
        INSTANCE.addModules();
    }

    public void addModules() {
        new Reflections("me.beavermod.module.impl")
                .getSubTypesOf(Module.class)
                .stream()
                .sorted(Comparator.comparing(Class::getSimpleName))
                .forEach(module -> {
                    try {
                        Beaver.LOGGER.info("Add Module: {}", module.getSimpleName());
                        this.put(module.newInstance(), module);
                    } catch (InstantiationException | IllegalAccessException exception) {
                        throw new RuntimeException(exception);
                    }
                });
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T get(Class<T> clazz) {
        return (T) this.keySet()
                .stream()
                .filter(module -> module.getClass() == clazz)
                .findFirst()
                .orElse(null);
    }

    public Module get(String name) {
        return this.keySet()
                .stream()
                .filter(module -> module.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public boolean isEnabled(Class<? extends Module> clazz) {
        try {
            return keySet().stream()
                    .filter(module -> module.getClass() == clazz)
                    .findFirst()
                    .orElse(null)
                    .isEnabled();
        } catch (NullPointerException ignored) {}

        return false;
    }

    public List<Module> search(String name) {
        return keySet()
                .stream()
                .filter(module -> module.matches(name))
                .collect(Collectors.toList());
    }

    public List<Module> getInCategory(Module.Category category) {
        return keySet()
                .stream()
                .filter(module -> module.category == category)
                .collect(Collectors.toList());
    }

    public void onKeyPress(int key) {
        for (Module module : keySet()) {
            if (module.getKey() == key) {
                module.toggle();
            }
        }
    }

}
