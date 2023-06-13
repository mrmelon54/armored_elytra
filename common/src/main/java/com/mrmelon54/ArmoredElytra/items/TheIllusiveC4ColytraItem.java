package com.mrmelon54.ArmoredElytra.items;


import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import com.mrmelon54.ArmoredElytra.InternalArrays;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TheIllusiveC4ColytraItem implements ChestplateWithElytraItem {
    public final ItemStack stack;
    public boolean isValid;
    public Item ChestplateType;

    public TheIllusiveC4ColytraItem(ItemStack stack) {
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

    public static TheIllusiveC4ColytraItem fromItemStack(ItemStack stack) {
        TheIllusiveC4ColytraItem item = new TheIllusiveC4ColytraItem(stack);
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
        if (!stack.isEmpty()) {
            if (InternalArrays.isItemChestplate(stack.getItem())) {
                CompoundTag elytra = getElytra();
                if (elytra != null) {
                    ChestplateType = stack.getItem();
                    return ChestplateType != Items.AIR;
                }
            }
        }
        return false;
    }

    public CompoundTag getElytra() {
        return stack.getOrCreateTagElement("colytra:ElytraUpgrade");
    }

    public CompoundTag getArmoredElytraData() {
        if (!stack.isEmpty()) return stack.getOrCreateTag();
        return null;
    }

    public CompoundTag getChestplate() {
        return stack.getOrCreateTag();
    }

    public ItemStack getChestplateItemStack() {
        return stack;
    }
}