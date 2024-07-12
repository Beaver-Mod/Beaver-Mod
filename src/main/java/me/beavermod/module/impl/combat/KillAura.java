/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.module.impl.combat;

import me.beavermod.event.PreMotionEvent;
import me.beavermod.module.Module;
import me.beavermod.module.setting.impl.EnumSetting;
import me.beavermod.module.setting.impl.FloatSetting;
import me.beavermod.module.setting.impl.SeperatorSetting;
import me.beavermod.module.setting.util.IChanged;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

public class KillAura extends Module {

    private final EnumSetting<TargetMode> targetMode = new EnumSetting<>("Target Mode", "Target mode", TargetMode.SINGLE);

    private final FloatSetting minAps = new FloatSetting("Min Attack Speed", "Minimum attack speed", 0, 20, 9, new DecimalFormat("#0.## APS"), new IChanged() {
        @Override
        public void onChanged() {
            if (minAps.get() > maxAps.get()) {
                maxAps.set(minAps.get());
            }
        }
    });

    private final FloatSetting maxAps = new FloatSetting("Max Attack Speed", "Minimum attack speed", 0, 20, 12, new DecimalFormat("#0.## APS"), new IChanged() {
        @Override
        public void onChanged() {
            if (maxAps.get() < minAps.get()) {
                minAps.set(maxAps.get());
            }
        }
    });

    private Entity target = null;

    public KillAura() {
        super("Kill Aura", "Automatically attacks entities", Category.COMBAT);
        addSettings(
                new SeperatorSetting("Targets"),
                targetMode,

                new SeperatorSetting("Attack Speed"),
                minAps, maxAps
        );
    }

    @SubscribeEvent
    public void onPreMotion(PreMotionEvent event) {

    }

    public void rotate(PreMotionEvent event) {

    }

    public enum TargetMode {
        SINGLE,
        SWITCH,
        PRIORITY
    }
}
