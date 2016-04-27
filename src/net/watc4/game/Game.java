package net.watc4.game;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import net.watc4.game.display.AnimationManager;
import net.watc4.game.display.Camera;
import net.watc4.game.display.TextRenderer;
import net.watc4.game.display.Window;
import net.watc4.game.entity.EntityRegistry;
import net.watc4.game.map.TileRegistry;
import net.watc4.game.states.State;
import net.watc4.game.states.menu.MainMenuState;
import net.watc4.game.utils.GameSettings;
import net.watc4.game.utils.GameUtils;
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
		EntityRegistry.createEntities();

		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					instance = new Game();
				} catch (Exception e)
				{}
			}
		});
	}

	/** Manages keyboard inputs from the user. */
	private InputManager inputManager;
	/** True if the <code>Game</code> is running. */
	private boolean isRunning;
	/** The current State of the Game. */
	private State state;
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

	/** @return The current State of the Game. */
	public State getCurrentState()
	{
		return this.state;
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

		BufferedImage screen = new BufferedImage(this.window.canvas.getWidth(), this.window.canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = screen.createGraphics();
		g.scale(this.window.canvas.getWidth() * 1f / Camera.WIDTH, this.window.canvas.getHeight() * 1f / Camera.HEIGHT);
		this.state.render(g);

		if (GameSettings.debugMode)
		{
			g.setColor(Color.DARK_GRAY);
			TextRenderer.setFontSize(15);
			TextRenderer.drawString(g, "Debug mode (F1)", 0, 0);
			TextRenderer.drawString(g, "FPS=" + GameUtils.currentFPS + ", UPS=" + GameUtils.currentUPS, 0, TextRenderer.getFontHeight());
		}
		g.dispose();

		BufferStrategy bufferStrategy = this.window.canvas.getBufferStrategy();
		Graphics g2 = bufferStrategy.getDrawGraphics();
		g2.drawImage(screen, 0, 0, null);
		bufferStrategy.show();
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
		this.state = new MainMenuState();

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

	/** Changes the current State of the Game.
	 * 
	 * @param state - The new State to apply. */
	public void setCurrentState(State state)
	{
		this.state = state;
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
