package net.watc4.game.states;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import net.watc4.game.Game;
import net.watc4.game.display.Camera;
import net.watc4.game.display.TextRenderer;
import net.watc4.game.states.menu.MainMenuState;

/** Displayed when the player loses. */
public class GameOverState extends State
{
	/** The GameState they lost from. */
	private GameState gameState;

	public GameOverState(GameState gameState)
	{
		this.gameState = gameState;
		this.setBackground(this.gameState);
	}

	@Override
	public void onKeyPressed(int keyID)
	{
		super.onKeyPressed(keyID);
		if (keyID == KeyEvent.VK_SPACE)
		{
			this.gameState.reset();
			Game.getGame().setCurrentState(this.gameState, false);
		}
		if (keyID == KeyEvent.VK_ESCAPE)
		{
			this.gameState.reset();
			Game.getGame().setCurrentState(new MainMenuState());
		}
	}

	@Override
	public void render(Graphics2D g)
	{
		super.render(g);

		int x = Camera.WIDTH / 2, y = Camera.HEIGHT / 2;
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, x * 2, y * 2);
		g.setColor(Color.RED);
		TextRenderer.setFontSize(30);
		TextRenderer.drawStringCentered(g, "Game Over", x, y - 30);
		TextRenderer.drawStringCentered(g, "Press Space to retry", x, y);
		TextRenderer.drawStringCentered(g, "Press Escape to exit", x, y + 30);
	}

	@Override
	public void update()
	{}

}
