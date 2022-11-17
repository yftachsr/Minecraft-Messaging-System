package com.yftach.messagemod.block.entity;

import com.yftach.messagemod.MessagingSystemMod;
import com.yftach.messagemod.init.BlockInit;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MessagingSystemMod.MOD_ID);
	
	public static final RegistryObject<BlockEntityType<MessageBlockEntity>> MESSAGE_BLOCK = 
			BLOCK_ENTITIES.register("message_block", () -> 
			BlockEntityType.Builder.of(MessageBlockEntity::new, BlockInit.MASSAGE_BLOCK.get()).build(null));
	
	public static void register(IEventBus eventBus) {
		BLOCK_ENTITIES.register(eventBus);
	}

}
