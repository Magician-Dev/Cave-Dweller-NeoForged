package com.magiciandev.cavenoise.mixin;

import com.magiciandev.cavenoise.entity.CaveDwellerEntity;

import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(targets = "net.minecraft.server.level.ChunkMap$TrackedEntity")
public class CaveDwellerUpdateIntervalMixin {

    @ModifyArg(
        method = "<init>(Lnet/minecraft/world/entity/Entity;IIZ)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerEntity;<init>(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;IZLjava/util/function/Consumer;)V"
        ),
        index = 2
    )
    private int cavenoise$changeUpdateInterval(int updateInterval, Entity entity) {
        if (entity instanceof CaveDwellerEntity) {
            return 1;
        }

        return updateInterval;
    }
}
