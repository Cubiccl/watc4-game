package net.watc4.game.map;

import net.watc4.game.display.Animation;
import net.watc4.game.utils.GameUtils;

/** Represents a Mirror. */
public class TileMirror extends Tile
{

	/** The direction this mirror is facing. */
	public final int direction;

	/** Creates a new Mirror Tile.
	 * 
	 * @param id - Its ID.
	 * @param animation - Its Animation.
	 * @param direction - The direction it's facing.
	 * @see GameUtils#UP */
	public TileMirror(int id, Animation animation, int direction)
	{
		super(id, (byte) 0, animation, true, false);
		this.direction = direction;
	}

}
