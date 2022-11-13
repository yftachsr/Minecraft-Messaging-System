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
import com.yftach.firstmod.messageIdentification.MessageIDProvider;
import com.yftach.firstmod.minecraftNetworking.Network;
import com.yftach.firstmod.minecraftNetworking.packet.MessageC2SPacket;
import com.yftach.firstmod.networking.Communication;
import com.yftach.firstmod.screen.widgets.ClearEditBox;
import com.yftach.firstmod.screen.widgets.TextField;
import com.yftach.firstmod.updating.Message;
import com.yftach.firstmod.updating.UpdateHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
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
	private String authorUUID;

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
		// TEXT FIELD
		textField = new TextField(5, this.font, this.width / 2 - 73, this.height / 3 - 25, 
				95, 10, Component.translatable("messageBlock.text"), this);
		for(ClearEditBox box: textField.getRows())
			this.addWidget(box);
		if(this.menu.blockEntity.getText() == "")
			initMessage();
		textField.setText(this.menu.blockEntity.getText());
		textField.setEditable(this.menu.blockEntity.isEditable());
		textField.setUneditableTextColor(10460889);

		// WRITEN BY TEXT
		ClearEditBox writenByBox = new ClearEditBox(this.font, this.width / 2 + 25, this.height / 3 - 30
				, 70, 10, Component.translatable("messageBlock.writen"));
		writenByBox.setValue("Writen By:");
		writenByBox.setEditable(false);
		writenByBox.setTextColorUneditable(10460889);
		this.addRenderableWidget(writenByBox);
		
		// AUTHOR NAME TEXT
		ClearEditBox authorBox = new ClearEditBox(this.font, this.width / 2 + 25, this.height / 3 - 17
				, 70, 10, Component.translatable("messageBlock.author"));
		authorBox.setValue(getAuthorName(authorUUID));
		authorBox.setEditable(false);
		authorBox.setTextColorUneditable(10460889);
		this.addRenderableWidget(authorBox);
		
	}

	private void btn() {
		this.menu.blockEntity.getCapability(MessageIDProvider.MESSAGE_ID).ifPresent(id -> {
			System.out.println(id.getId());
		});
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
		BlockPos messagePos = this.menu.blockEntity.getBlockPos();
		Message toSend = new Message(player.getStringUUID(), player.getName().toString(), 
				messagePos.getX(), messagePos.getY(), messagePos.getZ(), textField.getText(), dir);
		HttpResponse<String> res = Communication.postReq(FirstMod.SERVER_ADDRESS + FirstMod.MESSAGES_ROUTE,
				new Gson().toJson(toSend));
		JsonObject resJson = JsonParser.parseString(res.body()).getAsJsonObject();
		String id = resJson.get("_id").getAsString();
		//System.out.println("MESSAGE ID: " + id);
		
		Network.sendToServer(new MessageC2SPacket(id, messagePos));
		
		this.menu.blockEntity.setEditable(false);
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
			//System.out.println("Message: " + pos);
			//System.out.println("Entity: " + this.menu.blockEntity.getBlockPos() + "\n");
			if(pos.equals(this.menu.blockEntity.getBlockPos())) {
				this.menu.blockEntity.setEditable(false);
				authorUUID = message.getUUID();
				this.menu.blockEntity.setText(message.getText());
			}
		}
	}

}
