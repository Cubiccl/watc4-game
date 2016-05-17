package net.watc4.game.map.tiles;

import net.watc4.game.display.Animation;
import net.watc4.game.map.Tile;

/** A tile neither solid or opaque (i.e. air and objects) */
public class TileAir extends Tile
{

	/** Creates a new Air Tile.
	 * 
	 * @param id - Its ID.
	 * @param animation - Its Animation. */
	public TileAir(int id, byte maxData, Animation animation)
	{
		super(id, maxData, animation, false, false);
	}

}
