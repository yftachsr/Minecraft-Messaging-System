package com.yftach.firstmod.screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.yftach.firstmod.FirstMod;
import com.yftach.firstmod.screen.widgets.ClearEditBox;
import com.yftach.firstmod.screen.widgets.TextField;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent.MouseButtonPressed;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Tick;
import net.minecraft.client.gui.components.MultilineTextField;

public class MessageBlockScreen extends AbstractContainerScreen<MessageBlockMenu> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(FirstMod.MOD_ID,
			"textures/gui/message_block_gui.png");

	private final int TEXT_BOXES_NUM = 5;
	private ArrayList<ClearEditBox> textBoxes;
	protected MultilineTextField field;
	private TextField textField;
	private ClearEditBox box;


	public MessageBlockScreen(MessageBlockMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
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
		textField.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
	}

	@Override
	public boolean charTyped(char pCodePoint, int pModifiers) {

		super.charTyped(pCodePoint, pModifiers);
		textField.charTyped(pCodePoint, pModifiers);
		
		/*
			 * else if (SharedConstants.isAllowedChatCharacter(pCodePoint)) {
			 * this.pageEdit.insertText(Character.toString(pCodePoint)); return true; } else
			 * { return false; }
			 */
		if(pCodePoint == 'e') {
			System.out.println("\nrwgvwrvwrv");
			return false;
		}
			
		/*
		 * if(pCodePoint == 'l') { this.textBox1.insertText("\nq");
		 * System.out.println("\nlllllllllllllllllllllll"); }
		 */
		
		/*
		 * if(pCodePoint == 'v') { textBox1.setFocus(false); textBox3.setFocus(false);
		 * textBox2.setFocus(true);
		 * 
		 * this.setFocused(textBox2); }
		 */
			
		return true;
	}

	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		super.keyPressed(pKeyCode, pScanCode, pModifiers);
		textField.keyPressed(pKeyCode, pScanCode, pModifiers);
		/*
		 * if(pKeyCode == 'e') return true; if(pKeyCode == 'l' || pKeyCode == 13) {
		 * this.setFocused(textBox2); return true; } return true;
		 */
		return false;
	}
	
	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
		textField.mouseClicked(pMouseX, pMouseY, pButton);
		return super.mouseClicked(pMouseX, pMouseY, pButton);
	}
	

}
