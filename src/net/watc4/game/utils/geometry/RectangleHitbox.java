package net.watc4.game.utils.geometry;

import java.awt.Point;
import java.awt.geom.Point2D;

/** A Rectangle Hitbox. */
public class RectangleHitbox extends PolygonHitbox
{

	public RectangleHitbox(double x, double y, double width, double height)
	{
		super(new Point2D[]
		{ new Point((int) x, (int) y), new Point((int) (x + width), (int) (y)), new Point((int) (x + width), (int) (y + height)),
				new Point((int) (x), (int) (y + height)) });
	}

	@Override
	protected boolean collidesWith(PolygonHitbox hitbox)
	{
		if (hitbox instanceof RectangleHitbox)
		{
			RectangleHitbox r = (RectangleHitbox) hitbox;
			return r.x() < this.x() + this.width() && r.x() + r.width() > this.x() && r.y() < this.y() + this.height() && r.y() + r.height() > this.y();
		}
		return super.collidesWith(hitbox);
	}

	public double height()
	{
		return this.vertices[2].getY() - this.vertices[0].getY();
	}

	public double width()
	{
		return this.vertices[2].getX() - this.vertices[0].getX();
	}

	public double x()
	{
		return this.vertices[0].getX();
	}

	public double y()
	{
		return this.vertices[0].getY();
	}

}
