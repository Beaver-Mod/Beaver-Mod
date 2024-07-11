/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.util.entity;

public class SkyblockEntity {

    // The entity ID of the actual entity
    public final int entityId;

    // the entity ID of the armor stand with the name tag
    public final int nameTagId;

    public SkyblockEntity(int entityId, int nameTagId) {
        this.entityId = entityId;
        this.nameTagId = nameTagId;
    }

}
