package net.watc4.game.utils;

import java.util.HashSet;
import java.util.Iterator;

import javafx.geometry.Point2D;

public class Vector
{
	Point2D direction;
	Point2D position;

	public Vector(Point2D pos, Point2D dir)
	{
		this.position = pos;
		this.direction = dir;
	}

	public Vector(double posX, double posY, double dirX, double dirY)
	{
		position = new Point2D(posX, posY);
		direction = new Point2D(dirX, dirY);
	}

	public Point2D getDirection()
	{
		return direction;
	}

	public Point2D getPosition()
	{
		return position;
	}

	public Point2D intersect(HashSet<Vector> vectors)
	{
		Iterator<Vector> it = vectors.iterator();
		double tr = Double.MAX_VALUE;
		while (it.hasNext())
		{
			Vector vector = it.next();
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

	public void setDirection(Point2D p)
	{
		this.direction = p;
	}

	public void setPosition(Point2D p)
	{
		this.position = p;
	}

}
