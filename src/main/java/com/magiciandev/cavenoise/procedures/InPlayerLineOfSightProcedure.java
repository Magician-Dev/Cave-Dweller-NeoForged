package com.magiciandev.cavenoise.procedures;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

public class InPlayerLineOfSightProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		boolean hasLineOfSight = false;
		Entity pendingTarget = null;
		Entity cd = null;
		if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
			pendingTarget = entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null;
			cd = entity;
			return ((LivingEntity) pendingTarget).hasLineOfSight(cd);
		}
		return false;
	}
}