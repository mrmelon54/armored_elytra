package com.mrmelon54.ArmoredElytra.items;


import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import com.mrmelon54.ArmoredElytra.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public record VanillaTweaksArmoredElytraItem(ItemStack stack) implements ChestplateWithElytraItem {
    private static String ARM_ELY_DATA = "armElyData";

    public ItemStack getElytra() {
        CompoundTag armElyData = getArmoredElytraData();
        if (armElyData != null) return ItemStack.of(armElyData.getCompound("elytra"));
        return null;
    }

    public ItemStack getChestplate() {
        CompoundTag armElyData = getArmoredElytraData();
        if (armElyData != null) return ItemStack.of(armElyData.getCompound("chestplate"));
        return null;
    }

    public CompoundTag getArmoredElytraData() {
        return NbtUtils.getTagElementFix(stack, "armElyData");
    }
}
