/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */


package me.beavermod.util;

import me.beavermod.Beaver;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ChatUtil {

    public static final String CHAT_PREFIX = String.format("%s[%sBeaver%s]%s %s",
            EnumChatFormatting.DARK_GRAY,
            EnumChatFormatting.AQUA,
            EnumChatFormatting.DARK_GRAY,
            EnumChatFormatting.RESET,
            EnumChatFormatting.GRAY);

    public static void send(String fmt, Object... args) {
        Beaver.INSTANCE.mc.thePlayer.addChatMessage(
                new ChatComponentText(CHAT_PREFIX + String.format(fmt, args)));
    }

    public static void print(String fmt, Object... args) {
        Beaver.INSTANCE.mc.thePlayer.addChatMessage(
                new ChatComponentText(String.format("%s> %s%s",
                        EnumChatFormatting.WHITE,
                        EnumChatFormatting.GRAY,
                        String.format(fmt, args))));
    }

}
