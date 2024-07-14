/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.command;

public abstract class Command {

    public final String name;
    public final String description;
    public final String syntax;
    public final String[] aliases;

    protected Command(String name, String description, String syntax, String... aliases) {
        this.name = name;
        this.description = description;
        this.syntax = syntax;
        this.aliases = aliases;
    }

    public boolean nameMatches(String name) {
        if (this.name.equalsIgnoreCase(name)) return true;

        for (String alias : this.aliases) {
            if (alias.equalsIgnoreCase(name)) return true;
        }

        return false;
    }

    public abstract void onCommand(String[] args, String rawCommand);

}
