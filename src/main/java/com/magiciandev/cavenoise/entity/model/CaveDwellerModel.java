package com.magiciandev.cavenoise.entity.model;

import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.animation.AnimationState;

import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;

import com.magiciandev.cavenoise.entity.CaveDwellerEntity;

public class CaveDwellerModel extends GeoModel<CaveDwellerEntity> {
	// DefaultedEntityGeoModel-style paths (recommended GeckoLib 4 layout)
	private static final ResourceLocation MODEL_ENTITY = ResourceLocation.parse("cavenoise:geo/entity/cave_dweller.geo.json");
	private static final ResourceLocation ANIM_ENTITY = ResourceLocation.parse("cavenoise:animations/entity/cave_dweller.animation.json");
	// Flat legacy layout (older plugin imports)
	private static final ResourceLocation MODEL_FLAT = ResourceLocation.parse("cavenoise:geo/cave_dweller.geo.json");
	private static final ResourceLocation ANIM_FLAT = ResourceLocation.parse("cavenoise:animations/cave_dweller.animation.json");

	@Override
	public ResourceLocation getModelResource(CaveDwellerEntity animatable) {
		if (GeckoLibCache.getBakedModels().containsKey(MODEL_ENTITY))
			return MODEL_ENTITY;
		return MODEL_FLAT;
	}

	@Override
	public ResourceLocation getTextureResource(CaveDwellerEntity animatable) {
		// MCreator entity textures are imported to textures/entities/
		return ResourceLocation.parse("cavenoise:textures/entities/" + animatable.getTexture() + ".png");
	}

	@Override
	public ResourceLocation getAnimationResource(CaveDwellerEntity animatable) {
		if (GeckoLibCache.getBakedAnimations().containsKey(ANIM_ENTITY))
			return ANIM_ENTITY;
		return ANIM_FLAT;
	}

	@Override
	public void setCustomAnimations(CaveDwellerEntity animatable, long instanceId, AnimationState<CaveDwellerEntity> animationState) {
		GeoBone head = this.getAnimationProcessor().getBone("head");
		if (head != null) {
			EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
			if (entityData != null) {
				head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
				head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
			}
		}
	}
}