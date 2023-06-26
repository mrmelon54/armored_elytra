package com.mrmelon54.ArmoredElytra;

import com.mrmelon54.ArmoredElytra.items.VanillaTweaksArmoredElytraItem;
import com.mrmelon54.ArmoredElytra.items.VoodooTweaksPlatedElytraItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public interface ChestplateWithElytraItem {
    ItemStack getItemStack();

    boolean getStatus();

    Item getChestplateType();

    static ChestplateWithElytraItem fromItemStack(ItemStack stack) {
        VanillaTweaksArmoredElytraItem vtae = VanillaTweaksArmoredElytraItem.fromItemStack(stack);
        if (vtae != null) return vtae;
        VoodooTweaksPlatedElytraItem vtpe = VoodooTweaksPlatedElytraItem.fromItemStack(stack);
        if (vtpe != null) return vtpe;
        /*TheIllusiveC4ColytraItem ticc = TheIllusiveC4ColytraItem.fromItemStack(stack);
        if (ticc != null) return ticc;
        Pim16aap2SpigotArmoredElytraItem psae = Pim16aap2SpigotArmoredElytraItem.fromItemStack(stack);
        if (psae != null) return psae;*/
        return null;
    }

    boolean equals(ChestplateWithElytraItem b);

    boolean hasEnchantmentGlint();

    boolean isArmoredElytra();

    default int getLeatherChestplateColor() {
        CompoundTag leatherChestplate = getChestplate();
        if (ItemStack.of(leatherChestplate).getItem() != Items.LEATHER_CHESTPLATE) return -1;
        if (leatherChestplate == null) return -1;
        if (!leatherChestplate.contains("tag", 10)) return ArmoredElytra.DEFAULT_LEATHER_COLOR;
        CompoundTag tagData = leatherChestplate.getCompound("tag");
        if (!tagData.contains("display", 10)) return ArmoredElytra.DEFAULT_LEATHER_COLOR;
        CompoundTag displaydata = tagData.getCompound("display");
        if (!displaydata.contains("color", 99)) return ArmoredElytra.DEFAULT_LEATHER_COLOR;
        return displaydata.getInt("color");
    }

    CompoundTag getElytra();

    CompoundTag getChestplate();

    CompoundTag getArmoredElytraData();

    ItemStack getChestplateItemStack();
}
