package net.watc4.game.states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Background;
import net.watc4.game.display.Sprite;
import net.watc4.game.utils.GameSettings;
import net.watc4.game.utils.IRender;
import net.watc4.game.utils.IUpdate;

/** Represents a state of the Game, i.e. what is currently happening. */
public abstract class State implements IRender, IUpdate
{
	/** The background : drawn behind the menu. */
	private IRender background;

	public State()
	{
		this.background = new Background(new Animation(Sprite.TILE_WALL), null);
	}

	/** Called when a key is pressed.
	 * 
	 * @param keyID - The ID of the key.
	 * @see KeyEvent#VK_A */
	public void onKeyPressed(int keyID)
	{
		if (keyID == KeyEvent.VK_F1) GameSettings.debugMode = !GameSettings.debugMode;
		if (keyID == KeyEvent.VK_F2) GameSettings.godMode = !GameSettings.godMode;
		if (keyID == KeyEvent.VK_F3) GameSettings.lightMode = !GameSettings.lightMode;
		if (keyID == KeyEvent.VK_F4) GameSettings.drawHitboxes = !GameSettings.drawHitboxes;
	}

	@Override
	public void render(Graphics2D g)
	{
		if (this.background != null) this.background.render(g);
	}

	/** Changes this State's Background.
	 * 
	 * @param background - The new Background to apply. */
	public void setBackground(IRender background)
	{
		if (this.background instanceof Background) ((Background) this.background).animation.dispose();
		this.background = background;
	}

}
