package com.mrmelon54.ArmoredElytra;

import com.mrmelon54.ArmoredElytra.items.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public interface ChestplateWithElytraItem {
    List<Function<ItemStack, @NotNull ChestplateWithElytraItem>> decodeItemStack = List.of(
            VanillaTweaksArmoredElytraItem::new,
            VoodooTweaksPlatedElytraItem::new,
            TheIllusiveC4ColytraItem::new,
            Pim16aap2SpigotArmoredElytraItem::new,
            ValorlessHavenElytraItem::new,
            TeaksTweaksArmoredElytraItem::new
    );

    static ChestplateWithElytraItem fromItemStack(ItemStack stack) {
        for (Function<ItemStack, @NotNull ChestplateWithElytraItem> i : decodeItemStack) {
            ChestplateWithElytraItem item = i.apply(stack);
            if (item.isArmoredElytra()) return item;
        }
        return null;
    }

    default boolean isArmoredElytra() {
        ItemStack elytra = getElytra();
        ItemStack chestplate = getChestplate();
        return elytra != null && !elytra.isEmpty() && elytra.is(Items.ELYTRA) && chestplate != null && !chestplate.isEmpty() && InternalArrays.isItemChestplate(chestplate.getItem());
    }

    default int getLeatherChestplateColor() {
        ItemStack leatherChestplate = getChestplate();
        if (leatherChestplate.getItem() != Items.LEATHER_CHESTPLATE) return -1;
        CompoundTag tagData = leatherChestplate.getTag();
        if (tagData == null) return ArmoredElytra.DEFAULT_LEATHER_COLOR;
        if (!tagData.contains("display", Tag.TAG_COMPOUND)) return ArmoredElytra.DEFAULT_LEATHER_COLOR;
        CompoundTag displaydata = tagData.getCompound("display");
        if (!displaydata.contains("color", Tag.TAG_ANY_NUMERIC)) return ArmoredElytra.DEFAULT_LEATHER_COLOR;
        return displaydata.getInt("color");
    }

    ItemStack getElytra();

    ItemStack getChestplate();
}
