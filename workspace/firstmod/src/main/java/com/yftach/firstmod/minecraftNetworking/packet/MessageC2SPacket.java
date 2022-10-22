package com.yftach.firstmod.minecraftNetworking.packet;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class MessageC2SPacket {
	
	private String text;
	
	public MessageC2SPacket() {}
	
	public MessageC2SPacket(FriendlyByteBuf buf) {
		this.text = buf.readUtf();
	}
	
	public void toBytes(FriendlyByteBuf buf) {
		buf.writeUtf(text);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		//context.enqueueWork(() -> {
			
		//});
		return true;
	}
}
