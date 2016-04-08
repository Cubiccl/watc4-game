package net.watc4.game.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import net.watc4.game.Game;
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
	}

	@Override
	public void onKeyPressed(int keyID)
	{
		super.onKeyPressed(keyID);
		if (keyID == KeyEvent.VK_SPACE)
		{
			this.gameState.reset();
			Game.getGame().setCurrentState(this.gameState);
		}
		if (keyID == KeyEvent.VK_ESCAPE)
		{
			this.gameState.reset();
			Game.getGame().setCurrentState(new MainMenuState());
		}
	}

	@Override
	public void render(Graphics g)
	{
		this.gameState.render(g);

		int x = Game.getGame().window.canvas.getWidth() / 2, y = Game.getGame().window.canvas.getHeight() / 2;
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, x * 2, y * 2);
		g.setColor(Color.RED);
		TextRenderer.drawStringCentered(g, "Game Over", x, y - 30);
		TextRenderer.drawStringCentered(g, "Press Space to retry", x, y);
		TextRenderer.drawStringCentered(g, "Press Escape to exit", x, y + 30);
	}

	@Override
	public void update()
	{}

}
