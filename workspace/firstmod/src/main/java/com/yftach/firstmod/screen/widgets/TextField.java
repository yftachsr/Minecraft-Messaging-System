package com.yftach.firstmod.screen.widgets;

import java.util.ArrayList;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yftach.firstmod.screen.MessageBlockScreen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TextField extends AbstractWidget implements Widget, GuiEventListener {

	private int numOfRows, pX, pY, width, height, focusedRow;
	private Component message;
	private ArrayList<ClearEditBox> textBoxes;
	private Screen screen;
	private final int MAX_ROW_LENGTH = 14;
	private final int KEY_BACKSPACE = 259;
	private final int KEY_ENTER = 257;
	private final int KEY_DOWN = 264;
	private final int KEY_UP = 265;
	private boolean transfer = false, lineFull = false, fillLine = false;

	public TextField(int rows, Font font, int pX, int pY, int width, int rowHeight, Component message, Screen screen) {
		super(pX, pY, width, rowHeight * rows, message);
		textBoxes = new ArrayList<ClearEditBox>();
		for (int i = 0; i < rows; i++) {
			textBoxes.add(new ClearEditBox(font, pX, pY + i * rowHeight, width, rowHeight, null, message));
			textBoxes.get(i).setMaxLength(MAX_ROW_LENGTH + 1);
		}
			
		this.screen = screen;
		screen.setInitialFocus(textBoxes.get(0));
		focusedRow = 0;
	}

	@Override
	public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		for (ClearEditBox box : textBoxes)
			box.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
	}

	public ArrayList<ClearEditBox> getRows() {
		return textBoxes;
	}

	public String getText() {
		String result = "";
		for (ClearEditBox box : textBoxes)
			result += box.getValue() + "\n";
		return result;
	}

	@Override
	public void updateNarration(NarrationElementOutput pNarrationElementOutput) {
		pNarrationElementOutput.add(NarratedElementType.TITLE,
				Component.translatable("narration.clear_edit_box", this.getText()));
	}

	@Override
	public boolean charTyped(char pCodePoint, int pModifiers) {
		
		int cursorPos;
		if (focusedRow != textBoxes.size() - 1 && lineFull) {
			cursorPos = textBoxes.get(focusedRow).getCursorPosition();
			handleFullLine(focusedRow);
			if(cursorPos == MAX_ROW_LENGTH + 1) {
				setFocus(focusedRow + 1);
				textBoxes.get(focusedRow - 1).moveCursorToStart();
				textBoxes.get(focusedRow).setCursorPosition(1);
				textBoxes.get(focusedRow).setHighlightPos(1);
			}
			else {
				textBoxes.get(focusedRow).moveCursorToStart();
				textBoxes.get(focusedRow).setCursorPosition(cursorPos);
				textBoxes.get(focusedRow).setHighlightPos(cursorPos);
			}
			
			
		}
	
		return true;
	}
	
	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		
		//System.out.println(pKeyCode);
		int cursorPos;
		
		switch(pKeyCode) {
		case KEY_BACKSPACE:
			int currentFocusedRow = focusedRow;
			if(focusedRow != 0 && transfer) {
				setFocus(focusedRow - 1);
//				cursorPos = textBoxes.get(focusedRow).getValue().length() - 2;
//				textBoxes.get(focusedRow).setCursorPosition(cursorPos);
//				textBoxes.get(focusedRow).setHighlightPos(cursorPos);
				transferText(focusedRow + 1, focusedRow, true, true);
			}
			if(focusedRow != textBoxes.size() - 1 && fillLine
					&& textBoxes.get(currentFocusedRow).getValue().length() >= MAX_ROW_LENGTH - 1) {
				System.out.println("sdfsdbdfb");
				fillLine(currentFocusedRow, currentFocusedRow <= textBoxes.size() - 2 && 
						textBoxes.get(currentFocusedRow + 1).getValue().length() >= MAX_ROW_LENGTH 
						&& textBoxes.get(currentFocusedRow + 2).getValue().length() != 0);
			}
			break;
		case KEY_ENTER:
			
		case KEY_DOWN:
		case KEY_UP:
			int nextRow = pKeyCode == KEY_DOWN ? 1 : -1;
			cursorPos = textBoxes.get(focusedRow).getCursorPosition();
			setFocus(focusedRow + nextRow);
			textBoxes.get(focusedRow).setCursorPosition(cursorPos);
			textBoxes.get(focusedRow).setHighlightPos(cursorPos);
			break;
		}
		
		transfer = textBoxes.get(focusedRow).getCursorPosition() == 0 && focusedRow != 0;
		lineFull = textBoxes.get(focusedRow).getValue().length() >= MAX_ROW_LENGTH;
		fillLine = focusedRow != 0 || textBoxes.get(focusedRow).getCursorPosition() != 0;
		
		return true;
	}
	
	private void fillLine(int row, boolean lastIter) {
		
		if((row >= textBoxes.size() - 1 || textBoxes.get(row + 1).getValue().length() == 0) && lastIter)
			return;
		int cursorPos = textBoxes.get(row).getCursorPosition();
		String nextRowText = textBoxes.get(row + 1).getValue();
		textBoxes.get(row).setValue(textBoxes.get(row).getValue() + nextRowText.charAt(0));
		textBoxes.get(row + 1).setValue(nextRowText.substring(1, nextRowText.length()));
		textBoxes.get(row).setCursorPosition(cursorPos);
		textBoxes.get(row).setHighlightPos(cursorPos);

		fillLine(row + 1, row <= textBoxes.size() - 2 && 
			textBoxes.get(row + 1).getValue().length() >= MAX_ROW_LENGTH && textBoxes.get(row + 2).getValue().length() != 0);
	}
	
	private void handleFullLine(int row) {
		
		if(row >= textBoxes.size() - 1 || row == textBoxes.size() - 2 
				&& textBoxes.get(row + 1).getValue().length() >= MAX_ROW_LENGTH)
			return;
		
		if(textBoxes.get(row + 1).getValue().length() >= MAX_ROW_LENGTH) 
			handleFullLine(row + 1);
		
			
		//System.out.println(textBoxes.get(row).getValue());
		
		char transferChar;
		try {
			transferChar = textBoxes.get(row).getValue().charAt(MAX_ROW_LENGTH);
		} catch(StringIndexOutOfBoundsException e) {
			transferChar = textBoxes.get(row).getValue().charAt(MAX_ROW_LENGTH - 1);
		}
	
		textBoxes.get(row).setValue(textBoxes.get(row).getValue().substring(0, MAX_ROW_LENGTH));
		textBoxes.get(row + 1).setValue(transferChar + textBoxes.get(row + 1).getValue());
	}
	
	
	
	/**
	 * Cut the text after the cursor from one row to another.
	 * If there is not enough space in the row to copy to, the text will be partly cut.
	 * 
	 * @param from - row to cut from
	 * @param to - row to cut to
	 * @param cutFromBack - if there is not enough space in the row to copy to, 
	 * 						cut the text from the back
	 * @param copyToFront - whether to copy the text to the front of the row
	 * 
	 */
	private void transferText(int from, int to, boolean cutFromBack, boolean copyToFront) {
		
		int toCursorPos = textBoxes.get(to).getCursorPosition();
		int endPos = textBoxes.get(to).getValue().length();
		int spaceLeft = MAX_ROW_LENGTH - endPos;
		if(spaceLeft == -1)
			return;
		ClearEditBox fromBox = textBoxes.get(from);
		int fromCursorPos = fromBox.getCursorPosition();
		String fromText = fromBox.getValue().substring(fromCursorPos, fromBox.getValue().length());
		String toCopy = fromText;
		if(spaceLeft < toCopy.length()) 
			toCopy = cutFromBack ? toCopy.substring(0, spaceLeft) : 
				toCopy.substring(toCopy.length() - spaceLeft, toCopy.length());
		int copyPos = copyToFront ? endPos : 0;
		textBoxes.get(to).setCursorPosition(copyPos);
		textBoxes.get(to).setHighlightPos(copyPos);
		textBoxes.get(to).insertText(toCopy);
		textBoxes.get(from).setValue(fromText.substring(toCopy.length(), fromText.length()));
		textBoxes.get(to).setCursorPosition(endPos);
		textBoxes.get(to).setHighlightPos(endPos);
			
	}

	private void setFocus(int row) {

		if(row < textBoxes.size() && row >= 0)
			focusedRow = row;
		else
			return;
		
		for(int i = 0; i < textBoxes.size(); i++) {
			if(i == row) {
				textBoxes.get(i).setFocus(true);
				screen.setFocused(textBoxes.get(row));
			}
			else
				textBoxes.get(i).setFocus(false);
		}
		
		transfer = textBoxes.get(focusedRow).getCursorPosition() == 0 && focusedRow != 0;
		lineFull = textBoxes.get(focusedRow).getValue().length() >= MAX_ROW_LENGTH;
			
	}
	
	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
		
		int focusedIndex = -1;
		for(int i = 0; i < textBoxes.size(); i++)
			if(textBoxes.get(i).mouseClicked(pMouseX, pMouseY, pButton)) {
				focusedIndex = i;
				textBoxes.get(i).setFocus(true);
			}
			else
				textBoxes.get(i).setFocus(false);
				
		if(focusedIndex != -1) {
			screen.setFocused(textBoxes.get(focusedIndex));
			focusedRow = focusedIndex;
			transfer = textBoxes.get(focusedRow).getCursorPosition() == 0;
			lineFull = textBoxes.get(focusedRow).getValue().length() >= MAX_ROW_LENGTH;
		}
			
		
		
		return true;
	}


}
