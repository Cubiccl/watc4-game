package net.watc4.game.states;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import net.watc4.game.display.Background;
import net.watc4.game.display.TextRenderer;
import net.watc4.game.utils.GameSettings;
import net.watc4.game.utils.GameUtils;
import net.watc4.game.utils.IRender;
import net.watc4.game.utils.IRenderHud;
import net.watc4.game.utils.IUpdate;

/** Represents a state of the Game, i.e. what is currently happening. */
public abstract class State implements IRender, IUpdate, IRenderHud
{
	/** The background : drawn behind the menu. */
	private IRender background;

	public State()
	{}

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

	/** Called when this State is loaded. */
	public void onLoad()
	{}

	/** Called when this State is unloaded. */
	public void onUnload()
	{}

	@Override
	public void render(Graphics2D g)
	{
		if (this.background != null) this.background.render(g);
	}

	@Override
	public void renderHud(Graphics2D g, int x, int y, int width, int height)
	{
		if (GameSettings.debugMode)
		{
			g.setColor(Color.DARK_GRAY);
			TextRenderer.setFontSize(25);
			TextRenderer.drawString(g, "Debug mode (F1)", x, y);
			TextRenderer.drawString(g, "FPS=" + GameUtils.currentFPS + ", UPS=" + GameUtils.currentUPS, x, y + 30);
		}
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
