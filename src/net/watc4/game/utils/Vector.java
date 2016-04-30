package net.watc4.game.utils;

import java.util.HashSet;

import javafx.geometry.Point2D;

/** Represents a Segment. */
public class Vector
{
	/** Its direction. */
	Point2D direction;
	/** Its position. */
	Point2D position;

	/** Creates a new Vector.
	 * 
	 * @param posX - Its x position.
	 * @param posY - Its y position.
	 * @param dirX - Its x direction (width).
	 * @param dirY - Its y direction (height). */
	public Vector(double posX, double posY, double dirX, double dirY)
	{
		this(new Point2D(posX, posY), new Point2D(dirX, dirY));
	}

	/** Creates a new Vector.
	 * 
	 * @param pos - Its position.
	 * @param dir - Its direction. */
	public Vector(Point2D pos, Point2D dir)
	{
		this.position = pos;
		this.direction = dir;
	}

	/** @return This Vector's direction. */
	public Point2D getDirection()
	{
		return this.direction;
	}

	/** @return This begin Vector's position. */
	public Point2D getPosition()
	{
		return this.position;
	}

	/** @param segments - The segments to check.
	 * @return The position at which this Vector intersects one of the input segments, if it does. null if it doesn't. */
	public Point2D intersect(HashSet<Vector> segments)
	{
		double tr = Double.MAX_VALUE;
		for (Vector vector : segments)
		{
			double cartesianProd = (vector.direction.getX() * this.direction.getY()) - (vector.direction.getY() * this.direction.getX());
			if (cartesianProd != 0)
			{
				double ts = (this.direction.getX() * (vector.position.getY() - this.position.getY())
						+ this.direction.getY() * (this.position.getX() - vector.position.getX())) / cartesianProd;
				if (ts >= 0 && ts <= 1)
				{
					double tempTr = (vector.position.getX() + vector.direction.getX() * ts - this.position.getX()) / this.direction.getX();
					if (tempTr > 0 && tempTr < tr) tr = tempTr;
				}
			}
		}
		if (tr != Double.MAX_VALUE) return new Point2D(this.position.getX() + tr * this.direction.getX(), this.position.getY() + tr * this.direction.getY());
		return null;
	}

	public HashSet<Point2D> allIntersection(HashSet<Vector> segments)
	{
		HashSet<Point2D> res = new HashSet<>();
		res.add(this.position);
		double tr;
		for (Vector vector : segments)
		{
			double cartesianProd = (vector.direction.getX() * this.direction.getY()) - (vector.direction.getY() * this.direction.getX());
			if (cartesianProd != 0)
			{
				double ts = (this.direction.getX() * (vector.position.getY() - this.position.getY())
						+ this.direction.getY() * (this.position.getX() - vector.position.getX())) / cartesianProd;
				if (ts >= 0 && ts <= 1)
				{
					tr = (vector.position.getX() + vector.direction.getX() * ts - this.position.getX()) / this.direction.getX();
					if (tr >= 0 && tr <= 1)
						res.add(new Point2D(this.position.getX() + tr * this.direction.getX(), this.position.getY() + tr * this.direction.getY()));
				}
			}
		}
		return res;
	}

	/** Changes this Vector's direction.
	 * 
	 * @param p - The new direction to apply. */
	public void setDirection(Point2D p)
	{
		this.direction = p;
	}

	/** Changes this Vector's position.
	 * 
	 * @param p - The new position to apply. */
	public void setPosition(Point2D p)
	{
		this.position = p;
	}

}
