package com.yftach.messagemod.event;

import java.net.http.HttpResponse;

import com.yftach.messagemod.MessagingSystemMod;
import com.yftach.messagemod.networking.Communication;
import com.yftach.messagemod.screen.MessageBlockScreen;
import com.yftach.messagemod.updating.UpdateHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MessagingSystemMod.MOD_ID)
public class ModEvents {
	
	private final static int FREQUENCY = 1500;
	private static int worldage = 0;
	
	@SubscribeEvent
	public static void tick(TickEvent.LevelTickEvent event) {
		worldage++;
		if(worldage % FREQUENCY == 0) {
			HttpResponse<String> response = Communication.getReq(MessagingSystemMod.SERVER_ADDRESS + MessagingSystemMod.MESSAGES_ROUTE);
			if(response != null && response.statusCode() == 200) { 
				UpdateHandler.messages.addAll(UpdateHandler.toArrayList(response.body()));
			} else {
				for(Player player: event.level.players()) 
					player.sendSystemMessage(
							Component.literal("Couldn't retrive messages from the database").withStyle(ChatFormatting.RED));		
			}
			System.out.println(UpdateHandler.messages);
		}
	}
	
	@SubscribeEvent
	public static void initMessages(LevelEvent.Load event) {
		HttpResponse<String> response = Communication.getReq(MessagingSystemMod.SERVER_ADDRESS + MessagingSystemMod.MESSAGES_ROUTE);
		if(response != null && response.statusCode() == 200)
			UpdateHandler.messages = UpdateHandler.toArrayList(response.body());
	}
	
	@SubscribeEvent
	public static void loadChunkMessages(ChunkEvent.Load event) {
		if(UpdateHandler.messages != null && UpdateHandler.messages.size() > 0)
			UpdateHandler.placeEntites(event.getLevel(), event.getChunk());
	}
	
	@SubscribeEvent
	public static void messageBreak(BlockEvent.BreakEvent event) {
//		if(event.getState().getBlock() instanceof MessageBlock)
//			event.setCanceled(true);
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
