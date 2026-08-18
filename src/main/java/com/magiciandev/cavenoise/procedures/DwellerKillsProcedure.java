package com.magiciandev.cavenoise.procedures;

import net.minecraft.world.entity.Entity;

public class DwellerKillsProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putBoolean("toDespawn", true);
	}
}