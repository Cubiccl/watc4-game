package net.watc4.game.display;

import java.awt.Font;
import java.awt.Graphics;

/** Renders text and calculates text dimensions. */
public final class TextRenderer
{

	/** The Font to use to draw text. Defaults to bold Consolas 30. */
	public static Font font = new Font("Calibri", Font.BOLD, 30);

	/** Draws a String at the given coordinates.
	 * 
	 * @param g - The Graphics required to draw.
	 * @param text - The text to draw.
	 * @param x - Its x position.
	 * @param y - Its y position. */
	public static void drawString(Graphics g, String text, int x, int y)
	{
		g.setFont(font);
		g.drawString(text, x, y + getFontHeight());
	}

	/** Draws a String at the given coordinates. Will be centered around these coordinates.
	 * 
	 * @param g - The Graphics required to draw.
	 * @param text - The text to draw.
	 * @param x - Its x position.
	 * @param y - Its y position. */
	public static void drawStringCentered(Graphics g, String text, int x, int y)
	{
		g.setFont(font);
		g.drawString(text, x - getStringWidth(g, text) / 2, y + getFontHeight() / 2);
	}

	/** @return The Height of the Font. */
	public static int getFontHeight()
	{
		return font.getSize();
	}

	/** @param g - The Graphics required to draw.
	 * @param text - The input text.
	 * @return The width of the input text. */
	public static int getStringWidth(Graphics g, String text)
	{
		return (int) g.getFontMetrics(font).getStringBounds(text, g).getWidth();
	}

	public static void setFontSize(int size)
	{
		font = font.deriveFont(Font.BOLD, size);
	}

}
