package com.yftach.firstmod.updating;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yftach.firstmod.block.MessageBlock;
import com.yftach.firstmod.event.ModEvents;
import com.yftach.firstmod.init.BlockInit;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

/**
 * Class responsible for updating the mod blocks in the world <br>
 * Most of the methods in this class are generic to enable expansion of the mod in the future
 */
public class UpdateHandler {
	
	public static LinkedList<Message> messages;

	/**
	 * Converts a string in JSON format into a linked list <br>
	 * @param <T> - Type of object to convert to
	 * @param jsonString
	 * @return - The converted array list
	 */
	public static LinkedList<Message> toArrayList(String jsonString){
		Gson gson = new Gson();
		Type messageListType = new TypeToken<LinkedList<Message>>() {}.getType();
		return gson.fromJson(jsonString, messageListType);
	}
	
	public static void placeEntites(LinkedList<Message> schemas, LevelAccessor level, ChunkAccess chunk) {
	
			
		placeMessages(level, chunk);
		
	}
	
	private static void placeMessages(LevelAccessor level, ChunkAccess chunk) {
		Iterator<Message> iterator = messages.iterator();
		while (iterator.hasNext()) {
			Message message = iterator.next();
			if(!inChunk(chunk, message))
				continue;
			BlockState state = BlockInit.MASSAGE_BLOCK.get().defaultBlockState()
					.setValue(MessageBlock.FACING, MessageBlock.possibleDirections[message.dir]);
			BlockPos validPos = findValidBlockPos(chunk, message);
			if(chunk.getBlockState(validPos.below()).getBlock() != BlockInit.MASSAGE_BLOCK.get())
				chunk.setBlockState(validPos, state, false);
		    iterator.remove();
		}
	}
	
	private static <T extends ModSchema> BlockPos findValidBlockPos(ChunkAccess chunk, T block) {
		BlockPos currentPos = new BlockPos(block.x, chunk.getMaxBuildHeight(), block.z);
		while(currentPos.getY() > chunk.getMinBuildHeight()) {
			if(chunk.getBlockState(currentPos).getBlock() != Blocks.AIR)
				return currentPos.above();
			currentPos = currentPos.below();
		}
		return new BlockPos(block.getX(), block.getY(), block.getZ());
	}
	
	private static <T extends ModSchema> boolean inChunk(ChunkAccess chunk, T block) {
		ChunkPos chunkPos = chunk.getPos();
		return (int)block.getX() >= chunkPos.getMinBlockX() && (int)block.getX() <= chunkPos.getMaxBlockX() 
				&& (int)block.getZ() >= chunkPos.getMinBlockZ() && (int)block.getZ() <= chunkPos.getMaxBlockZ();
	}
}
