package net.watc4.game.map;

import java.awt.Graphics2D;
import java.io.File;

import net.watc4.game.display.LightManager;
import net.watc4.game.display.Sprite;
import net.watc4.game.entity.EntityLumi;
import net.watc4.game.entity.EntityManager;
import net.watc4.game.entity.EntityPattou;
import net.watc4.game.entity.EntityRegistry;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.FileUtils;
import net.watc4.game.utils.IRender;
import net.watc4.game.utils.IUpdate;

/** Represents the world the player evolves in. */
public class Map implements IRender, IUpdate
{
	/** Constant: size a each Tile */
	public static final int TILESIZE = 32;

	/** Creates a Map from the target URL.
	 * 
	 * @param mapName - The (file) name of the Map to use.
	 * @param game - The game instance.
	 * @return The built Map. */
	public static Map createFrom(String mapName, GameState game)
	{
		// Creating basic data
		String[] mapText = FileUtils.readFileAsStringArray("res/maps/" + mapName + ".txt");
		int info[] = new int[6]; // width, height, lumiSpawnX, lumiSpawnY, pattouSpawnX and pattouSpawnY
		for (int i = 0; i < info.length; i++)
			info[i] = Integer.valueOf(mapText[i].split(" = ")[1]);

		Map map = new Map(game, mapName, info[0], info[1], info[2] * Map.TILESIZE, info[3] * Map.TILESIZE, info[4] * Map.TILESIZE, info[5] * Map.TILESIZE);

		String[] values; // Tiles values temporarily stored per line from the map file
		for (int y = 0; y < info[1]; y++)
		{
			values = mapText[y + 7].split("\t");

			for (int x = 0; x < info[0]; x++)
			{
				String[] tileData = values[x].split(",");
				map.setTileAt(x, y, Integer.parseInt(tileData[0]));
				if (tileData.length > 1) map.setDataAt(x, y, Byte.parseByte(tileData[1]));
			}

		}
		map.createWalls();

		int index = 7 + info[1] + 1;
		while (index < mapText.length)
		{
			values = mapText[index].split(" ");
			EntityRegistry.createEntity(map, Integer.parseInt(values[0]), values);
			++index;
		}

		game.hasLumi = map.lumiSpawnX != -TILESIZE && map.lumiSpawnY != -TILESIZE;
		game.hasPattou = map.pattouSpawnX != -TILESIZE && map.pattouSpawnY != -TILESIZE;
		if (game.hasLumi) game.entityLumi = (EntityLumi) EntityRegistry.spawnEntity(map, 0, game, map.lumiSpawnX, map.lumiSpawnY);
		if (game.hasPattou) game.entityPattou = (EntityPattou) EntityRegistry.spawnEntity(map, 1, game, map.pattouSpawnX, map.pattouSpawnY);

		return map;
	}

	/** List of Areas of this Map. Used to limit Entity collision detections. */
	public final Chunk[][] chunks;
	/** Manages Entities in this Game. */
	public EntityManager entityManager;
	/** The instance of the GameState. */
	public final GameState game;
	/** True if this Map has a cutsom texture. */
	public final boolean hasCustomTexture;
	/** Height of the map in tiles. */
	public final int height;
	/** The LightManager */
	public final LightManager lightManager;
	/** Pattou's spawn point. */
	public final int lumiSpawnX, lumiSpawnY;
	/** This map's Name. */
	public final String name;
	/** Pattou's spawn point. */
	public final int pattouSpawnX, pattouSpawnY;
	/** Height of the map in tiles. */
	public final int width;

