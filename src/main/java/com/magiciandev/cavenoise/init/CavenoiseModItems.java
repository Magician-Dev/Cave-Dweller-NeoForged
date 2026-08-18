package com.magiciandev.cavenoise.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import net.minecraft.world.item.Item;

import com.magiciandev.cavenoise.CavenoiseMod;

public class CavenoiseModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(CavenoiseMod.MODID);
	public static final DeferredItem<Item> CAVE_DWELLER_SPAWN_EGG;
	static {
		CAVE_DWELLER_SPAWN_EGG = REGISTRY.register("cave_dweller_spawn_egg", () -> new DeferredSpawnEggItem(CavenoiseModEntities.CAVE_DWELLER, -14017007, -13555181, new Item.Properties()));
	}
	// Start of user code block custom items
	// End of user code block custom items
}