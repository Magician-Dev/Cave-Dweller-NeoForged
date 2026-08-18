package com.magiciandev.cavenoise.procedures;

import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.api.ScaleOperations;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.tags.BlockTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import java.util.Comparator;

import com.magiciandev.cavenoise.entity.CaveDwellerEntity;
import com.magiciandev.cavenoise.CavenoiseMod;

public class DwellerAggroProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		boolean hls = false;
		boolean tsm = false;
		boolean shouldCrouch = false;
		boolean isAboveSolid = false;
		boolean isTwoAboveSolid = false;
		boolean isThreeAboveSolid = false;
		boolean isFacingSolid = false;
		boolean isOffsetFacingSolid = false;
		boolean isOffsetFacingAboveSolid = false;
		boolean isOffsetFacingTwoAboveSolid = false;
		boolean shouldSqueeze = false;
		boolean justbreak = false;
		double yx = 0;
		double yz = 0;
		Entity target = null;
		if (entity instanceof CaveDwellerEntity _datEntL0 && _datEntL0.getEntityData().get(CaveDwellerEntity.DATA_SPOTTED)) {
			if (entity instanceof CaveDwellerEntity _datEntSetL)
				_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_SPOTTED, false);
		}
		if (entity instanceof CaveDwellerEntity _datEntL2 && _datEntL2.getEntityData().get(CaveDwellerEntity.DATA_STALKING)) {
			if (entity instanceof CaveDwellerEntity _datEntSetL)
				_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_STALKING, false);
		}
		if (entity instanceof CaveDwellerEntity _datEntL4 && _datEntL4.getEntityData().get(CaveDwellerEntity.DATA_FLEEING)) {
			if (entity instanceof CaveDwellerEntity _datEntSetL)
				_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_FLEEING, false);
		}
		if ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null) {
			if (!world.getEntitiesOfClass(Player.class, new AABB(Vec3.ZERO, Vec3.ZERO).move(new Vec3(x, y, z)).inflate(128 / 2d), e -> true).isEmpty()) {
				if (!((findEntityInWorldRange(world, Player.class, x, y, z, 128)) instanceof Player _plr ? _plr.getAbilities().instabuild : false)) {
					if (entity instanceof Mob _entity && (findEntityInWorldRange(world, Player.class, x, y, z, 128)) instanceof LivingEntity _ent)
						_entity.setTarget(_ent);
				}
			} else {
				DwellerDespawnProcedure.execute(world, x, y, z, entity);
			}
		} else {
			if ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) instanceof Player _plr ? _plr.getAbilities().instabuild : false) {
				if (entity instanceof Mob _entity)
					_entity.setTarget(null);
			} else {
				if (!(entity instanceof CaveDwellerEntity _datEntL16 && _datEntL16.getEntityData().get(CaveDwellerEntity.DATA_SQUEEZING))
						&& !(entity instanceof CaveDwellerEntity _datEntL17 && _datEntL17.getEntityData().get(CaveDwellerEntity.DATA_CROUCHING))) {
					if (entity instanceof CaveDwellerEntity) {
						((CaveDwellerEntity) entity).setControllerAnimation("procedure", "animation.cave_dweller.new_run");
					}
				} else {
					if (entity instanceof CaveDwellerEntity) {
						((CaveDwellerEntity) entity).setControllerAnimation("procedure", "animation.cave_dweller.crouch_run_new");
					}
				}
				if (!world.isClientSide()) {
					if (entity.getPersistentData().getDouble("chaseNoiseClock") <= 0) {
						entity.getPersistentData().putDouble("chaseNoiseClock", 80);
					} else {
						entity.getPersistentData().putDouble("chaseNoiseClock", (entity.getPersistentData().getDouble("chaseNoiseClock") - 1));
					}
					if (entity.getPersistentData().getDouble("chaseNoiseClock") == 0) {
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:chase")), SoundSource.MASTER, 10, 1);
							} else {
								_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:chase")), SoundSource.MASTER, 10, 1, false);
							}
						}
					}
				}
				CavenoiseMod.queueServerWork(600, () -> {
					entity.getPersistentData().putBoolean("toDespawn", true);
				});
				if (entity.getPersistentData().getBoolean("toDespawn")) {
					hls = InPlayerLineOfSightProcedure.execute(entity);
					tsm = DwellerTargetSeesMeProcedure.execute(entity);
					if (!hls || !tsm) {
						DwellerDespawnProcedure.execute(world, x, y, z, entity);
					}
				}
			}
		}
		if (world.getBlockState(BlockPos.containing(x + 1, y + 0, z)).canOcclude() && world.getBlockState(BlockPos.containing(x + 1, y + 1, z)).canOcclude() && world.getBlockState(BlockPos.containing(x + 1, y + 2, z)).canOcclude()
				|| world.getBlockState(BlockPos.containing(x - 1, y + 0, z)).canOcclude() && world.getBlockState(BlockPos.containing(x - 1, y + 1, z)).canOcclude() && world.getBlockState(BlockPos.containing(x - 1, y + 2, z)).canOcclude()
				|| world.getBlockState(BlockPos.containing(x, y + 0, z + 1)).canOcclude() && world.getBlockState(BlockPos.containing(x, y + 1, z + 1)).canOcclude() && world.getBlockState(BlockPos.containing(x, y + 2, z + 1)).canOcclude()
				|| world.getBlockState(BlockPos.containing(x, y + 0, z - 1)).canOcclude() && world.getBlockState(BlockPos.containing(x, y + 1, z - 1)).canOcclude() && world.getBlockState(BlockPos.containing(x, y + 2, z - 1)).canOcclude()) {
			if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
				{
					final Vec3 _center = new Vec3(x, y, z);
					for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(100 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
						if (entityiterator == (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null)) {
							if (entityiterator.getY() > entity.getY()) {
								if ((entity instanceof CaveDwellerEntity _datEntL51 && _datEntL51.getEntityData().get(CaveDwellerEntity.DATA_CROUCHING)) == false
										&& (entity instanceof CaveDwellerEntity _datEntL52 && _datEntL52.getEntityData().get(CaveDwellerEntity.DATA_SQUEEZING)) == false) {
									if (entity instanceof CaveDwellerEntity _datEntSetL)
										_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_CLIMBING, true);
								}
							}
						}
					}
				}
			}
		} else if (!world.getBlockState(BlockPos.containing(x + 1, y + 0, z)).canOcclude() && (entity.getDirection()) == Direction.EAST) {
			if (entity instanceof CaveDwellerEntity _datEntSetL)
				_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_CLIMBING, false);
		} else if (!world.getBlockState(BlockPos.containing(x - 1, y + 0, z)).canOcclude() && (entity.getDirection()) == Direction.WEST) {
			if (entity instanceof CaveDwellerEntity _datEntSetL)
				_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_CLIMBING, false);
		} else if (!world.getBlockState(BlockPos.containing(x, y + 0, z + 1)).canOcclude() && (entity.getDirection()) == Direction.SOUTH) {
			if (entity instanceof CaveDwellerEntity _datEntSetL)
				_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_CLIMBING, false);
		} else if (!world.getBlockState(BlockPos.containing(x, y + 0, z - 1)).canOcclude() && (entity.getDirection()) == Direction.NORTH) {
			if (entity instanceof CaveDwellerEntity _datEntSetL)
				_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_CLIMBING, false);
		}
		if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
			{
				final Vec3 _center = new Vec3(x, y, z);
				for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(100 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
					if (entityiterator == (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null)) {
						if (entityiterator.getY() < entity.getY()) {
							if (entity instanceof CaveDwellerEntity _datEntSetL)
								_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_CLIMBING, false);
						}
					}
				}
			}
		}
		if ((entity instanceof CaveDwellerEntity _datEntL79 && _datEntL79.getEntityData().get(CaveDwellerEntity.DATA_CLIMBING)) == true
				&& (entity instanceof CaveDwellerEntity _datEntL80 && _datEntL80.getEntityData().get(CaveDwellerEntity.DATA_CROUCHING)) == false
				&& (entity instanceof CaveDwellerEntity _datEntL81 && _datEntL81.getEntityData().get(CaveDwellerEntity.DATA_SQUEEZING)) == false) {
			if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
				{
					final Vec3 _center = new Vec3(x, y, z);
					for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(100 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
						if (entity.getDeltaMovement().y() < 0.1 && entityiterator == (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null)) {
							entity.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3((entityiterator.getX()), (entityiterator.getY() + 1.7), (entityiterator.getZ())));
						}
					}
				}
			}
			if (world.getBlockState(BlockPos.containing(x + 1, y + 0, z)).canOcclude() && (!world.getBlockState(BlockPos.containing(x, y + 2, z)).canOcclude() || !world.getBlockState(BlockPos.containing(x, y + 3, z)).canOcclude())
					&& (entity.getDirection()) == Direction.EAST) {
				entity.setDeltaMovement(new Vec3(0.2, 0.2, (entity.getDeltaMovement().z() / 4)));
				entity.fallDistance = 0;
			} else if (world.getBlockState(BlockPos.containing(x - 1, y + 0, z)).canOcclude() && (!world.getBlockState(BlockPos.containing(x, y + 2, z)).canOcclude() || !world.getBlockState(BlockPos.containing(x, y + 3, z)).canOcclude())
					&& (entity.getDirection()) == Direction.WEST) {
				entity.setDeltaMovement(new Vec3((-0.2), 0.2, (entity.getDeltaMovement().z() / 4)));
				entity.fallDistance = 0;
			} else if (world.getBlockState(BlockPos.containing(x, y + 0, z + 1)).canOcclude() && (!world.getBlockState(BlockPos.containing(x, y + 2, z)).canOcclude() || !world.getBlockState(BlockPos.containing(x, y + 3, z)).canOcclude())
					&& (entity.getDirection()) == Direction.SOUTH) {
				entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x() / 4), 0.2, 0.2));
				entity.fallDistance = 0;
			} else if (world.getBlockState(BlockPos.containing(x, y + 0, z - 1)).canOcclude() && (!world.getBlockState(BlockPos.containing(x, y + 2, z)).canOcclude() || !world.getBlockState(BlockPos.containing(x, y + 3, z)).canOcclude())
					&& (entity.getDirection()) == Direction.NORTH) {
				entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x() / 4), 0.2, (-0.2)));
				entity.fallDistance = 0;
			}
			if (world.getBlockState(BlockPos.containing(x, y + 3, z)).canOcclude() || (world.getBlockState(BlockPos.containing(x, y + 3, z))).is(BlockTags.create(ResourceLocation.parse("cavenoise:trapdoors")))) {
				if ((world.getBlockState(BlockPos.containing(x, y + 3, z))).is(BlockTags.create(ResourceLocation.parse("cavenoise:trapdoors")))) {
					if (!world.isClientSide()) {
						world.destroyBlock(BlockPos.containing(x, y + 3, z), false);
					}
				}
				if (world.getBlockState(BlockPos.containing(x, y + 3, z)).getDestroySpeed(world, BlockPos.containing(x, y + 3, z)) <= 5) {
					if (!world.isClientSide()) {
						world.destroyBlock(BlockPos.containing(x, y + 3, z), false);
					}
				} else {
					if (entity instanceof CaveDwellerEntity _datEntSetL)
						_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_CLIMBING, false);
				}
			}
			if (!world.isClientSide()) {
				if (entity.getPersistentData().getDouble("climbNoiseClock") <= 0) {
					entity.getPersistentData().putDouble("climbNoiseClock", 10);
				} else {
					entity.getPersistentData().putDouble("climbNoiseClock", (entity.getPersistentData().getDouble("climbNoiseClock") - 1));
				}
				if (entity.getPersistentData().getDouble("climbNoiseClock") == 0) {
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:climb")), SoundSource.MASTER, 5, 1);
						} else {
							_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:climb")), SoundSource.MASTER, 5, 1, false);
						}
					}
				}
			}
		}
		if (entity instanceof CaveDwellerEntity) {
			if (world.getBlockState(BlockPos.containing(x + 1, y + 0, z)).canOcclude() && world.getBlockState(BlockPos.containing(x + 1, y + 1, z)).canOcclude() && world.getBlockState(BlockPos.containing(x + 1, y + 2, z)).canOcclude()
					&& !(!world.getEntitiesOfClass(Player.class, new AABB(Vec3.ZERO, Vec3.ZERO).move(new Vec3(x, y, z)).inflate(5 / 2d), e -> true).isEmpty()) && (entity.getDirection()) == Direction.EAST) {
				if (entity.getDeltaMovement().y() == 0.2) {
					if (entity instanceof CaveDwellerEntity) {
						((CaveDwellerEntity) entity).setControllerAnimation("procedure", "animation.cave_dweller.climb");
					}
					{
						Entity _ent = entity;
						_ent.setYRot(-90);
						_ent.setXRot(entity.getXRot());
						_ent.setYBodyRot(_ent.getYRot());
						_ent.setYHeadRot(_ent.getYRot());
						_ent.yRotO = _ent.getYRot();
						_ent.xRotO = _ent.getXRot();
						if (_ent instanceof LivingEntity _entity) {
							_entity.yBodyRotO = _entity.getYRot();
							_entity.yHeadRotO = _entity.getYRot();
						}
					}
				}
			} else if (world.getBlockState(BlockPos.containing(x - 1, y + 0, z)).canOcclude() && world.getBlockState(BlockPos.containing(x - 1, y + 1, z)).canOcclude() && world.getBlockState(BlockPos.containing(x - 1, y + 2, z)).canOcclude()
					&& !(!world.getEntitiesOfClass(Player.class, new AABB(Vec3.ZERO, Vec3.ZERO).move(new Vec3(x, y, z)).inflate(5 / 2d), e -> true).isEmpty()) && (entity.getDirection()) == Direction.WEST) {
				if (entity.getDeltaMovement().y() == 0.2) {
					if (entity instanceof CaveDwellerEntity) {
						((CaveDwellerEntity) entity).setControllerAnimation("procedure", "animation.cave_dweller.climb");
					}
					{
						Entity _ent = entity;
						_ent.setYRot(90);
						_ent.setXRot(entity.getXRot());
						_ent.setYBodyRot(_ent.getYRot());
						_ent.setYHeadRot(_ent.getYRot());
						_ent.yRotO = _ent.getYRot();
						_ent.xRotO = _ent.getXRot();
						if (_ent instanceof LivingEntity _entity) {
							_entity.yBodyRotO = _entity.getYRot();
							_entity.yHeadRotO = _entity.getYRot();
						}
					}
				}
			} else if (world.getBlockState(BlockPos.containing(x, y + 0, z + 1)).canOcclude() && world.getBlockState(BlockPos.containing(x, y + 1, z + 1)).canOcclude() && world.getBlockState(BlockPos.containing(x, y + 2, z + 1)).canOcclude()
					&& !(!world.getEntitiesOfClass(Player.class, new AABB(Vec3.ZERO, Vec3.ZERO).move(new Vec3(x, y, z)).inflate(5 / 2d), e -> true).isEmpty()) && (entity.getDirection()) == Direction.SOUTH) {
				if (entity.getDeltaMovement().y() == 0.2) {
					if (entity instanceof CaveDwellerEntity) {
						((CaveDwellerEntity) entity).setControllerAnimation("procedure", "animation.cave_dweller.climb");
					}
					{
						Entity _ent = entity;
						_ent.setYRot(0);
						_ent.setXRot(entity.getXRot());
						_ent.setYBodyRot(_ent.getYRot());
						_ent.setYHeadRot(_ent.getYRot());
						_ent.yRotO = _ent.getYRot();
						_ent.xRotO = _ent.getXRot();
						if (_ent instanceof LivingEntity _entity) {
							_entity.yBodyRotO = _entity.getYRot();
							_entity.yHeadRotO = _entity.getYRot();
						}
					}
				}
			} else if (world.getBlockState(BlockPos.containing(x, y + 0, z - 1)).canOcclude() && world.getBlockState(BlockPos.containing(x, y + 1, z - 1)).canOcclude() && world.getBlockState(BlockPos.containing(x, y + 2, z - 1)).canOcclude()
					&& !(!world.getEntitiesOfClass(Player.class, new AABB(Vec3.ZERO, Vec3.ZERO).move(new Vec3(x, y, z)).inflate(5 / 2d), e -> true).isEmpty()) && (entity.getDirection()) == Direction.NORTH) {
				if (entity.getDeltaMovement().y() == 0.2) {
					if (entity instanceof CaveDwellerEntity) {
						((CaveDwellerEntity) entity).setControllerAnimation("procedure", "animation.cave_dweller.climb");
					}
					{
						Entity _ent = entity;
						_ent.setYRot(180);
						_ent.setXRot(entity.getXRot());
						_ent.setYBodyRot(_ent.getYRot());
						_ent.setYHeadRot(_ent.getYRot());
						_ent.yRotO = _ent.getYRot();
						_ent.xRotO = _ent.getXRot();
						if (_ent instanceof LivingEntity _entity) {
							_entity.yBodyRotO = _entity.getYRot();
							_entity.yHeadRotO = _entity.getYRot();
						}
					}
				}
			} else {
				if ((((CaveDwellerEntity) entity).getControllerAnimation("procedure")).equals("animation.cave_dweller.climb")) {
					if (entity instanceof CaveDwellerEntity) {
						((CaveDwellerEntity) entity).setControllerAnimation("procedure", "empty");
					}
				}
			}
		}
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "");
		if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
			target = entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null;
			Vec3 forward = new Vec3(target.getX() - entity.getX(), 0, target.getZ() - entity.getZ()).normalize();
			yx = entity.getX() + forward.x * 0.6;
			yz = entity.getZ() + forward.z * 0.6;
			if (world.getBlockState(BlockPos.containing(yx, entity.getY() + 1, yz)).canOcclude()) {
				if (!world.getBlockState(BlockPos.containing(yx, entity.getY(), yz)).canOcclude()) {
					shouldCrouch = true;
					if (!world.isClientSide()) {
						world.destroyBlock(BlockPos.containing(yx, entity.getY() + 1, yz), false);
					}
				}
			} else if (!world.getBlockState(BlockPos.containing(yx, entity.getY() + 1, yz)).canOcclude() && world.getBlockState(BlockPos.containing(yx, entity.getY(), yz)).canOcclude()) {
				shouldCrouch = true;
				if (!world.isClientSide()) {
					world.destroyBlock(BlockPos.containing(yx, entity.getY(), yz), false);
				}
			} else {
				shouldSqueeze = false;
				if (world.getBlockState(BlockPos.containing(yx, entity.getY() + 2, yz)).canOcclude()) {
					shouldCrouch = true;
				} else {
					shouldCrouch = false;
				}
			}
		}
		if (world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() + 2, entity.getZ())).canOcclude() || world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() + 1, entity.getZ())).canOcclude()
				|| world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() + 1.5, entity.getZ())).canOcclude() || world.getBlockState(BlockPos.containing(Math.ceil(entity.getX()), entity.getY() + 2, entity.getZ())).canOcclude()
				|| world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() + 2, Math.ceil(entity.getZ()))).canOcclude()
				|| world.getBlockState(BlockPos.containing(Math.ceil(entity.getX()), entity.getY() + 2, Math.ceil(entity.getZ()))).canOcclude()
				|| world.getBlockState(BlockPos.containing(Math.floor(entity.getX()), entity.getY() + 2, entity.getZ())).canOcclude()
				|| world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() + 2, Math.floor(entity.getZ()))).canOcclude()
				|| world.getBlockState(BlockPos.containing(Math.floor(entity.getX()), entity.getY() + 2, Math.floor(entity.getZ()))).canOcclude() || entity.isInWall()) {
			shouldCrouch = true;
		}
		if (!(entity instanceof CaveDwellerEntity _datEntL245 && _datEntL245.getEntityData().get(CaveDwellerEntity.DATA_CLIMBING))) {
			if (shouldSqueeze) {
				if (entity instanceof CaveDwellerEntity _datEntSetL)
					_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_SQUEEZING, true);
			} else {
				if (entity instanceof CaveDwellerEntity _datEntSetL)
					_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_SQUEEZING, false);
				if (shouldCrouch) {
					if (entity instanceof CaveDwellerEntity _datEntSetL)
						_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_CROUCHING, true);
				} else {
					if (entity instanceof CaveDwellerEntity _datEntSetL)
						_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_CROUCHING, false);
				}
			}
		} else {
			if (entity instanceof CaveDwellerEntity _datEntSetL)
				_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_SQUEEZING, false);
			if (entity instanceof CaveDwellerEntity _datEntSetL)
				_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_CROUCHING, false);
		}
		if (entity instanceof CaveDwellerEntity _datEntL252 && _datEntL252.getEntityData().get(CaveDwellerEntity.DATA_CROUCHING)) {
			ScaleTypes.HITBOX_HEIGHT.getScaleData(entity).setTargetScale((float) ScaleOperations.SET.applyAsDouble(ScaleTypes.HITBOX_HEIGHT.getScaleData(entity).getTargetScale(), 0.68));
			if (entity instanceof CaveDwellerEntity) {
				((CaveDwellerEntity) entity).setControllerAnimation("procedure", "animation.cave_dweller.crouch_run_new");
			}
		} else {
			ScaleTypes.HITBOX_HEIGHT.getScaleData(entity).setTargetScale((float) ScaleOperations.SET.applyAsDouble(ScaleTypes.HITBOX_HEIGHT.getScaleData(entity).getTargetScale(), 1));
		}
		if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.LAVA) {
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 3, false, false));
			entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x() * 2), (entity.getDeltaMovement().y()), (entity.getDeltaMovement().z() * 2)));
		}
	}

	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}
}