package com.yftach.messagemod.screen.widgets;

import java.util.ArrayList;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TextField extends AbstractWidget implements Widget, GuiEventListener {

	private int focusedRow;
	private ArrayList<ClearEditBox> textBoxes;
	private Screen screen;
	private int maxRowLength;
	private final int KEY_BACKSPACE = 259;
	private final int KEY_ENTER = 257;
	private final int KEY_DOWN = 264;
	private final int KEY_UP = 265;
	private boolean transfer = false, lineFull = false, fillLine = false;

	public TextField(int rows, int maxRowLength, Font font, int pX, int pY, int width, int rowHeight, Component message, Screen screen) {
		super(pX, pY, width, rowHeight * rows, message);
		this.maxRowLength = maxRowLength;
		textBoxes = new ArrayList<ClearEditBox>();
		for (int i = 0; i < rows; i++) {
			textBoxes.add(new ClearEditBox(font, pX, pY + i * rowHeight, width, rowHeight, null, message));
			textBoxes.get(i).setMaxLength(maxRowLength + 1);
			
		}
		this.screen = screen;
		screen.setInitialFocus(textBoxes.get(0));
		focusedRow = 0;
	}
	
	public void setTextColor(int color) {
		for(ClearEditBox box: textBoxes)
			box.setTextColor(color);
	}
	
	public void setUneditableTextColor(int color) {
		for(ClearEditBox box: textBoxes)
			box.setTextColorUneditable(color);
	}

	@Override
	public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		for (ClearEditBox box : textBoxes)
			box.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
	}

	public ArrayList<ClearEditBox> getRows() {
		return textBoxes;
	}

	public void setEditable(boolean editable) {
		for(ClearEditBox box: textBoxes)
			box.setEditable(editable);
	}
	
	public String getText() {
		String result = "";
		for (ClearEditBox box : textBoxes)
			result += box.getValue() + "\n";
		return result;
	}
	
	public boolean isEmpty() {
		String text = getText();
		for(int i = 0; i < textBoxes.size(); i++)
			if(text.charAt(i) != '\n')
				return false;
		return true;
	}
	
	public void setText(String text) {	
		for(ClearEditBox box: textBoxes) {
			int index = text.indexOf("\n");
			if(index <= maxRowLength && index > -1) {
				box.setValue(text.substring(0, index));
				try {
					text = text.substring(index + 1);
				} catch(StringIndexOutOfBoundsException e) {
					break;
				}
				
			}
		}
			
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
			if(cursorPos == maxRowLength + 1) {
				setFocus(focusedRow + 1);
				textBoxes.get(focusedRow - 1).moveCursorToStart();
				setCursorPos(focusedRow, 1);
			} else {
				textBoxes.get(focusedRow).moveCursorToStart();
				setCursorPos(focusedRow, cursorPos);
			}	
		}
	
		return true;
	}
	
	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		
		int cursorPos;
		switch(pKeyCode) {
		case KEY_BACKSPACE:
			int currentFocusedRow = focusedRow;
			if(focusedRow != 0 && transfer) {
				setFocus(focusedRow - 1);
				transferText(focusedRow + 1, focusedRow, true, true);
			}
			if(focusedRow != textBoxes.size() - 1 && fillLine
					&& textBoxes.get(currentFocusedRow).getValue().length() >= maxRowLength - 1) {
				fillLine(currentFocusedRow, currentFocusedRow >= textBoxes.size() - 2 || 
						textBoxes.get(currentFocusedRow + 1).getValue().length() < maxRowLength);
			}
			break;
		case KEY_ENTER:
			if(focusedRow != textBoxes.size() - 1) {
				setFocus(focusedRow + 1);
				lowerLines(focusedRow);
				transferText(focusedRow - 1, focusedRow, false, false);
			}
			break;
		case KEY_DOWN:
		case KEY_UP:
			int nextRow = pKeyCode == KEY_DOWN ? 1 : -1;
			cursorPos = textBoxes.get(focusedRow).getCursorPosition();
			setFocus(focusedRow + nextRow);
			setCursorPos(focusedRow, cursorPos);
			break;
		}
		
		transfer = textBoxes.get(focusedRow).getCursorPosition() == 0 && focusedRow != 0;
		lineFull = textBoxes.get(focusedRow).getValue().length() >= maxRowLength;
		fillLine = focusedRow != 0 || textBoxes.get(focusedRow).getCursorPosition() != 0;
		
		return true;
	}
	
	/**
	 * Lowers recursively all of the text one line down
	 * @param row - Row to lower from
	 */
	private void lowerLines(int row) {
		if(row >= textBoxes.size() - 1)
			return;
		if(textBoxes.get(row + 1).getValue().length() != 0)
			lowerLines(row + 1);
		int cursorPos = textBoxes.get(row).getCursorPosition();
		setCursorPos(row, 0);
		transferText(row, row + 1, false, false);
		setCursorPos(row, cursorPos);
		
	}
	
	/**
	 * Stream the text upwards recursively
	 * @param row - the row to stream the text to
	 * @param lastIter - is the last iteration of the recursion
	 */
	private void fillLine(int row, boolean lastIter) {
		if(row >= textBoxes.size() - 1 || textBoxes.get(row + 1).getValue().length() == 0)
			return;
		int cursorPos = textBoxes.get(row).getCursorPosition();
		String nextRowText = textBoxes.get(row + 1).getValue();
		textBoxes.get(row).setValue(textBoxes.get(row).getValue() + nextRowText.charAt(0));
		textBoxes.get(row + 1).setValue(nextRowText.substring(1, nextRowText.length()));
		setCursorPos(row, cursorPos);
		if(!lastIter)
			fillLine(row + 1, row + 1 >= textBoxes.size() - 2 || 
				textBoxes.get(row + 2).getValue().length() < maxRowLength);
	}
	
	/**
	 * Handles recursively the situation where the line is full by flooding 
	 * the text to the next rows
	 * @param row - the full row
	 */
	private void handleFullLine(int row) {
		
		if(row >= textBoxes.size() - 1 || row == textBoxes.size() - 2 
				&& textBoxes.get(row + 1).getValue().length() >= maxRowLength)
			return;
		
		if(textBoxes.get(row + 1).getValue().length() >= maxRowLength) 
			handleFullLine(row + 1);
		
		char transferChar;
		try {
			transferChar = textBoxes.get(row).getValue().charAt(maxRowLength);
		} catch(StringIndexOutOfBoundsException e) {
			transferChar = textBoxes.get(row).getValue().charAt(maxRowLength - 1);
		}
	
		textBoxes.get(row).setValue(textBoxes.get(row).getValue().substring(0, maxRowLength));
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
		
		int endPos = textBoxes.get(to).getValue().length();
		int spaceLeft = maxRowLength - endPos;
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
		setCursorPos(to, copyPos);
		textBoxes.get(to).insertText(toCopy);
		if(cutFromBack) 
			textBoxes.get(from).setValue(fromText.substring(toCopy.length(), fromText.length()));		
		else 
			textBoxes.get(from).setValue(fromBox.getValue().substring(0, fromBox.getValue().length() - toCopy.length()));
			
		setCursorPos(to, copyPos);
			
	}
	
	/**
	 * Sets the focused row
	 * @param row - row to focus
	 */
	public void setFocus(int row) {

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
		lineFull = textBoxes.get(focusedRow).getValue().length() >= maxRowLength;
			
	}
	
	public int getFocusedRow() {
		return focusedRow;
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
			lineFull = textBoxes.get(focusedRow).getValue().length() >= maxRowLength;
		}
				
		return true;
	}
	
	public void setCursorPos(int row, int pos) {
		textBoxes.get(row).setCursorPosition(pos);
		textBoxes.get(row).setHighlightPos(pos);
	}


}
