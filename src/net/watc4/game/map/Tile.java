package net.watc4.game.map;

import net.watc4.game.display.Animation;

/** A part of the Map. */
public class Tile
{
	/** Its Identifier. Allows better memory usage. */
	public final int id;
	/** Its sprite. */
	public final Animation sprite;

	public final boolean solid;

	public final boolean opaque;

	/** Creates a new Tile.
	 * 
	 * @param id - Its identifier.
	 * @param sprite - Its Sprite. */
	Tile(int id, Animation sprite, boolean solid, boolean opaque)
	{
		this.id = id;
		this.sprite = sprite;
		this.solid = solid;
		this.opaque = opaque;
		TileRegistry.registerTile(this);
	}

	/** @return opaque attribute */
	public boolean isOpaque()
	{
		return this.opaque;
	}

	/** @return solid attribute */
	public boolean isSolid()
	{
		return this.solid;
	}
}
