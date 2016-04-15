package net.watc4.game.utils;

import java.awt.Graphics;

/** Allows updating and rendering an Object on the screen. */
public interface IRender
{

	/** Draws this Object onto the <code>Window</code>. Called at each frame.
	 * 
	 * @param g - The <code>Graphics</code> used to draw. */
	public void render(Graphics g);

}
