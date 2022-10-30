package com.yftach.firstmod.minecraftNetworking.packet;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class MessageC2SPacket {
	
	private String uuid;
	
	public MessageC2SPacket(String id) {
		this.uuid = id;
	}
	
	public MessageC2SPacket(FriendlyByteBuf buf) {
		this.uuid = buf.readUtf();
	}
	
	public void toBytes(FriendlyByteBuf buf) {
		buf.writeUtf(uuid);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		//context.enqueueWork(() -> {
			
		//});
		return true;
	}
}
