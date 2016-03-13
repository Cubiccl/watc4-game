package net.watc4.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.display.Window;
import net.watc4.game.states.GameState;

/** Main object. Contains a thread to run the game. */
public class Game implements Runnable
{

	/** True if the <code>Game</code> is running. */
	private boolean isRunning;
	private GameState state;

	private Animation testAnimation; // TODO remove this test Animation
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
		BufferStrategy bufferStrategy = Main.getCanvas().getBufferStrategy();
		Graphics g = bufferStrategy.getDrawGraphics();

		g.clearRect(0, 0, Main.getCanvas().getWidth(), Main.getCanvas().getHeight());
		// Render here
		this.state.render(g);

		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				g.drawImage(Sprite.TILE_DEFAULT.getImage(), i * 32, j * 32, 32, 32, null);

		g.drawImage(Sprite.TILE_WALL.getImage(), 320, 0, 32, 32, null);
		g.drawImage(Sprite.TILE_GROUND.getImage(), 320 + 32, 0, 32, 32, null);
		g.drawImage(this.testAnimation.getImage(), 320 + 64, 0, 32, 32, null);
		g.setColor(Color.WHITE);
		g.drawString("Current FPS : " + GameUtils.currentFPS, 0, g.getFont().getSize());

		bufferStrategy.show();
		g.dispose();
	}

	@Override
	public void run()
	{
		// Preparing FPS handling
		int fps = 60, ticks = 0;
		double timePerTick = 1000000000 / fps, delta = 0;
		long now, lastTime = System.nanoTime(), timer = 0;
		this.testAnimation = new Animation(10, Sprite.PATTOU_MOVING1, Sprite.PATTOU_MOVING2, Sprite.PATTOU_MOVING3, Sprite.PATTOU_MOVING4,
				Sprite.PATTOU_MOVING5, Sprite.PATTOU_MOVING6);

		this.isRunning = true;
		while (this.isRunning)
		{
			// Calculate elapsed time
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;

			// If a tick has passed, update and render
			if (delta >= 1)
			{
				this.update();
				this.render();
				delta--;
				ticks++;
			}

			// If a second passed, update the FPS
			if (timer >= 1000000000)
			{
				GameUtils.currentFPS = ticks;
				ticks = 0;
				timer = 0;
			}

			try
			{
				Thread.sleep(1);
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
		Main.getAnimationManager().update();
		this.state.update();
	}

}
