package com.yftach.firstmod.screen;

import java.net.http.HttpResponse;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yftach.firstmod.FirstMod;
import com.yftach.firstmod.block.MessageBlock;
import com.yftach.firstmod.init.BlockInit;
import com.yftach.firstmod.networking.Communication;
import com.yftach.firstmod.screen.widgets.ClearEditBox;
import com.yftach.firstmod.screen.widgets.TextField;
import com.yftach.firstmod.updating.Message;
import com.yftach.firstmod.updating.UpdateHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class MessageBlockScreen extends AbstractContainerScreen<MessageBlockMenu> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(FirstMod.MOD_ID,
			"textures/gui/message_block_gui.png");
	private final int uneditableColor = 10460889;

	private TextField textField;
	private Player player;
	private String authorUUID, authorName;
	private static boolean open = false; 
	private boolean commit = true;
	private ClearEditBox likesCounter;

	public MessageBlockScreen(MessageBlockMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
		open = true;
		player = pPlayerInventory.player;	
	}

	@Override
	protected void init() {
		open = true;
		super.init();
		this.addRenderableWidget(new Button(this.width / 2 + 27, this.height / 2 - 40, 32, 20,
				Component.literal("LIKE"), (p_169820_) -> {
			this.btn();
		}));
		
		// TEXT FIELD
		textField = new TextField(5, this.font, this.width / 2 - 73, this.height / 3 - 25, 
				95, 10, Component.translatable("messageBlock.text"), this);
		for(ClearEditBox box: textField.getRows())
			this.addWidget(box);
		if(this.menu.blockEntity.getText() == "") 
			initMessage();
			
		textField.setText(this.menu.blockEntity.getText());
		textField.setEditable(this.menu.blockEntity.isEditable());
		textField.setUneditableTextColor(uneditableColor);
		textField.setFocus(this.menu.blockEntity.getFocusedRow());
		textField.setCursorPos(this.menu.blockEntity.getFocusedRow(), this.menu.blockEntity.getCusorPos());
		
		// WRITEN BY TEXT
		ClearEditBox writenByBox = new ClearEditBox(this.font, this.width / 2 + 25, this.height / 3 - 30
				, 70, 10, Component.translatable("messageBlock.writen"));
		writenByBox.setValue("Writen By:");
		writenByBox.setEditable(false);
		writenByBox.setTextColorUneditable(uneditableColor);
		this.addRenderableWidget(writenByBox);
		
		// AUTHOR NAME TEXT
		ClearEditBox authorBox = new ClearEditBox(this.font, this.width / 2 + 25, this.height / 3 - 17
				, 70, 10, Component.translatable("messageBlock.author"));
		if(this.menu.blockEntity.getAuthorName() == "" && this.menu.blockEntity.getFindAuthorName())
			authorName = getAuthorName(authorUUID);
		else
			authorName = this.menu.blockEntity.getAuthorName();
		this.menu.blockEntity.setFindAuthorName(true);
		authorBox.setValue(authorName);
		authorBox.setEditable(false);
		authorBox.setTextColorUneditable(uneditableColor);
		this.addRenderableWidget(authorBox);
		
		// LIKES COUNTER
		likesCounter = new ClearEditBox(this.font, this.width / 2 + 65, this.height / 2 - 35,
				50, 10, Component.translatable("messageBlock.likes"));
		likesCounter.setValue("" + this.menu.blockEntity.getLikes());
		likesCounter.setEditable(false);
		likesCounter.setTextColorUneditable(uneditableColor);
		this.addRenderableWidget(likesCounter);
		
	}

	private void btn() {
		if(this.menu.blockEntity.getLiked()) {
			player.sendSystemMessage(Component.literal(
					"You have already liked this message!").withStyle(ChatFormatting.RED));
			return;	
		}
		this.menu.blockEntity.setLikes(this.menu.blockEntity.getLikes() + 1);
		this.menu.blockEntity.setLiked(true);
		int dir = directionToInt(this.menu.blockEntity.getBlockState());
		BlockPos messagePos = this.menu.blockEntity.getBlockPos();
		likesCounter.setValue("" + this.menu.blockEntity.getLikes());
		if(shouldCommit()) { // a new message so POST is required
			Message toSend = new Message(player.getStringUUID(),
					messagePos.getX(), messagePos.getY(), messagePos.getZ(),
					textField.getText(), dir, this.menu.blockEntity.getLikes());
			Communication.postReq(FirstMod.SERVER_ADDRESS + FirstMod.MESSAGES_ROUTE, 
					new Gson().toJson(toSend));
		} else {
			Message toSend = new Message(player.getStringUUID(), this.menu.blockEntity.getId(),
					messagePos.getX(), messagePos.getY(), messagePos.getZ(),
					textField.getText(), dir, this.menu.blockEntity.getLikes());	
			Communication.putReq(FirstMod.SERVER_ADDRESS + FirstMod.MESSAGES_ROUTE,
					new Gson().toJson(toSend));
		}		
		
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		
		this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
	}

	@Override
	public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		renderBackground(pPoseStack);
		super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
		renderTooltip(pPoseStack, pMouseX, pMouseY);
		textField.render(pPoseStack, pMouseX, pMouseY, pPartialTick);	
	}

	@Override
	public boolean charTyped(char pCodePoint, int pModifiers) {

		super.charTyped(pCodePoint, pModifiers);
		textField.charTyped(pCodePoint, pModifiers);
		this.menu.blockEntity.setText(textField.getText());
			
		return false;
	}

	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		System.out.println(pKeyCode);
		boolean forceScreen = false;
		if(pKeyCode == 69) { // 'e' = 69
			commit = false;
			forceScreen = true;
		}
			
		super.keyPressed(pKeyCode, pScanCode, pModifiers);
		textField.keyPressed(pKeyCode, pScanCode, pModifiers);
		String currentText = textField.getText();
		this.menu.blockEntity.setText(currentText);
		if(forceScreen) {
			this.menu.blockEntity.setFindAuthorName(false);
			minecraft.forceSetScreen(this);
			this.menu.blockEntity.setText(currentText);
		}
		
		return false;
	}
	
	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
		textField.mouseClicked(pMouseX, pMouseY, pButton);
		return super.mouseClicked(pMouseX, pMouseY, pButton);
	}
	
	@Override
	public void onClose() {
		
		this.menu.blockEntity.setFocusedRow(textField.getFocusedRow());
		this.menu.blockEntity.setCursorPos(textField.getRows().get(textField.getFocusedRow()).getCursorPosition());
		if(!shouldCommit()) { // not a new message
			commit = true;
			super.onClose();
			return;
		}
		
		this.menu.blockEntity.setAuthorName(authorName);
		this.menu.blockEntity.setEditable(false);
		int dir = directionToInt(this.menu.blockEntity.getBlockState());
		BlockPos messagePos = this.menu.blockEntity.getBlockPos();
		Message toSend = new Message(player.getStringUUID(), 
				messagePos.getX(), messagePos.getY(), messagePos.getZ(), textField.getText(), dir, 0);
		Communication.postReq(FirstMod.SERVER_ADDRESS + FirstMod.MESSAGES_ROUTE,
				new Gson().toJson(toSend));
		System.out.println("CLOSED");
		super.onClose();
	}
	
	private String getAuthorName(String uuid) {
		HttpResponse<String> res = Communication.getReq("https://api.mojang.com/user/profile/" + uuid);
		if(res.statusCode() != 200) {
			player.sendSystemMessage(Component.literal(
					"Couldn't retrieve message author username from Mojange servers").withStyle(ChatFormatting.RED));	
			return "";
		}
		
		JsonObject resJson = JsonParser.parseString(res.body()).getAsJsonObject();
		return resJson.get("name").getAsString();
		
	}
	
	private int directionToInt(BlockState state) {
		for(int i = 0; i < MessageBlock.possibleDirections.length; i++) 	
			if(state.equals(BlockInit.MASSAGE_BLOCK.get().defaultBlockState()
					.setValue(MessageBlock.FACING, MessageBlock.possibleDirections[i])))
				return i;
		return 0;
	}
	
	private void initMessage() {
		Iterator<Message> iterator = UpdateHandler.messages.iterator();
		while(iterator.hasNext()) {
			Message message = iterator.next();
			BlockPos pos = new BlockPos(message.getX(), message.getY(), message.getZ());
			if(pos.equals(this.menu.blockEntity.getBlockPos())) {
				this.menu.blockEntity.setEditable(false);
				authorUUID = message.getUUID();
				this.menu.blockEntity.setLikes(message.getLikes());
				this.menu.blockEntity.setId(message.getId());
				this.menu.blockEntity.setText(message.getText());
			}
		}
	}
	
	public static boolean isOpen() {
		return open;
	}
	
	public static void setOpen(boolean isOpen) {
		open  = isOpen;
	}
	
	private boolean shouldCommit() {
		return commit && !textField.isEmpty() && this.menu.blockEntity.isEditable();
	}

}
