package com.yftach.firstmod.screen;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yftach.firstmod.FirstMod;
import com.yftach.firstmod.block.MessageBlock;
import com.yftach.firstmod.init.BlockInit;
import com.yftach.firstmod.networking.Communication;
import com.yftach.firstmod.screen.widgets.ClearEditBox;
import com.yftach.firstmod.screen.widgets.TextField;
import com.yftach.firstmod.updating.Message;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class MessageBlockScreen extends AbstractContainerScreen<MessageBlockMenu> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(FirstMod.MOD_ID,
			"textures/gui/message_block_gui.png");

	private TextField textField;
	private Player player;

	public MessageBlockScreen(MessageBlockMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
		player = pPlayerInventory.player;
		
	}

	@Override
	protected void init() {
		super.init();
		this.addRenderableWidget(new Button(100, 100, 20, 20, CommonComponents.GUI_DONE, (p_169820_) -> {
			this.btn();
		}));
		textField = new TextField(5, this.font, this.width / 2 - 73, this.height / 3 - 25, 
				95, 10, Component.translatable("messageBlock.text"), this);
		for(ClearEditBox box: textField.getRows())
			this.addWidget(box);
		textField.setText(this.menu.blockEntity.getText());
		textField.setEditable(this.menu.blockEntity.isEditable());
		textField.setUneditableTextColor(10460889);
		//if(this.menu.blockEntity.id != "")
		
		ClearEditBox authorBox = new ClearEditBox(this.font, this.width / 2 + 25, this.height / 3 - 30
				, 70, 10, Component.translatable("messageBlock.text"));
		authorBox.setValue("Writen By:");
		authorBox.setEditable(false);
		authorBox.setTextColorUneditable(10460889);
		this.addRenderableWidget(authorBox);
	}

	private void btn() {
		System.out.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
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
		if(pKeyCode == 'e') {
			
		}
		super.keyPressed(pKeyCode, pScanCode, pModifiers);
		textField.keyPressed(pKeyCode, pScanCode, pModifiers);
		this.menu.blockEntity.setText(textField.getText());
		
		return false;
	}
	
	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
		textField.mouseClicked(pMouseX, pMouseY, pButton);
		return super.mouseClicked(pMouseX, pMouseY, pButton);
	}
	
	@Override
	public void onClose() {
		if(textField.isEmpty() || !this.menu.blockEntity.isEditable()) {
			super.onClose();
			return;
		}
		int dir = directionToInt(this.menu.blockEntity.getBlockState());
		System.out.println(dir);
		Message toSend = new Message(player.getStringUUID(), player.getName().toString(), 
				player.getX(), player.getY(), player.getZ(), textField.getText(), dir);
		Communication.postReq(FirstMod.SERVER_ADDRESS, new Gson().toJson(toSend));
		this.menu.blockEntity.setEditable(false);
		System.out.println("CLOSED");
		super.onClose();
	}
	
	//private String getAuthorName()
	
	private int directionToInt(BlockState state) {
		for(int i = 0; i < MessageBlock.possibleDirections.length; i++) 	
			if(state.equals(BlockInit.MASSAGE_BLOCK.get().defaultBlockState()
					.setValue(MessageBlock.FACING, MessageBlock.possibleDirections[i])))
				return i;
		return 0;
	}

}
