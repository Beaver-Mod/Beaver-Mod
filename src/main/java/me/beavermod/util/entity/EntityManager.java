package me.beavermod.util.entity;

import me.beavermod.Beaver;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
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

    public static boolean isInTab(EntityPlayer player) {
        for (NetworkPlayerInfo info : Beaver.INSTANCE.mc.getNetHandler().getPlayerInfoMap()) {
            if (info.getGameProfile().getName().equals(player.getName())) {
                return true;
            }
        }

        return false;
    }

}
