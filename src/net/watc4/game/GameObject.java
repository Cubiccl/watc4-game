package net.watc4.game;

import java.awt.Graphics;

/** Allows updating and rendering an Object on the screen. */
public interface GameObject
{

	/** Draws this Object onto the <code>Window</code>. Called at each frame.
	 * 
	 * @param g - The <code>Graphics</code> used to draw. */
	public void render(Graphics g);

	/** Updates this Object. Called at each tick. */
	public void update();

}
