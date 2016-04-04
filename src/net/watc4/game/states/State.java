package net.watc4.game.states;

import java.awt.event.KeyEvent;

import net.watc4.game.GameObject;

/** Represents a state of the Game, i.e. what is currently happening. */
public abstract class State implements GameObject
{

	/** Called when a key is pressed.
	 * 
	 * @param keyID - The ID of the key.
	 * @see KeyEvent#VK_A */
	public void onKeyPressed(int keyID)
	{}

}
