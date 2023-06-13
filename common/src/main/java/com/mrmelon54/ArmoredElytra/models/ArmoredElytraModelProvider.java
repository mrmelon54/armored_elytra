package com.mrmelon54.ArmoredElytra.models;

import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import com.mrmelon54.ArmoredElytra.InternalArrays;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ArmoredElytraModelProvider implements ClampedItemPropertyFunction {
    @Override
    public float unclampedCall(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
        if (itemStack == null || itemStack.isEmpty()) return 0;
        ChestplateWithElytraItem item = ChestplateWithElytraItem.fromItemStack(itemStack);
        if (item == null || !item.getStatus()) return 0;
        float armElyId = InternalArrays.chestplateToArmoredElytraId(item.getChestplateType());
        if (armElyId == -1) {
            System.out.println("Chestplate type doesn't have a corresponding armored elytra type: returning missing model");
            return 0;
        }
        return (armElyId + 1) / 100;
    }
}
