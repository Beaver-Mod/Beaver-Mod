package me.beavermod.command.impl;

import me.beavermod.command.Command;
import me.beavermod.module.Module;
import me.beavermod.module.ModuleManager;
import me.beavermod.util.ChatUtil;

public class ToggleCmd extends Command {

    public ToggleCmd() {
        super("Toggle", "Toggles a module", ".toggle <module>", "t");
    }

    @Override
    public void onCommand(String[] args, String rawCommand) {

        if (args.length < 2) {
            ChatUtil.send("\247cMissing arguments");
            return;
        }

        Module module = ModuleManager.INSTANCE.get(args[1]);

        if (module == null) {
            ChatUtil.send("\247c'%s' is not a module", args[1].toLowerCase());
            return;
        }

        module.toggle();
        ChatUtil.send("%s %s", module.isEnabled() ? "Enabled" : "Disabled", module.name);

    }
}
