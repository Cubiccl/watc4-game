package net.watc4.game.map;

import java.util.HashMap;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.map.tiles.TileAir;
import net.watc4.game.map.tiles.TileGround;
import net.watc4.game.map.tiles.TileLadder;
import net.watc4.game.map.tiles.TileSlope;

/** Registers all Tiles. */
public class TileRegistry
{
	public static Tile DEFAULT, AIR, LADDER_TOP;

	/** List of all Tiles. */
	private static HashMap<Integer, Tile> tiles;

	/** Creates all Tiles in the Game. */
	public static void createTiles()
	{
		tiles = new HashMap<Integer, Tile>();
		DEFAULT = new TileAir(0, (byte) 0, new Animation(Sprite.TILE_DEFAULT)); // Black
		new TileGround(1, (byte) 0, new Animation(Sprite.TILE_GROUND)); // Ground
		AIR = new TileAir(2, (byte) 0, null); // BG Wall
		new Tile(3, (byte) 0, new Animation(Sprite.TILE_GLASS), true, false); // Glass
		new TileMirror();
		LADDER_TOP = new TileLadder(5, new Animation(Sprite.TILE_LADDER_TOP)); // Top Ladder
		new TileLadder(6, new Animation(Sprite.TILE_LADDER_BASE)); // Ladder
		new TileLadder(7, new Animation(Sprite.TILE_LADDER_BOTTOM)); // Bottom Ladder
		new TileSlope(8);
		new TileAir(9, (byte) 0, new Animation(Sprite.TILE_DOOR));
		new Tile(10, (byte) 0, null, false, true);
	}

	/** @param id - A Tile identifier.
	 * @return The Tile with the input ID. */
	public static Tile getTileFromId(int id)
	{
		return tiles.get(new Integer(id));
	}

	/** @return The list of all Tiles */
	public static HashMap<Integer, Tile> getTiles()
	{
		return tiles;
	}

	/** Registers a new Tile.
	 * 
	 * @param tile - The new Tile. */
	public static void registerTile(Tile tile)
	{
		tiles.put(tile.id, tile);
	}

}
