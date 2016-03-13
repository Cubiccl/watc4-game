package net.watc4.game;

import java.awt.Canvas;

import net.watc4.game.display.AnimationManager;
import net.watc4.game.display.Window;

/** Main class. Contains all important objects of the game. */
public class Main
{
	/** The game itself. */
	private static Game game;
	/** The <code>Window</code> to display the <code>Game</code>. */
	private static Window window;
	/** Used to tick all Animations. */
	private static AnimationManager animationManager;

	/** @return The Canvas used to draw the <code>Game</code>. */
	public static Canvas getCanvas()
	{
		return getWindow().getCanvas();
	}

	/** @return The instance of the <code>Game</code> itself. */
	public static Game getGame()
	{
		return game;
	}

	/** @return The Animation Manager. */
	public static AnimationManager getAnimationManager()
	{
		return animationManager;
	}

	/** @return The <code>Window</code> to display the <code>Game</code>. */
	public static Window getWindow()
	{
		return window;
	}

	public static void main(String[] args)
	{
		window = new Window();
		animationManager = new AnimationManager();
		game = new Game();
	}

}
