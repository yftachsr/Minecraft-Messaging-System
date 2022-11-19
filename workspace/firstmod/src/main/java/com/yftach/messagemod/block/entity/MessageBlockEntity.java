package com.yftach.messagemod.block.entity;

import com.yftach.messagemod.screen.MessageBlockMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MessageBlockEntity extends BlockEntity implements MenuProvider{
	
	private String text = "", authorUUID = "", authorName = "", id = "";
	private boolean editable = true, findAuthorName = true, liked = false;
	private int focusedRow = 0, cursorPos = 0, likes = 0;

	public MessageBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(BlockEntities.MESSAGE_BLOCK.get(), pPos, pBlockState);
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
	protected void saveAdditional(CompoundTag nbt) {
		nbt.putString("text", text);
		nbt.putBoolean("editable", editable);
		nbt.putInt("cursor", cursorPos);
		nbt.putInt("row", focusedRow);
		nbt.putString("author", authorName);
		nbt.putString("authorUUID", authorUUID);
		nbt.putBoolean("get_name", findAuthorName);
		nbt.putString("id", id);
		nbt.putInt("likes", likes);
		nbt.putBoolean("liked", liked);
		super.saveAdditional(nbt);	
	}
	
	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		text = nbt.getString("text");
		editable = nbt.getBoolean("editable");
		cursorPos = nbt.getInt("cursor");
		focusedRow = nbt.getInt("row");
		authorName = nbt.getString("author");
		authorUUID = nbt.getString("authorUUID");
		findAuthorName = nbt.getBoolean("get_name");
		id = nbt.getString("id");
		likes = nbt.getInt("likes");
		liked = nbt.getBoolean("liked");
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public int getFocusedRow() {
		return focusedRow;
	}
	
	public void setFocusedRow(int row) {
		this.focusedRow = row;
	}
	
	public int getCusorPos() {
		return cursorPos;
	}
	
	public void setCursorPos(int pos) {
		this.cursorPos = pos;
	}
	
	public String getAuthorName() {
		return authorName;
	}
	
	public void setAuthorName(String name) {
		this.authorName = name;
	}
	
	public String getAuthorUUID() {
		return authorUUID;
	}
	
	public void setAuthorUUID(String uuid) {
		this.authorUUID = uuid;
	}
	
	public boolean getFindAuthorName() {
		return findAuthorName;
	}
	
	public void setFindAuthorName(boolean find) {
		this.findAuthorName = find;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public int getLikes() {
		return likes;
	}
	
	public void setLikes(int likes) {
		this.likes = likes;
	}
	
	public boolean getLiked() {
		return liked;
	}
	
	public void setLiked(boolean liked) {
		this.liked = liked;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
}
