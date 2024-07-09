package me.beavermod.util;

import me.beavermod.Beaver;

public class ChatUtil {

    public static final String chatPrefix = "\2478[\247bBeaver\2478]\247r \2477";

    public static void send(String fmt, Object... args) {
        Beaver.INSTANCE.mc.thePlayer.sendChatMessage(chatPrefix + String.format(fmt, args));
    }

    // Sends a chat message without the prefix
    public static void print(String fmt, Object... args) {
        Beaver.INSTANCE.mc.thePlayer.sendChatMessage(String.format(fmt, args));
    }

}
