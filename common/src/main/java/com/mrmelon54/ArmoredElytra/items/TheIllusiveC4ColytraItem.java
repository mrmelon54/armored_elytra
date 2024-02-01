package com.mrmelon54.ArmoredElytra.items;


import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import com.mrmelon54.ArmoredElytra.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public record TheIllusiveC4ColytraItem(ItemStack stack) implements ChestplateWithElytraItem {
    private static String COLYTRA_TAG = "colytra:ElytraUpgrade";

    public ItemStack getElytra() {
        CompoundTag elytraUpgrade = NbtUtils.getTagElementFix(stack, COLYTRA_TAG);
        if (elytraUpgrade == null) return null;
        return ItemStack.of(elytraUpgrade);
    }

    public ItemStack getChestplate() {
        return stack;
    }

    @Override
    public boolean baseStackIsElytra() {
        return false;
    }
}
