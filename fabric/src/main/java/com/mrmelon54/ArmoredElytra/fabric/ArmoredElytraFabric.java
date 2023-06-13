package com.mrmelon54.ArmoredElytra.fabric;

import com.mrmelon54.ArmoredElytra.fabriclike.ArmoredElytraFabricLike;
import net.fabricmc.api.ModInitializer;

public class ArmoredElytraFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ArmoredElytraFabricLike.init();
    }
}
