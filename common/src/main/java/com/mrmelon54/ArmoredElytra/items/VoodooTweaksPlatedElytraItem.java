package com.mrmelon54.ArmoredElytra.items;


import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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

    public ItemStack getItemStack() {
        return stack;
    }

    @Override
    public boolean getStatus() {
        return isValid;
    }

    @Override
    public Item getChestplateType() {
        return ChestplateType;
    }

    public static VoodooTweaksPlatedElytraItem fromItemStack(ItemStack stack) {
        VoodooTweaksPlatedElytraItem item = new VoodooTweaksPlatedElytraItem(stack);
        return item.isValid ? item : null;
    }

    public boolean equals(ChestplateWithElytraItem b) {
        if (b == null) return false;
        if (b instanceof VoodooTweaksPlatedElytraItem) return stack == ((VoodooTweaksPlatedElytraItem) b).stack;
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
            if (stack.getItem() == Items.ELYTRA) {
                CompoundTag chestplate = getChestplate();
                CompoundTag elytra = getElytra();
                if (chestplate != null && elytra != null) {
                    ItemStack chestplateStack = ItemStack.of(chestplate);
                    ChestplateType = chestplateStack.getItem();
                    return ChestplateType != Items.AIR;
                }
            }
        }
        return false;
    }

    public CompoundTag getElytra() {
        return getArmoredElytraData();
    }

    public CompoundTag getChestplate() {
        CompoundTag armElyData = getArmoredElytraData();
        if (armElyData != null) {
            String plate = armElyData.getCompound("tag").getString("Plate");
            CompoundTag chestplateStack;
            switch (plate) {
                case "netherite":
                    chestplateStack = (new ItemStack(Items.NETHERITE_CHESTPLATE)).getOrCreateTag();
                    break;
                case "diamond":
                    chestplateStack = (new ItemStack(Items.DIAMOND_CHESTPLATE)).getOrCreateTag();
                    break;
                case "golden":
                    chestplateStack = (new ItemStack(Items.GOLDEN_CHESTPLATE)).getOrCreateTag();
                    break;
                case "iron":
                    chestplateStack = (new ItemStack(Items.IRON_CHESTPLATE)).getOrCreateTag();
                    break;
                case "chainmail":
                    chestplateStack = (new ItemStack(Items.CHAINMAIL_CHESTPLATE)).getOrCreateTag();
                    break;
                case "leather":
                    chestplateStack = (new ItemStack(Items.LEATHER_CHESTPLATE)).getOrCreateTag();
                    break;
                default:
                    return null;
            }
            if (armElyData.getCompound("tag").contains("color")) {
                CompoundTag displaytag = chestplateStack.getCompound("tag").getCompound("display");
                displaytag.putInt("color", armElyData.getCompound("tag").getInt("color"));
                chestplateStack.getCompound("tag").put("display", displaytag);
            }
            return chestplateStack;
        }
        return null;
    }

    public CompoundTag getArmoredElytraData() {
        if (!stack.isEmpty() && stack.getItem() == Items.ELYTRA) return stack.getOrCreateTag();
        return null;
    }

    public ItemStack getChestplateItemStack() {
        return ItemStack.of(getChestplate());
    }
}
