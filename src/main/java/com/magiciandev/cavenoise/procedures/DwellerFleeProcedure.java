package com.magiciandev.cavenoise.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.commands.arguments.EntityAnchorArgument;

import java.util.Comparator;

import com.magiciandev.cavenoise.entity.CaveDwellerEntity;
import com.magiciandev.cavenoise.CavenoiseMod;

public class DwellerFleeProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		Vec3 finalPos = Vec3.ZERO;
		if (entity.getPersistentData().getDouble("randomX") == 0 && entity.getPersistentData().getDouble("randomZ") == 0) {
			if (entity instanceof Mob _entity)
				_entity.getNavigation().stop();
			if (entity instanceof CaveDwellerEntity) {
				((CaveDwellerEntity) entity).setControllerAnimation("procedure", "animation.cave_dweller.spotted");
			}
			if (!world.isClientSide()) {
				entity.getPersistentData().putDouble("randomX", (x + Mth.nextDouble(RandomSource.create(), -200, 200)));
				entity.getPersistentData().putDouble("randomY", (Mth.nextDouble(RandomSource.create(), -64, y - 3)));
				entity.getPersistentData().putDouble("randomZ", (z + Mth.nextDouble(RandomSource.create(), -200, 200)));
			}
		}
		if (Math.round(x) != Math.round(entity.getPersistentData().getDouble("randomX")) || true) {
			if (entity instanceof CaveDwellerEntity) {
				((CaveDwellerEntity) entity).setControllerAnimation("procedure", "animation.cave_dweller.flee");
			}
		} else {
			if (entity instanceof CaveDwellerEntity) {
				((CaveDwellerEntity) entity).setControllerAnimation("procedure", "animation.cave_dweller.spotted");
			}
			CavenoiseMod.queueServerWork(20, () -> {
				DwellerDespawnProcedure.execute(world, x, y, z, entity);
			});
		}
		if (!world.isClientSide()) {
			if (entity instanceof Mob _entity)
				_entity.getNavigation().moveTo((entity.getPersistentData().getDouble("randomX")), (entity.getPersistentData().getDouble("randomY")), (entity.getPersistentData().getDouble("randomZ")), 1.2);
			entity.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3((entity.getPersistentData().getDouble("randomX")), (entity.getPersistentData().getDouble("randomY")), (entity.getPersistentData().getDouble("randomZ"))));
		}
		if (!world.getEntitiesOfClass(Player.class, new AABB(Vec3.ZERO, Vec3.ZERO).move(new Vec3(x, y, z)).inflate(360 / 2d), e -> true).isEmpty()) {
			if (entity instanceof Mob _entity && (findEntityInWorldRange(world, Player.class, x, y, z, 360)) instanceof LivingEntity _ent)
				_entity.setTarget(_ent);
		} else {
			DwellerDespawnProcedure.execute(world, x, y, z, entity);
		}
		if (!DwellerTargetSeesMeProcedure.execute(entity) || !InPlayerLineOfSightProcedure.execute(entity)) {
			DwellerDespawnProcedure.execute(world, x, y, z, entity);
		}
		if (entity instanceof Mob _entity)
			_entity.setTarget(null);
		CavenoiseMod.LOGGER.info(entity.getPersistentData().getDouble("randomX") + "" + (" " + entity.getPersistentData().getDouble("randomZ")));
		finalPos = new Vec3((entity.getPersistentData().getDouble("randomX")), (entity.getPersistentData().getDouble("randomY")), (entity.getPersistentData().getDouble("randomZ")));
		if (!world.isClientSide()) {
			if ((entity.position()).distanceTo(finalPos) < 0.5) {
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.SMOKE, x, y, z, 10, 2, 3, 2, 0);
				DwellerDespawnProcedure.execute(world, x, y, z, entity);
			}
		}
	}

	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}
}