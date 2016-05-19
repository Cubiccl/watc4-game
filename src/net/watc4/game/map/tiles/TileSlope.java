package net.watc4.game.map.tiles;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.map.Map;
import net.watc4.game.utils.geometry.Hitbox;
import net.watc4.game.utils.geometry.PolygonHitbox;

/** A Tile with a Slope. */
public class TileSlope extends TileGround
{
	/** Data values. */
	public static final int TOP_LEFT = 0, TOP_RIGHT = 1, BOTTOM_RIGHT = 2, BOTTOM_LEFT = 3;

	public TileSlope(int id)
	{
		super(id, (byte) 3, new Animation(Sprite.TILE_SLOPE[0]));
	}

	@Override
	public BufferedImage getSprite(Map map, int x, int y, int data)
	{
		return Sprite.TILE_SLOPE[data].getImage();
	}

	@Override
	public Hitbox hitbox(Map map, int x, int y, int data)
	{
		x *= Map.TILESIZE;
		y *= Map.TILESIZE;
		Point2D tl = new Point(x, y), tr = new Point(x + Map.TILESIZE - 1, y), br = new Point(x + Map.TILESIZE - 1, y + Map.TILESIZE - 1), bl = new Point(x, y
				+ Map.TILESIZE - 1);
		switch (data)
		{
			case TOP_RIGHT:
				return new PolygonHitbox(tl, br, bl);

			case BOTTOM_RIGHT:
				return new PolygonHitbox(tl, tr, bl);

			case BOTTOM_LEFT:
				return new PolygonHitbox(tl, tr, br);

			default:
				return new PolygonHitbox(tr, br, bl);
		}
	}

}
