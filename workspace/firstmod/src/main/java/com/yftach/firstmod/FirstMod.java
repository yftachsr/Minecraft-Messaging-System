package com.yftach.firstmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.yftach.firstmod.block.entity.BlockEntities;
import com.yftach.firstmod.init.BlockInit;
import com.yftach.firstmod.init.ItemInit;
import com.yftach.firstmod.minecraftNetworking.Network;
import com.yftach.firstmod.screen.MenuTypes;
import com.yftach.firstmod.screen.MessageBlockScreen;

@Mod("firstmod")
public class FirstMod {
	
	public static final String MOD_ID = "firstmod";
	
	public static final CreativeModeTab NEW_TAB = new CreativeModeTab(MOD_ID) {
		
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack makeIcon() {
			return new ItemStack(ItemInit.MessagingCrystal.get());
		}
	};
	
	public FirstMod() {
		
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		ItemInit.ITEMS.register(bus);
		BlockInit.BLOCKS.register(bus);
		
		BlockEntities.BLOCK_ENTITIES.register(bus);
		
		MenuTypes.register(bus);
		
		bus.addListener(this::commonSetup);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void commonSetup(final FMLCommonSetupEvent event) {
		Network.register();
	}
	
	@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			MenuScreens.register(MenuTypes.MESSAGE_BLOCK_MENU.get(), MessageBlockScreen::new);
		}
	}

}
