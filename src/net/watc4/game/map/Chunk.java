package net.watc4.game.map;

import java.awt.Color;
import java.awt.Graphics;

import net.watc4.game.utils.GameSettings;
import net.watc4.game.utils.IRender;

public class Chunk implements IRender
{
	/** The Size of a Chunk, in pixels. */
	public static final int ACTUAL_SIZE = 10 * Map.TILESIZE;
	/** The Size of a Chunk, in tiles. */
	public static final int SIZE = 10;

	/** This Chunk's size.
	 * 
	 * @see Chunk#SIZE */
	public final int size;
	/** The tiles in this Chunk. */
	private int[][] tiles;
	/** The position of this Chunk (in tiles.) */
	public final int xPos, yPos;

	/** Creates a new Chunk.
	 * 
	 * @param xPos - Its X Position.
	 * @param yPos - Its Y Position. */
	public Chunk(int xPos, int yPos)
	{
		this.xPos = xPos;
		this.yPos = yPos;
		this.size = SIZE;
		this.tiles = new int[this.size][this.size];
	}

	/** @param x - X position.
	 * @param y - Y position.
	 * @return The Tile at the given coordinates. */
	public Tile getTileAt(int x, int y)
	{
		if (x < 0 || x >= this.size || y < 0 || y >= this.size) return TileRegistry.DEFAULT;
		return TileRegistry.getTileFromId(this.tiles[x][y]);
	}

	@Override
	public void render(Graphics g)
	{
		for (int x = 0; x < this.size; x++)
		{
			for (int y = 0; y < this.size; y++)
			{
				if (this.getTileAt(x, y).sprite != null) g.drawImage(this.getTileAt(x, y).sprite.getImage(), this.xPos * ACTUAL_SIZE + x * Map.TILESIZE,
						this.yPos * ACTUAL_SIZE + y * Map.TILESIZE, null);
			}
		}
		if (GameSettings.drawHitboxes)
		{
			g.setColor(Color.GREEN);
			g.drawRect(this.xPos * ACTUAL_SIZE, this.yPos * ACTUAL_SIZE, ACTUAL_SIZE, ACTUAL_SIZE);
		}
	}

	/** Sets the Tile at x, y to the input Tile.
	 * 
	 * @param x - The X coordinate.
	 * @param y - The Y coordinate.
	 * @param tile - The Tile to set. */
	public void setTileAt(int x, int y, int tile)
	{
		if (x >= 0 && x < this.size && y >= 0 && y < this.size) this.tiles[x][y] = tile;
	}
}
