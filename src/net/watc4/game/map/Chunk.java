package net.watc4.game.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

import javafx.geometry.Point2D;
import net.watc4.game.display.Camera;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.GameSettings;
import net.watc4.game.utils.IRender;
import net.watc4.game.utils.Vector;

public class Chunk implements IRender
{
	/** The Size of a Chunk, in pixels. */
	public static final int ACTUAL_SIZE = 10 * Map.TILESIZE;
	/** The Size of a Chunk, in tiles. */
	public static final int SIZE = 10;

	/** The tile data. */
	private byte[][] data;
	/** A reference to the Map it belongs to. */
	private Map map;
	/** This Chunk's size.
	 * 
	 * @see Chunk#SIZE */
	public final int size;
	/** The tiles in this Chunk. */
	private int[][] tiles;
	/** List of segments stopping light */
	private CopyOnWriteArraySet<Vector> wallSet;
	/** The position of this Chunk (in tiles.) */
	public final int xPos, yPos;

	/** Creates a new Chunk.
	 * 
	 * @param xPos - Its X Position.
	 * @param yPos - Its Y Position.
	 * @param map - The Map it belongs to. */
	public Chunk(int xPos, int yPos, Map map)
	{
		this.xPos = xPos;
		this.yPos = yPos;
		this.map = map;
		this.size = SIZE;
		this.tiles = new int[this.size][this.size];
		this.data = new byte[this.size][this.size];
	}

	/** Creates the Walls. */
	public void createWalls()
	{
		this.wallSet = new CopyOnWriteArraySet<Vector>();

		// Add vectors
		for (int y = -1; y < this.size; y++)
		{
			for (int x = -1; x < this.size; x++)
			{
				// Add the vertical ones
				{
					if (this.getTileAt(x, y).isOpaque && !this.getTileAt(x + 1, y).isOpaque) this.wallSet.add(new Vector((this.xPos * this.size + x + 1)
							* Map.TILESIZE, (this.yPos * this.size + y) * Map.TILESIZE, 0, Map.TILESIZE));
					else if (!this.getTileAt(x, y).isOpaque && this.getTileAt(x + 1, y).isOpaque) this.wallSet.add(new Vector((this.xPos * this.size + x + 1)
							* Map.TILESIZE, (this.yPos * this.size + y + 1) * Map.TILESIZE, 0, -Map.TILESIZE));
				}
				// Add the horizontal ones
				{
					if (this.getTileAt(x, y).isOpaque && !this.getTileAt(x, y + 1).isOpaque) this.wallSet.add(new Vector((this.xPos * this.size + x + 1)
							* Map.TILESIZE, (this.yPos * this.size + y + 1) * Map.TILESIZE, -Map.TILESIZE, 0));
					else if (!this.getTileAt(x, y).isOpaque && this.getTileAt(x, y + 1).isOpaque) this.wallSet.add(new Vector((this.xPos * this.size + x)
							* Map.TILESIZE, (this.yPos * this.size + y + 1) * Map.TILESIZE, Map.TILESIZE, 0));
				}
			}
		}

		// Merges adjacent segments into single ones
		boolean done = false;
		while (!done)
		{
			done = true;
			for (Vector targetVector : wallSet)
			{
				boolean manyVectorFound = false;
				Vector vectorFound = null;
				Iterator<Vector> jt = this.wallSet.iterator();

				while (jt.hasNext() && !manyVectorFound)
				{
					Vector arrowVector = (Vector) jt.next();
					if (targetVector.getPosition().getX() == arrowVector.getPosition().getX() + arrowVector.getDirection().getX()
							&& targetVector.getPosition().getY() == arrowVector.getPosition().getY() + arrowVector.getDirection().getY())
					{
						if (vectorFound != null) manyVectorFound = true;
						vectorFound = arrowVector;
					}
				}
				if (vectorFound == null) manyVectorFound = true;
				if (!manyVectorFound
						&& targetVector.getDirection().getX() * vectorFound.getDirection().getY() - targetVector.getDirection().getY()
								* vectorFound.getDirection().getX() == 0)
				{
					vectorFound.setDirection(new Point2D(vectorFound.getDirection().getX() + targetVector.getDirection().getX(), vectorFound.getDirection()
							.getY() + targetVector.getDirection().getY()));
					this.wallSet.remove(targetVector);
					done = false;
				}
			}
		}
	}

	/** @param x - X position.
	 * @param y - Y position.
	 * @return The Tile Data at the given coordinates. */
	public byte getDataAt(int x, int y)
	{
		if (x < 0 || x >= this.size || y < 0 || y >= this.size) return 0;
		return this.data[x][y];
	}

	/** @param x - X position.
	 * @param y - Y position.
	 * @return The Tile at the given coordinates. */
	public Tile getTileAt(int x, int y)
	{
		if (x < 0 || x >= this.size || y < 0 || y >= this.size) return TileRegistry.DEFAULT;
		return TileRegistry.getTileFromId(this.tiles[x][y]);
	}

	public CopyOnWriteArraySet<Vector> getWallSet()
	{
		return this.wallSet;
	}

	@Override
	public void render(Graphics2D g)
	{
		for (int x = 0; x < this.size; x++)
			for (int y = 0; y < this.size; y++)
				this.getTileAt(x, y).renderAt(g, this.map, this.xPos * this.size + x, this.yPos * this.size + y, this.getDataAt(x, y));

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
	 * @param data - The Tile Data to set. */
	public void setDataAt(int x, int y, byte data)
	{
		if (x >= 0 && x < this.size && y >= 0 && y < this.size) this.data[x][y] = data;
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

	/** @return True if this Chunk should Render onto the Screen. */
	public boolean shouldRender()
	{
		Camera camera = GameState.getInstance().camera;
		return new Rectangle(this.xPos * ACTUAL_SIZE, this.yPos * ACTUAL_SIZE, ACTUAL_SIZE, ACTUAL_SIZE).intersects(camera.rectangle());
	}

	/** @return True if this Chunk should Update. For now it always does, it may not in bigger maps. */
	public boolean shouldUpdate()
	{
		return true;
	}
}
