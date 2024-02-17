package com.mrmelon54.ArmoredElytra.items;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mrmelon54.ArmoredElytra.ArmoredElytra;
import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.awt.*;

public record ValorlessHavenElytraItem(ItemStack stack) implements ChestplateWithElytraItem {
    public ItemStack getElytra() {
        return stack;
    }

    public ItemStack getChestplate() {
        CompoundTag elytraTag = getElytra().getTag();
        if (elytraTag == null) return null;
        ItemStack chestplate = null;
        switch (elytraTag.getString("havenelytra:chestplate-type")) {
            case "LEATHER_CHESTPLATE" -> {
                chestplate = Items.LEATHER_CHESTPLATE.getDefaultInstance();
                JsonObject chestplateData = new Gson().fromJson(elytraTag.getString("havenelytra:chestplate-meta"), JsonObject.class);
                // Chestplate has a saved color
                if (chestplateData.has("color")) {
                    int color = new Color(
                            chestplateData.get("color").getAsJsonObject().get("RED").getAsInt(),
                            chestplateData.get("color").getAsJsonObject().get("GREEN").getAsInt(),
                            chestplateData.get("color").getAsJsonObject().get("BLUE").getAsInt()
                    ).getRGB();
                    // Color is not the default color
                    if (color != ArmoredElytra.DEFAULT_LEATHER_COLOR) {
                        CompoundTag colorTag = new CompoundTag();
                        colorTag.putInt("color", color);
                        chestplate.getOrCreateTag().put("display", colorTag);
                    }
                }
            }
            case "CHAINMAIL_CHESTPLATE" -> chestplate = Items.CHAINMAIL_CHESTPLATE.getDefaultInstance();
            case "IRON_CHESTPLATE" -> chestplate = Items.IRON_CHESTPLATE.getDefaultInstance();
            case "GOLDEN_CHESTPLATE" -> chestplate = Items.GOLDEN_CHESTPLATE.getDefaultInstance();
            case "DIAMOND_CHESTPLATE" -> chestplate = Items.DIAMOND_CHESTPLATE.getDefaultInstance();
            case "NETHERITE_CHESTPLATE" -> chestplate = Items.NETHERITE_CHESTPLATE.getDefaultInstance();
        }
        return chestplate;
    }
}
