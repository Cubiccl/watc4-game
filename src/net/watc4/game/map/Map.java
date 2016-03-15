package net.watc4.game.map;

import java.awt.Graphics;

import net.watc4.game.GameObject;
import net.watc4.game.entity.EntityManager;
import net.watc4.game.map.Tile;

/** @author Krowk */
public class Map implements GameObject
{
	/**
	 * 
	 */
	EntityManager entityManager;
	/**
	 *  
	 */
	int width;
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

	/** @param width
	 * @param height
	 * @param lumiSpawnX
	 * @param lumiSpawnY
	 * @param pattouSpawnX
	 * @param pattouSpawnY */
	Map(int width, int height, int lumiSpawnX, int lumiSpawnY, int pattouSpawnX, int pattouSpawnY)
	{
		this.width = width / TILESIZE;
		this.height = height / TILESIZE;
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

	/** @param width
	 * @param height
	 * @param tileAt */
	public void setTileAt(int width, int height, Tile tileAt)
	{
		tiles[width][height] = tileAt.getId();
	}

	/**
	 * 
	 */
	@Override
	public void render(Graphics g)
	{
		// TODO Auto-generated method stub

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
