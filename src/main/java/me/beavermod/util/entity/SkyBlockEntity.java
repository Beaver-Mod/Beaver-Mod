/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.util.entity;

import net.minecraft.entity.Entity;

public class SkyBlockEntity {

    // The entity ID of the actual entity
    public final Entity entity;

    // the entity ID of the armor stand with the name tag
    public final Entity nameTag;

    // The type of entity, either a Player a Mob or an NPC
    public final Type type;

    public SkyBlockEntity(Entity entity, Entity nameTag, Type type) {
        this.entity = entity;
        this.nameTag = nameTag;
        this.type = type;
    }

    public enum Type {
        PLAYER,
        MOB,
        NPC
    }

}
