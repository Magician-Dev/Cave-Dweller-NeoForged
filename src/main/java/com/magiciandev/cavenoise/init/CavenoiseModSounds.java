/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.magiciandev.cavenoise.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import com.magiciandev.cavenoise.CavenoiseMod;

public class CavenoiseModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, CavenoiseMod.MODID);
	public static final DeferredHolder<SoundEvent, SoundEvent> CHASE = REGISTRY.register("chase", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("cavenoise", "chase")));
	public static final DeferredHolder<SoundEvent, SoundEvent> STALK = REGISTRY.register("stalk", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("cavenoise", "stalk")));
	public static final DeferredHolder<SoundEvent, SoundEvent> SPOTTED = REGISTRY.register("spotted", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("cavenoise", "spotted")));
	public static final DeferredHolder<SoundEvent, SoundEvent> FLEE = REGISTRY.register("flee", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("cavenoise", "flee")));
	public static final DeferredHolder<SoundEvent, SoundEvent> DWELLER_HURT = REGISTRY.register("dweller_hurt", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("cavenoise", "dweller_hurt")));
	public static final DeferredHolder<SoundEvent, SoundEvent> DWELLER_DEATH = REGISTRY.register("dweller_death", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("cavenoise", "dweller_death")));
	public static final DeferredHolder<SoundEvent, SoundEvent> DISAPPEAR = REGISTRY.register("disappear", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("cavenoise", "disappear")));
	public static final DeferredHolder<SoundEvent, SoundEvent> CLIMB = REGISTRY.register("climb", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("cavenoise", "climb")));
	public static final DeferredHolder<SoundEvent, SoundEvent> CHASE_STEP = REGISTRY.register("chase_step", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("cavenoise", "chase_step")));
	public static final DeferredHolder<SoundEvent, SoundEvent> CAVENOISE = REGISTRY.register("cavenoise", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("cavenoise", "cavenoise")));
	public static final DeferredHolder<SoundEvent, SoundEvent> BREATHING = REGISTRY.register("breathing", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("cavenoise", "breathing")));
}