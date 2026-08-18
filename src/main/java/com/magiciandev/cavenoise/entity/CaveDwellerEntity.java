package com.magiciandev.cavenoise.entity;

import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.GeoEntity;

import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.common.NeoForgeMod;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.*;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

import com.magiciandev.cavenoise.procedures.DwellerStrollConditionProcedure;
import com.magiciandev.cavenoise.procedures.DwellerMeleeConditionProcedure;
import com.magiciandev.cavenoise.procedures.DwellerDespawnProcedure;
import com.magiciandev.cavenoise.procedures.CaveDwellerTickProcedure;
import com.magiciandev.cavenoise.procedures.CaveDwellerSpawnsProcedure;
import com.magiciandev.cavenoise.procedures.DwellerKillsProcedure;

public class CaveDwellerEntity extends Monster implements GeoEntity {
	public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(CaveDwellerEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(CaveDwellerEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(CaveDwellerEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Boolean> DATA_FLEEING = SynchedEntityData.defineId(CaveDwellerEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> DATA_CROUCHING = SynchedEntityData.defineId(CaveDwellerEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> DATA_SQUEEZING = SynchedEntityData.defineId(CaveDwellerEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> DATA_SPOTTED = SynchedEntityData.defineId(CaveDwellerEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> DATA_AGGRO = SynchedEntityData.defineId(CaveDwellerEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> DATA_STALKING = SynchedEntityData.defineId(CaveDwellerEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> DATA_CLIMBING = SynchedEntityData.defineId(CaveDwellerEntity.class, EntityDataSerializers.BOOLEAN);
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private boolean swinging;
	private boolean lastloop;
	private long lastSwing;
	public String animationprocedure = "empty";

	public CaveDwellerEntity(EntityType<CaveDwellerEntity> type, Level world) {
		super(type, world);
		xpReward = 15;
		setNoAi(false);
		setPersistenceRequired();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(SHOOT, false);
		builder.define(ANIMATION, "undefined");
		builder.define(TEXTURE, "cave_dweller_texture");
		builder.define(DATA_FLEEING, false);
		builder.define(DATA_CROUCHING, false);
		builder.define(DATA_SQUEEZING, false);
		builder.define(DATA_SPOTTED, false);
		builder.define(DATA_AGGRO, false);
		builder.define(DATA_STALKING, false);
		builder.define(DATA_CLIMBING, false);
	}

	public void setTexture(String texture) {
		this.entityData.set(TEXTURE, texture);
	}

	public String getTexture() {
		return this.entityData.get(TEXTURE);
	}

	@Override
public boolean doHurtTarget(Entity target) {
    float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);

    DamageSource source = new DamageSource(
        this.level().holderOrThrow(
            ResourceKey.create(
                Registries.DAMAGE_TYPE,
                ResourceLocation.parse("cavenoise:dweller_damage")
            )
        ),
        this
    );

    boolean hurt = target.hurt(source, damage);

    if (hurt) {
        double strength = 1.3D;

        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();

        double distance = Math.sqrt(dx * dx + dz * dz);

        if (distance > 0.0D) {
            dx /= distance;
            dz /= distance;
        }

        Vec3 velocity = target.getDeltaMovement();

        target.setDeltaMovement(
            velocity.x + dx * strength,
            velocity.y,
            velocity.z + dz * strength
        );

        target.hurtMarked = true;
    }

    return hurt;
}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false) {
			@Override
			protected boolean canPerformAttack(LivingEntity entity) {
				return this.isTimeToAttack() && this.mob.distanceToSqr(entity) < (this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth()) && this.mob.getSensing().hasLineOfSight(entity);
			}

			@Override
			public boolean canUse() {
				double x = CaveDwellerEntity.this.getX();
				double y = CaveDwellerEntity.this.getY();
				double z = CaveDwellerEntity.this.getZ();
				Entity entity = CaveDwellerEntity.this;
				Level world = CaveDwellerEntity.this.level();
				return super.canUse() && DwellerMeleeConditionProcedure.execute(entity);
			}

			@Override
			public boolean canContinueToUse() {
				double x = CaveDwellerEntity.this.getX();
				double y = CaveDwellerEntity.this.getY();
				double z = CaveDwellerEntity.this.getZ();
				Entity entity = CaveDwellerEntity.this;
				Level world = CaveDwellerEntity.this.level();
				return super.canContinueToUse() && DwellerMeleeConditionProcedure.execute(entity);
			}

		});
		this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.7) {
			@Override
			public boolean canUse() {
				double x = CaveDwellerEntity.this.getX();
				double y = CaveDwellerEntity.this.getY();
				double z = CaveDwellerEntity.this.getZ();
				Entity entity = CaveDwellerEntity.this;
				Level world = CaveDwellerEntity.this.level();
				return super.canUse() && DwellerStrollConditionProcedure.execute(entity);
			}

			@Override
			public boolean canContinueToUse() {
				double x = CaveDwellerEntity.this.getX();
				double y = CaveDwellerEntity.this.getY();
				double z = CaveDwellerEntity.this.getZ();
				Entity entity = CaveDwellerEntity.this;
				Level world = CaveDwellerEntity.this.level();
				return super.canContinueToUse() && DwellerStrollConditionProcedure.execute(entity);
			}
		});
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this) {
			@Override
			public boolean canUse() {
				double x = CaveDwellerEntity.this.getX();
				double y = CaveDwellerEntity.this.getY();
				double z = CaveDwellerEntity.this.getZ();
				Entity entity = CaveDwellerEntity.this;
				Level world = CaveDwellerEntity.this.level();
				return super.canUse() && DwellerStrollConditionProcedure.execute(entity);
			}

			@Override
			public boolean canContinueToUse() {
				double x = CaveDwellerEntity.this.getX();
				double y = CaveDwellerEntity.this.getY();
				double z = CaveDwellerEntity.this.getZ();
				Entity entity = CaveDwellerEntity.this;
				Level world = CaveDwellerEntity.this.level();
				return super.canContinueToUse() && DwellerStrollConditionProcedure.execute(entity);
			}
		});
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:chase_step")), 0.15f, 1);
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:dweller_hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("cavenoise:dweller_death"));
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud || source.typeHolder().is(NeoForgeMod.POISON_DAMAGE))
			return false;
		if (source.is(DamageTypes.FALL))
			return false;
		if (source.is(DamageTypes.CACTUS))
			return false;
		if (source.is(DamageTypes.DROWN))
			return false;
		if (source.is(DamageTypes.LIGHTNING_BOLT))
			return false;
		if (source.is(DamageTypes.DRAGON_BREATH))
			return false;
		if (source.is(DamageTypes.WITHER) || source.is(DamageTypes.WITHER_SKULL))
			return false;
		return super.hurt(source, amount);
	}

	@Override
protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    if (onGround && this.fallDistance > 0.0F) {
        this.fallDistance = 0.0F;
        return;
    }

    super.checkFallDamage(y, onGround, state, pos);
}
	
	@Override
	public void die(DamageSource source) {
		super.die(source);
		DwellerDespawnProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata) {
		SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata);
		CaveDwellerSpawnsProcedure.execute(world);
		return retval;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("Texture", this.getTexture());
		compound.putBoolean("DataFLEEING", this.entityData.get(DATA_FLEEING));
		compound.putBoolean("DataCROUCHING", this.entityData.get(DATA_CROUCHING));
		compound.putBoolean("DataSQUEEZING", this.entityData.get(DATA_SQUEEZING));
		compound.putBoolean("DataSPOTTED", this.entityData.get(DATA_SPOTTED));
		compound.putBoolean("DataAGGRO", this.entityData.get(DATA_AGGRO));
		compound.putBoolean("DataSTALKING", this.entityData.get(DATA_STALKING));
		compound.putBoolean("DataCLIMBING", this.entityData.get(DATA_CLIMBING));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("Texture"))
			this.setTexture(compound.getString("Texture"));
		if (compound.contains("DataFLEEING"))
			this.entityData.set(DATA_FLEEING, compound.getBoolean("DataFLEEING"));
		if (compound.contains("DataCROUCHING"))
			this.entityData.set(DATA_CROUCHING, compound.getBoolean("DataCROUCHING"));
		if (compound.contains("DataSQUEEZING"))
			this.entityData.set(DATA_SQUEEZING, compound.getBoolean("DataSQUEEZING"));
		if (compound.contains("DataSPOTTED"))
			this.entityData.set(DATA_SPOTTED, compound.getBoolean("DataSPOTTED"));
		if (compound.contains("DataAGGRO"))
			this.entityData.set(DATA_AGGRO, compound.getBoolean("DataAGGRO"));
		if (compound.contains("DataSTALKING"))
			this.entityData.set(DATA_STALKING, compound.getBoolean("DataSTALKING"));
		if (compound.contains("DataCLIMBING"))
			this.entityData.set(DATA_CLIMBING, compound.getBoolean("DataCLIMBING"));
	}

	@Override
	public void awardKillScore(Entity entity, int score, DamageSource damageSource) {
		super.awardKillScore(entity, score, damageSource);
		DwellerKillsProcedure.execute(entity);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		CaveDwellerTickProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
		this.refreshDimensions();
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose pose) {
		return super.getDefaultDimensions(pose).scale(1f);
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.4);
		builder = builder.add(Attributes.MAX_HEALTH, 180);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 6);
		builder = builder.add(Attributes.FOLLOW_RANGE, 128);
		builder = builder.add(Attributes.STEP_HEIGHT, 1);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.8);
		builder = builder.add(Attributes.ATTACK_KNOCKBACK, 1.3);
		return builder;
	}

	private PlayState movementPredicate(AnimationState event) {
		if (this.animationprocedure.equals("empty")) {
			if ((event.isMoving() || !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F))

			) {
				return event.setAndContinue(RawAnimation.begin().thenLoop("animation.cave_dweller.run"));
			}
			return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
		}
		return PlayState.STOP;
	}

	String prevAnim = "empty";

	private PlayState procedurePredicate(AnimationState event) {
		if (!animationprocedure.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED || (!this.animationprocedure.equals(prevAnim) && !this.animationprocedure.equals("empty"))) {
			if (!this.animationprocedure.equals(prevAnim))
				event.getController().forceAnimationReset();
			event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
			if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
				this.animationprocedure = "empty";
				event.getController().forceAnimationReset();
			}
		} else if (animationprocedure.equals("empty")) {
			prevAnim = "empty";
			return PlayState.STOP;
		}
		prevAnim = this.animationprocedure;
		return PlayState.CONTINUE;
	}

	@Override
	protected void tickDeath() {
		++this.deathTime;
		if (this.deathTime == 20) {
			this.remove(CaveDwellerEntity.RemovalReason.KILLED);
			this.dropExperience(this);
		}
	}

	public String getSyncedAnimation() {
		return this.entityData.get(ANIMATION);
	}

	public void setAnimation(String animation) {
		this.entityData.set(ANIMATION, animation);
	}

	/**
	 * Moves whatever procedures wrote into the synched controller slots over to the
	 * fields the AnimationControllers read, the same way EntityAnimationFactory
	 * handles the built-in "procedure" controller.
	 */
	public void applySyncedControllerAnimations() {
	}

	/**
	 * Plays an animation on a named controller. A blank or unknown controller name
	 * falls back to the built-in "procedure" controller, so procedures written
	 * before custom controllers existed keep behaving exactly as before.
	 */
	public void setControllerAnimation(String controller, String animation) {
		if (controller == null || controller.isBlank()) {
			this.setAnimation(animation);
			return;
		}
		switch (controller) {
			default -> this.setAnimation(animation);
		}
	}

	/** Currently playing animation on a named controller, or "empty" if idle. */
	public String getControllerAnimation(String controller) {
		if (controller == null || controller.isBlank())
			return this.animationprocedure;
		return switch (controller) {
			default -> this.animationprocedure;
		};
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data) {
		data.add(new AnimationController<>(this, "movement", 4, this::movementPredicate));
		data.add(new AnimationController<>(this, "procedure", 4, this::procedurePredicate));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}