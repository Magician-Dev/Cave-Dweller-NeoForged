package com.magiciandev.cavenoise.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;

public class DwellerTargetSeesMeProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		boolean yawPlayerLookingTowards = false;
		boolean pitchPlayerLookingTowards = false;
		boolean shouldOnlyUsePitch = false;
		double fov = 0;
		double yFovMod = 0;
		double fovMod = 0;
		double newAngle = 0;
		double lookX = 0;
		double lookZ = 0;
		double newLookAngle = 0;
		double newNewAngle = 0;
		double yFov = 0;
		double yAngle = 0;
		double lookY = 0;
		double yLookAngle = 0;
		double newYAngle = 0;
		Entity pendingTarget = null;
		Vec3 a = Vec3.ZERO;
		Vec3 b = Vec3.ZERO;
		Vec3 dist = Vec3.ZERO;
		Vec3 yDist = Vec3.ZERO;
		Vec3 lookDist = Vec3.ZERO;
		String clientDirectoryTestVariable = "";
		clientDirectoryTestVariable = "Client: " + Minecraft.getInstance().gameDirectory.getAbsolutePath();
		if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
			pendingTarget = entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null;
			Minecraft minecraft = Minecraft.getInstance();
			fov = (double) minecraft.options.fov().get().intValue();
			yFovMod = 0.65;
			fovMod = (35 / (fov - 1)) * 0.4 + 1;
			fov = fov * fovMod;
			a = pendingTarget.position();
			b = entity.position();
			dist = new Vec3((b.x() - a.x()), 0, (b.z() - a.z()));
			dist = dist.normalize();
			newAngle = Math.toDegrees(Math.atan2(dist.x(), dist.z()));
			lookX = (pendingTarget.getLookAngle()).x();
			lookZ = (pendingTarget.getLookAngle()).z();
			newLookAngle = Math.toDegrees(Math.atan2(lookX, lookZ));
			newNewAngle = LoopAngleProcedure.execute(newAngle - newLookAngle) + fov;
			newNewAngle = LoopAngleProcedure.execute(newNewAngle);
			if (newNewAngle > 0 && newNewAngle < fov * 2) {
				yawPlayerLookingTowards = true;
			}
			yFov = fov * yFovMod;
			yDist = new Vec3(Math.sqrt((b.x() - a.x()) * (b.x() - a.x()) + (b.z() - a.z()) * (b.z() - a.z())), 0, (b.y() - a.y()));
			yDist = yDist.normalize();
			yAngle = Math.toDegrees(Math.atan2(yDist.x(), yDist.z()));
			lookY = (pendingTarget.getLookAngle()).y();
			lookDist = new Vec3(Math.sqrt(lookX * lookX + lookZ * lookZ), 0, lookY);
			lookDist = lookDist.normalize();
			yLookAngle = Math.toDegrees(Math.atan2(lookDist.x(), lookDist.z()));
			newYAngle = LoopAngleProcedure.execute(yAngle - yLookAngle) + yFov;
			newYAngle = LoopAngleProcedure.execute(newYAngle);
			if (newYAngle > 0 && newYAngle < yFov * 2) {
				pitchPlayerLookingTowards = true;
			}
			if (!(yLookAngle < 180 - yFov) || !(yLookAngle > yFov)) {
				shouldOnlyUsePitch = true;
			}
			return (yawPlayerLookingTowards || shouldOnlyUsePitch) && pitchPlayerLookingTowards;
		}
		return false;
	}
}