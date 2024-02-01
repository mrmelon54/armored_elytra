package com.mrmelon54.ArmoredElytra.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrmelon54.ArmoredElytra.ChestplateWithElytraItem;
import com.mrmelon54.ArmoredElytra.duck.SecondLayerProvider;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow
    public abstract void render(ItemStack arg, ItemDisplayContext arg2, boolean bl, PoseStack arg3, MultiBufferSource arg4, int i, int j, BakedModel arg5);

    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;

    @Inject(method = "render", at = @At("HEAD"))
    private void injectedRenderStatic(ItemStack itemStack, ItemDisplayContext itemDisplayContext, boolean bl, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, BakedModel bakedModel, CallbackInfo ci) {
        ChestplateWithElytraItem chestplateWithElytraItem = ChestplateWithElytraItem.fromItemStack(itemStack);
        if (chestplateWithElytraItem == null) return;

        ItemStack nextLayer = chestplateWithElytraItem.baseStackIsElytra() ? chestplateWithElytraItem.getChestplate() : chestplateWithElytraItem.getElytra();
        if (nextLayer.isEmpty()) return;

        SecondLayerProvider duck = (SecondLayerProvider) (Object) nextLayer;
        BakedModel itemModel = itemModelShaper.getItemModel(nextLayer);
        duck.armored_elytra$setSecondLayer(true);
        render(nextLayer, itemDisplayContext, bl, poseStack, multiBufferSource, i, j, itemModel);
        duck.armored_elytra$setSecondLayer(false);
    }

    @Inject(method = "renderModelLists", at = @At("HEAD"))
    private void injectedRenderModelLists(BakedModel bakedModel, ItemStack itemStack, int i, int j, PoseStack poseStack, VertexConsumer vertexConsumer, CallbackInfo ci) {
        if (!((SecondLayerProvider) (Object) itemStack).armored_elytra$isSecondLayer()) return;
        poseStack.pushPose();
        poseStack.translate(0, 0.2f, 0.01f);
    }

    @Inject(method = "renderModelLists", at = @At("TAIL"))
    private void injectedRenderModelListsTail(BakedModel bakedModel, ItemStack itemStack, int i, int j, PoseStack poseStack, VertexConsumer vertexConsumer, CallbackInfo ci) {
        if (!((SecondLayerProvider) (Object) itemStack).armored_elytra$isSecondLayer()) return;
        poseStack.popPose();
    }
}
