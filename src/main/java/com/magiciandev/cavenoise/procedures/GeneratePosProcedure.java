package com.magiciandev.cavenoise.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

public class GeneratePosProcedure {
	public static Vec3 execute(LevelAccessor world, Entity victim) {
		if (victim == null)
			return Vec3.ZERO;
		Vec3 playerPos = Vec3.ZERO;
		double randX = 0;
		double randZ = 0;
		double posX = 0;
		double posY = 0;
		double posZ = 0;
		double runFor = 0;
		playerPos = victim.position();
		randX = Mth.nextInt(RandomSource.create(), 0, 70) - 35;
		randZ = Mth.nextInt(RandomSource.create(), 0, 70) - 35;
		posX = playerPos.x() + randX;
		posY = playerPos.y() + 10;
		posZ = playerPos.z() + randZ;
		runFor = 100;
		while (runFor >= 0) {
			runFor = runFor - 1;
			if ((world.getBlockState(BlockPos.containing(posX, posY, posZ))).is(BlockTags.create(ResourceLocation.parse("cavenoise:passthrough")))
					&& (world.getBlockState(BlockPos.containing(posX, posY + 1, posZ))).is(BlockTags.create(ResourceLocation.parse("cavenoise:passthrough")))
					&& (world.getBlockState(BlockPos.containing(posX, posY + 2, posZ))).is(BlockTags.create(ResourceLocation.parse("cavenoise:passthrough")))
					&& (world.getBlockState(BlockPos.containing(posX, posY - 1, posZ))).is(BlockTags.create(ResourceLocation.parse("cavenoise:passthrough")))) {
				break;
			}
			posY = posY - 1;
		}
		return new Vec3(posX, posY, posZ);
	}
}