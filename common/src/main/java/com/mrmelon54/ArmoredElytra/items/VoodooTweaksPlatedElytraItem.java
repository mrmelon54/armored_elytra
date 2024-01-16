package com.mrmelon54.ArmoredElytra.items;


import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class VoodooTweaksPlatedElytraItem implements ChestplateWithElytraItem {
    public final ItemStack stack;
    public boolean isValid;
    public Item ChestplateType;

    public VoodooTweaksPlatedElytraItem(ItemStack stack) {
        this.stack = stack;
        this.isValid = isArmoredElytra();
    }

    @Override
    public boolean isInvalid() {
        return !isValid;
    }

    @Override
    public Item getChestplateType() {
        return ChestplateType;
    }

    public static VoodooTweaksPlatedElytraItem fromItemStack(ItemStack stack) {
        VoodooTweaksPlatedElytraItem item = new VoodooTweaksPlatedElytraItem(stack);
        return item.isValid ? item : null;
    }

    public boolean isArmoredElytra() {
        ItemStack elytra = getElytra();
        ItemStack chestplate = getChestplate();
        if (chestplate == null || elytra == null) return false;

        ChestplateType = chestplate.getItem();
        return ChestplateType != Items.AIR;
    }

    public ItemStack getElytra() {
        return stack.is(Items.ELYTRA) ? stack : null;
    }

    public ItemStack getChestplate() {
        CompoundTag armElyData = stack.getTag();
        if (armElyData == null) return null;

        String plate = armElyData.getString("Plate");
        ItemStack chestplateStack;
        switch (plate) {
            case "netherite":
                chestplateStack = (new ItemStack(Items.NETHERITE_CHESTPLATE));
                break;
            case "diamond":
                chestplateStack = (new ItemStack(Items.DIAMOND_CHESTPLATE));
                break;
            case "golden":
                chestplateStack = (new ItemStack(Items.GOLDEN_CHESTPLATE));
                break;
            case "iron":
                chestplateStack = (new ItemStack(Items.IRON_CHESTPLATE));
                break;
            case "chainmail":
                chestplateStack = (new ItemStack(Items.CHAINMAIL_CHESTPLATE));
                break;
            case "leather":
                chestplateStack = (new ItemStack(Items.LEATHER_CHESTPLATE));
                break;
            default:
                return null;
        }
        if (armElyData.contains("OriginalChestplate", Tag.TAG_COMPOUND)) {
            CompoundTag originalChestplate = armElyData.getCompound("OriginalChestplate");
            CompoundTag chestplateTag = chestplateStack.getOrCreateTag();
            for (String key : originalChestplate.getAllKeys())
                chestplateTag.put(key, originalChestplate.get(key));
        }
        return chestplateStack;
    }
}
