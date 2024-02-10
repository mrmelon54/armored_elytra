package com.mrmelon54.ArmoredElytra.items;


import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public record TheIllusiveC4ColytraItem(ItemStack stack) implements ChestplateWithElytraItem {
    public ItemStack getElytra() {
        CompoundTag tag = stack.getTag();
        if (tag == null) return null;
        if (!tag.contains("colytra:ElytraUpgrade", Tag.TAG_COMPOUND)) return null;
        return ItemStack.of(tag.getCompound("colytra:ElytraUpgrade"));
    }

    public ItemStack getChestplate() {
        return stack;
    }
}
