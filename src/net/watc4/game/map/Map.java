package net.watc4.game.map;

import java.awt.Graphics;

import net.watc4.game.GameObject;
import net.watc4.game.display.LightManager;
import net.watc4.game.entity.Entity;
import net.watc4.game.utils.FileUtils;

/** Represents the world the player evolves in. */
public class Map implements GameObject
{
	/** Constant: size a each Tile */
	public static final int TILESIZE = 32;
	/** Height of the map in tiles. */
	public final int height;
	/** The LightManager */
	public final LightManager lightManager;
	/** Pattou's spawn point. */
	public final int lumiSpawnX, lumiSpawnY;
	/** Pattou's spawn point. */
	public final int pattouSpawnX, pattouSpawnY;
	/** List of Tiles. */
	private int[][] tiles;
	/** Height of the map in tiles. */
	public final int width;

	/** @param width - Its width, in Tiles.
	 * @param height - Its height, in Tiles.
	 * @param lumiSpawnX - Lumi's spawn, x coordinate.
	 * @param lumiSpawnY - Lumi's spawn, y coordinate.
	 * @param pattouSpawnX - Pattou's spawn, x coordinate.
	 * @param pattouSpawnY - Pattou's spawn, y coordinate. */
	public Map(String url)
	{
		String[] mapText = FileUtils.readFileAsStringArray(url);
		int info[] = new int[6]; // width, height, lumiSpawnX, lumiSpawnY, pattouSpawnX and pattouSpawnY
		for (int i = 0; i < info.length; i++)
		{
			info[i] = Integer.valueOf(mapText[i].split(" = ")[1]);
		}
		this.width = info[0];
		this.height = info[1];
		this.lumiSpawnX = info[2] * Map.TILESIZE;
		this.lumiSpawnY = info[3] * Map.TILESIZE;
		this.pattouSpawnX = info[4] * Map.TILESIZE;
		this.pattouSpawnY = info[5] * Map.TILESIZE;

		this.tiles = new int[this.width][this.height];
		String[] values; // Tiles values temporarily stored per line from the map file
		for (int i = 0; i < info[1]; i++)
		{
			values = mapText[i + 7].split("\t");

			for (int j = 0; j < info[0]; j++)
			{
				this.setTileAt(j, i, Integer.valueOf(values[j]));
			}

		}
		lightManager = new LightManager(this);
	}

	/** @param entity - The Entity to test.
	 * @param xPosition - Its x position.
	 * @param yPosition - Its y position.
	 * @return The coordinates of the Tile it collides with, if it does. null if it doesn't. */
	public int[] detectCollision(Entity entity, float xPosition, float yPosition)
	{
		int tileXStart = (int) (xPosition / TILESIZE), tileYStart = (int) (yPosition / TILESIZE);
		int tileXEnd = (int) ((xPosition + entity.getHitbox().getWidth() - 1) / TILESIZE + 1);
		int tileYEnd = (int) ((yPosition + entity.getHitbox().getHeight() - 1) / TILESIZE + 1);
		for (int x = tileXStart; x < tileXEnd; ++x)
		{
			for (int y = tileYStart; y < tileYEnd; ++y)
				if (this.getTileAt(x, y).isSolid) return new int[]
				{ x, y };
		}
		return null;
	}

	/** @param x - X position.
	 * @param y - Y position.
	 * @return The Tile at the given coordinates. */
	public Tile getTileAt(int x, int y)
	{
		if (x < 0 || y < 0 || x >= this.tiles.length || y >= this.tiles[x].length) return TileRegistry.getTileFromId(2);
		return TileRegistry.getTileFromId(this.tiles[x][y]);
	}

	@Override
	public void render(Graphics g)
	{
		for (int i = 0; i < this.tiles.length; i++)
		{
			for (int j = 0; j < this.tiles[i].length; j++)
			{
				g.drawImage(this.getTileAt(i, j).sprite.getImage(), i * TILESIZE, j * TILESIZE, null);
			}
		}
		this.lightManager.render(g);
	}

	/** Sets the Tile at x, y to the input Tile's id.
	 * 
	 * @param x - The X coordinate.
	 * @param y - The Y coordinate.
	 * @param id - The ID of the Tile to set. */
	public void setTileAt(int x, int y, int id)
	{
		this.tiles[x][y] = id;
	}

	/** Sets the Tile at x, y to the input Tile.
	 * 
	 * @param x - The X coordinate.
	 * @param y - The Y coordinate.
	 * @param tile - The Tile to set. */
	public void setTileAt(int x, int y, Tile tile)
	{
		this.setTileAt(x, y, tile.id);
	}

	@Override
	public void update()
	{}
}
