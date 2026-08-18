package com.magiciandev.cavenoise.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

import com.magiciandev.cavenoise.network.CavenoiseModVariables;
import com.magiciandev.cavenoise.CavenoiseMod;

@EventBusSubscriber
public class PlayerTickProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double runfor = 0;
		double rand = 0;
		double randd = 0;
		Entity noiseVictim = null;
		Entity victem = null;
		Entity cavenoiseVictim = null;
		String txt = "";
		if (entity instanceof Player) {
			if (entity.getY() < 40 && !world.canSeeSkyFromBelowWater(BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()))) {
				if (!entity.getData(CavenoiseModVariables.PLAYER_VARIABLES).spelunker) {
					{
						CavenoiseModVariables.PlayerVariables _vars = entity.getData(CavenoiseModVariables.PLAYER_VARIABLES);
						_vars.spelunker = true;
						_vars.markSyncDirty();
					}
				}
				if (!CavenoiseModVariables.MapVariables.get(world).spelunkers.isEmpty()) {
					runfor = CavenoiseModVariables.MapVariables.get(world).spelunkers.size() - 1;
					while (runfor >= 0) {
						if ((CavenoiseModVariables.MapVariables.get(world).spelunkers.get((int) runfor) instanceof Entity _entity8 ? _entity8 : null) == entity) {
							break;
						}
						if (runfor == 0) {
							CavenoiseModVariables.MapVariables.get(world).spelunkers.add(entity);
							CavenoiseModVariables.MapVariables.get(world).markSyncDirty();
						}
						runfor = runfor - 1;
					}
				} else {
					CavenoiseModVariables.MapVariables.get(world).spelunkers.add(entity);
					CavenoiseModVariables.MapVariables.get(world).markSyncDirty();
				}
			} else {
				{
					CavenoiseModVariables.PlayerVariables _vars = entity.getData(CavenoiseModVariables.PLAYER_VARIABLES);
					_vars.spelunker = false;
					_vars.markSyncDirty();
				}
			}
		}
		if (CavenoiseModVariables.MapVariables.get(world).cavenoiseClock <= 0) {
			if (!CavenoiseModVariables.MapVariables.get(world).dwellerExists) {
				if (!CavenoiseModVariables.MapVariables.get(world).spelunkers.isEmpty()) {
					victem = CavenoiseModVariables.MapVariables.get(world).spelunkers.get(Mth.nextInt(RandomSource.create(), 0, (int) (CavenoiseModVariables.MapVariables.get(world).spelunkers.size() - 1))) instanceof Entity _entity15
							? _entity15
							: null;
				}
				if (!(victem == null)) {
					DwellerSpawnProcedure.execute(world, x, y, z, victem);
					CavenoiseMod.LOGGER.info("ATTEMPT TO SPAWN CD");
				}
			}
			CavenoiseModVariables.MapVariables.get(world).cavenoiseClock = Mth.nextInt(RandomSource.create(), 9600, 52800);
			CavenoiseModVariables.MapVariables.get(world).markSyncDirty();
		} else if (CavenoiseModVariables.MapVariables.get(world).cavenoiseClock <= 6000) {
			if (!CavenoiseModVariables.MapVariables.get(world).dwellerExists) {
				rand = Mth.nextInt(RandomSource.create(), 1, 1200);
				if (rand == 1) {
					if (!CavenoiseModVariables.MapVariables.get(world).dwellerExists) {
						if (!CavenoiseModVariables.MapVariables.get(world).spelunkers.isEmpty()) {
							noiseVictim = CavenoiseModVariables.MapVariables.get(world).spelunkers.get(Mth.nextInt(RandomSource.create(), 0, (int) (CavenoiseModVariables.MapVariables.get(world).spelunkers.size() - 1))) instanceof Entity _entity22
									? _entity22
									: null;
							if (!(noiseVictim == null)) {
								if (world instanceof Level _level) {
									if (!_level.isClientSide()) {
										_level.playSound(null, BlockPos.containing(noiseVictim.getX() + Mth.nextDouble(RandomSource.create(), -5, 5), noiseVictim.getY(), noiseVictim.getZ() + Mth.nextDouble(RandomSource.create(), -5, 5)),
												BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:cavenoise")), SoundSource.NEUTRAL, 3, 1);
									} else {
										_level.playLocalSound((noiseVictim.getX() + Mth.nextDouble(RandomSource.create(), -5, 5)), (noiseVictim.getY()), (noiseVictim.getZ() + Mth.nextDouble(RandomSource.create(), -5, 5)),
												BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:cavenoise")), SoundSource.NEUTRAL, 3, 1, false);
									}
								}
							}
						}
					}
				}
			}
			CavenoiseModVariables.MapVariables.get(world).cavenoiseClock = CavenoiseModVariables.MapVariables.get(world).cavenoiseClock - 1;
			CavenoiseModVariables.MapVariables.get(world).markSyncDirty();
		} else {
			if (CavenoiseModVariables.MapVariables.get(world).cavenoiseClock <= 12000) {
				if (!CavenoiseModVariables.MapVariables.get(world).dwellerExists) {
					randd = Mth.nextInt(RandomSource.create(), 1, 1600);
					if (randd == 1) {
						if (!CavenoiseModVariables.MapVariables.get(world).dwellerExists) {
							if (!CavenoiseModVariables.MapVariables.get(world).spelunkers.isEmpty()) {
								cavenoiseVictim = CavenoiseModVariables.MapVariables.get(world).spelunkers
										.get(Mth.nextInt(RandomSource.create(), 0, (int) (CavenoiseModVariables.MapVariables.get(world).spelunkers.size() - 1))) instanceof Entity _entity34 ? _entity34 : null;
								if (!(cavenoiseVictim == null)) {
									if (world instanceof Level _level) {
										if (!_level.isClientSide()) {
											_level.playSound(null,
													BlockPos.containing(cavenoiseVictim.getX() + Mth.nextDouble(RandomSource.create(), -5, 5), cavenoiseVictim.getY(), cavenoiseVictim.getZ() + Mth.nextDouble(RandomSource.create(), -5, 5)),
													BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("ambient.cave")), SoundSource.NEUTRAL, 1, 1);
										} else {
											_level.playLocalSound((cavenoiseVictim.getX() + Mth.nextDouble(RandomSource.create(), -5, 5)), (cavenoiseVictim.getY()), (cavenoiseVictim.getZ() + Mth.nextDouble(RandomSource.create(), -5, 5)),
													BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("ambient.cave")), SoundSource.NEUTRAL, 1, 1, false);
										}
									}
								}
							}
						}
					}
				}
			}
			CavenoiseModVariables.MapVariables.get(world).cavenoiseClock = CavenoiseModVariables.MapVariables.get(world).cavenoiseClock - 1;
			CavenoiseModVariables.MapVariables.get(world).markSyncDirty();
		}
	}
}