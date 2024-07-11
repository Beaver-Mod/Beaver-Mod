package me.beavermod.command;


import me.beavermod.Beaver;
import me.beavermod.event.SendPacketEvent;
import me.beavermod.util.ChatUtil;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.reflections.Reflections;

import java.util.LinkedHashMap;

public class CommandManager extends LinkedHashMap<Command, Class<? extends Command>> {

    public static final String PREFIX = ".";

    public static CommandManager INSTANCE = null;

    public static void init() {
        INSTANCE = new CommandManager();
        INSTANCE.addCommands();
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    public void addCommands() {
        new Reflections("me.beavermod.command.impl").getSubTypesOf(Command.class).forEach(command -> {
            try {
                Beaver.LOGGER.info("Add Command: {}", command.getSimpleName());
                this.put(command.newInstance(), command);
            } catch (InstantiationException | IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @SubscribeEvent
    public void handleChat(SendPacketEvent event) {
        if (event.getPacket() instanceof C01PacketChatMessage) {
            String message = ((C01PacketChatMessage) event.getPacket()).getMessage();

            if (message.startsWith(PREFIX)) {

                event.setCanceled(true);
                String[] args = message.split("\\s+");

                String commandName = args[0].substring(PREFIX.length());

                for (Command command : this.keySet()) {
                    if (command.nameMatches(commandName)) {
                        try {
                            command.onCommand(args, message);
                        } catch (Exception exception) {
                            ChatUtil.send("%sError: %s", EnumChatFormatting.RED, exception.getMessage());
                        }
                        return;
                    }
                }

                ChatUtil.send("%s'%s' is not a command", EnumChatFormatting.RED, commandName.toLowerCase());

            }
        }
    }

}
