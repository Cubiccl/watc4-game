package net.watc4.game.display;

import java.awt.Graphics2D;

import net.watc4.game.states.GameState;
import net.watc4.game.utils.IRender;

/** Allows to draw an Image repetitively to fill the screen. Can act as a background or a foreground. */
public class Background implements IRender
{
	/** The Animation to fill the screen with. */
	public Animation animation;
	/** Reference to the Game State. */
	public GameState game;
	/** Between 0 and 1. How slow the background is compared to the camera. */
	public double parallax;

	public Background(Animation animation, GameState game)
	{
		this.animation = animation;
		this.game = game;
		this.parallax = 0.25;
	}

	@Override
	public void render(Graphics2D g)
	{
		/*
		int width = this.animation.getImage().getWidth(), height = this.animation.getImage().getHeight();
		int xOffset = 0, yOffset = 0;
		if (this.game != null)
		{
			xOffset = (int) (this.game.camera.getXOffset() * this.parallax - this.game.camera.getXOffset());
			yOffset = (int) (this.game.camera.getYOffset() * this.parallax - this.game.camera.getYOffset());
		}
		for (int x = 0; x < Camera.WIDTH / width / this.parallax; x++)
		{
			for (int y = 0; y < Camera.HEIGHT / height / this.parallax; y++)
			{
				g.drawImage(this.animation.getImage(), x * width - xOffset, y * height - yOffset, null);
			}
		}*/
		g.drawImage(this.animation.getImage(), 0, 0, null);
	}

}
