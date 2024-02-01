package com.mrmelon54.ArmoredElytra;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ArmoredElytra {
    public static final String MOD_ID = "armored_elytra";
    public static final int DEFAULT_LEATHER_COLOR = 10511680;
    public static final Map<UUID, ChestplateWithElytraItem> armoredElytraMappings = new HashMap<>();

    public static void init() {
        if (Platform.getOptionalMod("advancednetherite").isPresent()) {
            // TODO: add advanced netherite support later
            System.out.println("[Armored Elytra] Detected Advanced Netherite so adding those chestplates");
            //InternalArrays.CHESTPLATES.addAll(List.of(ModItems.NETHERITE_IRON_CHESTPLATE, ModItems.NETHERITE_GOLD_CHESTPLATE, ModItems.NETHERITE_EMERALD_CHESTPLATE, ModItems.NETHERITE_DIAMOND_CHESTPLATE));
        }

        // Listen for the end of every tick
        ClientTickEvent.CLIENT_POST.register(ArmoredElytra::tick);

        // Clear armored elytra mappings when quitting a level
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(player -> armoredElytraMappings.clear());

        ClientTooltipEvent.ITEM.register((stack, lines, flag) -> {
            ChestplateWithElytraItem item = ChestplateWithElytraItem.fromItemStack(stack);
            if (item == null) return;
            if (!item.isArmoredElytra()) return;
            ItemStack chestplateItemStack = item.getChestplate();
            if (chestplateItemStack == null) return;
            Minecraft mc = Minecraft.getInstance();
            List<Component> tooltipLines = chestplateItemStack.getTooltipLines(mc.player, flag);
            int i = lines.indexOf(CommonComponents.EMPTY);
            int j = tooltipLines.indexOf(CommonComponents.EMPTY);
            if (i == -1 || j == -1) return;
            lines.addAll(i, tooltipLines.subList(1, j));
        });
    }

    private static void tick(Minecraft minecraft) {
        if (minecraft.level == null) return;

        // rip fps
        // kinda surprised this doesn't kill the fps
        for (Entity entity : minecraft.level.entitiesForRendering()) {
            if (entity == null) continue;
            if (entity instanceof LivingEntity livingEntity) updateWearingArmoredElytra(livingEntity);
        }
    }

    private static void updateWearingArmoredElytra(LivingEntity livingEntity) {
        ItemStack chestSlot = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        ChestplateWithElytraItem item = ChestplateWithElytraItem.fromItemStack(chestSlot);
        if (item != null) armoredElytraMappings.put(livingEntity.getUUID(), item);
        else armoredElytraMappings.remove(livingEntity.getUUID());
    }
}
