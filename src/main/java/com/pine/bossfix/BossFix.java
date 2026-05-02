package com.pine.bossfix;

import com.mega.endinglib.coremod.forge.LaunchPluginServiceBuilder;
import com.mojang.logging.LogUtils;
import com.pine.bossfix.coremod.BossFixProcessor;

import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(BossFix.MOD_ID)
public class BossFix {
    public static final String MOD_ID = "pinebossfix";
    private static final Logger LOGGER = LogUtils.getLogger();


    public BossFix() {
        LOGGER.info("[BossFix] Mod initialized!");
    }



}