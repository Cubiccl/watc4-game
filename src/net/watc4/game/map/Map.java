package net.watc4.game.map;

import java.awt.Graphics;

import net.watc4.game.GameObject;
import net.watc4.game.display.LightManager;
import net.watc4.game.utils.FileUtils;

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
	/** The LightManager */
	public LightManager lightManager;

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
		
		this.tiles = new int[this.height][this.width];
		String[] values; // Tiles values temporarily stored per line from the map file
		for (int i = 0; i < info[0]; i++)
		{
			values = mapText[i + 7].split("\t");

			for (int j = 0; j < info[1]; j++)
			{
				this.setTileAt(j, i, Integer.valueOf(values[j]));
			}

		}
		lightManager = new LightManager(this);
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
		lightManager.render(g);
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
	{
		lightManager.update();
	}
}
