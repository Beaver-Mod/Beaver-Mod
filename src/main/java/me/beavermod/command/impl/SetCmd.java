package me.beavermod.command.impl;

import me.beavermod.command.Command;
import me.beavermod.module.Module;
import me.beavermod.module.ModuleManager;
import me.beavermod.module.setting.Setting;
import me.beavermod.util.ChatUtil;
import net.minecraft.util.EnumChatFormatting;

public class SetCmd extends Command {


    public SetCmd() {
        super("Set", "Change a setting", ".set <module> <setting> <value>");
    }

    @Override
    public void onCommand(String[] args, String rawCommand) {
        if (args.length < 4) {
            ChatUtil.send("%sMissing arguments.", EnumChatFormatting.RED);
            return;
        }

        Module module = ModuleManager.INSTANCE.get(args[1]);
        if (module == null) {
            ChatUtil.send("%s'%s' is not a valid module.", EnumChatFormatting.RED, args[1].toLowerCase());
            return;
        }

        Setting<?> setting = module.getSetting(args[2]);
        if (setting == null) {
            ChatUtil.send("%s'%s' is not a setting in %s", EnumChatFormatting.RED, args[2].toLowerCase(), module.name);
            return;
        }

        String oldValue = setting.getDisplayValue();
        setting.parseString(args[3]);

        ChatUtil.send("%s set to: %s, previously set to: %s", setting.name, setting.getDisplayValue(), oldValue);
    }
}
