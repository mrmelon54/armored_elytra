package com.mrmelon54.ArmoredElytra.mixin;

import com.mrmelon54.ArmoredElytra.duck.SecondLayerProvider;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemStack.class)
public class ItemStackMixin implements SecondLayerProvider {
    @Unique
    private boolean armored_elytra$isSecondArmoredElytraLayer;


    @Override
    public void armored_elytra$setSecondLayer(boolean secondLayer) {
        armored_elytra$isSecondArmoredElytraLayer = true;
    }

    @Override
    public boolean armored_elytra$isSecondLayer() {
        return armored_elytra$isSecondArmoredElytraLayer;
    }
}
