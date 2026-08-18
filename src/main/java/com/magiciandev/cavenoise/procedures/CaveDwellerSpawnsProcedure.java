package com.magiciandev.cavenoise.procedures;

import net.minecraft.world.level.LevelAccessor;

import com.magiciandev.cavenoise.network.CavenoiseModVariables;

public class CaveDwellerSpawnsProcedure {
	public static void execute(LevelAccessor world) {
		CavenoiseModVariables.MapVariables.get(world).dwellerExists = true;
		CavenoiseModVariables.MapVariables.get(world).markSyncDirty();
	}
}