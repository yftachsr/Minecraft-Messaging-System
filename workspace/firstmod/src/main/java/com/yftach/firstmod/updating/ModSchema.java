package com.yftach.firstmod.updating;


public abstract class ModSchema {
	protected String playerUUID, playerName;
	protected double x, y, z;
	protected int dir;
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
}
