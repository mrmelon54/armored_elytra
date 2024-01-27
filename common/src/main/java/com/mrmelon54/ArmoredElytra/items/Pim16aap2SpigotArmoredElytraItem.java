package com.mrmelon54.ArmoredElytra.items;

import com.mrmelon54.ArmoredElytra.ArmoredElytra;
import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record Pim16aap2SpigotArmoredElytraItem(ItemStack stack) implements ChestplateWithElytraItem {
    public ItemStack getElytra() {
        return stack;
    }

    public ItemStack getChestplate() {
        CompoundTag stackTag = stack.getTag();
        if (stackTag == null) return null;
        if (!stackTag.contains("PublicBukkitValues", Tag.TAG_COMPOUND)) return null;
        CompoundTag pbv = stackTag.getCompound("PublicBukkitValues");

        Item chestplateType;
        int color = ArmoredElytra.DEFAULT_LEATHER_COLOR;
        switch (pbv.getInt("armoredelytra:armor_tier_level")) {
            case 1 -> {
                chestplateType = Items.LEATHER_CHESTPLATE;
                if (pbv.contains("armoredelytra:armored_elytra_color", Tag.TAG_INT))
                    color = pbv.getInt("armoredelytra:armored_elytra_color");
            }
            case 2 -> chestplateType = Items.GOLDEN_CHESTPLATE;
            case 3 -> chestplateType = Items.CHAINMAIL_CHESTPLATE;
            case 4 -> chestplateType = Items.IRON_CHESTPLATE;
            case 5 -> chestplateType = Items.DIAMOND_CHESTPLATE;
            case 6 -> chestplateType = Items.NETHERITE_CHESTPLATE;
            default -> {
                return null;
            }
        }
        ItemStack chestplateStack = new ItemStack(chestplateType);
        if (chestplateType == Items.LEATHER_CHESTPLATE) {
            CompoundTag subtag = new CompoundTag();
            subtag.putInt("color", color);
            chestplateStack.addTagElement("display", subtag);
        }
        return chestplateStack;
    }
}
