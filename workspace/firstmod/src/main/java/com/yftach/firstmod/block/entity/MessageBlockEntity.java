package com.yftach.firstmod.block.entity;

import java.util.Iterator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.yftach.firstmod.messageIdentification.IMessageIDHandler;
import com.yftach.firstmod.messageIdentification.MessageIDHandler;
import com.yftach.firstmod.messageIdentification.MessageIDProvider;
import com.yftach.firstmod.screen.MessageBlockMenu;
import com.yftach.firstmod.screen.Capabilities.ModCapabilities;
import com.yftach.firstmod.updating.Message;
import com.yftach.firstmod.updating.UpdateHandler;

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
	
//	private final MessageIDHandler idHandler = new MessageIDHandler() {
//		@Override
//		protected void onContentsChanged() {
//			setChanged();
//		}
//	};
	
	private String text = "";
	private boolean editable = true;
	private static int x = 0;
	public int num;
	
	
	private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
	private LazyOptional<IMessageIDHandler> lazyIdHandler = LazyOptional.empty();
	
	//protected final ContainerData data;
	//private String text = "";
	

	public MessageBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(BlockEntities.MESSAGE_BLOCK.get(), pPos, pBlockState);
		num = x++;
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
//		System.out.println("BLOCK ENTITY " + num);
//		Iterator<Message> iterator = UpdateHandler.messages.iterator();
//		while(iterator.hasNext()) {
//			Message message = iterator.next();
//			BlockPos pos = new BlockPos(message.getX(), message.getY(), message.getZ());
//			if(pos.equals(pPos)) {
//				this.text = message.getText();
//				System.out.println(num + "\n" + this.text);
//				this.editable = false;
//				break;
//			}
//		}
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
		
		if(cap == ForgeCapabilities.ITEM_HANDLER) 
			return lazyItemHandler.cast();
//		if(cap == ModCapabilities.ID_HANDLER)
//			return lazyIdHandler.cast();
		return super.getCapability(cap, side);
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		lazyItemHandler = LazyOptional.of(() -> itemHandler);
		//lazyIdHandler = LazyOptional.of(() -> idHandler);
	}
	
	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		lazyItemHandler.invalidate();
		//lazyIdHandler.invalidate();
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt) {
		//nbt.put("messageID", idHandler.serializeNBT());
		nbt.put("inventory", itemHandler.serializeNBT());
		nbt.putString("text", text);
		nbt.putBoolean("editable", editable);
		super.saveAdditional(nbt);	
	}
	
	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		//idHandler.deserializeNBT(nbt.getCompound("messageID"));
		itemHandler.deserializeNBT(nbt.getCompound("inventory"));
		text = nbt.getString("text");
		editable = nbt.getBoolean("editable");		
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
//	public void setID(String id) {
//		this.idHandler.setID(id);
//	}
	
//	public String getID() {
//		return this.idHandler.getID();
//	}
	
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
