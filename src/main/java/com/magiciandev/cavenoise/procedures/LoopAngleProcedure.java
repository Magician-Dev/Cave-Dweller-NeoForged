package com.magiciandev.cavenoise.procedures;

public class LoopAngleProcedure {
	public static double execute(double angle) {
		if (angle > 360) {
			return angle - 360;
		} else {
			if (angle < 0) {
				return angle + 360;
			}
		}
		return angle;
	}
}