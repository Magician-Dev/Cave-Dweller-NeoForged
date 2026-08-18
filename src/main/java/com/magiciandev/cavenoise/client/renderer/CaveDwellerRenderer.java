package com.magiciandev.cavenoise.client.renderer;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

import com.magiciandev.cavenoise.entity.model.CaveDwellerModel;
import com.magiciandev.cavenoise.entity.layer.CaveDwellerLayer;
import com.magiciandev.cavenoise.entity.CaveDwellerEntity;

public class CaveDwellerRenderer extends GeoEntityRenderer<CaveDwellerEntity> {
	public CaveDwellerRenderer(EntityRendererProvider.Context context) {
		super(context, new CaveDwellerModel());
		this.shadowRadius = 0.3f;
		this.addRenderLayer(new CaveDwellerLayer(this));
	}

	@Override
public boolean shouldRender(CaveDwellerEntity entity,
                            Frustum frustum,
                            double camX,
                            double camY,
                            double camZ) {

    if (entity.isInvisible()) {
        return false;
    }

    return super.shouldRender(entity, frustum, camX, camY, camZ);
}

	@Override
	public void preRender(PoseStack poseStack, CaveDwellerEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
		// Hide Blockbench utility bones so they are never drawn as geometry
		model.getBone("hitbox").ifPresent(bone -> bone.setHidden(true));
		model.getBone("tag_name").ifPresent(bone -> bone.setHidden(true));
		float scale = 1.3f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
	}
}