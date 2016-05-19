package net.watc4.game.map;

import java.awt.image.BufferedImage;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.utils.GameUtils;

/** Represents a Mirror. */
public class TileMirror extends Tile
{

	/** Creates a new Mirror Tile.
	 * 
	 * @param id - Its ID.
	 * @param animation - Its Animation.
	 * @param direction - The direction it's facing.
	 * @see GameUtils#UP */
	public TileMirror()
	{
		super(4, (byte) 3, new Animation(Sprite.TILE_MIRROR[0]), true, false);
	}

	@Override
	public BufferedImage getSprite(Map map, int x, int y, int data)
	{
		return Sprite.TILE_MIRROR[data].getImage();
	}

}
