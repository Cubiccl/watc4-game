package net.watc4.game.map.tiles;

import java.awt.image.BufferedImage;

import net.watc4.game.display.Sprite;
import net.watc4.game.map.Map;
import net.watc4.game.utils.geometry.Hitbox;
import net.watc4.game.utils.geometry.RectangleHitbox;

public class TileSide extends TileGround
{

	public TileSide()
	{
		super(11, (byte) 3, null);
	}

	@Override
	public BufferedImage getSprite(Map map, int x, int y, int data)
	{
		return Sprite.TILE_SIDE[data].getImage();
	}

	@Override
	public Hitbox hitbox(Map map, int x, int y, int data)
	{
		x *= Map.TILESIZE;
		y *= Map.TILESIZE;

		switch (data)
		{
			case 1:
				return new RectangleHitbox(x + Map.TILESIZE - 2, y, 1, Map.TILESIZE);

			case 2:
				return new RectangleHitbox(x, y + Map.TILESIZE - 2, Map.TILESIZE, 1);

			case 3:
				return new RectangleHitbox(x, y, 1, Map.TILESIZE);

			default:
				return new RectangleHitbox(x, y, Map.TILESIZE, 1);
		}
	}

}
