/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.module;

import me.beavermod.Beaver;
import me.beavermod.util.ChatUtil;
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
        new Reflections("me.beavermod.module.impl").getSubTypesOf(Module.class).forEach(module -> {
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
        return Objects.requireNonNull(keySet().stream()
                .filter(module -> module.getClass() == clazz)
                .findFirst()
                .orElse(null)
        ).isEnabled();
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
