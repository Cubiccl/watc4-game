package net.watc4.game.map;

import java.util.HashMap;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;

public class TileRegistry
{
	private static HashMap<Integer, Tile> tiles;

	public static void createTiles()
	{
		tiles = new HashMap<Integer, Tile>();
		new Tile(0, new Animation(Sprite.TILE_DEFAULT));
		new Tile(1, new Animation(Sprite.TILE_GROUND));
		new Tile(2, new Animation(Sprite.TILE_WALL));
	}

	public static Tile getTileFromId(int id)
	{
		return tiles.get(new Integer(id));
	}

	public static void registerTile(Tile tile)
	{
		tiles.put(tile.getId(), tile);
	}
}
