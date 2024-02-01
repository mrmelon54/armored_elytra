package com.mrmelon54.ArmoredElytra.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrmelon54.ArmoredElytra.ArmoredElytra;
import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
        this.renderArmorPiece(poseStack, multiBufferSource, livingEntity, EquipmentSlot.OFFHAND, i, this.getArmorModel(EquipmentSlot.CHEST));
    }

    @Redirect(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack renderFakeArmorPiece(LivingEntity instance, EquipmentSlot equipmentSlot) {
        // only modify if the equipment slot is OFFHAND (the chestplate repeat)
        if (equipmentSlot != EquipmentSlot.OFFHAND) return instance.getItemBySlot(equipmentSlot);

        // grab armored elytra from cache, null values are sent as the EMPTY stack to prevent crashes
        ChestplateWithElytraItem item = ArmoredElytra.armoredElytraMappings.get(instance.getUUID());
        if (item == null) return ItemStack.EMPTY;
        ItemStack chestplate = item.getChestplate();
        return chestplate == null ? ItemStack.EMPTY : chestplate;
    }

    @Inject(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER))
    private void injectedResetEqupimentSlot(CallbackInfo ci, @Local(argsOnly = true) LocalRef<EquipmentSlot> localRef) {
        // reset equipment slot value for the rest of the renderArmorPiece method
        EquipmentSlot equipmentSlot = localRef.get();
        localRef.set(equipmentSlot == EquipmentSlot.OFFHAND ? EquipmentSlot.CHEST : equipmentSlot);
    }
}
