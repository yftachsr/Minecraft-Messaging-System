package com.yftach.firstmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.*;
import net.minecraftforge.eventbus.api.IEventBus;
import com.yftach.firstmod.init.BlockInit;
import com.yftach.firstmod.init.ItemInit;

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
		
		
		MinecraftForge.EVENT_BUS.register(this);
	}

}
