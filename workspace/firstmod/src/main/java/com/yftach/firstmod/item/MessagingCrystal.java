package com.yftach.firstmod.item;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.yftach.firstmod.FirstMod;
import com.yftach.firstmod.block.MessageBlock;
import com.yftach.firstmod.block.entity.MessageBlockEntity;
import com.yftach.firstmod.init.BlockInit;
import com.yftach.firstmod.networking.Communication;

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

public class MessagingCrystal extends Item{

	public MessagingCrystal(Properties p_41383_) {
		super(p_41383_);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)  {
		
		if(level.isClientSide())
			return super.use(level, player, hand);
		
//		HttpResponse<String> res = Communication.getReq("https://api.mojang.com/user/profile/" + player.getStringUUID());
//		if(res.statusCode() != 200) {
//			player.sendSystemMessage(Component.literal(
//					"Couldn't retrieve player username from Mojange servers").withStyle(ChatFormatting.RED));
//			return super.use(world, player, hand);
//		}
		//JsonObject resJson = JsonParser.parseString(res.toString()).getAsJsonObject();
		//toSend.add("playerName", resJson.get("name"));
		
		if(player.isOnGround() && !player.isInFluidType() && 
				!isOnMessageBlock(level, player)) {
			BlockPos playerPos = player.blockPosition();
			level.setBlockAndUpdate(playerPos, getDirection(player));
			System.out.println(level.getChunkAt(playerPos).getMaxBuildHeight());
			player.sendSystemMessage(Component.literal(
					"To commit the message please enter text (after commiting the message cannot be edited!)")
					.withStyle(ChatFormatting.GREEN));
		} 
		else
			player.sendSystemMessage(Component.literal(
					"Cannot place a message here!").withStyle(ChatFormatting.RED));
		
		
		return super.use(level, player, hand);
		
	}
	
	private boolean isOnMessageBlock(Level world, Player player) {
		
		BlockState state = world.getBlockState(player.blockPosition());
		for(Direction dir: MessageBlock.possibleDirections) 
			if(state.equals(BlockInit.MASSAGE_BLOCK.get().defaultBlockState()
					.setValue(MessageBlock.FACING, dir)))
				return true;
		return false;
			
	}
	
	private BlockState getDirection(Player player) {
		
		Direction dir = Direction.fromAxisAndDirection(player.getDirection().getAxis(),
				player.getDirection().getAxisDirection());
		return BlockInit.MASSAGE_BLOCK.get().defaultBlockState().setValue(MessageBlock.FACING, dir);
		
	}
}
