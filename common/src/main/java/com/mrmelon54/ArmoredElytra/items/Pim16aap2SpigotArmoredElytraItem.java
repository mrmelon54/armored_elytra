package com.mrmelon54.ArmoredElytra.items;

import com.mrmelon54.ArmoredElytra.ArmoredElytra;
import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Pim16aap2SpigotArmoredElytraItem implements ChestplateWithElytraItem {
    public final ItemStack stack;
    public boolean isValid;
    public Item ChestplateType;
    public int color = ArmoredElytra.DEFAULT_LEATHER_COLOR;

    public Pim16aap2SpigotArmoredElytraItem(ItemStack stack) {
        this.stack = stack;
        this.isValid = isArmoredElytra();
    }

    public boolean isInvalid() {
        return !isValid;
    }

    @Override
    public Item getChestplateType() {
        return ChestplateType;
    }

    public static Pim16aap2SpigotArmoredElytraItem fromItemStack(ItemStack stack) {
        Pim16aap2SpigotArmoredElytraItem item = new Pim16aap2SpigotArmoredElytraItem(stack);
        return item.isValid ? item : null;
    }

    public boolean isArmoredElytra() {
        if (stack.isEmpty()) return false;
        CompoundTag elytra = getElytra().getTag();
        if (elytra == null) return false;
        switch (elytra.getInt("armoredelytra:armor_tier_level")) {
            case 1 -> {
                ChestplateType = Items.LEATHER_CHESTPLATE;
                if (elytra.contains("armoredelytra:armored_elytra_color", 3))
                    color = elytra.getInt("armoredelytra:armored_elytra_color");
                return true;
            }
            case 2 -> {
                ChestplateType = Items.GOLDEN_CHESTPLATE;
                return true;
            }
            case 3 -> {
                ChestplateType = Items.CHAINMAIL_CHESTPLATE;
                return true;
            }
            case 4 -> {
                ChestplateType = Items.IRON_CHESTPLATE;
                return true;
            }
            case 5 -> {
                ChestplateType = Items.DIAMOND_CHESTPLATE;
                return true;
            }
            case 6 -> {
                ChestplateType = Items.NETHERITE_CHESTPLATE;
                return true;
            }
            default -> {
                ChestplateType = Items.AIR;
                return false;
            }
        }
    }

    public int getLeatherChestplateColor() {
        if (ChestplateType != Items.LEATHER_CHESTPLATE) return -1;
        return color;
    }

    public ItemStack getElytra() {
        return ItemStack.of(stack.getTagElement("PublicBukkitValues"));
    }

    public ItemStack getChestplate() {
        ItemStack chestplate = new ItemStack(ChestplateType);
        if (ChestplateType == Items.LEATHER_CHESTPLATE) {
            CompoundTag subtag = new CompoundTag();
            subtag.putInt("color", color);
            chestplate.addTagElement("display", subtag);
        }
        return chestplate;
    }
}