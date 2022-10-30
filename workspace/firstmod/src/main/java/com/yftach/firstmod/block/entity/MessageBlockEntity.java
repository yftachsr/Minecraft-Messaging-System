package com.yftach.firstmod.block.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.yftach.firstmod.screen.MessageBlockMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class MessageBlockEntity extends BlockEntity implements MenuProvider{
	
	private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
	};
	
	private String text = "";
	private String id = "";
	private boolean editable = true;
	
	
	private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
	private LazyOptional<String> lazyText = LazyOptional.empty();
	
	//protected final ContainerData data;
	//private String text = "";
	

	public MessageBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(BlockEntities.MESSAGE_BLOCK.get(), pPos, pBlockState);
		/*
		 * this.data = new ContainerData() {
		 * 
		 * @Override public void set(int pIndex, int pValue) { // TODO Auto-generated
		 * method stub
		 * 
		 * }
		 * 
		 * @Override public int getCount() { // TODO Auto-generated method stub return
		 * 0; }
		 * 
		 * @Override public int get(int pIndex) { return text; } };
		 */
	}

	@Override
	public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
		return new MessageBlockMenu(pContainerId, pPlayerInventory, this);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("Message Block");
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		
		if(cap == ForgeCapabilities.ITEM_HANDLER) {
			return lazyItemHandler.cast();
		}
		
		return super.getCapability(cap, side);
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		lazyItemHandler = LazyOptional.of(() -> itemHandler);
		lazyText = LazyOptional.of(() -> text);
	}
	
	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		lazyItemHandler.invalidate();
	}
	
	@Override
	protected void saveAdditional(CompoundTag pTag) {
		pTag.put("inventory", itemHandler.serializeNBT());
		pTag.putString("text", text);
		pTag.putBoolean("editable", editable);
		super.saveAdditional(pTag);
		
	}
	
	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		itemHandler.deserializeNBT(pTag.getCompound("inventory"));
		text = pTag.getString("text");
		editable = pTag.getBoolean("editable");
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	
	/*
	 * public static void tick(Level level, BlockPos pos, BlockState state,
	 * MessageBlockEntity pEntity) { if(level.isClientSide()) return;
	 * 
	 * }
	 */

}
