package com.yftach.firstmod.messageIdentification;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class MessageIDProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>{
	
	public static Capability<MessageID> MESSAGE_ID = CapabilityManager.get(new CapabilityToken<MessageID>() {});
	
	private MessageID id = null;
	private LazyOptional<MessageID> optional = LazyOptional.of(this::createMessageID);
	
	private MessageID createMessageID() {
		if(this.id == null)
			this.id = new MessageID();
		return this.id;
	}
	
	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		createMessageID().saveNBTData(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		createMessageID().loadNBTData(nbt);
	}

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		System.out.println(optional.cast().isPresent());
		if(cap == MESSAGE_ID)
			return optional.cast();
		return LazyOptional.empty();
	}
	
	
}
