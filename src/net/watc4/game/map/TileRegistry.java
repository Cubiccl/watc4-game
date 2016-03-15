package net.watc4.game.map;

import java.util.HashMap;

public class TileRegistry
{
	static HashMap<Integer, Tile> tiles;

	public void CreatTiles()
	{
		tiles = new HashMap<Integer, Tile>();
	}

	public static Tile getTileFromId(int id)
	{
		return tiles.get(id);
	}

	public void registerTile(Tile tile)
	{
		this.tiles.put(tile.getId(), tile);
	}
}
