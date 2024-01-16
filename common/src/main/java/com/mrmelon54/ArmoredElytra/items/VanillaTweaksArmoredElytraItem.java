package com.mrmelon54.ArmoredElytra.items;


import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class VanillaTweaksArmoredElytraItem implements ChestplateWithElytraItem {
    public final ItemStack stack;
    public boolean isValid;
    public Item ChestplateType;

    public VanillaTweaksArmoredElytraItem(ItemStack stack) {
        this.stack = stack;
        this.isValid = isArmoredElytra();
    }

    public boolean isInvalid() {
        return !isValid;
    }

    @Override
    public Item getChestplateType() {
        return ChestplateType;
    }

    public static VanillaTweaksArmoredElytraItem fromItemStack(ItemStack stack) {
        VanillaTweaksArmoredElytraItem item = new VanillaTweaksArmoredElytraItem(stack);
        return item.isValid ? item : null;
    }

    public boolean isArmoredElytra() {
        if (stack.isEmpty() || stack.getItem() != Items.ELYTRA) return false;

        ItemStack chestplate = getChestplate();
        ItemStack elytra = getElytra();
        if (chestplate == null || elytra == null) return false;

        ChestplateType = chestplate.getItem();
        return ChestplateType != Items.AIR;
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