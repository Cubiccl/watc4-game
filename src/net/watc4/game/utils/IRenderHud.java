package net.watc4.game.utils;

import java.awt.Graphics2D;

/** Allows updating and rendering a HUD on the screen. */
public interface IRenderHud
{
	/** Draws this Object'HUD onto the <code>Window</code>. Called at each frame.
	 * 
	 * @param g - The <code>Graphics</code> used to draw. */
	public void renderHud(Graphics2D g, int x, int y, int width, int height);

}
