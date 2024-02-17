package com.mrmelon54.ArmoredElytra;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class NbtUtils {
    @Nullable
    public static CompoundTag getTagElementFix(ItemStack stack, String key) {
        CompoundTag tag = stack.getTagElement(key);
        if (tag != null && tag.getAllKeys().isEmpty()) {
            stack.removeTagKey(key);
            tag = null;
        }
        return tag;
    }
}
