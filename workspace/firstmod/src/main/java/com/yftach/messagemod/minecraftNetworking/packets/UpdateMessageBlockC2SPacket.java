package com.yftach.messagemod.minecraftNetworking.packets;

import java.util.function.Supplier;

import com.yftach.messagemod.block.MessageBlock;
import com.yftach.messagemod.init.BlockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkEvent;

public class UpdateMessageBlockC2SPacket {
	
	private BlockPos pos;
	private boolean place; // whether to place or delete a message
	private int direction;

	public UpdateMessageBlockC2SPacket(BlockPos pos, boolean place, int direction) {
		this.pos = pos;
		this.place = place;
		this.direction = direction;
	}
	
	public UpdateMessageBlockC2SPacket(BlockPos pos, boolean place) {
		this.pos = pos;
		this.place = place;
		this.direction = 0;
	}
	 
	public UpdateMessageBlockC2SPacket(FriendlyByteBuf buf) {
		pos = buf.readBlockPos();
		place = buf.readBoolean();
		direction = buf.readInt();
	}
	 
	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeBoolean(place);
		buf.writeInt(direction);
	}
	 
	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> {
			ServerLevel level = context.getSender().getLevel();
			if(!place) 
				level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());		
			else
				level.setBlockAndUpdate(pos, BlockInit.MASSAGE_BLOCK.get().defaultBlockState()
					.setValue(MessageBlock.FACING, MessageBlock.POSSIBLE_DIRECTIONS[this.direction]));
		});
		
		return true;		 
	}
}
