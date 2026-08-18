package com.magiciandev.cavenoise.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.arguments.EntityAnchorArgument;

import java.util.Comparator;

import com.magiciandev.cavenoise.network.CavenoiseModVariables;
import com.magiciandev.cavenoise.entity.CaveDwellerEntity;
import com.magiciandev.cavenoise.CavenoiseMod;

public class DwellerSpottedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double continueStalking = 0;
		double nextChance = 0;
		if (!world.getEntitiesOfClass(Player.class, new AABB(Vec3.ZERO, Vec3.ZERO).move(new Vec3(x, y, z)).inflate(128 / 2d), e -> true).isEmpty()) {
			if (!entity.getPersistentData().getBoolean("continuedS")) {
				entity.getPersistentData().putDouble("continueStalking", (Mth.nextDouble(RandomSource.create(), 1, 4)));
				entity.getPersistentData().putBoolean("continuedS", true);
			}
			if (entity.getPersistentData().getDouble("continueStalking") == 1) {
				if (CavenoiseModVariables.MapVariables.get(world).continueStalkingClock == 69420) {
					CavenoiseModVariables.MapVariables.get(world).continueStalkingClock = Mth.nextInt(RandomSource.create(), 20, 80);
					CavenoiseModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (CavenoiseModVariables.MapVariables.get(world).continueStalkingClock == 0) {
					entity.getPersistentData().putDouble("continueStalking", 3);
					CavenoiseModVariables.MapVariables.get(world).continueStalkingClock = 2147483647;
					CavenoiseModVariables.MapVariables.get(world).markSyncDirty();
				} else {
					CavenoiseModVariables.MapVariables.get(world).continueStalkingClock = CavenoiseModVariables.MapVariables.get(world).continueStalkingClock - 1;
					CavenoiseModVariables.MapVariables.get(world).markSyncDirty();
					CavenoiseMod.LOGGER.info(CavenoiseModVariables.MapVariables.get(world).continueStalkingClock);
					CavenoiseMod.LOGGER.info(continueStalking);
				}
				if (entity instanceof Mob _entity)
					_entity.getNavigation().moveTo(((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getX()), ((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getY()),
							((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getZ()), 0.7);
			} else {
				CavenoiseMod.LOGGER.info("works");
				if (entity instanceof Mob _entity && (findEntityInWorldRange(world, Player.class, x, y, z, 128)) instanceof LivingEntity _ent)
					_entity.setTarget(_ent);
				if (entity instanceof Mob _entity)
					_entity.getNavigation().stop();
				entity.lookAt(EntityAnchorArgument.Anchor.EYES,
						new Vec3(((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getX()), ((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getY()), ((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getZ())));
				if (!entity.getPersistentData().getBoolean("spotSound")) {
					entity.getPersistentData().putBoolean("spotSound", true);
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getX(), (findEntityInWorldRange(world, Player.class, x, y, z, 128)).getY(),
									(findEntityInWorldRange(world, Player.class, x, y, z, 128)).getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:spotted")), SoundSource.NEUTRAL, 1, 1);
						} else {
							_level.playLocalSound(((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getX()), ((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getY()),
									((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:spotted")), SoundSource.NEUTRAL, 1, 1, false);
						}
					}
				}
				if (entity instanceof CaveDwellerEntity) {
					((CaveDwellerEntity) entity).setControllerAnimation("procedure", "animation.cave_dweller.spotted");
				}
				if (CavenoiseModVariables.MapVariables.get(world).clockToDweller <= 0) {
					if (!world.isClientSide()) {
						if (!CavenoiseModVariables.MapVariables.get(world).shouldNotContinue) {
							nextChance = Mth.nextInt(RandomSource.create(), 1, 3);
							CavenoiseModVariables.MapVariables.get(world).shouldNotContinue = true;
							CavenoiseModVariables.MapVariables.get(world).markSyncDirty();
							CavenoiseMod.LOGGER.info("SNC: " + nextChance);
						}
						if (nextChance == 1) {
							if (entity instanceof CaveDwellerEntity _datEntSetL)
								_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_SPOTTED, false);
							if (entity instanceof CaveDwellerEntity _datEntSetL)
								_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_AGGRO, true);
						} else if (nextChance == 2) {
							if (entity instanceof CaveDwellerEntity _datEntSetL)
								_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_SPOTTED, false);
							if (entity instanceof CaveDwellerEntity _datEntSetL)
								_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_FLEEING, true);
						} else if (nextChance == 3) {
							if (!world.getEntitiesOfClass(Player.class, new AABB(Vec3.ZERO, Vec3.ZERO).move(new Vec3(x, y, z)).inflate(12 / 2d), e -> true).isEmpty()) {
								if (entity instanceof CaveDwellerEntity _datEntSetL)
									_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_SPOTTED, false);
								if (entity instanceof CaveDwellerEntity _datEntSetL)
									_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_AGGRO, true);
							}
							if (!InPlayerLineOfSightProcedure.execute(entity) || !DwellerTargetSeesMeProcedure.execute(entity)) {
								DwellerDespawnProcedure.execute(world, x, y, z, entity);
							}
							CavenoiseModVariables.MapVariables.get(world).currentlyStaring = true;
							CavenoiseModVariables.MapVariables.get(world).markSyncDirty();
						}
					}
				} else {
					CavenoiseModVariables.MapVariables.get(world).clockToDweller = CavenoiseModVariables.MapVariables.get(world).clockToDweller - 1;
					CavenoiseModVariables.MapVariables.get(world).markSyncDirty();
				}
			}
		}
		if (CavenoiseModVariables.MapVariables.get(world).currentlyStaring) {
			if (!world.getEntitiesOfClass(Player.class, new AABB(Vec3.ZERO, Vec3.ZERO).move(new Vec3(x, y, z)).inflate(18 / 2d), e -> true).isEmpty()) {
				if (entity instanceof CaveDwellerEntity _datEntSetL)
					_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_SPOTTED, false);
				if (entity instanceof CaveDwellerEntity _datEntSetL)
					_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_AGGRO, true);
			}
			if (entity.getPersistentData().getBoolean("toDespawn")) {
				if (!InPlayerLineOfSightProcedure.execute(entity) || !DwellerTargetSeesMeProcedure.execute(entity)) {
					DwellerDespawnProcedure.execute(world, x, y, z, entity);
				}
			}
			if (entity.getPersistentData().getDouble("ticksStared") > 600) {
				entity.getPersistentData().putBoolean("toDespawn", true);
			} else {
				entity.getPersistentData().putDouble("ticksStared", (entity.getPersistentData().getDouble("ticksStared") + 1));
			}
			if (entity.getPersistentData().getDouble("breathingClock") == 0) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:breathing")), SoundSource.NEUTRAL, (float) 0.5, 1);
					} else {
						_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:breathing")), SoundSource.NEUTRAL, (float) 0.5, 1, false);
					}
				}
				entity.getPersistentData().putDouble("breathingClock", 80);
			} else {
				entity.getPersistentData().putDouble("breathingClock", (entity.getPersistentData().getDouble("breathingClock") - 1));
			}
		}
		if (nextChance == 2) {
			if (!entity.getPersistentData().getBoolean("toPlayFleeingSound")) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getX(), (findEntityInWorldRange(world, Player.class, x, y, z, 128)).getY(),
								(findEntityInWorldRange(world, Player.class, x, y, z, 128)).getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:flee")), SoundSource.NEUTRAL, 1, 1);
					} else {
						_level.playLocalSound(((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getX()), ((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getY()),
								((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:flee")), SoundSource.NEUTRAL, 1, 1, false);
					}
				}
				entity.getPersistentData().putBoolean("toPlayFleeingSound", true);
			}
		}
	}

	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}
}