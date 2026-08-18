package com.magiciandev.cavenoise.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import java.util.Comparator;

import com.magiciandev.cavenoise.init.CavenoiseModEntities;
import com.magiciandev.cavenoise.entity.CaveDwellerEntity;
import com.magiciandev.cavenoise.CavenoiseMod;

public class DwellerSpawnProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity victim) {
		if (victim == null)
			return;
		Vec3 spawnPos = Vec3.ZERO;
		if (world instanceof ServerLevel _level) {
			Entity entityToSpawn = CavenoiseModEntities.CAVE_DWELLER.get().spawn(_level, BlockPos.containing(victim.getX(), victim.getY() + 10, victim.getZ()), MobSpawnType.MOB_SUMMONED);
			if (entityToSpawn != null) {
			}
		}
		if ((findEntityInWorldRange(world, CaveDwellerEntity.class, (victim.getX()), (victim.getY()), (victim.getZ()), 200)) instanceof LivingEntity _entity && !_entity.level().isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 1200, 1, false, false));
		CavenoiseMod.LOGGER.info("SPAWNED CD");
		spawnPos = GeneratePosProcedure.execute(world, victim);
		CavenoiseMod.LOGGER.info("ENTITY: " + findEntityInWorldRange(world, CaveDwellerEntity.class, (victim.getX()), (victim.getY()), (victim.getZ()), 200));
		if (!world.getEntitiesOfClass(CaveDwellerEntity.class, new AABB(Vec3.ZERO, Vec3.ZERO).move(new Vec3(x, y, z)).inflate(200 / 2d), e -> true).isEmpty()) {
			{
				Entity _ent = (findEntityInWorldRange(world, CaveDwellerEntity.class, (victim.getX()), (victim.getY()), (victim.getZ()), 200));
				double _tx = (spawnPos.x());
				double _ty = (spawnPos.y());
				double _tz = (spawnPos.z());
				_ent.teleportTo(_tx, _ty, _tz);
				if (_ent instanceof ServerPlayer _serverPlayer)
					_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
			}
			CavenoiseMod.LOGGER.info("POS: " + ("X: " + (spawnPos.x() + "" + (" --Y: " + (spawnPos.y() + "" + (" --Z: " + spawnPos.z()))))));
		}
	}

	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}
}