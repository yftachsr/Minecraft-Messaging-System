package com.yftach.messagemod.screen;

import com.yftach.messagemod.block.entity.MessageBlockEntity;
import com.yftach.messagemod.init.BlockInit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MessageBlockMenu extends AbstractContainerMenu {
	
	public final MessageBlockEntity blockEntity;
	private final Level level;

	public MessageBlockMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
		this(id, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()));	
	}

	public MessageBlockMenu(int id, Inventory inv, BlockEntity entity) {
		super(MenuTypes.MESSAGE_BLOCK_MENU.get(), id);
		checkContainerSize(inv, 1);
		blockEntity = (MessageBlockEntity) entity;
		this.level = inv.player.level;
		addPlayerInventory(inv);
		addPlayerHotbar(inv);	    
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
				pPlayer, BlockInit.MASSAGE_BLOCK.get());
	}
	
	private void addPlayerInventory(Inventory playerInventory) {
		final int rows = 3, colums = 9;
        for (int i = 0; i < rows; ++i) {
            for (int l = 0; l < colums; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * colums + colums, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
    	final int colums = 9;
        for (int i = 0; i < colums; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }

	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		return null;
	}
	

}
