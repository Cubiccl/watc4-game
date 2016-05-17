package net.watc4.game.map.tiles;

import net.watc4.game.display.Animation;
import net.watc4.game.map.Tile;

/** Represents a solid, opaque Tile (i.e. ground, walls, etc...) */
public class TileGround extends Tile
{

	/** Creates a new Ground Tile.
	 * 
	 * @param id - Its ID
	 * @param sprite - Its Animation. */
	public TileGround(int id, byte maxData, Animation sprite)
	{
		super(id, maxData, sprite, true, true);
	}

}
