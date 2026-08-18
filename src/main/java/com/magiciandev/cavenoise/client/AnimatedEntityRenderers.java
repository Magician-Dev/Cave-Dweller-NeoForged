package com.magiciandev.cavenoise.client;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import com.magiciandev.cavenoise.init.CavenoiseModEntities;
import com.magiciandev.cavenoise.client.renderer.CaveDwellerRenderer;
import com.magiciandev.cavenoise.CavenoiseMod;

@EventBusSubscriber(modid = CavenoiseMod.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class AnimatedEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(CavenoiseModEntities.CAVE_DWELLER.get(), CaveDwellerRenderer::new);
	}
}