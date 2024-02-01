package com.mrmelon54.ArmoredElytra.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrmelon54.ArmoredElytra.ArmoredElytra;
import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HumanoidArmorLayer.class, priority = 999)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    @Shadow
    protected abstract void renderArmorPiece(PoseStack poseStack, MultiBufferSource multiBufferSource, T livingEntity, EquipmentSlot equipmentSlot, int i, A humanoidModel);

    @Shadow
    protected abstract A getArmorModel(EquipmentSlot equipmentSlot);

    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("TAIL"))
    private void renderFakeChestplate(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        // Using OFFHAND equipment slot to signal that this is the chestplate repeat
        // This feels less hacky than swapping out the item in the inventory slot
        ChestplateWithElytraItem item = ArmoredElytra.armoredElytraMappings.get(livingEntity.getUUID());
        if (item == null) return;
        ItemStack chestplate = item.getChestplate();
        if (chestplate == null) return;

        //noinspection unchecked
        this.renderArmorPiece(poseStack, multiBufferSource, (T) new LivingEntity((EntityType<? extends LivingEntity>) livingEntity.getType(), livingEntity.level()) {
            @Override
            public @NotNull Iterable<ItemStack> getArmorSlots() {
                return livingEntity.getArmorSlots();
            }

            @Override
            public @NotNull ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
                if (equipmentSlot == EquipmentSlot.CHEST) return chestplate;
                return livingEntity.getItemBySlot(equipmentSlot);
            }

            @Override
            public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
                livingEntity.setItemSlot(equipmentSlot, itemStack);
            }

            @Override
            public @NotNull HumanoidArm getMainArm() {
                return livingEntity.getMainArm();
            }
        }, EquipmentSlot.CHEST, i, this.getArmorModel(EquipmentSlot.CHEST));
    }
}
