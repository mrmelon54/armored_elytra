package com.mrmelon54.ArmoredElytra.items;


import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record VanillaTweaksArmoredElytraItem(ItemStack stack) implements ChestplateWithElytraItem {
    public static VanillaTweaksArmoredElytraItem fromItemStack(ItemStack stack) {
        VanillaTweaksArmoredElytraItem item = new VanillaTweaksArmoredElytraItem(stack);
        return item.isArmoredElytra() ? item : null;
    }

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
        if (!stack.isEmpty() && stack.getItem() == Items.ELYTRA) return stack.getOrCreateTagElement("armElyData");
        return null;
    }
}