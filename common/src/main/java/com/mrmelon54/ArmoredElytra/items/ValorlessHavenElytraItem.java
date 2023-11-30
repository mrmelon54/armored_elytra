package com.mrmelon54.ArmoredElytra.items;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mrmelon54.ArmoredElytra.ArmoredElytra;
import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.awt.*;

public class ValorlessHavenElytraItem implements ChestplateWithElytraItem {
    public final ItemStack stack;
    public boolean isValid;
    public Item chestplateType;
    public int color = ArmoredElytra.DEFAULT_LEATHER_COLOR;

    public ValorlessHavenElytraItem(ItemStack stack) {
        this.stack = stack;
        this.isValid = isArmoredElytra();
    }

    @Override
    public ItemStack getItemStack() {
        return stack;
    }

    @Override
    public boolean getStatus() {
        return isValid;
    }

    public static ValorlessHavenElytraItem fromItemStack(ItemStack stack) {
        ValorlessHavenElytraItem item = new ValorlessHavenElytraItem(stack);
        return item.isValid ? item : null;
    }

    @Override
    public Item getChestplateType() {
        return chestplateType;
    }

    @Override
    public boolean equals(ChestplateWithElytraItem b) {
        if (b == null) return false;
        if (b instanceof ValorlessHavenElytraItem) return stack == ((ValorlessHavenElytraItem) b).stack;
        return false;
    }

    @Override
    public boolean hasEnchantmentGlint() {
        ListTag elytraEnch = stack.getEnchantmentTags();
        ListTag chestEnch = getChestplateItemStack().getEnchantmentTags();
        return elytraEnch.size() + chestEnch.size() > 0;
    }

    @Override
    public boolean isArmoredElytra() {
        if (!stack.isEmpty()) {
            CompoundTag elytra = getElytra();
            if (elytra != null) {
                switch (elytra.getString("havenelytra:chestplate-type")) {
                    case "LEATHER_CHESTPLATE" -> {
                        chestplateType = Items.LEATHER_CHESTPLATE;
                        JsonObject chestplateData = new Gson().fromJson(elytra.getString("havenelytra:chestplate-meta"), JsonObject.class);
                        if (chestplateData.has("color")) {
                            color = new Color(
                                    chestplateData.get("color").getAsJsonObject().get("RED").getAsInt(),
                                    chestplateData.get("color").getAsJsonObject().get("GREEN").getAsInt(),
                                    chestplateData.get("color").getAsJsonObject().get("BLUE").getAsInt()
                            ).getRGB();
                        }
                        return true;
                    }
                    case "CHAINMAIL_CHESTPLATE" -> {
                        chestplateType = Items.CHAINMAIL_CHESTPLATE;
                        return true;
                    }
                    case "IRON_CHESTPLATE" -> {
                        chestplateType = Items.IRON_CHESTPLATE;
                        return true;
                    }
                    case "GOLDEN_CHESTPLATE" -> {
                        chestplateType = Items.GOLDEN_CHESTPLATE;
                        return true;
                    }
                    case "DIAMOND_CHESTPLATE" -> {
                        chestplateType = Items.DIAMOND_CHESTPLATE;
                        return true;
                    }
                    case "NETHERITE_CHESTPLATE" -> {
                        chestplateType = Items.NETHERITE_CHESTPLATE;
                        return true;
                    }
                    default -> {
                        chestplateType = Items.AIR;
                        return false;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int getLeatherChestplateColor() {
        if (chestplateType != Items.LEATHER_CHESTPLATE) return -1;
        return color;
    }

    @Override
    public CompoundTag getElytra() {
        return stack.getTagElement("PublicBukkitValues");
    }

    @Override
    public CompoundTag getChestplate() {
        return getChestplateItemStack().getOrCreateTag();
    }

    @Override
    public CompoundTag getArmoredElytraData() {
        if (!stack.isEmpty()) return stack.getOrCreateTag();
        return null;
    }

    @Override
    public ItemStack getChestplateItemStack() {
        ItemStack chestplate = new ItemStack(chestplateType);
        if (chestplateType == Items.LEATHER_CHESTPLATE && color != ArmoredElytra.DEFAULT_LEATHER_COLOR) {
            CompoundTag subtag = new CompoundTag();
            subtag.putInt("color", color);
            chestplate.addTagElement("display", subtag);
        }
        return chestplate;
    }
}
