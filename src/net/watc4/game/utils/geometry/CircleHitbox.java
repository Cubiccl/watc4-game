package net.watc4.game.utils.geometry;

import java.awt.Color;
import java.awt.Graphics;
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
	protected boolean collidesWith(CircleHitbox hitbox)
	{
		return this.center.distance(hitbox.center) <= this.radius + hitbox.radius;
	}

	@Override
	protected boolean collidesWith(PolygonHitbox hitbox)
	{
		return hitbox.collidesWith(this);
	}

	@Override
	public boolean contains(Point2D point)
	{
		return this.center.distance(point) <= this.radius;
	}

	@Override
	public void render(Graphics g, Color color)
	{
		g.setColor(color);
		g.drawOval((int) (this.center.getX() - this.radius), (int) (this.center.getY() - this.radius), (int) this.radius * 2, (int) this.radius * 2);
	}

}
