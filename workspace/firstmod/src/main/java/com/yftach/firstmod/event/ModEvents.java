package com.yftach.firstmod.event;


import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedList;

import com.yftach.firstmod.FirstMod;
import com.yftach.firstmod.block.MessageBlock;
import com.yftach.firstmod.init.BlockInit;
import com.yftach.firstmod.networking.Communication;
import com.yftach.firstmod.updating.Message;
import com.yftach.firstmod.updating.ModSchema;
import com.yftach.firstmod.updating.UpdateHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FirstMod.MOD_ID)
public class ModEvents {
	
	private static int worldage = 0;
	
	
//	@SubscribeEvent
//	public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event){
//		if(event.getEntity() instanceof MessageBlockEntity) {
//			
//		}
//	}
	
	@SubscribeEvent
	public static void tick(TickEvent.LevelTickEvent event) {
		worldage++;
		if(worldage % 2500 == 0) {
			HttpResponse<String> response = Communication.getReq(FirstMod.SERVER_ADDRESS);
			if(response != null) // TO DO add code response check
				UpdateHandler.messages = UpdateHandler.toArrayList(response.body());
			
		}
		
	}
	
	@SubscribeEvent
	public static void initMessages(LevelEvent.Load event) {
		HttpResponse<String> response = Communication.getReq(FirstMod.SERVER_ADDRESS);
		if(response != null) // TO DO add code response check
			UpdateHandler.messages = UpdateHandler.toArrayList(response.body());
	}
	
	@SubscribeEvent
	public static void loadChunkMessages(ChunkEvent.Load event) {
		if(UpdateHandler.messages != null && UpdateHandler.messages.size() > 0)
			UpdateHandler.placeEntites(UpdateHandler.messages, event.getLevel(), event.getChunk());
	}
	
	@SubscribeEvent
	public static void messageBreak(BlockEvent.BreakEvent event) {
		//event.setCanceled(true);
	}
	
	
	
}
