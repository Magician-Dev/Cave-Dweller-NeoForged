package com.magiciandev.cavenoise.procedures;

import net.minecraft.world.entity.Entity;

import com.magiciandev.cavenoise.entity.CaveDwellerEntity;

public class DwellerMeleeConditionProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if (entity instanceof CaveDwellerEntity _datEntL0 && _datEntL0.getEntityData().get(CaveDwellerEntity.DATA_AGGRO)) {
			return true;
		}
		return false;
	}
}