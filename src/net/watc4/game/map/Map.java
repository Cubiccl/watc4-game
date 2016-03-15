package net.watc4.game.map;

import java.awt.Graphics;

import net.watc4.game.GameObject;

/** @author Krowk */
public class Map implements GameObject
{
	/**
	* 
	*/
	int height;
	/**
	 * 
	 */
	int lumiSpawnX;
	/**
	 * 
	 */
	int lumiSpawnY;
	/**
	 * 
	 */
	int pattouSpawnX;
	/**
	 * 
	 */
	int pattouSpawnY;
	/**
	 * 
	 */
	int[][] tiles;

	/** Const: size a each Tile */
	public final int TILESIZE = 32;
	/**
	 *  
	 */
	int width;

	/** @param width
	 * @param height
	 * @param lumiSpawnX
	 * @param lumiSpawnY
	 * @param pattouSpawnX
	 * @param pattouSpawnY */
	public Map(int width, int height, int lumiSpawnX, int lumiSpawnY, int pattouSpawnX, int pattouSpawnY)
	{
		this.width = width;
		this.height = height;
		this.lumiSpawnX = lumiSpawnX;
		this.lumiSpawnY = lumiSpawnY;
		this.pattouSpawnX = pattouSpawnX;
		this.pattouSpawnY = pattouSpawnY;
		this.tiles = new int[this.width][this.height];
	}

	/** @param width
	 * @param height
	 * @return */
	public Tile getTileAt(int width, int height)
	{
		return TileRegistry.getTileFromId(tiles[width][height]);
	}

	/**
	 * 
	 */
	@Override
	public void render(Graphics g)
	{
		for (int i = 0; i < tiles.length; i++)
		{
			for (int j = 0; j < tiles[i].length; j++)
			{
				// g.drawImage(this.getTileAt(i, i).sprite.getImage(), i * 32, j * 32, null);
			}
		}
	}

	/** @param x
	 * @param y
	 * @param tile */
	public void setTileAt(int x, int y, Tile tile)
	{
		if (tile != null) this.tiles[x][y] = tile.getId();
	}

	/**
	 * 
	 */
	@Override
	public void update()
	{
		// TODO Auto-generated method stub

	}
}
