package com.magiciandev.cavenoise.entity.layer;

import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LightTexture;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

import com.magiciandev.cavenoise.entity.CaveDwellerEntity;

public class CaveDwellerLayer extends GeoRenderLayer<CaveDwellerEntity> {
	private static final ResourceLocation GLOW_TEXTURE = ResourceLocation.parse("cavenoise:textures/entities/cave_dweller_eyes_texture.png");

	public CaveDwellerLayer(GeoRenderer<CaveDwellerEntity> entityRenderer) {
		super(entityRenderer);
	}

	@Override
	public void render(PoseStack poseStack, CaveDwellerEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		RenderType glowRenderType = RenderType.eyes(GLOW_TEXTURE);
		getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, LightTexture.FULL_SKY, OverlayTexture.NO_OVERLAY,
				getRenderer().getRenderColor(animatable, partialTick, packedLight).argbInt());
	}
}