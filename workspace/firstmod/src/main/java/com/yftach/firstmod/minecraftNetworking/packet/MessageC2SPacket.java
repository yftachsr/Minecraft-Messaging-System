package com.yftach.firstmod.minecraftNetworking.packet;

import java.util.function.Supplier;

import com.yftach.firstmod.messageIdentification.MessageIDProvider;
import com.yftach.firstmod.minecraftNetworking.Network;

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
			
			level.getBlockEntity(this.pos).getCapability(MessageIDProvider.MESSAGE_ID).ifPresent(id -> {
				System.out.println("ENQUE WORK");
				id.setId(uuid);
			});
			Network.sendToPlayer(new MessageS2CPacket(uuid, pos), context.getSender());
		});
		return true;
	}
}
