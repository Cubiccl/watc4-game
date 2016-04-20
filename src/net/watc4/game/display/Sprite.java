package net.watc4.game.display;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/** Contains all Images to be used in the <code>Game</code>. Usually, sprites are square, and dimensions are 16x, 32x, 64x, etc. */
public enum Sprite
{
	LUMI("res/textures/playerLumi.png", 0, 0, 32, 32),
	LUMI_EYE("res/textures/playerLumi.png", 32, 0, 15, 15),
	PATTOU_FALLING_LEFT("res/textures/playerPattou.png", 160, 64, 32, 32, true),
	PATTOU_FALLING_RIGHT("res/textures/playerPattou.png", 160, 64, 32, 32),
	PATTOU_IDLE_LEFT1("res/textures/playerPattou.png", 0, 0, 32, 32, true),
	PATTOU_IDLE_LEFT2("res/textures/playerPattou.png", 32, 0, 32, 32, true),
	PATTOU_IDLE_RIGHT1("res/textures/playerPattou.png", 0, 0, 32, 32),
	PATTOU_IDLE_RIGHT2("res/textures/playerPattou.png", 32, 0, 32, 32),
	PATTOU_JUMPING_LEFT1("res/textures/playerPattou.png", 0, 64, 32, 32, true),
	PATTOU_JUMPING_LEFT2("res/textures/playerPattou.png", 32, 64, 32, 32, true),
	PATTOU_JUMPING_LEFT3("res/textures/playerPattou.png", 64, 64, 32, 32, true),
	PATTOU_JUMPING_LEFT4("res/textures/playerPattou.png", 96, 64, 32, 32, true),
	PATTOU_JUMPING_LEFT5("res/textures/playerPattou.png", 128, 64, 32, 32, true),
	PATTOU_JUMPING_LEFT6("res/textures/playerPattou.png", 160, 64, 32, 32, true),
	PATTOU_JUMPING_RIGHT1("res/textures/playerPattou.png", 0, 64, 32, 32),
	PATTOU_JUMPING_RIGHT2("res/textures/playerPattou.png", 32, 64, 32, 32),
	PATTOU_JUMPING_RIGHT3("res/textures/playerPattou.png", 64, 64, 32, 32),
	PATTOU_JUMPING_RIGHT4("res/textures/playerPattou.png", 96, 64, 32, 32),
	PATTOU_JUMPING_RIGHT5("res/textures/playerPattou.png", 128, 64, 32, 32),
	PATTOU_JUMPING_RIGHT6("res/textures/playerPattou.png", 160, 64, 32, 32),
	PATTOU_MOVING_LEFT1("res/textures/playerPattou.png", 0, 32, 32, 32, true),
	PATTOU_MOVING_LEFT2("res/textures/playerPattou.png", 32, 32, 32, 32, true),
	PATTOU_MOVING_LEFT3("res/textures/playerPattou.png", 64, 32, 32, 32, true),
	PATTOU_MOVING_LEFT4("res/textures/playerPattou.png", 96, 32, 32, 32, true),
	PATTOU_MOVING_LEFT5("res/textures/playerPattou.png", 128, 32, 32, 32, true),
	PATTOU_MOVING_LEFT6("res/textures/playerPattou.png", 160, 32, 32, 32, true),
	PATTOU_MOVING_RIGHT1("res/textures/playerPattou.png", 0, 32, 32, 32),
	PATTOU_MOVING_RIGHT2("res/textures/playerPattou.png", 32, 32, 32, 32),
	PATTOU_MOVING_RIGHT3("res/textures/playerPattou.png", 64, 32, 32, 32),
	PATTOU_MOVING_RIGHT4("res/textures/playerPattou.png", 96, 32, 32, 32),
	PATTOU_MOVING_RIGHT5("res/textures/playerPattou.png", 128, 32, 32, 32),
	PATTOU_MOVING_RIGHT6("res/textures/playerPattou.png", 160, 32, 32, 32),
	PATTOU_LADDER1("res/textures/playerPattou.png", 160, 0, 32, 32),
	PATTOU_LADDER2("res/textures/playerPattou.png", 128, 0, 32, 32),
	PATTOU_LADDER3("res/textures/playerPattou.png", 160, 0, 32, 32, true),
	PATTOU_LADDER4("res/textures/playerPattou.png", 128, 0, 32, 32, true),
	TILE_DEFAULT("res/textures/tileset.png", 0, 0, 32, 32),
	TILE_GLASS("res/textures/tileset.png", 96, 0, 32, 32),
	TILE_GROUND("res/textures/tileset.png", 32, 0, 32, 32),
	TILE_LADDER_BACK("res/textures/tileset.png", 192, 0, 32, 32),
	TILE_LADDER_BASE("res/textures/tileset.png", 128, 0, 32, 32),
	TILE_LADDER_TOP("res/textures/tileset.png", 160, 0, 32, 32),
	TILE_MIRROR_BACK("res/textures/tileset.png", 256, 0, 32, 32),
	TILE_MIRROR_LEFT("res/textures/tileset.png", 288, 0, 32, 32),
	TILE_MIRROR_RIGHT("res/textures/tileset.png", 224, 0, 32, 32),
	TILE_MIRROR_TOP("res/textures/tileset.png", 0, 32, 32, 32),
	TILE_WALL("res/textures/tileset.png", 64, 0, 32, 32),
	UNKNOWN("res/textures/unknown.png", 0, 0, 32, 32);

	/** The height of the Sprite. */
	private int height;
	/** The actual Image. */
	private BufferedImage image;
	/** True if the Sprite is a mirror of the actual Image. */
	private boolean reversed;
	/** The URL to the Sprite sheet file. */
	private String url;
	/** The width of the Sprite. */
	private int width;
	/** The position of the Sprite in the Sprite sheet. */
	private int x, y;

	/** A Sprite in a Sprite sheet.
	 * 
	 * @param url - The path to its Sprite sheet.
	 * @param x - Its x position in the sheet.
	 * @param y - Its y position in the sheet.
	 * @param size - Its size. */
	private Sprite(String url, int x, int y, int width, int height)
	{
		this(url, x, y, width, height, false);
	}

	/** A Sprite in a Sprite sheet.
	 * 
	 * @param url - The path to its Sprite sheet.
	 * @param x - Its x position in the sheet.
	 * @param y - Its y position in the sheet.
	 * @param size - Its size.
	 * @param reversed - True if the Sprite is a mirror of the actual Image. */
	private Sprite(String url, int x, int y, int width, int height, boolean reversed)
	{
		this.url = url;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.reversed = reversed;
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
			this.image = this.image.getSubimage(x, y, width, height);
			if (this.reversed)
			{
				AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
				tx.translate(-this.image.getWidth(null), 0);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				this.image = op.filter(this.image, null);
			}
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
