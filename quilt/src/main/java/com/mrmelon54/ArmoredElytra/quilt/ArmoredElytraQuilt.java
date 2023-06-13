package com.mrmelon54.ArmoredElytra.quilt;

import com.mrmelon54.ArmoredElytra.fabriclike.ArmoredElytraFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class ArmoredElytraQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        ArmoredElytraFabricLike.init();
    }
}
