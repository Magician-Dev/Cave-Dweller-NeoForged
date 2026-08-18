package com.magiciandev.cavenoise.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import java.util.Comparator;

import com.magiciandev.cavenoise.entity.CaveDwellerEntity;

public class CaveDwellerTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (!world.getEntitiesOfClass(Player.class, new AABB(Vec3.ZERO, Vec3.ZERO).move(new Vec3(x, y, z)).inflate(128 / 2d), e -> true).isEmpty()) {
			if (entity instanceof Mob _entity && (findEntityInWorldRange(world, Player.class, x, y, z, 128)) instanceof LivingEntity _ent)
				_entity.setTarget(_ent);
		}
		if (entity instanceof CaveDwellerEntity _datEntL3 && _datEntL3.getEntityData().get(CaveDwellerEntity.DATA_STALKING)) {
			DwellerStalkProcedure.execute(world, x, y, z, entity);
		}
		if (entity instanceof CaveDwellerEntity _datEntL4 && _datEntL4.getEntityData().get(CaveDwellerEntity.DATA_SPOTTED)) {
			DwellerSpottedProcedure.execute(world, x, y, z, entity);
		}
		if (entity instanceof CaveDwellerEntity _datEntL5 && _datEntL5.getEntityData().get(CaveDwellerEntity.DATA_AGGRO)) {
			DwellerAggroProcedure.execute(world, x, y, z, entity);
			if (!world.getEntitiesOfClass(Player.class, new AABB(Vec3.ZERO, Vec3.ZERO).move(new Vec3(x, y, z)).inflate(5 / 2d), e -> true).isEmpty()) {
				if (((findEntityInWorldRange(world, Player.class, x, y, z, 5)) instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == Items.SHIELD) {
					if (world instanceof ServerLevel _level) {
						((findEntityInWorldRange(world, Player.class, x, y, z, 5)) instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).hurtAndBreak(256, _level, null, _stkprov -> {
						});
					}
				}
				if (((findEntityInWorldRange(world, Player.class, x, y, z, 5)) instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.SHIELD) {
					if (world instanceof ServerLevel _level) {
						((findEntityInWorldRange(world, Player.class, x, y, z, 5)) instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).hurtAndBreak(256, _level, null, _stkprov -> {
						});
					}
				}
			}
		}
		if (entity instanceof CaveDwellerEntity _datEntL19 && _datEntL19.getEntityData().get(CaveDwellerEntity.DATA_FLEEING)) {
			DwellerFleeProcedure.execute(world, x, y, z, entity);
		}
		if (!(world.getBlockState(BlockPos.containing(x, y - 1, z))).is(BlockTags.create(ResourceLocation.parse("cavenoise:passthrough")))) {
			if (entity instanceof LivingEntity _livEnt22 && _livEnt22.hasEffect(MobEffects.INVISIBILITY) && (!DwellerTargetSeesMeProcedure.execute(entity) || !InPlayerLineOfSightProcedure.execute(entity))) {
				if (entity instanceof LivingEntity _entity)
					_entity.removeEffect(MobEffects.INVISIBILITY);
				if (entity instanceof CaveDwellerEntity _datEntSetL)
					_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_STALKING, true);
			}
			if (!(entity instanceof CaveDwellerEntity _datEntL25 && _datEntL25.getEntityData().get(CaveDwellerEntity.DATA_FLEEING))
					&& !(entity instanceof CaveDwellerEntity _datEntL26 && _datEntL26.getEntityData().get(CaveDwellerEntity.DATA_CROUCHING))
					&& !(entity instanceof CaveDwellerEntity _datEntL27 && _datEntL27.getEntityData().get(CaveDwellerEntity.DATA_SQUEEZING))
					&& !(entity instanceof CaveDwellerEntity _datEntL28 && _datEntL28.getEntityData().get(CaveDwellerEntity.DATA_SPOTTED))
					&& !(entity instanceof CaveDwellerEntity _datEntL29 && _datEntL29.getEntityData().get(CaveDwellerEntity.DATA_AGGRO))
					&& !(entity instanceof CaveDwellerEntity _datEntL30 && _datEntL30.getEntityData().get(CaveDwellerEntity.DATA_STALKING))
					&& !(entity instanceof CaveDwellerEntity _datEntL31 && _datEntL31.getEntityData().get(CaveDwellerEntity.DATA_CLIMBING)) && !(entity instanceof LivingEntity _livEnt32 && _livEnt32.hasEffect(MobEffects.INVISIBILITY))) {
				if (entity instanceof CaveDwellerEntity _datEntSetL)
					_datEntSetL.getEntityData().set(CaveDwellerEntity.DATA_STALKING, true);
			}
		}
		if (entity.isPassenger()) {
			entity.stopRiding();
		}
		if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 1200, 5, false, false));
		if (!((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem() == Items.LEATHER_BOOTS)) {
			{
				Entity _entity = entity;
				if (_entity instanceof Player _player) {
					_player.getInventory().armor.set(0, new ItemStack(Items.LEATHER_BOOTS));
					_player.getInventory().setChanged();
				} else if (_entity instanceof LivingEntity _living) {
					_living.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
				}
			}
			if (!((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).isEnchanted())) {
				(entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).enchant(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.DEPTH_STRIDER), 10);
			}
		}
	}

	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}
}