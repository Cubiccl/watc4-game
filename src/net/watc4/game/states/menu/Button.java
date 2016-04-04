package net.watc4.game.states.menu;

import java.awt.Color;
import java.awt.Graphics;

import net.watc4.game.GameObject;
import net.watc4.game.display.TextRenderer;

/** Represents a choice in a MenuState. */
public class Button implements GameObject
{

	/** The Identifier of this button. Used to determine which button was selected. */
	public final int id;
	/** True if this Button is currently selected. */
	public boolean isSelected;
	/** The text of this Button. */
	public String text;
	/** The coordinates of the center of this Button on the Screen. */
	public int xPosition, yPosition;

	/** Creates a new Button. Coordinates are set to (0, 0).
	 * 
	 * @param id - The Identifier of this Button.
	 * @param text - The text of this Button. */
	public Button(int id, String text)
	{
		this(id, text, 0, 0);
	}

	/** Creates a new Button.
	 * 
	 * @param id - The Identifier of this Button.
	 * @param text - The text of this Button.
	 * @param xPosition - Its x position on the Screen.
	 * @param yPosition - Its y position on the Screen. */
	public Button(int id, String text, int xPosition, int yPosition)
	{
		this.id = id;
		this.text = text;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}

	@Override
	public void render(Graphics g)
	{
		if (this.isSelected) g.setColor(Color.RED);
		else g.setColor(Color.BLUE);
		TextRenderer.drawStringCentered(g, this.text, this.xPosition, this.yPosition);
	}

	@Override
	public void update()
	{}

}
