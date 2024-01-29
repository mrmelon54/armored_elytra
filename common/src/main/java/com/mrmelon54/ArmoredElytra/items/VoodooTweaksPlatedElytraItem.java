package com.mrmelon54.ArmoredElytra.items;


import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record VoodooTweaksPlatedElytraItem(ItemStack stack) implements ChestplateWithElytraItem {
    public ItemStack getElytra() {
        return stack.is(Items.ELYTRA) ? stack : null;
    }

    public ItemStack getChestplate() {
        CompoundTag armElyData = stack.getTag();
        if (armElyData == null) return null;

        String plate = armElyData.getString("Plate");
        Item chestplateType;
        switch (plate) {
            case "netherite" -> chestplateType = Items.NETHERITE_CHESTPLATE;
            case "diamond" -> chestplateType = Items.DIAMOND_CHESTPLATE;
            case "golden" -> chestplateType = Items.GOLDEN_CHESTPLATE;
            case "iron" -> chestplateType = Items.IRON_CHESTPLATE;
            case "chainmail" -> chestplateType = Items.CHAINMAIL_CHESTPLATE;
            case "leather" -> chestplateType = Items.LEATHER_CHESTPLATE;
            default -> {
                return null;
            }
        }
        ItemStack chestplateStack = new ItemStack(chestplateType);

        if (armElyData.contains("OriginalChestplate", Tag.TAG_COMPOUND)) {
            CompoundTag originalChestplate = armElyData.getCompound("OriginalChestplate");
            CompoundTag chestplateTag = chestplateStack.getOrCreateTag();
            for (String key : originalChestplate.getAllKeys())
                chestplateTag.put(key, originalChestplate.get(key));
        }
        return chestplateStack;
    }
}
