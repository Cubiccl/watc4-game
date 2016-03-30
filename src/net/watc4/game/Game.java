package net.watc4.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import net.watc4.game.display.AnimationManager;
import net.watc4.game.display.Window;
import net.watc4.game.map.TileRegistry;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.InputManager;

/** Main object. Contains a thread to run the game. */
public class Game implements Runnable
{
	/** The instance of the Game. */
	private static Game instance;

	/** @return The instance of the <code>Game</code> itself. */
	public static Game getGame()
	{
		return instance;
	}

	public static void main(String[] args)
	{
		AnimationManager.create();
		TileRegistry.createTiles();
		instance = new Game();
	}

	/** Manages keyboard inputs from the user. */
	private InputManager inputManager;
	/** True if the <code>Game</code> is running. */
	private boolean isRunning;
	private GameState state;
	/** A Thread used to update & render the <code>Game</code>. */
	private Thread thread;
	/** The <code>Window</code> to display the <code>Game</code>. */
	public final Window window;

	public Game()
	{
		this.window = new Window();
		this.isRunning = false;
		this.inputManager = new InputManager(window);
		this.thread = new Thread(this);
		this.thread.start();
	}

	/** @param key - The ID of the key pressed.
	 * @see KeyEvent#VK_UP
	 * @return True if the key is being pressed. */
	public boolean isKeyPressed(int key)
	{
		return this.inputManager.isKeyPressed(key);
	}

	/** Displays the <code>Game</code> onto the {@link Window}. */
	private void render()
	{
		AnimationManager.update();
		BufferStrategy bufferStrategy = this.window.canvas.getBufferStrategy();
		Graphics g = bufferStrategy.getDrawGraphics();

		g.clearRect(0, 0, this.window.canvas.getWidth(), this.window.canvas.getHeight());
		// Render here
		this.state.render(g);
		g.setColor(Color.GRAY);
		g.drawString("FPS=" + GameUtils.currentFPS + ", UPS=" + GameUtils.currentUPS, 0, g.getFont().getSize());

		bufferStrategy.show();
		g.dispose();
	}

	@Override
	public void run()
	{
		// Preparing FPS handling
		long startTime = System.nanoTime(), currentTime = startTime, timer = 0;
		int fps = 0, ups = 0;
		double framesTime = 0, updatesTime = 0;
		final int targetFPS = 60, targetUPS = 60;
		double timePerFrame = 1000000000 / targetFPS, timePerUpdate = 1000000000 / targetUPS;
		this.state = GameState.getInstance();

		this.isRunning = true;
		while (this.isRunning)
		{
			// Calculate elapsed time
			long elapsedTime = System.nanoTime() - currentTime;
			currentTime += elapsedTime;
			timer += elapsedTime;
			framesTime += elapsedTime / timePerFrame;
			updatesTime += elapsedTime / timePerUpdate;

			// If a tick has passed, update until there is no delayed update
			while (updatesTime >= 1)
			{
				this.update();
				ups += 1;
				--updatesTime;
			}

			// If a frame passed, draw.
			if (framesTime >= 1)
			{
				this.render();
				fps += 1;
				--framesTime;
			}

			// If a second passed, update the FPS
			if (timer >= 1000000000)
			{
				GameUtils.currentFPS = fps;
				GameUtils.currentUPS = ups;
				fps = 0;
				ups = 0;
				timer = 0;
			}

			try
			{
				Thread.sleep(5);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		// When not running, exit the game.
		this.window.dispose();
	}

	/** Exits the game. */
	public void stop()
	{
		this.isRunning = false;
	}

	/** Manages the <code>Game</code> logic. */
	private void update()
	{
		this.state.update();
	}

}
