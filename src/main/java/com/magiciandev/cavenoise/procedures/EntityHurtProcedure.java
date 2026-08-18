package com.magiciandev.cavenoise.procedures;

import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

import com.magiciandev.cavenoise.entity.CaveDwellerEntity;

@EventBusSubscriber
public class EntityHurtProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingDamageEvent.Pre event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity(), event.getSource().getEntity());
		}
	}

	public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
		execute(null, world, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if (sourceentity instanceof Player && entity instanceof CaveDwellerEntity) {
			if (!world.getEntitiesOfClass(Player.class, new AABB(Vec3.ZERO, Vec3.ZERO).move(new Vec3((entity.getX()), (entity.getY()), (entity.getZ()))).inflate(128 / 2d), e -> true).isEmpty()) {
				if (entity instanceof Mob _entity && sourceentity instanceof LivingEntity _ent)
					_entity.setTarget(_ent);
			}
			if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
				if (entity instanceof CaveDwellerEntity _datEntL9 && _datEntL9.getEntityData().get(CaveDwellerEntity.DATA_STALKING) || entity instanceof CaveDwellerEntity _datEntL10 && _datEntL10.getEntityData().get(CaveDwellerEntity.DATA_SPOTTED)) {
					if (entity instanceof CaveDwellerEntity _datEntSetL)
						_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_SPOTTED, false);
					if (entity instanceof CaveDwellerEntity _datEntSetL)
						_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_STALKING, false);
					if (entity instanceof CaveDwellerEntity _datEntSetL)
						_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_AGGRO, true);
				}
			}
		}
	}
}