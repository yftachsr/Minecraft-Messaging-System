package com.yftach.firstmod.messageIdentification;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public class MessageIDHandler implements IMessageIDHandler, INBTSerializable<CompoundTag>{
	
	protected String id;
	
	public MessageIDHandler() {
		id = "";
	}
	
	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
        nbt.putString("id", id);
        return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		setID(nbt.contains("id", Tag.TAG_STRING) ? nbt.getString("id") : id);
		onLoad();
	}
	
	protected void onLoad() {
		
	}
	
	protected void onContentsChanged() {

    }

	@Override
	public String getID() {
		return id;
	}

	@Override
	public void setID(String id) {
		this.id = id;
		onContentsChanged();
	}

}
