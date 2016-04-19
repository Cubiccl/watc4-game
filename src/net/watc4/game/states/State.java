package net.watc4.game.states;

import java.awt.event.KeyEvent;

import net.watc4.game.utils.GameSettings;
import net.watc4.game.utils.IRender;
import net.watc4.game.utils.IUpdate;

/** Represents a state of the Game, i.e. what is currently happening. */
public abstract class State implements IRender, IUpdate
{

	/** Called when a key is pressed.
	 * 
	 * @param keyID - The ID of the key.
	 * @see KeyEvent#VK_A */
	public void onKeyPressed(int keyID)
	{
		if (keyID == KeyEvent.VK_F1) GameSettings.debugMode = !GameSettings.debugMode;
		if (keyID == KeyEvent.VK_F2) GameSettings.godMode = !GameSettings.godMode;
		if (keyID == KeyEvent.VK_F3) GameSettings.lightMode = !GameSettings.lightMode;
	}

}
