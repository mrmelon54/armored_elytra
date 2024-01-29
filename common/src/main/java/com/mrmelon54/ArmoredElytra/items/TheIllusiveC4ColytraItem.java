package com.mrmelon54.ArmoredElytra.items;


import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import com.mrmelon54.ArmoredElytra.InternalArrays;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record TheIllusiveC4ColytraItem(ItemStack stack) implements ChestplateWithElytraItem {
    public ItemStack getElytra() {
        return ItemStack.of(stack.getOrCreateTagElement("colytra:ElytraUpgrade"));
    }

    public ItemStack getChestplate() {
        return stack;
    }
}
