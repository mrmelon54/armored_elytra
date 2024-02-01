package com.mrmelon54.ArmoredElytra.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import com.mrmelon54.ArmoredElytra.duck.SecondLayerProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow
    public abstract BakedModel getModel(ItemStack arg, Level arg2, LivingEntity arg3, int i);

    @Shadow
    public abstract void render(ItemStack arg, ItemDisplayContext arg2, boolean bl, PoseStack arg3, MultiBufferSource arg4, int i, int j, BakedModel arg5);

    @Inject(method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V"))
    private void injectedRenderStatic(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, boolean bl, PoseStack poseStack, MultiBufferSource multiBufferSource, Level level, int i, int j, int k, CallbackInfo ci) {
        ItemStack layer = ChestplateWithElytraItem.getSecondLayer(itemStack);
        if (layer == null) return;

        BakedModel bakedModel = this.getModel(layer, level, livingEntity, k);
        this.render(layer, itemDisplayContext, false, poseStack, multiBufferSource, i, OverlayTexture.NO_OVERLAY, bakedModel);
    }

    @Inject(method = "renderModelLists", at = @At("HEAD"))
    private void injectedRenderModelLists(BakedModel bakedModel, ItemStack itemStack, int i, int j, PoseStack poseStack, VertexConsumer vertexConsumer, CallbackInfo ci) {
        if (itemStack == null) return;
        if (((SecondLayerProvider) (Object) itemStack).armored_elytra$isNotSecondLayer()) return;
        poseStack.pushPose();
        poseStack.translate(0, 0.2f, 0.01f);
    }

    @Inject(method = "renderModelLists", at = @At("TAIL"))
    private void injectedRenderModelListsTail(BakedModel bakedModel, ItemStack itemStack, int i, int j, PoseStack poseStack, VertexConsumer vertexConsumer, CallbackInfo ci) {
        if (((SecondLayerProvider) (Object) itemStack).armored_elytra$isNotSecondLayer()) return;
        poseStack.popPose();
    }
}
