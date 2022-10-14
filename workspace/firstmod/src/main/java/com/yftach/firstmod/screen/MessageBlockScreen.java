package com.yftach.firstmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yftach.firstmod.FirstMod;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Tick;

public class MessageBlockScreen extends AbstractContainerScreen<MessageBlockMenu> {
	
	private static final ResourceLocation TEXTURE = 
			new ResourceLocation(FirstMod.MOD_ID, "textures/gui/message_block_gui.png");
	
	protected EditBox searchBox;

	public MessageBlockScreen(MessageBlockMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}
	
	@Override
	protected void init() {
		super.init();
		this.addRenderableWidget(new Button(100, 100, 20, 20, CommonComponents.GUI_DONE, (p_169820_) -> {
	         this.btn();
	      }));
		this.searchBox = new EditBox(this.font, this.width / 2 - 100, 22, 200, 20, this.searchBox, Component.translatable("selectWorld.search"));
		this.addWidget(this.searchBox);
		this.setInitialFocus(this.searchBox);
	}
	
	private void btn() {
		System.out.println("\n\n GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG \n");
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
		this.searchBox.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
	}
	
	

}
