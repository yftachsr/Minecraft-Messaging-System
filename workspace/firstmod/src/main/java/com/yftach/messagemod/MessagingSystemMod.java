package com.yftach.messagemod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.yftach.messagemod.block.entity.BlockEntities;
import com.yftach.messagemod.init.BlockInit;
import com.yftach.messagemod.init.ItemInit;
import com.yftach.messagemod.screen.MenuTypes;
import com.yftach.messagemod.screen.MessageBlockScreen;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod("messagemod")
public class MessagingSystemMod {
	
	public static final String MOD_ID = "messagemod";
	public static final String SERVER_ADDRESS = "http://localhost:3000";
	public static final String MESSAGES_ROUTE = "/messages";
	
	public static final CreativeModeTab NEW_TAB = new CreativeModeTab(MOD_ID) {
		
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack makeIcon() {
			return new ItemStack(ItemInit.MessagingCrystal.get());
		}
	};
	
	public MessagingSystemMod() {
		
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		ItemInit.ITEMS.register(bus);
		BlockInit.BLOCKS.register(bus);
		
		BlockEntities.BLOCK_ENTITIES.register(bus);
		
		MenuTypes.register(bus);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			MenuScreens.register(MenuTypes.MESSAGE_BLOCK_MENU.get(), MessageBlockScreen::new);
		}
	}

}
