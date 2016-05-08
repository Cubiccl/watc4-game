package net.watc4.game.utils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import net.watc4.game.Game;
import net.watc4.game.display.Window;

/** Manages keyboard input. */
public class InputManager implements KeyListener
{

	/** Number of keys available. */
	private static final int NUM_KEY_CODES = 256;
	public KeyAction[] keyActions = new KeyAction[NUM_KEY_CODES];
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
	
	public void mapToKey(KeyAction keyAction, int keyCode) 
	{
		
		keyActions[keyCode] = keyAction;
	}
	
	public void clearMap(KeyAction keyAction) 
	{

		for (int i=0; i<keyActions.length; i++) 
		{
	 		if (keyActions[i] == keyAction) 
	 		{ 
				keyActions[i] = null;
			} 
		}
		
	    keyAction.reset();
	}
	
	public ArrayList<String> getMaps(KeyAction keyAction) 
	{
		ArrayList<String> list = new ArrayList<String>();
		
		for(int i = 0; i<keyActions.length; i++) {
			if (keyActions[i] == keyAction) {
				list.add(getKeyName(i));
			}
		}
		
		return list;
	}
	
	public static String getKeyName(int keyCode) 
	{
		return KeyEvent.getKeyText(keyCode);
	}
	
	private KeyAction getKeyAction(KeyEvent e) 
	{ 
		
		int keyCode = e.getKeyCode();
		
		if(keyCode < keyActions.length) 
		{
			return keyActions[keyCode];
		}
		else 
		{
			return null;
		}
	}
	
	public KeyAction getKeyAction(int e) 
	{ 
		
		int keyCode = e;
		
		if(keyCode < keyActions.length) 
		{
			return keyActions[keyCode];
		}
		else 
		{
			return null;
		}
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
		if (!Game.getGame().isTransitionning()) Game.getGame().getCurrentState().onKeyPressed(e.getKeyCode());
	
		KeyAction keyAction = getKeyAction(e);
		
		if(keyAction != null) {
			keyAction.press();
		}
		// make sure the key isn't processed for anything else
		e.consume();
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		this.pressedKeys[e.getKeyCode()] = false;
		
		KeyAction keyAction = getKeyAction(e); 
		if (keyAction != null) {
            keyAction.release();
        }
		// make sure the key isn't processed for anything else
        e.consume();

	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		e.consume();
	}

}
