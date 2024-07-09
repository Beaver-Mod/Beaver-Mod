package me.beavermod.command.impl;

import me.beavermod.command.Command;
import me.beavermod.command.CommandManager;
import me.beavermod.util.ChatUtil;

public class HelpCmd extends Command {

    public HelpCmd() {
        super("Help", "Displays help message", ".help", "?");
    }

    @Override
    public void onCommand(String[] args, String rawCommand) {
        for (Command command : CommandManager.INSTANCE.keySet()) {
            ChatUtil.print("\247b%s\2477: %s", command.name, command.description);
        }
    }
}
