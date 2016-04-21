package net.watc4.game.map;

import java.awt.Graphics;
import java.util.HashSet;
import java.util.Iterator;

import javafx.geometry.Point2D;
import net.watc4.game.display.LightManager;
import net.watc4.game.entity.Entity;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.FileUtils;
import net.watc4.game.utils.IRender;
import net.watc4.game.utils.IUpdate;
import net.watc4.game.utils.Vector;

/** Represents the world the player evolves in. */
public class Map implements IRender, IUpdate
{
	/** Constant: size a each Tile */
	public static final int TILESIZE = 32;
	/** List of Areas of this Map. Used to limit Entity collision detections. */
	public final Chunk[][] chunks;
	/** Height of the map in tiles. */
	public final int height;
	/** The LightManager */
	public final LightManager lightManager;
	/** Pattou's spawn point. */
	public final int lumiSpawnX, lumiSpawnY;
	/** Pattou's spawn point. */
	public final int pattouSpawnX, pattouSpawnY;
	/** List of segments stopping light */
	private HashSet<Vector> wallSet;
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
		// Creating basic data
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

		// Creating Chunks
		int xChunks = this.width / Chunk.SIZE, yChunks = this.height / Chunk.SIZE;
		if (this.width % Chunk.SIZE != 0) ++xChunks;
		if (this.height % Chunk.SIZE != 0) ++yChunks;
		this.chunks = new Chunk[xChunks][yChunks];
		for (int x = 0; x < this.chunks.length; x++)
			for (int y = 0; y < this.chunks.length; y++)
				this.chunks[x][y] = new Chunk(x, y);

		String[] values; // Tiles values temporarily stored per line from the map file
		for (int y = 0; y < info[1]; y++)
		{
			values = mapText[y + 7].split("\t");

			for (int x = 0; x < info[0]; x++)
			{
				this.setTileAt(x, y, Integer.valueOf(values[x]));
			}

		}

		this.createWalls();