	/** @param game - The game instance.
	 * @param pattouSpawnY
	 * @param pattouSpawnX
	 * @param lumiSpawnY
	 * @param lumiSpawnX
	 * @param height
	 * @param width */
	public Map(GameState game, String name, int width, int height, int lumiSpawnX, int lumiSpawnY, int pattouSpawnX, int pattouSpawnY)
	{
		this.game = game;
		this.name = name;
		this.width = width;
		this.height = height;
		this.lumiSpawnX = lumiSpawnX;
		this.lumiSpawnY = lumiSpawnY;
		this.pattouSpawnX = pattouSpawnX;
		this.pattouSpawnY = pattouSpawnY;
		this.entityManager = new EntityManager(this);
		this.lightManager = new LightManager(this);
		this.hasCustomTexture = new File("res/textures/maps/" + this.name + ".png").exists();

		// Creating Chunks
		int xChunks = this.width / Chunk.SIZE, yChunks = this.height / Chunk.SIZE;
		if (this.width % Chunk.SIZE != 0) ++xChunks;
		if (this.height % Chunk.SIZE != 0) ++yChunks;
		this.chunks = new Chunk[xChunks][yChunks];
		for (int x = 0; x < this.chunks.length; x++)
			for (int y = 0; y < this.chunks[x].length; y++)
			{
				this.chunks[x][y] = new Chunk(x, y, this);
				this.entityManager.registerChunk(this.chunks[x][y]);
			}

		if (this.hasCustomTexture) this.createCustomTexture();
	}

	/** Creates the Custom Texture. */
	private void createCustomTexture()
	{
		Sprite[] sheet = Sprite.loadSpriteSheet("res/textures/maps/" + this.name + ".png", 0, 0, 32, -1, false);
		for (int y = 0; y < this.height; ++y)
		{
			for (int x = 0; x < this.width; ++x)
			{
				this.setCustomSprite(x, y, sheet[x + y * this.width]);
			}
		}
	}

	private void createWalls()
	{
		for (int x = 0; x < this.chunks.length; x++)
			for (int y = 0; y < this.chunks[x].length; y++)
				this.chunks[x][y].createWalls();
	}

	/** @param x - The X Coordinate (in pixels)
	 * @param y - The Y Coordinate (in pixels)
	 * @return The Chunk containing the given coordinates. */
	public Chunk getChunk(float x, float y)
	{
		return this.getChunk((int) x / Map.TILESIZE, (int) y / Map.TILESIZE);
	}

	/** @param x - The X Coordinate (in tiles)
	 * @param y - The Y Coordinate (in tiles)
	 * @return The Chunk containing the given coordinates. */
	public Chunk getChunk(int x, int y)
	{
		int xChunk = x / Chunk.SIZE, yChunk = y / Chunk.SIZE;
		if (xChunk >= 0 && xChunk < this.chunks.length && yChunk >= 0 && yChunk < this.chunks[xChunk].length) return this.chunks[xChunk][yChunk];
		return null;
	}

	/** @param x - X position.
	 * @param y - Y position.
	 * @return The Custom Sprite for the Tile at the given coordinates. */
	public Sprite getCustomSprite(int x, int y)
	{
		Chunk chunk = this.getChunk(x, y);
		if (chunk != null) return chunk.getCustomSprite(x % Chunk.SIZE, y % Chunk.SIZE);
		return Sprite.UNKNOWN;
	}

	/** @param x - X position.
	 * @param y - Y position.
	 * @return The Tile data at the given coordinates. */
	public byte getDataAt(int x, int y)
	{
		Chunk chunk = this.getChunk(x, y);
		if (chunk != null) return chunk.getDataAt(x % Chunk.SIZE, y % Chunk.SIZE);
		return 0;
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

	@Override
	public void render(Graphics2D g)
	{
		for (int x = 0; x < this.chunks.length; ++x)
			for (int y = 0; y < this.chunks[x].length; ++y)
				if (this.chunks[x][y].shouldRender()) this.chunks[x][y].render(g);
		this.entityManager.render(g);
		this.lightManager.render(g);
	}

	/** Sets the Tile at x, y to the input Custom Sprite.
	 * 
	 * @param x - The X coordinate.
	 * @param y - The Y coordinate.
	 * @param sprite - The Custom Sprite to set. */
	public void setCustomSprite(int x, int y, Sprite sprite)
	{
		Chunk chunk = this.getChunk(x, y);
		if (chunk != null) chunk.setCustomSprite(x % Chunk.SIZE, y % Chunk.SIZE, sprite);
	}

	/** Sets the Tile data at x, y to the input data.
	 * 
	 * @param x - The X coordinate.
	 * @param y - The Y coordinate.
	 * @param id - The data of the Tile to set. */
	public void setDataAt(int x, int y, byte data)
	{
		Chunk chunk = this.getChunk(x, y);
		if (chunk != null) chunk.setDataAt(x % Chunk.SIZE, y % Chunk.SIZE, data);
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
	{
		this.entityManager.update();
		this.lightManager.update();
	}
}
