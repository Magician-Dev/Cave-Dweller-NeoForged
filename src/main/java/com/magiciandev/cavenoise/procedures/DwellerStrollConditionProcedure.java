package com.magiciandev.cavenoise.procedures;

import net.minecraft.world.entity.Entity;

import com.magiciandev.cavenoise.entity.CaveDwellerEntity;

public class DwellerStrollConditionProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if (!(entity instanceof CaveDwellerEntity _datEntL0 && _datEntL0.getEntityData().get(CaveDwellerEntity.DATA_STALKING)) && !(entity instanceof CaveDwellerEntity _datEntL1 && _datEntL1.getEntityData().get(CaveDwellerEntity.DATA_FLEEING))
				&& !(entity instanceof CaveDwellerEntity _datEntL2 && _datEntL2.getEntityData().get(CaveDwellerEntity.DATA_AGGRO)) && !(entity instanceof CaveDwellerEntity _datEntL3 && _datEntL3.getEntityData().get(CaveDwellerEntity.DATA_SPOTTED))) {
			return true;
		}
		return false;
	}
}