		this.lightManager = new LightManager(this);
	}

	/** Creates the Walls. */
	private void createWalls()
	{
		this.wallSet = new HashSet<>((this.height * (2 * this.width + 1) + this.width) / 1, 3);
		// Add the externals borders of the map
		for (int i = 0; i < this.width; i++)
		{
			this.wallSet.add(new Vector(1 * i * Map.TILESIZE, 0, Map.TILESIZE, 0));
			this.wallSet.add(new Vector((this.width * Map.TILESIZE) - (1 * i * Map.TILESIZE), this.height * Map.TILESIZE, -Map.TILESIZE, 0));
		}
		for (int i = 0; i < this.height; i++)
		{
			this.wallSet.add(new Vector(0, (this.height * Map.TILESIZE) - (1 * i * Map.TILESIZE), 0, -Map.TILESIZE));
			this.wallSet.add(new Vector(this.width * Map.TILESIZE, 1 * i * Map.TILESIZE, 0, Map.TILESIZE));
		}

		// Add vectors
		for (int y = 0; y < this.height - 1; y++)
		{
			// Add the horizontal ones
			for (int x = 0; x < this.width - 1; x++)
			{
				if (this.getTileAt(x, y).isOpaque && !this.getTileAt(x + 1, y).isOpaque) this.wallSet.add(new Vector((x + 1) * Map.TILESIZE, y * Map.TILESIZE,
						0, Map.TILESIZE));
				else if (!this.getTileAt(x, y).isOpaque && this.getTileAt(x + 1, y).isOpaque) this.wallSet.add(new Vector((x + 1) * Map.TILESIZE, (y + 1)
						* Map.TILESIZE, 0, -Map.TILESIZE));

			}
			// Add the vertical ones
			for (int x = 0; x < this.width; x++)
			{
				if (this.getTileAt(x, y).isOpaque && !this.getTileAt(x, y + 1).isOpaque) this.wallSet.add(new Vector((x + 1) * Map.TILESIZE, (y + 1)
						* Map.TILESIZE, -Map.TILESIZE, 0));
				else if (!this.getTileAt(x, y).isOpaque && this.getTileAt(x, y + 1).isOpaque) this.wallSet.add(new Vector(x * Map.TILESIZE, (y + 1)
						* Map.TILESIZE, Map.TILESIZE, 0));
			}
		}

		// Add the last horizontal ones
		for (int x = 0; x < this.width - 1; x++)
		{
			if (this.getTileAt(x, this.height - 1).isOpaque && !this.getTileAt(x + 1, this.height - 1).isOpaque) this.wallSet.add(new Vector((x + 1)
					* Map.TILESIZE, (this.height - 1) * Map.TILESIZE, 0, Map.TILESIZE));
			else if (!this.getTileAt(x, (this.height - 1)).isOpaque && this.getTileAt(x + 1, (this.height - 1)).isOpaque) this.wallSet.add(new Vector((x + 1)
					* Map.TILESIZE, ((this.height - 1) + 1) * Map.TILESIZE, 0, -Map.TILESIZE));

		}

		// Merges adjacent segments into single ones
		boolean done = false;
		boolean manyVectorFound = false;
		while (!done)
		{
			Iterator<Vector> it = this.wallSet.iterator();
			done = true;
			while (it.hasNext())
			{
				Vector targetVector = (Vector) it.next();
				Vector vectorFound = null;
				Iterator<Vector> jt = this.wallSet.iterator();

				while (jt.hasNext() && !manyVectorFound)
				{
					Vector arrowVector = (Vector) jt.next();
					if (targetVector.getPosition().getX() == arrowVector.getPosition().getX() + arrowVector.getDirection().getX()
							&& targetVector.getPosition().getY() == arrowVector.getPosition().getY() + arrowVector.getDirection().getY())
					;
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

	/** @param entity - The Entity to test.
	 * @param xPosition - Its x position.
	 * @param yPosition - Its y position.
	 * @return The coordinates of the Tile it collides with, if it does. null if it doesn't. */
	public int[] detectCollision(Entity entity, float xPosition, float yPosition)
	{
		int tileXStart = (int) (xPosition / TILESIZE), tileYStart = (int) (yPosition / TILESIZE);
		int tileXEnd = (int) ((xPosition + entity.getWidth() - 1) / TILESIZE + 1);
		int tileYEnd = (int) ((yPosition + entity.getHeight() - 1) / TILESIZE + 1);
		for (int x = tileXStart; x < tileXEnd; ++x)
		{
			for (int y = tileYStart; y < tileYEnd; ++y)
				if (this.getTileAt(x, y).isSolid) return new int[]
				{ x, y };
		}
		return null;
	}

	/** @param x - The X Coordinate
	 * @param y - The Y Coordinate
	 * @return The Chunk containing the given coordinates. */
	public Chunk getChunk(int x, int y)
	{
		int xChunk = x / Chunk.SIZE, yChunk = y / Chunk.SIZE;
		if (xChunk >= 0 && xChunk < this.chunks.length && yChunk >= 0 && yChunk < this.chunks[xChunk].length) return this.chunks[xChunk][yChunk];
		return null;
	}

	/** @param x - X position.
	 * @param y - Y position.
	 * @return The Tile at the given coordinates. */
	public Tile getTileAt(int x, int y)
	{
		Chunk chunk = this.getChunk(x, y);
		if (chunk != null) return chunk.getTileAt(x % Chunk.SIZE, y % Chunk.SIZE);
		return TileRegistry.DEFAULT;
	}

	public HashSet<Vector> getWallSet()
	{
		return this.wallSet;
	}

	@Override
	public void render(Graphics g)
	{
		for (int x = 0; x < this.chunks.length; ++x)
			for (int y = 0; y < this.chunks.length; ++y)
				this.chunks[x][y].render(g);
		
		GameState.getInstance().entityManager.render(g);
		this.lightManager.render(g);

	}

	/** Sets the Tile at x, y to the input Tile's id.
	 * 
	 * @param x - The X coordinate.
	 * @param y - The Y coordinate.
	 * @param id - The ID of the Tile to set. */
	public void setTileAt(int x, int y, int id)
	{
		Chunk chunk = this.getChunk(x, y);
		if (chunk != null) chunk.setTileAt(x % Chunk.SIZE, y % Chunk.SIZE, id);
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
