package net.watc4.game.utils.geometry;

import java.awt.geom.Point2D;

public class CircleHitbox extends Hitbox
{
	public final Point2D center;
	public final double radius;

	public CircleHitbox(Point2D center, double radius)
	{
		this.center = center;
		this.radius = radius;
	}

	@Override
	public boolean contains(Point2D point)
	{
		return this.center.distance(point) <= this.radius;
	}

	@Override
	protected boolean collidesWith(CircleHitbox hitbox)
	{
		return this.center.distance(hitbox.center) <= this.radius + hitbox.radius;
	}

	@Override
	protected boolean collidesWith(PolygonHitbox hitbox)
	{
		return hitbox.collidesWith(this);
	}

}
