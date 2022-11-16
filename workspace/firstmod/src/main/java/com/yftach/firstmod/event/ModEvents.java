package com.yftach.firstmod.event;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.text.html.parser.Entity;

import com.yftach.firstmod.FirstMod;
import com.yftach.firstmod.block.MessageBlock;
import com.yftach.firstmod.block.entity.MessageBlockEntity;
import com.yftach.firstmod.init.BlockInit;
import com.yftach.firstmod.messageIdentification.MessageID;
import com.yftach.firstmod.messageIdentification.MessageIDProvider;
import com.yftach.firstmod.networking.Communication;
import com.yftach.firstmod.screen.MessageBlockScreen;
import com.yftach.firstmod.updating.Message;
import com.yftach.firstmod.updating.ModSchema;
import com.yftach.firstmod.updating.UpdateHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FirstMod.MOD_ID)
public class ModEvents {
	
	private static int worldage = 0;
	private static boolean messageBlockScreen = false;
	private static char lastChar;
	
	@SubscribeEvent
	public static void tick(TickEvent.LevelTickEvent event) {
		worldage++;
		if(worldage % 1500 == 0) {
			HttpResponse<String> response = Communication.getReq(FirstMod.SERVER_ADDRESS + FirstMod.MESSAGES_ROUTE);
			if(response != null && response.statusCode() == 200) { 
				LinkedList<Message> list = UpdateHandler.toArrayList(response.body());
				UpdateHandler.messages.addAll(list.subList(UpdateHandler.messages.size(), list.size()));
			} else {
				for(Player player: event.level.players()) 
					player.sendSystemMessage(
							Component.literal("Couldn't retrive messages from the database").withStyle(ChatFormatting.RED));		
			}
			//System.out.println(UpdateHandler.messages);
		}
	}
	
	@SubscribeEvent
	public static void initMessages(LevelEvent.Load event) {
		HttpResponse<String> response = Communication.getReq(FirstMod.SERVER_ADDRESS + FirstMod.MESSAGES_ROUTE);
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
		//event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onAttachCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
		if(event.getObject() instanceof MessageBlockEntity) 
			if(!event.getObject().getCapability(MessageIDProvider.MESSAGE_ID).isPresent()) 
				event.addCapability(new ResourceLocation(FirstMod.MOD_ID, "properties"), new MessageIDProvider());		
	}
	
	@SubscribeEvent
	public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
		event.register(MessageID.class);
	}
	
	@SubscribeEvent
	public static void onScreenOpening(ScreenEvent.Opening event) {
		if(MessageBlockScreen.isOpen() && (event.getNewScreen() instanceof InventoryScreen
				|| event.getNewScreen() instanceof CreativeModeInventoryScreen
				|| event.getNewScreen() instanceof PauseScreen)) {
			event.setCanceled(true);
			MessageBlockScreen.setOpen(false);
		}
			
	}
	
}
