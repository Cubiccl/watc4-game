package net.watc4.game.utils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import net.watc4.game.Game;
import net.watc4.game.display.Window;

/** Manages keyboard input. */
public class InputManager implements KeyListener
{

	/** Number of keys available. */
	private static final int NUM_KEY_CODES = 256;
	/** Contains all keys states. */
	private boolean[] pressedKeys;

	/** Creates the InputManager.
	 * 
	 * @param window - The window to listen for inputs from. */
	public InputManager(Window window)
	{
		this.pressedKeys = new boolean[NUM_KEY_CODES];

		for (int i = 0; i < NUM_KEY_CODES; i++)
		{
			this.pressedKeys[i] = false;
		}

		window.canvas.addKeyListener(this);
		window.canvas.setFocusTraversalKeysEnabled(false);

	}

	/** @param key - The identifier of a key.
	 * @see KeyEvent#VK_UP
	 * @return True if the key is pressed. */
	public boolean isKeyPressed(int key)
	{
		return this.pressedKeys[key];
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		this.pressedKeys[e.getKeyCode()] = true;
		Game.getGame().getCurrentState().onKeyPressed(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		this.pressedKeys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e)
	{}

}
