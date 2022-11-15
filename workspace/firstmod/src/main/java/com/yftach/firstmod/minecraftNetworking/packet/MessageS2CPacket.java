package com.yftach.firstmod.minecraftNetworking.packet;

import java.util.function.Supplier;

import com.yftach.firstmod.messageIdentification.MessageID;
import com.yftach.firstmod.messageIdentification.MessageIDProvider;
import com.yftach.firstmod.minecraftNetworking.Network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;

public class MessageS2CPacket {
	private String id;
	private BlockPos pos;
	
	public MessageS2CPacket(String id, BlockPos pos) {
		this.id = id;
		this.pos = pos;
		System.out.println("S2C CONS: " + pos);
	}
	
	public MessageS2CPacket(FriendlyByteBuf buf) {
		this.id = buf.readUtf();
		this.pos = buf.readBlockPos();
	}
	
	public void toBytes(FriendlyByteBuf buf) {
		buf.writeUtf(id);
		buf.writeBlockPos(pos);
		
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
        	System.out.println("POS IN CLIENT1: " + pos);
        	ServerLevel level = null;
        	
        	try {
        		level = context.getSender().getLevel();
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        	
        	System.out.println("POS IN CLIENT2: " + pos);
        	level.getBlockEntity(pos).getCapability(MessageIDProvider.MESSAGE_ID).ifPresent(messageId -> {
        		System.out.println("CLIENTTTT");
        		messageId.setId(id);
        	});
        });
        return true;
    }
	
}
