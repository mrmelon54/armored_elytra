package com.mrmelon54.ArmoredElytra.items;


import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public record TheIllusiveC4ColytraItem(ItemStack stack) implements ChestplateWithElytraItem {
    private static String COLYTRA_TAG = "colytra:ElytraUpgrade";

    public ItemStack getElytra() {
        CompoundTag tag = stack.getTag();
        if (tag == null) return null;
        if (!tag.contains(COLYTRA_TAG, Tag.TAG_COMPOUND)) return null;
        CompoundTag colytraTag = tag.getCompound(COLYTRA_TAG);
        if (colytraTag.getAllKeys().isEmpty()) {
            tag.remove(COLYTRA_TAG);
            return null;
        }
        return ItemStack.of(colytraTag);
    }

    public ItemStack getChestplate() {
        return stack;
    }
}
