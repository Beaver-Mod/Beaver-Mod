/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.util.entity;

import me.beavermod.Beaver;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;

public class EntityManager {

    private static final Map<Integer, SkyBlockEntity> entities = new HashMap<>();

    // TODO: finish this...
    public static void addEntity(Entity entity) {
        if (entity instanceof EntityPlayer) {
            if (isInTab((EntityPlayer)entity)) {
                entities.put(entity.getEntityId(), new SkyBlockEntity(entity, null, SkyBlockEntity.Type.PLAYER));
            }
        }
    }

    public static EntityArmorStand findAssociatedArmorStand(Entity entity) {
        for (EntityArmorStand armorStand : Beaver.INSTANCE.mc.theWorld.getEntities(EntityArmorStand.class, input -> true)) {
            if (armorStand.getDistanceToEntity(entity) > 0.5F) continue;
        }

        return null;
    }

    public static boolean isInTab(EntityPlayer player) {
        for (NetworkPlayerInfo info : Beaver.INSTANCE.mc.getNetHandler().getPlayerInfoMap()) {
            if (info.getGameProfile().getName().equals(player.getName())) {
                return true;
            }
        }

        return false;
    }

}
