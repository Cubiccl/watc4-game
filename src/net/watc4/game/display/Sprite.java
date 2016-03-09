package net.watc4.game.display;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/** Contains all Images to be used in the <code>Game</code>. Usually, sprites are square, and dimensions are 16x, 32x, 64x, etc. */
public enum Sprite
{

	TILE_DEFAULT("res/textures/tiles/default.png"),
	TILE_WALL("res/textures/tiles/wall.png"),
	TILE_GROUND("res/textures/tiles/ground.png");

	/** The actual Image. */
	private BufferedImage image;
	/** The URL to the Image file. */
	private String url;

	/** A Sprite.
	 * 
	 * @param url - The path to its Image. */
	private Sprite(String url)
	{
		this.url = url;
	}

	/** @return The Image to show. */
	public BufferedImage getImage()
	{
		if (this.image == null) this.loadImage();
		return this.image;
	}

	/** Loads the Image from the File. If the file is not found, uses the default "unknown" sprite.<br />
	 * Called when used for the first time, to avoid filling the memory with unused sprites. */
	private void loadImage()
	{
		try
		{
			this.image = ImageIO.read(new File(url));
		} catch (IOException e)
		{
			System.out.println("\"" + url + "\" is not a valid File. Using default Sprite.");
			e.printStackTrace();

			try
			{
				this.image = ImageIO.read(new File("res/textures/unknown.png"));
			} catch (IOException e2)
			{
				e2.printStackTrace();
			}
		}
	}

}
