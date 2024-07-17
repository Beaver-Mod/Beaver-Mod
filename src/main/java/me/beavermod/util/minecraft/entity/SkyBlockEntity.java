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

package me.beavermod.util.minecraft.entity;

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
