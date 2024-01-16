package com.mrmelon54.ArmoredElytra.items;


import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import com.mrmelon54.ArmoredElytra.InternalArrays;
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

    public boolean isInvalid() {
        return !isValid;
    }

    @Override
    public Item getChestplateType() {
        return ChestplateType;
    }

    public static TheIllusiveC4ColytraItem fromItemStack(ItemStack stack) {
        TheIllusiveC4ColytraItem item = new TheIllusiveC4ColytraItem(stack);
        return item.isValid ? item : null;
    }

    public boolean isArmoredElytra() {
        if (stack.isEmpty() || !InternalArrays.isItemChestplate(stack.getItem())) return false;
        ItemStack elytra = getElytra();
        if (elytra == null) return false;
        ChestplateType = stack.getItem();
        return ChestplateType != Items.AIR;
    }

    public ItemStack getElytra() {
        return ItemStack.of(stack.getOrCreateTagElement("colytra:ElytraUpgrade"));
    }

    public ItemStack getChestplate() {
        return stack;
    }
}