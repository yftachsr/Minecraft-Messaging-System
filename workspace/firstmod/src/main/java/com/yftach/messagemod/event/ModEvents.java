package com.yftach.messagemod.event;

import java.net.http.HttpResponse;
import java.util.Iterator;

import com.yftach.messagemod.MessagingSystemMod;
import com.yftach.messagemod.minecraftNetworking.ModMessages;
import com.yftach.messagemod.minecraftNetworking.packets.UpdateMessageBlockC2SPacket;
import com.yftach.messagemod.networking.Communication;
import com.yftach.messagemod.screen.MessageBlockScreen;
import com.yftach.messagemod.updating.Message;
import com.yftach.messagemod.updating.UpdateHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MessagingSystemMod.MOD_ID)
public class ModEvents {
	
	private static final String FAILURE_MESSAGE = "message.messagemod.db_messages_failure";
	
	private final static int FREQUENCY = 1200; // Currently pretty frequent, subject to change
	private static int updateCounter = 0;
	public static boolean earlyUpdate = false;
	
	
	@SubscribeEvent
	public static void tick(TickEvent.LevelTickEvent event) {
		updateCounter++;
		if(updateCounter % FREQUENCY == 0 || earlyUpdate) {
			HttpResponse<String> response = Communication.getReq(MessagingSystemMod.SERVER_ADDRESS + MessagingSystemMod.MESSAGES_ROUTE);
			if(response != null && response.statusCode() == 200) { 
				UpdateHandler.messages.addAll(UpdateHandler.parseJSON(response.body()));
			} else {
				for(Player player: event.level.players()) 
					player.sendSystemMessage(
							Component.translatable(FAILURE_MESSAGE).withStyle(ChatFormatting.RED));		
			}
			earlyUpdate = false;
			updateCounter = 1; // To avoid reaching gigantic numbers
			//System.out.println(UpdateHandler.messages);
		}
		
	}
	
	@SubscribeEvent
	public static void initModBlocks(LevelEvent.Load event) {
		if(event.getLevel().isClientSide())
			return;
		HttpResponse<String> response = Communication.getReq(MessagingSystemMod.SERVER_ADDRESS + MessagingSystemMod.MESSAGES_ROUTE);
		if(response != null && response.statusCode() == 200)
			UpdateHandler.messages = UpdateHandler.parseJSON(response.body());	
	}
	
	@SubscribeEvent
	public static void loadChunkModBlocks(ChunkEvent.Load event) {
		if(UpdateHandler.messages.size() > 0)
			UpdateHandler.placeEntites(event.getLevel(), event.getChunk());
	}
	
	@SubscribeEvent
	public static void unloadChunkModBlocks(ChunkEvent.Unload event) {
		Iterator<Message> iter = UpdateHandler.messages.iterator();
		while(iter.hasNext()) {
			Message message = iter.next();
			if(UpdateHandler.inChunk(event.getChunk(), message.getBlockPos())) { // The message is in the chunk
				if(event.getLevel().isClientSide())
					ModMessages.sendToServer(new UpdateMessageBlockC2SPacket(message.getBlockPos(), false));
				else
					event.getChunk().setBlockState(message.getBlockPos(), Blocks.AIR.defaultBlockState(), false);
				
				if(message.toBeDeleted())
					iter.remove();
			}	
		}
		
	}
	
	@SubscribeEvent
	public static void onScreenOpening(ScreenEvent.Opening event) {
		if(event.getNewScreen() instanceof MessageBlockScreen && MessageBlockScreen.toCancel()) {
			event.setCanceled(true);
			MessageBlockScreen.setToCancel(false);
		}
		
		if(MessageBlockScreen.isOpen() && (event.getNewScreen() instanceof InventoryScreen
				|| event.getNewScreen() instanceof CreativeModeInventoryScreen
				|| event.getNewScreen() instanceof PauseScreen)) {
			event.setCanceled(true);
			MessageBlockScreen.setOpen(false);
		}
			
	}
	
}
