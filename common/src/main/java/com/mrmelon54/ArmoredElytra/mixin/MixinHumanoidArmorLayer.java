package com.mrmelon54.ArmoredElytra.mixin;

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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HumanoidArmorLayer.class, priority = 999)
public abstract class MixinHumanoidArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    @Shadow
    protected abstract void renderArmorPiece(PoseStack poseStack, MultiBufferSource multiBufferSource, T livingEntity, EquipmentSlot equipmentSlot, int i, A humanoidModel);

    @Shadow
    protected abstract A getArmorModel(EquipmentSlot equipmentSlot);

    public MixinHumanoidArmorLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
    }

    @Unique
    private boolean armored_elytra$isFakeArmorPass = false;
    @Unique
    private T armored_elytra$currentLivingEntity = null;

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("TAIL"))
    private void renderFakeChestplate(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        // Toggle isFakeArmorPass to signal that this is the chestplate repeat
        armored_elytra$isFakeArmorPass = true;
        armored_elytra$currentLivingEntity = livingEntity;
        this.renderArmorPiece(poseStack, multiBufferSource, livingEntity, EquipmentSlot.OFFHAND, i, this.getArmorModel(EquipmentSlot.CHEST));
        armored_elytra$isFakeArmorPass = false;
    }

    @ModifyVariable(method = "renderArmorPiece", at = @At("STORE"), ordinal = 0)
    private ItemStack injectedFakeArmorPiece(ItemStack value) {
        if (!armored_elytra$isFakeArmorPass) return value;

        // grab armored elytra from cache, null values are sent as the EMPTY stack to prevent crashes
        ChestplateWithElytraItem item = ArmoredElytra.armoredElytraMappings.get(armored_elytra$currentLivingEntity.getUUID());
        if (item == null) return ItemStack.EMPTY;
        ItemStack chestplate = item.getChestplate();
        return chestplate == null ? ItemStack.EMPTY : chestplate;
    }
}
