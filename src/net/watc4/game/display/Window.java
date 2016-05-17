package net.watc4.game.display;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;

import javax.swing.JFrame;

import net.watc4.game.utils.GameSettings;
import net.watc4.game.utils.GameUtils;

@SuppressWarnings("serial")
public class Window extends JFrame
{
	/** The Canvas used to draw the <code>Game</code>. */
	public final Canvas canvas;
	private int width, height, offset;

	public Window()
	{
		super();
		this.createFrame();

		this.canvas = new Canvas();
		this.add(this.canvas);
		this.canvas.createBufferStrategy(2); // Allows to refresh the Canvas using the render method in Game.
	}

	/** Checks and applies the resolution in the settings. */
	public void applyResolution()
	{
		this.dispose();
		if (GameSettings.resolution == GameSettings.FULLSCREEN)
		{
			this.setExtendedState(MAXIMIZED_BOTH);
		} else
		{
			this.setExtendedState(JFrame.NORMAL);
			switch (GameSettings.resolution)
			{
				case GameSettings.R640:
					this.setSize(640, 480);
					this.setLocationRelativeTo(null);
					break;

				case GameSettings.R960:
					this.setSize(960, 720);
					this.setLocationRelativeTo(null);
					break;

				case GameSettings.R1280:
					this.setSize(1280, 960);
					this.setLocationRelativeTo(null);
					break;

				default:
					break;
			}
		}
		this.setUndecorated(GameSettings.resolution == GameSettings.FULLSCREEN);
		this.setVisible(true);

		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				canvas.requestFocus();
				canvas.setSize(getSize());
				height = canvas.getHeight();
				width = height * 4 / 3;
				offset = (canvas.getWidth() - width) / 2;
			}
		});
	}

	/** Creates and sets up the Frame. */
	private void createFrame()
	{
		this.setTitle(GameUtils.NAME);
		this.setSize(640, 480);
		this.setResizable(false);
		this.setFocusable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.applyResolution();
	}

	/** Draws the borders of the Window.
	 * 
	 * @param g - The Graphics required to draw. */
	public void drawBorders(Graphics2D g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.offset, this.height);
		g.fillRect(this.offset + this.width, 0, this.offset, this.height);
	}

	public int[] getGameDimensions()
	{
		return new int[]
		{ this.offset, 0, this.width, this.height };
	}

	/** Prepares the Graphics to draw the Game. Scales and places the Graphics depending on this Window's dimensions.
	 * 
	 * @param g - The Graphics to use. */
	public void prepareGraphics(Graphics2D g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
		g.translate(this.offset, 0);
		g.scale(this.width * 1f / Camera.WIDTH, this.height * 1f / Camera.HEIGHT);
	}

}
