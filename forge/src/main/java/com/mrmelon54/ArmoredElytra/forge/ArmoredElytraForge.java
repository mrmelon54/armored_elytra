package com.mrmelon54.ArmoredElytra.forge;

import dev.architectury.platform.forge.EventBuses;
import com.mrmelon54.ArmoredElytra.ArmoredElytra;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ArmoredElytra.MOD_ID)
public class ArmoredElytraForge {
    public ArmoredElytraForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ArmoredElytra.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ArmoredElytra.init();
    }
}
