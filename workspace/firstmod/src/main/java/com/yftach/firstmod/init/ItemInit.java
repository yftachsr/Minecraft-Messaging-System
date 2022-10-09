package com.yftach.firstmod.init;

import com.google.common.base.Supplier;
import com.yftach.firstmod.FirstMod;
import com.yftach.firstmod.item.MessagingCrystal;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FirstMod.MOD_ID);
	public static final RegistryObject<Item> PinkApple = register("pink_apple",() -> 
		new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD)));
	public static final RegistryObject<Item> MessagingCrystal = register("messaging_crystal",() -> 
	new MessagingCrystal(new Item.Properties().tab(FirstMod.NEW_TAB).stacksTo(1)));
	
	
	private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item){
		return ITEMS.register(name, item);
	}
}
