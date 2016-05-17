package net.watc4.game.utils.geometry;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class PolygonHitbox extends Hitbox
{
	public final Point2D[] vertices;

	public PolygonHitbox(Point2D... vertices)
	{
		this.vertices = vertices;
	}

	@Override
	protected boolean collidesWith(CircleHitbox hitbox)
	{
		Point2D a, b;
		for (int i = 0; i < this.vertices.length; ++i)
		{
			a = this.vertices[i];
			if (hitbox.contains(a)) return true;
			if (i == this.vertices.length - 1) b = this.vertices[0];
			else b = this.vertices[i + 1];
			Point2D h = horthogonalProjection(a, b, hitbox.center);
			if (h != null && hitbox.contains(h)) return true;
		}

		if (this.contains(hitbox.center)) return true;
		return false;
	}

	@Override
	protected boolean collidesWith(PolygonHitbox hitbox)
	{
		if (this.vertices.length > hitbox.vertices.length) return hitbox.collidesWith(this);

		Point2D a, b, c, d;
		for (int i = 0; i < this.vertices.length; ++i)
		{
			a = this.vertices[i];
			if (i == this.vertices.length - 1) b = this.vertices[0];
			else b = this.vertices[i + 1];
			for (int j = 0; j < hitbox.vertices.length; ++j)
			{
				c = hitbox.vertices[j];
				if (j == hitbox.vertices.length - 1) d = hitbox.vertices[0];
				else d = hitbox.vertices[j + 1];
				if (segmentsCross(a, b, c, d)) return true;
			}
		}

		return false;
	}

	@Override
	public boolean contains(Point2D point)
	{
		Point2D a, b;
		for (int i = 0; i < this.vertices.length; ++i)
		{
			a = this.vertices[i];
			if (i == this.vertices.length - 1) b = this.vertices[0];
			else b = this.vertices[i + 1];
			if ((b.getX() - a.getX()) * (point.getY() - a.getY()) - (b.getY() - a.getY()) * (point.getX() - a.getX()) > 0) return false;
		}
		return true;
	}

	@Override
	public void render(Graphics g, Color color)
	{
		g.setColor(color);
		Point2D a, b;
		for (int i = 0; i < this.vertices.length; ++i)
		{
			a = this.vertices[i];
			if (i == this.vertices.length - 1) b = this.vertices[0];
			else b = this.vertices[i + 1];
			g.drawLine((int) a.getX(), (int) a.getY(), (int) b.getX(), (int) b.getY());
		}
	}

}
