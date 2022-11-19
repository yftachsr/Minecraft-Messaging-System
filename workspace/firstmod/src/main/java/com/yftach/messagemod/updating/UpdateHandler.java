package com.yftach.messagemod.updating;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yftach.messagemod.init.BlockInit;
import com.yftach.messagemod.minecraftNetworking.ModMessages;
import com.yftach.messagemod.minecraftNetworking.packets.UpdateModBlockC2SPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
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
	public static HashSet<Message> parseJSON(String jsonString){
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
			if(!inChunk(chunk, message.getBlockPos()))
				continue;
			
			BlockPos validPos = findValidBlockPos(chunk, message);
			boolean onMessage = chunk.getBlockState(validPos.below()).getBlock() == BlockInit.MASSAGE_BLOCK.get();
			if(level.isClientSide()) {
				iterator.remove();
				message.setY(validPos.getY() + (onMessage ? -1 : 0));
				toAdd.add(message);
				if(!onMessage)
					ModMessages.sendToServer(new UpdateModBlockC2SPacket(message.getBlockPos(), true, message.dir));
			}
			
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
		BlockPos currentPos = new BlockPos(block.getX(), chunk.getMaxBuildHeight(), block.getZ());
		while(currentPos.getY() > chunk.getMinBuildHeight()) {
			if(chunk.getBlockState(currentPos).getBlock() == Blocks.GRASS) // avoid placing on top of grass
				return chunk.getBlockState(currentPos.below()).getBlock() == Blocks.GRASS 
						? currentPos.below() : currentPos;		
			if(chunk.getBlockState(currentPos).getBlock() != Blocks.AIR)
				return currentPos.above();
			currentPos = currentPos.below();
		}
		return block.getBlockPos();
	}
	
	/**
	 * Checks whether a block is located in the given chunk
	 * @param chunk - The current chunk
	 * @param block - The block to be checked
	 * @return Whether the block is in the chunk
	 */
	public static <T extends ModSchema> boolean inChunk(ChunkAccess chunk, BlockPos pos) {
		ChunkPos chunkPos = chunk.getPos();
		return (int)pos.getX() >= chunkPos.getMinBlockX() && (int)pos.getX() <= chunkPos.getMaxBlockX() 
				&& (int)pos.getZ() >= chunkPos.getMinBlockZ() && (int)pos.getZ() <= chunkPos.getMaxBlockZ();
	}
	
	/**
	 * Mark a message with the corresponding id to be deleted
	 * @param id - id of the message to mark to be deleted
	 */
	public static void setToBeDeleted(String id) {
		Iterator<Message> iter = messages.iterator();
		while(iter.hasNext()) {
			Message message = iter.next();
			if(message.getId().equals(id)) {
				iter.remove();
				message.setToBeDeleted(true);
				messages.add(message);
				break;
			}
		}
	}
	
}
