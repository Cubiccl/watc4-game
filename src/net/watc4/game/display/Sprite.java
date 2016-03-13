package net.watc4.game.display;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/** Contains all Images to be used in the <code>Game</code>. Usually, sprites are square, and dimensions are 16x, 32x, 64x, etc. */
public enum Sprite
{

	TILE_DEFAULT("res/textures/tiles/default.png"),
	TILE_GROUND("res/textures/tiles/ground.png"),
	TILE_WALL("res/textures/tiles/wall.png"),
	PATTOU_IDLE1("res/textures/playerPattou.png", 0, 0, 32),
	PATTOU_IDLE2("res/textures/playerPattou.png", 32, 0, 32),
	PATTOU_MOVING1("res/textures/playerPattou.png", 0, 32, 32),
	PATTOU_MOVING2("res/textures/playerPattou.png", 0, 64, 32),
	PATTOU_MOVING3("res/textures/playerPattou.png", 0, 96, 32),
	PATTOU_MOVING4("res/textures/playerPattou.png", 0, 128, 32),
	PATTOU_MOVING5("res/textures/playerPattou.png", 0, 160, 32),
	PATTOU_MOVING6("res/textures/playerPattou.png", 0, 192, 32),
	LUMI("res/textures/playerLumi.png", 0, 0, 32),
	LUMI_EYE("res/textures/playerLumi.png", 0, 32, 32);

	/** The actual Image. */
	private BufferedImage image;
	/** Its size. -1 if it is the entire Sprite sheet. */
	private int size;
	/** The URL to the Sprite sheet file. */
	private String url;
	/** The position of the Sprite in the Sprite sheet. */
	private int x, y;

	/** A Sprite.
	 * 
	 * @param url - The path to its Image. */
	private Sprite(String url)
	{
		this(url, 0, 0, -1);
	}

	/** A Sprite in a Sprite sheet.
	 * 
	 * @param url - The path to its Sprite sheet.
	 * @param x - Its x position in the sheet.
	 * @param y - Its y position in the sheet.
	 * @param size - Its size. */
	private Sprite(String url, int x, int y, int size)
	{
		this.url = url;
		this.x = x;
		this.y = y;
		this.size = size;
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
			if (size != -1) this.image = this.image.getSubimage(x, y, size, size);
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
