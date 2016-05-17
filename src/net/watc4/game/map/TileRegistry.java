package net.watc4.game.map;

import java.util.HashMap;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.map.tiles.TileAir;
import net.watc4.game.map.tiles.TileGround;
import net.watc4.game.map.tiles.TileLadder;
import net.watc4.game.map.tiles.TileSlope;
import net.watc4.game.utils.GameUtils;

/** Registers all Tiles. */
public class TileRegistry
{
	/** List of all Tiles. */
	private static HashMap<Integer, Tile> tiles;

	/** @return The list of all Tiles*/
	public static HashMap<Integer, Tile> getTiles()
	{
		return tiles;
	}

	public static Tile DEFAULT, AIR, LADDER_TOP;

	/** Creates all Tiles in the Game. */
	public static void createTiles()
	{
		tiles = new HashMap<Integer, Tile>();
		DEFAULT = new TileAir(0, new Animation(Sprite.TILE_DEFAULT)); // Black
		new TileGround(1, new Animation(Sprite.TILE_GROUND)); // Ground
		AIR = new TileAir(2, null); // BG Wall
		new Tile(3, new Animation(Sprite.TILE_GLASS), true, false); // Glass
		new TileMirror(4, new Animation(Sprite.TILE_MIRROR_TOP), GameUtils.UP); // Mirror Top
		new TileMirror(5, new Animation(Sprite.TILE_MIRROR_RIGHT), GameUtils.RIGHT); // Mirror Right
		new TileMirror(6, new Animation(Sprite.TILE_MIRROR_BACK), GameUtils.DOWN); // Mirror Bottom
		new TileMirror(7, new Animation(Sprite.TILE_MIRROR_LEFT), GameUtils.LEFT); // Mirror Left
		LADDER_TOP = new TileLadder(8, new Animation(Sprite.TILE_LADDER_TOP)); // Top Ladder
		new TileLadder(9, new Animation(Sprite.TILE_LADDER_BASE)); // Ladder
		new TileLadder(10, new Animation(Sprite.TILE_LADDER_BACK)); // Bottom Ladder
		new TileSlope(11, new Animation(Sprite.TILE_SLOPE_TL));
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
