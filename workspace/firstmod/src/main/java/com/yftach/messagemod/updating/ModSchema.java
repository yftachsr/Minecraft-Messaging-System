package com.yftach.messagemod.updating;

import net.minecraft.core.BlockPos;

public abstract class ModSchema {
	protected String playerUUID, _id;
	protected double x, y, z;
	protected int dir;
	protected boolean toBeDeleted = false;
	
	public ModSchema(String playerUUID, String _id, double x, double y, double z, int dir) {
		this.playerUUID = playerUUID;
		this._id = _id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.dir = dir;
	}
	
	public ModSchema(String playerUUID, double x, double y, double z, int dir) {
		this.playerUUID = playerUUID;
		this.x = x;
		this.y = y;
		this.z = z;
		this.dir = dir;
	}
	
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
	
	public BlockPos getBlockPos() {
		return new BlockPos(x, y, z);
	}
	
	public String getUUID() {
		return playerUUID;
	}
	
	public String getId() {
		return _id;
	}
	
	public boolean toBeDeleted() {
		return toBeDeleted;
	}
	
	public void setToBeDeleted(boolean delete) {
		this.toBeDeleted = delete;
	}
	
}
