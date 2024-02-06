package com.mrmelon54.ArmoredElytra.items;

import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import net.minecraft.nbt.*;
import net.minecraft.world.item.ItemStack;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public record TeaksTweaksArmoredElytraItem(ItemStack stack) implements ChestplateWithElytraItem {
    @Override
    public ItemStack getElytra() {
        return stack;
    }

    @Override
    public ItemStack getChestplate() {
        CompoundTag stackTag = stack.getTag();
        if (stackTag == null) return null;
        if (!stackTag.contains("PublicBukkitValues", Tag.TAG_COMPOUND)) return null;
        CompoundTag pbv = stackTag.getCompound("PublicBukkitValues");

        if (!pbv.contains("teakstweaks:armored_elytra", Tag.TAG_STRING)) return null;
        if (!pbv.contains("teakstweaks:chestplate_storage", Tag.TAG_BYTE_ARRAY)) return null;
        byte[] byteArray = pbv.getByteArray("teakstweaks:chestplate_storage");
        try {
            CompoundTag t = NbtIo.readCompressed(new DataInputStream(new ByteArrayInputStream(byteArray)), NbtAccounter.unlimitedHeap());
            return ItemStack.of(t);
        } catch (IOException e) {
            return null;
        }
    }
}
