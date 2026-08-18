package com.magiciandev.cavenoise.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import java.util.Comparator;

import com.magiciandev.cavenoise.network.CavenoiseModVariables;
import com.magiciandev.cavenoise.entity.CaveDwellerEntity;
import com.magiciandev.cavenoise.CavenoiseMod;

public class DwellerStalkProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		boolean hls = false;
		boolean tsm = false;
		double cooldownChance = 0;
		double cooldownTime = 0;
		if (!world.getEntitiesOfClass(Player.class, new AABB(Vec3.ZERO, Vec3.ZERO).move(new Vec3(x, y, z)).inflate(128 / 2d), e -> true).isEmpty()) {
			if (!((findEntityInWorldRange(world, Player.class, x, y, z, 128)) instanceof Player _plr ? _plr.getAbilities().instabuild : false)) {
				if (entity instanceof Mob _entity && (findEntityInWorldRange(world, Player.class, x, y, z, 128)) instanceof LivingEntity _ent)
					_entity.setTarget(_ent);
				if (!(entity instanceof LivingEntity _livEnt5 && _livEnt5.hasEffect(MobEffects.INVISIBILITY))) {
					if (entity instanceof Mob _entity)
						_entity.getNavigation().moveTo(((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getX()), ((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getY()),
								((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getZ()), 0.7);
				}
				if (entity instanceof CaveDwellerEntity) {
					((CaveDwellerEntity) entity).setControllerAnimation("procedure", "animation.cave_dweller.stalking");
				}
				if (entity.getPersistentData().getDouble("stalkNoiseTimer") <= 0) {
					entity.getPersistentData().putDouble("stalkNoiseTimer", (Mth.nextInt(RandomSource.create(), 100, 400)));
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null,
									BlockPos.containing((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getX() + Mth.nextDouble(RandomSource.create(), -5, 5), (findEntityInWorldRange(world, Player.class, x, y, z, 128)).getY(),
											(findEntityInWorldRange(world, Player.class, x, y, z, 128)).getZ() + Mth.nextDouble(RandomSource.create(), -5, 5)),
									BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:stalk")), SoundSource.NEUTRAL, (float) Mth.nextDouble(RandomSource.create(), 1, 3), 1);
						} else {
							_level.playLocalSound(((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getX() + Mth.nextDouble(RandomSource.create(), -5, 5)), ((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getY()),
									((findEntityInWorldRange(world, Player.class, x, y, z, 128)).getZ() + Mth.nextDouble(RandomSource.create(), -5, 5)), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:stalk")),
									SoundSource.NEUTRAL, (float) Mth.nextDouble(RandomSource.create(), 1, 3), 1, false);
						}
					}
				} else {
					entity.getPersistentData().putDouble("stalkNoiseTimer", (entity.getPersistentData().getDouble("stalkNoiseTimer") - 1));
				}
				hls = InPlayerLineOfSightProcedure.execute(entity);
				tsm = DwellerTargetSeesMeProcedure.execute(entity);
				if (!(entity instanceof LivingEntity _livEnt29 && _livEnt29.hasEffect(MobEffects.INVISIBILITY)) && hls && tsm) {
					if (!world.isClientSide()) {
						cooldownChance = Mth.nextInt(RandomSource.create(), 1, 4);
						if (cooldownChance == 4) {
							cooldownTime = Mth.nextInt(RandomSource.create(), 10, 80);
						} else {
							cooldownTime = Mth.nextInt(RandomSource.create(), 1, 1);
						}
						CavenoiseMod.queueServerWork((int) cooldownTime, () -> {
							if (entity instanceof CaveDwellerEntity _datEntSetL)
								_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_STALKING, false);
							if (entity instanceof CaveDwellerEntity _datEntSetL)
								_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_SPOTTED, true);
							CavenoiseModVariables.MapVariables.get(world).clockToDweller = Mth.nextInt(RandomSource.create(), 24, 67);
							CavenoiseModVariables.MapVariables.get(world).markSyncDirty();
						});
					}
				}
			}
		}
	}

	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}
}