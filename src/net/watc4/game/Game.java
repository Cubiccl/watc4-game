package net.watc4.game;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import net.watc4.game.display.Window;
import net.watc4.game.states.GameState;

/** Main object. Contains a thread to run the game. */
public class Game implements Runnable
{

	/** True if the <code>Game</code> is running. */
	private boolean isRunning;
	private GameState state;

	/** A Thread used to update & render the <code>Game</code>. */
	private Thread thread;

	public Game()
	{
		this.isRunning = false;
		this.thread = new Thread(this);
		this.thread.start();
	}

	/** Displays the <code>Game</code> onto the {@link Window}. */
	private void render()
	{
		Main.getAnimationManager().update();
		BufferStrategy bufferStrategy = Main.getCanvas().getBufferStrategy();
		Graphics g = bufferStrategy.getDrawGraphics();

		g.clearRect(0, 0, Main.getCanvas().getWidth(), Main.getCanvas().getHeight());
		// Render here
		this.state.render(g);
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
		Main.getWindow().dispose();
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
