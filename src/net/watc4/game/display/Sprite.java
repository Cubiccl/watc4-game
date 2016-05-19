package net.watc4.game.display;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/** Contains all Images to be used in the <code>Game</code>. Usually, sprites are square, and dimensions are 16x, 32x, 64x, etc. */
public class Sprite
{
	public static Sprite ARROW_DOWN, ARROW_UP, CROSS, PLUS;
	public static Sprite BATTERY, CUTSCENE, DOOR, FLUORESCENT;
	public static Sprite LUMI, LUMI_EYE;
	public static Sprite[] PATTOU_IDLE_RIGHT, PATTOU_IDLE_LEFT;
	public static Sprite[] PATTOU_JUMPING_RIGHT, PATTOU_JUMPING_LEFT;
	public static Sprite[] PATTOU_LADDER;
	public static Sprite[] PATTOU_MOVING_RIGHT, PATTOU_MOVING_LEFT;
	public static Sprite TILE_DEFAULT, TILE_GROUND, TILE_WALL, TILE_DOOR, TILE_GLASS;
	public static Sprite TILE_LADDER_BOTTOM, TILE_LADDER_BASE, TILE_LADDER_TOP;
	public static Sprite[] TILE_MIRROR;
	public static Sprite[] TILE_SLOPE;
	public static Sprite UNKNOWN;

	/** Creates the main Sprites. */
	public static void createMainSprites()
	{
		UNKNOWN = new Sprite("res/textures/unknown.png", 0, 0, 32, 32);

		ARROW_DOWN = new Sprite("res/textures/icons.png", 0, 0, 16, 16);
		ARROW_UP = new Sprite("res/textures/icons.png", 16, 0, 16, 16);
		CROSS = new Sprite("res/textures/icons.png", 32, 0, 16, 16);
		PLUS = new Sprite("res/textures/icons.png", 48, 0, 16, 16);

		LUMI = new Sprite("res/textures/playerLumi.png", 0, 0, 32, 32);
		LUMI_EYE = new Sprite("res/textures/playerLumi.png", 32, 0, 15, 15);

		PATTOU_IDLE_RIGHT = new Sprite[2];
		PATTOU_IDLE_LEFT = new Sprite[2];
		PATTOU_IDLE_RIGHT[0] = new Sprite("res/textures/playerPattou.png", 0, 0, 32, 32);
		PATTOU_IDLE_RIGHT[1] = new Sprite("res/textures/playerPattou.png", 32, 0, 32, 32);
		PATTOU_IDLE_LEFT[0] = new Sprite("res/textures/playerPattou.png", 0, 0, 32, 32, true);
		PATTOU_IDLE_LEFT[1] = new Sprite("res/textures/playerPattou.png", 32, 0, 32, 32, true);

		PATTOU_LADDER = new Sprite[4];
		PATTOU_LADDER[0] = new Sprite("res/textures/playerPattou.png", 160, 0, 32, 32);
		PATTOU_LADDER[1] = new Sprite("res/textures/playerPattou.png", 128, 0, 32, 32);
		PATTOU_LADDER[2] = new Sprite("res/textures/playerPattou.png", 160, 0, 32, 32, true);
		PATTOU_LADDER[3] = new Sprite("res/textures/playerPattou.png", 128, 0, 32, 32, true);

		PATTOU_MOVING_RIGHT = loadSpriteSheet("res/textures/playerPattou.png", 0, 32, 32, 6, false);
		PATTOU_MOVING_LEFT = loadSpriteSheet("res/textures/playerPattou.png", 0, 32, 32, 6, true);

		PATTOU_JUMPING_RIGHT = loadSpriteSheet("res/textures/playerPattou.png", 0, 64, 32, 6, false);
		PATTOU_JUMPING_LEFT = loadSpriteSheet("res/textures/playerPattou.png", 0, 64, 32, 6, true);

		BATTERY = new Sprite("res/textures/battery.png", 0, 0, 32, 32);
		DOOR = new Sprite("res/textures/door.png", 0, 0, 32, 32);
		CUTSCENE = new Sprite("res/textures/cutscene.png", 0, 0, 32, 32);
		FLUORESCENT = new Sprite("res/textures/fluorescent.png", 0, 0, 32, 32);

		TILE_DEFAULT = new Sprite("res/textures/tileset.png", 0, 0, 32, 32);
		TILE_GROUND = new Sprite("res/textures/tileset.png", 32, 0, 32, 32);
		TILE_WALL = new Sprite("res/textures/tileset.png", 64, 0, 32, 32);
		TILE_GLASS = new Sprite("res/textures/tileset.png", 96, 0, 32, 32);
		TILE_DOOR = new Sprite("res/textures/tileset.png", 160, 32, 32, 32);

		TILE_LADDER_BASE = new Sprite("res/textures/tileset.png", 128, 0, 32, 32);
		TILE_LADDER_TOP = new Sprite("res/textures/tileset.png", 160, 0, 32, 32);
		TILE_LADDER_BOTTOM = new Sprite("res/textures/tileset.png", 192, 0, 32, 32);

		TILE_SLOPE = loadSpriteSheet("res/textures/tileset.png", 32, 32, 32, 4, false);

		TILE_MIRROR = new Sprite[4];
		TILE_MIRROR[0] = new Sprite("res/textures/tileset.png", 0, 32, 32, 32);
		TILE_MIRROR[1] = new Sprite("res/textures/tileset.png", 256, 0, 32, 32);
		TILE_MIRROR[2] = new Sprite("res/textures/tileset.png", 224, 0, 32, 32);
		TILE_MIRROR[3] = new Sprite("res/textures/tileset.png", 288, 0, 32, 32);
	}

	/** @param URL - The URL of the SpriteSheet to load.
	 * @param x - The X position to start at in the sheet.
	 * @param y - The Y position to start at in the sheet.
	 * @param size - The Size of the Sprites to extract.
	 * @param length - The number of Sprites to extract. If -1, return everything.
	 * @param reverse - True if the sprites should be reversed (horizontally).
	 * @return An Array containing length sprites from the target SpriteSheet. */
	public static Sprite[] loadSpriteSheet(String URL, int x, int y, int size, int length, boolean reverse)
	{
		try
		{
			BufferedImage sheet = ImageIO.read(new File(URL));
			Sprite[] sprites;
			if (length == -1) sprites = new Sprite[(sheet.getWidth() / size) * (sheet.getHeight() / size)];
			else sprites = new Sprite[length];
			for (int i = 0; i < sprites.length; ++i)
			{
				BufferedImage img = sheet.getSubimage(x, y, size, size);
				if (reverse)
				{
					AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
					tx.translate(-img.getWidth(null), 0);
					AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
					img = op.filter(img, null);
				}
				sprites[i] = new Sprite(img);
				x += size;
				if (x >= sheet.getWidth())
				{
					x = 0;
					y += size;
				}
			}

			return sprites;
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return new Sprite[0];
	}

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

	/** @param img - The Image of this Sprite. */
	public Sprite(BufferedImage img)
	{
		this.image = img;
	}

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
