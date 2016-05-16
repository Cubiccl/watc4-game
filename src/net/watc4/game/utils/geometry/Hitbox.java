package net.watc4.game.utils.geometry;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

/** Defines a shape to test collisions. <br />
 * WARNING : These work only considering this game. If a complex polygon doesn't work, redo some of this. */
public abstract class Hitbox
{
	/** @return The Orthogonal Projection of C onto AB. */
	public static Point2D horthogonalProjection(Point2D A, Point2D B, Point2D C)
	{
		double xu = B.getX() - A.getX();
		double yu = B.getY() - A.getY();

		double t = (xu * (C.getX() - A.getX()) + yu * (C.getY() - A.getY())) / (xu * xu + yu * yu);
		Point2D h = new Point((int) (A.getX() + t * xu), (int) (A.getY() + t * yu));
		if (!((h.getX() >= A.getX() && h.getX() <= B.getX()) || (h.getX() <= A.getX() && h.getX() >= B.getX()))) return null;
		if (!((h.getY() >= A.getY() && h.getY() <= B.getY()) || (h.getY() <= A.getY() && h.getY() >= B.getY()))) return null;
		return h;
	}

	/** @return True if AB crosses CD. */
	public static boolean segmentsCross(Point2D A, Point2D B, Point2D C, Point2D D)
	{
		double s1_x = B.getX() - A.getX();
		double s1_y = B.getY() - A.getY();
		double s2_x = D.getX() - C.getX();
		double s2_y = D.getY() - C.getY();

		double s = (-s1_y * (A.getX() - C.getX()) + s1_x * (A.getY() - C.getY())) / (-s2_x * s1_y + s1_x * s2_y);
		double t = (s2_x * (A.getY() - C.getY()) - s2_y * (A.getX() - C.getX())) / (-s2_x * s1_y + s1_x * s2_y);

		return s >= 0 && s <= 1 && t >= 0 && t <= 1;
	}

	/** @param hitbox - Another Hitbox.
	 * @return True if the Hitboxes touch each other. */
	protected abstract boolean collidesWith(CircleHitbox hitbox);

	/** @param hitbox - Another Hitbox.
	 * @return True if the Hitboxes touch each other. */
	public boolean collidesWith(Hitbox hitbox)
	{
		if (hitbox instanceof PolygonHitbox) return this.collidesWith((PolygonHitbox) hitbox);
		if (hitbox instanceof CircleHitbox) return this.collidesWith((CircleHitbox) hitbox);
		return false;
	}

	/** @param hitbox - Another Hitbox.
	 * @return True if the Hitboxes touch each other. */
	protected abstract boolean collidesWith(PolygonHitbox hitbox);

	/** @param point- The point.
	 * @return True if this Hitbox contains the Point with he given coordinates. */
	public abstract boolean contains(Point2D point);

	/** Draws this Hitbox.
	 * 
	 * @param g - The Graphics required to draw.
	 * @param color - The Color of the Hitbox. */
	public abstract void render(Graphics g, Color color);

}
