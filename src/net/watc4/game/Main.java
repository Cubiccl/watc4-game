package net.watc4.game;

import java.awt.Canvas;

import net.watc4.game.display.AnimationManager;
import net.watc4.game.display.Window;
import net.watc4.game.map.TileRegistry;
import net.watc4.game.utils.InputManager;

/** Main class. Contains all important objects of the game. */
public class Main
{
	/** Used to tick all Animations. */
	private static AnimationManager animationManager;
	/** The game itself. */
	private static Game game;
	private static InputManager inputManager;
	/** The <code>Window</code> to display the <code>Game</code>. */
	private static Window window;

	/** @return The Animation Manager. */
	public static AnimationManager getAnimationManager()
	{
		return animationManager;
	}

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

	/** @return The <code>Window</code> to display the <code>Game</code>. */
	public static Window getWindow()
	{
		return window;
	}

	public static boolean isKeyPressed(int key)
	{
		return inputManager.isKeyPressed(key);
	}

	public static void main(String[] args)
	{
		window = new Window();
		inputManager = new InputManager(window);
		animationManager = new AnimationManager();
		TileRegistry.createTiles();
		game = new Game();
	}

}
