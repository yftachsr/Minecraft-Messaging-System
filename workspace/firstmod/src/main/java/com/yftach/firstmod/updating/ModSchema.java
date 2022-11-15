package com.yftach.firstmod.updating;


public abstract class ModSchema {
	protected String playerUUID, id;
	protected double x, y, z;
	protected int dir;
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getZ() {
		return z;
	}
	
	public String getUUID() {
		return playerUUID;
	}
}
