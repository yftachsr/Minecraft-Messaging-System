package com.yftach.messagemod.item;

import com.yftach.messagemod.block.MessageBlock;
import com.yftach.messagemod.init.BlockInit;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MessagingCrystal extends Item {
	
	private static final String INSTRUCTION_MESSAGE = "message.messagemod.instruction_message";
	private static final String PLACEMENT_FAILURE = "message.messagemod.placement_failure";

	public MessagingCrystal(Properties p_41383_) {
		super(p_41383_);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)  {
		
		if(level.isClientSide())
			return super.use(level, player, hand);
		
		if(player.isOnGround() && !player.isInFluidType() && 
				!isOnMessageBlock(level, player)) {
			BlockPos playerPos = player.blockPosition();
			level.setBlockAndUpdate(playerPos, getBlockState(player));
			player.sendSystemMessage(Component.translatable(INSTRUCTION_MESSAGE).withStyle(ChatFormatting.GREEN));
		} 
		else
			player.sendSystemMessage(Component.translatable(PLACEMENT_FAILURE).withStyle(ChatFormatting.RED));
		
		return super.use(level, player, hand);
		
	}
	
	private boolean isOnMessageBlock(Level world, Player player) {
		
		BlockState state = world.getBlockState(player.blockPosition());
		for(Direction dir: MessageBlock.POSSIBLE_DIRECTIONS) 
			if(state.equals(BlockInit.MASSAGE_BLOCK.get().defaultBlockState()
					.setValue(MessageBlock.FACING, dir)))
				return true;
		return false;
			
	}
	
	private BlockState getBlockState(Player player) {
		
		Direction dir = Direction.fromAxisAndDirection(player.getDirection().getAxis(),
				player.getDirection().getAxisDirection());
		return BlockInit.MASSAGE_BLOCK.get().defaultBlockState().setValue(MessageBlock.FACING, dir);
		
	}
}
