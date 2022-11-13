package com.yftach.firstmod.minecraftNetworking.packet;

import java.util.function.Supplier;

import com.yftach.firstmod.messageIdentification.MessageIDProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;

public class MessageC2SPacket {
	
	private String uuid;
	private BlockPos pos;
	
	public MessageC2SPacket(String id, BlockPos pos) {
		this.uuid = id;
		this.pos = pos;
		System.out.println("C2S CONS: " + this.pos);
	}
	
	public MessageC2SPacket(FriendlyByteBuf buf) {
		this.uuid = buf.readUtf();
		this.pos = buf.readBlockPos();
	}
	
	public void toBytes(FriendlyByteBuf buf) {
		buf.writeUtf(uuid);
		buf.writeBlockPos(pos);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> {
			ServerLevel level = context.getSender().getLevel();
			
			System.out.println("BLOCK ENTITY POS: " + this.pos);
			try {
				System.out.println("HOMO " + level.getBlockEntity(this.pos));
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			level.getBlockEntity(this.pos).getCapability(MessageIDProvider.MESSAGE_ID).ifPresent(id -> {
				System.out.println("ENQUE WORK");
				id.setId(uuid);
			});
		});
		return true;
	}
}
