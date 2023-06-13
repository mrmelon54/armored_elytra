package com.mrmelon54.ArmoredElytra.items;


import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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

    public ItemStack getItemStack() {
        return stack;
    }

    public boolean getStatus() {
        return isValid;
    }

    @Override
    public Item getChestplateType() {
        return ChestplateType;
    }

    public static VanillaTweaksArmoredElytraItem fromItemStack(ItemStack stack) {
        VanillaTweaksArmoredElytraItem item = new VanillaTweaksArmoredElytraItem(stack);
        return item.isValid ? item : null;
    }

    public boolean equals(ChestplateWithElytraItem b) {
        if (b == null) return false;
        if (b instanceof VanillaTweaksArmoredElytraItem) return stack == ((VanillaTweaksArmoredElytraItem) b).stack;
        return false;
    }

    @Override
    public boolean hasEnchantmentGlint() {
        ListTag elytraEnch = stack.getEnchantmentTags();
        ListTag chestEnch = getChestplateItemStack().getEnchantmentTags();
        return elytraEnch.size() + chestEnch.size() > 0;
    }

    public boolean isArmoredElytra() {
        if (stack.isEmpty() || stack.getItem() != Items.ELYTRA) return false;

        CompoundTag chestplate = getChestplate();
        CompoundTag elytra = getElytra();
        if (chestplate != null && elytra != null) {
            ItemStack chestplateStack = ItemStack.of(chestplate);
            ChestplateType = chestplateStack.getItem();
            return ChestplateType != Items.AIR;
        }
        return false;
    }

    public CompoundTag getElytra() {
        CompoundTag armElyData = getArmoredElytraData();
        if (armElyData != null) return armElyData.getCompound("elytra");
        return null;
    }

    public CompoundTag getChestplate() {
        CompoundTag armElyData = getArmoredElytraData();
        if (armElyData != null) return armElyData.getCompound("chestplate");
        return null;
    }

    public CompoundTag getArmoredElytraData() {
        if (!stack.isEmpty() && stack.getItem() == Items.ELYTRA) return stack.getOrCreateTagElement("armElyData");
        return null;
    }

    public ItemStack getChestplateItemStack() {
        return ItemStack.of(getChestplate());
    }
}