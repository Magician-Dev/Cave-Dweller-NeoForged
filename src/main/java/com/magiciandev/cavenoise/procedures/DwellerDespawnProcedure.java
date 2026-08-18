package com.magiciandev.cavenoise.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import com.magiciandev.cavenoise.network.CavenoiseModVariables;
import com.magiciandev.cavenoise.entity.CaveDwellerEntity;

public class DwellerDespawnProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof CaveDwellerEntity _datEntSetL)
			_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_FLEEING, false);
		if (entity instanceof CaveDwellerEntity _datEntSetL)
			_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_CROUCHING, false);
		if (entity instanceof CaveDwellerEntity _datEntSetL)
			_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_SQUEEZING, false);
		if (entity instanceof CaveDwellerEntity _datEntSetL)
			_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_SPOTTED, false);
		if (entity instanceof CaveDwellerEntity _datEntSetL)
			_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_AGGRO, false);
		if (entity instanceof CaveDwellerEntity _datEntSetL)
			_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_STALKING, false);
		entity.getPersistentData().putDouble("chaseNoiseClock", 0);
		entity.getPersistentData().putDouble("climbNoiseClock", 0);
		entity.getPersistentData().putDouble("breathingClock", 0);
		entity.getPersistentData().putDouble("ticksStared", 0);
		CavenoiseModVariables.MapVariables.get(world).clockToDweller = 0;
		CavenoiseModVariables.MapVariables.get(world).continueStalkingClock = 69420;
		CavenoiseModVariables.MapVariables.get(world).markSyncDirty();
		entity.getPersistentData().putBoolean("toDespawn", false);
		entity.getPersistentData().putBoolean("toPlayFleeingSound", false);
		CavenoiseModVariables.MapVariables.get(world).shouldNotContinue = false;
		CavenoiseModVariables.MapVariables.get(world).currentlyStaring = false;
		CavenoiseModVariables.MapVariables.get(world).breakInvis = false;
		CavenoiseModVariables.MapVariables.get(world).dwellerExists = false;
		CavenoiseModVariables.MapVariables.get(world).markSyncDirty();
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:disappear")), SoundSource.NEUTRAL, 1, 1);
			} else {
				_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:disappear")), SoundSource.NEUTRAL, 1, 1, false);
			}
		}
		if (!entity.level().isClientSide())
			entity.discard();
	}
}