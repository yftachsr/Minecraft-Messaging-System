package com.yftach.firstmod.messageIdentification;

import net.minecraft.nbt.CompoundTag;

public class MessageID {

	private String id = "";
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void saveNBTData(CompoundTag nbt) {
		nbt.putString("messageID", id);
	}
	
	public void loadNBTData(CompoundTag nbt) {
		this.id = nbt.getString("messageID");
	}
}
