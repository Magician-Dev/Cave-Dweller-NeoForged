package com.magiciandev.cavenoise.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;

import com.magiciandev.cavenoise.network.CavenoiseModVariables;

public class CDSetClockProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments) {
		CavenoiseModVariables.MapVariables.get(world).cavenoiseClock = DoubleArgumentType.getDouble(arguments, "ticks");
		CavenoiseModVariables.MapVariables.get(world).markSyncDirty();
	}
}