package com.yftach.messagemod.updating;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yftach.messagemod.block.MessageBlock;
import com.yftach.messagemod.init.BlockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

/**
 * Class responsible for updating the mod blocks in the world
 */
public class UpdateHandler {
	
	public static HashSet<Message> messages = new HashSet<Message>();

	/**
	 * Converts a string in JSON format into a linked list <br>
	 * @param <T> - Type of object to convert to
	 * @param jsonString
	 * @return - The converted array list
	 */
	public static HashSet<Message> toArrayList(String jsonString){
		Gson gson = new Gson();
		Type messageListType = new TypeToken<HashSet<Message>>() {}.getType();
		return gson.fromJson(jsonString, messageListType);
	}
	
	public static void placeEntites(LevelAccessor level, ChunkAccess chunk) {	
		placeMessages(level, chunk);	
	}
	
	private static void placeMessages(LevelAccessor level, ChunkAccess chunk) {
		Iterator<Message> iterator = messages.iterator();
		HashSet<Message> toAdd = new HashSet<Message>();
		while (iterator.hasNext()) {
			Message message = iterator.next();
			if(!inChunk(chunk, message))
				continue;
			BlockState state = BlockInit.MASSAGE_BLOCK.get().defaultBlockState()
					.setValue(MessageBlock.FACING, MessageBlock.possibleDirections[message.dir]);
			BlockPos validPos = findValidBlockPos(chunk, message);
			
			if(level.isClientSide()) {
				iterator.remove();
				message.setY(validPos.getY() - 1);
				toAdd.add(message);
			}
			
			if(!level.isClientSide() && chunk.getBlockState(validPos.below()).getBlock() != BlockInit.MASSAGE_BLOCK.get())
				chunk.setBlockState(validPos, state, false);
		}
		messages.addAll(toAdd);
	}
	
	/**
	 * Find a valid position for placement on the ground
	 * @param chunk - The current chunk
	 * @param block - The block to be placed
	 * @return A valid position for placement
	 */
	private static <T extends ModSchema> BlockPos findValidBlockPos(ChunkAccess chunk, T block) {
		BlockPos currentPos = new BlockPos(block.x, chunk.getMaxBuildHeight(), block.z);
		while(currentPos.getY() > chunk.getMinBuildHeight()) {
			if(chunk.getBlockState(currentPos).getBlock() == Blocks.GRASS) // avoid placing on top of grass
				return chunk.getBlockState(currentPos.below()).getBlock() == Blocks.GRASS 
						? currentPos.below() : currentPos;		
			if(chunk.getBlockState(currentPos).getBlock() != Blocks.AIR)
				return currentPos.above();
			currentPos = currentPos.below();
		}
		return new BlockPos(block.getX(), block.getY(), block.getZ());
	}
	
	/**
	 * Checks whether a block is located in the given chunk
	 * @param chunk - The current chunk
	 * @param block - The block to be checked
	 * @return Whether the block is in the chunk
	 */
	private static <T extends ModSchema> boolean inChunk(ChunkAccess chunk, T block) {
		ChunkPos chunkPos = chunk.getPos();
		return (int)block.getX() >= chunkPos.getMinBlockX() && (int)block.getX() <= chunkPos.getMaxBlockX() 
				&& (int)block.getZ() >= chunkPos.getMinBlockZ() && (int)block.getZ() <= chunkPos.getMaxBlockZ();
	}
	
}
