package net.watc4.game.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

import net.watc4.game.display.Camera;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.GameSettings;
import net.watc4.game.utils.IRender;
import net.watc4.game.utils.Vector;
import net.watc4.game.utils.geometry.PolygonHitbox;

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
		for (int y = 0; y < this.size; y++)
		{
			for (int x = 0; x < this.size; x++)
			{
				// Create walls from hitbot
				if (this.getTileAt(x, y).isOpaque)
				{
					Point2D[] vertices = ((PolygonHitbox) this.getTileAt(x, y).hitbox(map, x, y, this.getDataAt(x, y))).vertices;
				
					for (int i = 0; i < vertices.length - 1; i++)
					{
						Vector wall = new Vector(new Point2D.Double(xPos * ACTUAL_SIZE + vertices[i].getX(), yPos * ACTUAL_SIZE + vertices[i].getY()),
								new Point2D.Double(vertices[i + 1].getX() - vertices[i].getX(), vertices[i + 1].getY() - vertices[i].getY()));
						wallSet.add(wall);
					}
					Vector wall = new Vector(
							new Point2D.Double(xPos * ACTUAL_SIZE + vertices[vertices.length - 1].getX(),
									yPos * ACTUAL_SIZE + vertices[vertices.length - 1].getY()),
							new Point2D.Double(vertices[0].getX() - vertices[vertices.length - 1].getX(),
									vertices[0].getY() - vertices[vertices.length - 1].getY()));
					wallSet.add(wall);
				}
			}
		}
		// Delete dulpicates walls
		for (Vector wall1 : wallSet)
			for (Vector wall2 : wallSet)
			{
				if (wall1.getPosition().getX() == wall2.getPosition().getX() + wall2.getDirection().getX()
						&& wall1.getPosition().getY() == wall2.getPosition().getY() + wall2.getDirection().getY()
						&& wall2.getPosition().getX() == wall1.getPosition().getX() + wall1.getDirection().getX()
						&& wall2.getPosition().getY() == wall1.getPosition().getY() + wall1.getDirection().getY())
				{
					wallSet.remove(wall1);
					wallSet.remove(wall2);
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
				if (!manyVectorFound && targetVector.getDirection().getX() * vectorFound.getDirection().getY()
						- targetVector.getDirection().getY() * vectorFound.getDirection().getX() == 0)
				{
					vectorFound.setDirection(new Point2D.Double(vectorFound.getDirection().getX() + targetVector.getDirection().getX(),
							vectorFound.getDirection().getY() + targetVector.getDirection().getY()));
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
