package net.watc4.game.display;

import java.awt.Canvas;
import java.awt.EventQueue;

import javax.swing.JFrame;

import net.watc4.game.utils.GameSettings;
import net.watc4.game.utils.GameUtils;

@SuppressWarnings("serial")
public class Window extends JFrame
{
	/** The Canvas used to draw the <code>Game</code>. */
	public final Canvas canvas;

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
					this.setSize(960, 640);
					this.setLocationRelativeTo(null);
					break;

				case GameSettings.R1280:
					this.setSize(1280, 760);
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
			}
		});
	}

	/** Creates and sets up the Frame. */
	private void createFrame()
	{
		this.setTitle(GameUtils.NAME);
		this.setResizable(false);
		this.setFocusable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.applyResolution();
	}

}
