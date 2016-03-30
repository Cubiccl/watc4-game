package net.watc4.game.display;

import java.awt.Canvas;

import javax.swing.JFrame;

import net.watc4.game.GameUtils;

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

	/** Creates and sets up the Frame. */
	private void createFrame()
	{
		this.setTitle(GameUtils.NAME);
		this.setSize(800, 600);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}
