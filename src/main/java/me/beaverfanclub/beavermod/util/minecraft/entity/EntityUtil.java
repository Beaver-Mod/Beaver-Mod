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

package me.beaverfanclub.beavermod.util.minecraft.entity;

import me.beaverfanclub.beavermod.util.minecraft.ChatUtil;
import me.beaverfanclub.beavermod.Beaver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.EnumChatFormatting;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

public class EntityUtil {

    public static final Pattern PLAYER_REGEX = Pattern.compile("§r§8\\[§[a-z0-9][0-9]+§8] §[a-z0-9][A-Za-z0-9_]+.*§r");
    public static final Pattern MOB_REGEX = Pattern.compile("§[58]\\[§[7d]Lv[0-9]+§[58]] §[c4][A-Za-z0-9§ ]+§r §[a-z0-9][0-9,.kMBT]+§f/§[a-z0-9][0-9,.kMBT]+§c❤§r");
    public static final Pattern SLAYER_BOSS_REGEX = Pattern.compile("§c☠ §[a-z0-9][A-Za-z ]+ §[a-z0-9][0-9,.kMBT]+§c❤§r");
    public static final Pattern SLAYER_MINI_BOSS_REGEX = Pattern.compile("§c[A-Za-z ]+ §[a-z0-9][0-9,.kMBT]+§c❤§r");

    public static EntityArmorStand findAssociatedArmorStand(@NotNull Entity entity) {
        return Beaver.INSTANCE.mc.theWorld.getEntities(EntityArmorStand.class, Objects::nonNull)
                .stream()
                .filter(e -> e != entity && horizontalDistance(e, entity) <= 1.0 && verticalDistance(e, entity) <= entity.height + 0.5)
                .min((o1, o2) -> Float.compare(o1.getDistanceToEntity(entity), o2.getDistanceToEntity(entity)))
                .orElse(null);
    }

    public static void printEntityDebugInfo(@NotNull Entity entity) {

        EntityArmorStand armorStand = findAssociatedArmorStand(entity);


        ChatUtil.print("%s--- Entity Info ---", EnumChatFormatting.AQUA);
        ChatUtil.print("%sID%s: %d", EnumChatFormatting.AQUA, EnumChatFormatting.GRAY, entity.getEntityId());
        ChatUtil.print("%sName%s: %s", EnumChatFormatting.AQUA, EnumChatFormatting.GRAY,  entity.getName());
        ChatUtil.print("%sDisplay%s: %s", EnumChatFormatting.AQUA, EnumChatFormatting.GRAY, entity.getDisplayName().getFormattedText());
        ChatUtil.print("%sPosition%s: (%f,%f,%f)", EnumChatFormatting.AQUA, EnumChatFormatting.GRAY, entity.posX, entity.posY, entity.posZ);
        ChatUtil.print("%sClass%s: %s", EnumChatFormatting.AQUA, EnumChatFormatting.GRAY,  entity.getClass().getSimpleName());

        if (armorStand == null) {
            ChatUtil.print("%sArmor Stand%s: None", EnumChatFormatting.AQUA, EnumChatFormatting.GRAY, "None");
        } else {
            ChatUtil.Builder.of(String.format("%s%sArmor Stand%s: %d", ChatUtil.SHORT_PREFIX, EnumChatFormatting.AQUA, EnumChatFormatting.GRAY, armorStand.getEntityId()))
                    .setHoverEvent("View Armor Stand Info")
                    .setClickEvent(ClickEvent.Action.RUN_COMMAND, ".debug entityinfo " + armorStand.getEntityId())
                    .send();
        }

    }

    public static double horizontalDistance(Entity entity1, Entity entity2) {
        double x = entity1.posX - entity2.posX;
        double z = entity1.posZ - entity2.posZ;
        return Math.sqrt(x * x + z * z);
    }

    public static double verticalDistance(Entity entity1, Entity entity2) {
        return Math.abs(entity1.posY - entity2.posY);
    }

}
