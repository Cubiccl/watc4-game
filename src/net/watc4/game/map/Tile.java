package net.watc4.game.map;

import net.watc4.game.display.Animation;

public class Tile
{
	int id;
	Animation sprite;

	Tile(int id, Animation sprite)
	{
		this.id = id;
		this.sprite = sprite;
		TileRegistry.registerTile(this);
	}

	public int getId()
	{
		return this.id;
	}

}
