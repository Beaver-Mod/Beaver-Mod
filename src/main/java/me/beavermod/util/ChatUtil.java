package me.beavermod.util;

import me.beavermod.Beaver;
import net.minecraft.util.ChatComponentText;

public class ChatUtil {

    public static final String CHAT_PREFIX = "\2478[\247bBeaver\2478]\247r \2477";

    public static void send(String fmt, Object... args) {
        Beaver.INSTANCE.mc.thePlayer.addChatMessage(new ChatComponentText(CHAT_PREFIX + String.format(fmt, args)));
    }

    public static void print(String fmt, Object... args) {
        Beaver.INSTANCE.mc.thePlayer.addChatMessage(new ChatComponentText("\247f> \2477" + String.format(fmt, args)));
    }

}
