package net.watc4.game.map;

import java.util.HashMap;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;

/** Registers all Tiles. */
public class TileRegistry
{
	/** List of all Tiles. */
	private static HashMap<Integer, Tile> tiles;

	/** Creates all Tiles in the Game. */
	public static void createTiles()
	{
		tiles = new HashMap<Integer, Tile>();
		new Tile(0, new Animation(Sprite.TILE_DEFAULT), false, false);
		new Tile(1, new Animation(Sprite.TILE_GROUND), true, true);
		new Tile(2, new Animation(Sprite.TILE_WALL), false, false);
		new Tile(3, new Animation(Sprite.TILE_GLASS), true, false);
	}

	/** @param id - A Tile identifier.
	 * @return The Tile with the input ID. */
	public static Tile getTileFromId(int id)
	{
		return tiles.get(new Integer(id));
	}

	/** Registers a new Tile.
	 * 
	 * @param tile - The new Tile. */
	public static void registerTile(Tile tile)
	{
		tiles.put(tile.id, tile);
	}

}
