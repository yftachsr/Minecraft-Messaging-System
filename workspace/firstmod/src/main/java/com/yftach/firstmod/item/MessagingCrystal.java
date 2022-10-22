package com.yftach.firstmod.item;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yftach.firstmod.block.MessageBlock;
import com.yftach.firstmod.init.BlockInit;
import com.yftach.firstmod.networking.Communication;

import net.minecraft.ChatFormatting;
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
	
	private Communication com;
	private String serverAddress = "http://localhost:3000";

	public MessagingCrystal(Properties p_41383_) {
		super(p_41383_);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)  {
		
		if(world.isClientSide())
			return super.use(world, player, hand);
		com = new Communication("https://api.mojang.com/user/profile/" + player.getStringUUID());
//		HttpResponse<String> res = com.getReq();
//		if(res.statusCode() != 200) {
//			player.sendSystemMessage(Component.literal(
//					"Couldn't retrieve player username from Mojange servers").withStyle(ChatFormatting.RED));
//			return super.use(world, player, hand);
//		}
		//JsonObject resJson = JsonParser.parseString(res.toString()).getAsJsonObject();
		JsonObject toSend = new JsonObject();
		//toSend.add("playerName", resJson.get("name"));
		toSend.addProperty("playerName", player.getName().toString());
		toSend.addProperty("x", player.getX());
		toSend.addProperty("y", player.getY());
		toSend.addProperty("z", player.getZ());
		com = new Communication(serverAddress);
		if(player.isOnGround() && !player.isInFluidType() && 
				!isOnMessageBlock(world, player)) {
			world.setBlockAndUpdate(player.blockPosition(), getDirection(player));
			com.postReq(toSend);
		} 
		else
			player.sendSystemMessage(Component.literal(
					"Cannot place a message here!").withStyle(ChatFormatting.RED));
		
		
		return super.use(world, player, hand);
		
	}
	
	private boolean isOnMessageBlock(Level world, Player player) {
		
		Direction directions[] = {Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.EAST};
		BlockState state = world.getBlockState(player.blockPosition());
		for(int i = 0; i < 4; i++) {
			if(state == BlockInit.MASSAGE_BLOCK.get().defaultBlockState()
					.setValue(MessageBlock.FACING, directions[i]))
				return true;
		}
		
		return false;
			
	}
	
	private void sendData(Player player) {
		
		String postEndpoint = "http://localhost:3000";
		
		String inputJson = "{ \"playerName\": \"" + player.getName().toString() + "\"" +
        		", \"x\": \"" + player.getX() + "\"" +
        		", \"y\": \"" + player.getY() + "\"" +
        		", \"z\": \"" + player.getZ() + "\" }";
        		//", \"text\": \"\" }";
 
       
        HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(postEndpoint))
	            .header("Content-Type", "application/json")
	            .POST(HttpRequest.BodyPublishers.ofString(inputJson))
	            .build();
		
        HttpResponse<String> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

        System.out.println("status:" + response.statusCode());
        System.out.println("response:\n" + response.body());
	}
	
	private BlockState getDirection(Player player) {
		
		Direction dir = Direction.fromAxisAndDirection(player.getDirection().getAxis(),
				player.getDirection().getAxisDirection());
		return BlockInit.MASSAGE_BLOCK.get().defaultBlockState().setValue(MessageBlock.FACING, dir);
		
	}
}
