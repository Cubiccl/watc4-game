package net.watc4.game.map;

import net.watc4.game.display.Animation;

/** A part of the Map. */
public class Tile
{
	/** Its Identifier. Allows better memory usage. */
	public final int id;
	/** Its sprite. */
	public final Animation sprite;

	/** Creates a new Tile.
	 * 
	 * @param id - Its identifier.
	 * @param sprite - Its Sprite. */
	Tile(int id, Animation sprite)
	{
		this.id = id;
		this.sprite = sprite;
		TileRegistry.registerTile(this);
	}

}
