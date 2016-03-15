package net.watc4.game.map;

import java.awt.Graphics;

import net.watc4.game.GameObject;

/** @author Krowk */
public class Map implements GameObject
{
	/** Const: size a each Tile */
	public static final int TILESIZE = 32;
	/**
	* 
	*/
	public final int height;
	/**
	 * 
	 */
	public final int lumiSpawnX;
	/**
	 * 
	 */
	public final int lumiSpawnY;
	/**
	 * 
	 */
	public final int pattouSpawnX;
	/**
	 * 
	 */
	public final int pattouSpawnY;

	/**
	 * 
	 */
	private int[][] tiles;
	/**
	 *  
	 */
	public final int width;

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
		for (int i = 0; i < this.tiles.length; i++)
		{
			for (int j = 0; j < this.tiles[i].length; j++)
			{
				this.tiles[i][j] = 0;
			}
		}
	}

	/** @param width
	 * @param height
	 * @return */
	public Tile getTileAt(int width, int height)
	{
		return TileRegistry.getTileFromId(this.tiles[width][height]);
	}

	/**
	 * 
	 */
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

	public void setTileAt(int x, int y, int id)
	{
		this.tiles[x][y] = id;
	}

	/** @param x
	 * @param y
	 * @param tile */
	public void setTileAt(int x, int y, Tile tile)
	{
		this.setTileAt(x, y, tile.getId());
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
