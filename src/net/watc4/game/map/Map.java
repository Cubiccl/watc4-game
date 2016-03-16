package net.watc4.game.map;

import java.awt.Graphics;

import net.watc4.game.GameObject;

/** Represents the world the player evolves in. */
public class Map implements GameObject
{
	/** Constant: size a each Tile */
	public static final int TILESIZE = 32;
	/** Height of the map in tiles. */
	public final int height;
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
	public Map(int width, int height, int lumiSpawnX, int lumiSpawnY, int pattouSpawnX, int pattouSpawnY)
	{
		this.width = width;
		this.height = height;
		this.lumiSpawnX = lumiSpawnX;
		this.lumiSpawnY = lumiSpawnY;
		this.pattouSpawnX = pattouSpawnX;
		this.pattouSpawnY = pattouSpawnY;
		this.tiles = new int[this.width][this.height];
		for (int i = 0; i < this.tiles.length; i++)
		{
			for (int j = 0; j < this.tiles[i].length; j++)
			{
				this.tiles[i][j] = 0;
			}
		}
	}

	/** @param x - X position.
	 * @param y - Y position.
	 * @return The Tile at the given coordinates. */
	public Tile getTileAt(int x, int y)
	{
		return TileRegistry.getTileFromId(this.tiles[x][y]);
	}

	@Override
	public void render(Graphics g)
	{
		for (int i = 0; i < this.tiles.length; i++)
		{
			for (int j = 0; j < this.tiles[i].length; j++)
			{
				g.drawImage(this.getTileAt(i, j).sprite.getImage(), i * 32, j * 32, null);
			}
		}
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
	 * @param tiel - The Tile to set. */
	public void setTileAt(int x, int y, Tile tile)
	{
		this.setTileAt(x, y, tile.id);
	}

	@Override
	public void update()
	{}
}
