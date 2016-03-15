package net.watc4.game.utils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import net.watc4.game.display.Window;

public class InputManager implements KeyListener
{

	private static final int NUM_KEY_CODES = 256;

	private boolean[] pressedKeys;

	public InputManager(Window comp)
	{
		this.pressedKeys = new boolean[NUM_KEY_CODES];

		for (int i = 0; i < NUM_KEY_CODES; i++)
		{
			this.pressedKeys[i] = false;
		}

		comp.getCanvas().addKeyListener(this);
		comp.getCanvas().setFocusTraversalKeysEnabled(false);

	}

	public boolean isKeyPressed(int key)
	{

		return this.pressedKeys[key];

	}

	@Override
	public void keyPressed(KeyEvent e)
	{

		this.pressedKeys[e.getKeyCode()] = true;

	}

	@Override
	public void keyReleased(KeyEvent e)
	{

		this.pressedKeys[e.getKeyCode()] = false;

	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

}
