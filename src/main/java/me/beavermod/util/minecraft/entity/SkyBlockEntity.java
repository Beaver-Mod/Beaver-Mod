/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.util.minecraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.EnumChatFormatting;

public class SkyBlockEntity {

    // The entity ID of the actual entity
    public final EntityLivingBase entity;

    // The type of entity, either a Player a Mob or an NPC
    public final Type type;

    public SkyBlockEntity(EntityLivingBase entity, Type type) {
        this.entity = entity;
        this.type = type;
    }

    public EntityArmorStand getArmorStand() {
        return EntityUtil.findAssociatedArmorStand(entity);
    }

    public String getNameTag() {

        try {
            switch (type) {
                case PLAYER:
                    return entity.getDisplayName().getFormattedText();

                case MOB:
                case NPC:
                    String name = getArmorStand().getDisplayName().getFormattedText();
                    if (name.isEmpty()) {
                        return EnumChatFormatting.DARK_GRAY + "Loading...";
                    }
                    return name;
            }
        } catch (NullPointerException ignored) {}

        return null;
    }

    public int getLevel() {
        return 0;
    }

    public int getHealth() {
        return 0;
    }

    public int getMaxHealth() {
        return 0;
    }

    public enum Type {
        PLAYER,
        MOB,
        NPC
    }

}
