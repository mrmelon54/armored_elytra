package com.mrmelon54.ArmoredElytra;

import com.mrmelon54.ArmoredElytra.items.Pim16aap2SpigotArmoredElytraItem;
import com.mrmelon54.ArmoredElytra.items.VanillaTweaksArmoredElytraItem;
import com.mrmelon54.ArmoredElytra.items.VoodooTweaksPlatedElytraItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public interface ChestplateWithElytraItem {
    static ChestplateWithElytraItem fromItemStack(ItemStack stack) {
        VanillaTweaksArmoredElytraItem vtae = VanillaTweaksArmoredElytraItem.fromItemStack(stack);
        if (vtae != null) return vtae;
        VoodooTweaksPlatedElytraItem vtpe = VoodooTweaksPlatedElytraItem.fromItemStack(stack);
        if (vtpe != null) return vtpe;
        /*TheIllusiveC4ColytraItem ticc = TheIllusiveC4ColytraItem.fromItemStack(stack);
        if (ticc != null) return ticc;*/
        Pim16aap2SpigotArmoredElytraItem psae = Pim16aap2SpigotArmoredElytraItem.fromItemStack(stack);
        if (psae != null) return psae;
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
