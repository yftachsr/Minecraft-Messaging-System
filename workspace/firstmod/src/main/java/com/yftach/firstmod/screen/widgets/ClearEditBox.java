package com.yftach.firstmod.screen.widgets;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class ClearEditBox extends EditBox {

	private static final int BORDER_COLOR_FOCUSED = -1;
	private static final int BORDER_COLOR = -6250336;
	private static final int BACKGROUND_COLOR = -16777216;

	public ClearEditBox(Font pFont, int pX, int pY, int pWidth, int pHeight, EditBox p_94111_, Component pMessage) {
		super(pFont, pX, pY, pWidth, pHeight, p_94111_, pMessage);
	}
	
	@Override
	public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		fill(pPoseStack, this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, 139);
        fill(pPoseStack, this.x, this.y, this.x + this.width, this.y + this.height, 139);
		super.renderButton(pPoseStack, pMouseX, pMouseY, pPartialTick);	
		fill(pPoseStack, this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, 139);
        fill(pPoseStack, this.x, this.y, this.x + this.width, this.y + this.height, 139);
        
	}
}
