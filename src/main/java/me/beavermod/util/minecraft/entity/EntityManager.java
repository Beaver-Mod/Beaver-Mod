/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.util.minecraft.entity;

import me.beavermod.Beaver;
import me.beavermod.event.JoinEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class EntityManager {

    private static final Map<Integer, SkyBlockEntity> ENTITIES = new HashMap<>();

    public static void addEntity(@Nullable EntityLivingBase entity) {

        if (entity == null || ENTITIES.containsKey(entity.getEntityId())) return;

        String displayName = entity.getDisplayName().getFormattedText();

        if (entity instanceof EntityPlayer && EntityUtil.PLAYER_REGEX.matcher(displayName).matches()) {
            ENTITIES.put(entity.getEntityId(), new SkyBlockEntity(entity, SkyBlockEntity.Type.PLAYER));
            return;
        }

        EntityArmorStand armorStand = EntityUtil.findAssociatedArmorStand(entity);

        if (armorStand == null) return;
        displayName = armorStand.getDisplayName().getFormattedText();

        if (EntityUtil.MOB_REGEX.matcher(displayName).matches() || EntityUtil.SLAYER_BOSS_REGEX.matcher(displayName).matches() || EntityUtil.SLAYER_MINI_BOSS_REGEX.matcher(displayName).matches()) {
            ENTITIES.put(entity.getEntityId(), new SkyBlockEntity(entity, SkyBlockEntity.Type.MOB));
            return;
        }

        if (!(entity instanceof EntityArmorStand)) {
            ENTITIES.put(entity.getEntityId(), new SkyBlockEntity(entity, SkyBlockEntity.Type.NPC));
        }

    }

    public static SkyBlockEntity getSkyBlockEntity(EntityLivingBase entity) {
        return getSkyBlockEntity(entity.getEntityId());
    }

    public static SkyBlockEntity getSkyBlockEntity(int id) {
        if (!ENTITIES.containsKey(id)) {
            Entity entity = Beaver.INSTANCE.mc.theWorld.getEntityByID(id);
            if (!(entity instanceof EntityLivingBase)) return null;
            addEntity((EntityLivingBase)entity);
        }
        return ENTITIES.getOrDefault(id, null);
    }

    @SubscribeEvent
    public static void reset(JoinEvent event) {
        ENTITIES.clear();
    }

    static {
        MinecraftForge.EVENT_BUS.register(EntityManager.class);
    }

}
