package net.watc4.game.display;

import java.awt.Rectangle;

import net.watc4.game.entity.Entity;
import net.watc4.game.map.Map;

/** Determines what part of the Map to draw on the Screen. */
public class Camera
{
	public static final int WIDTH = 640, HEIGHT = 480;

	/** The Camera scale. Allows zooming out when Lumi and Pattou are far away from each other. */
	private double scale;
	/** The Dimensions of the Camera. */
	public final int width, height;
	/** The offset of the Camera. Represents how far from the original topleft corner the camera is at. */
	private double xOffset, yOffset;

	public Camera()
	{
		this(WIDTH, HEIGHT);
	}

	public Camera(double xOffset, double yOffset, int width, int height)
	{
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.scale = 1;
	}

	public Camera(int width, int height)
	{
		this(0, 0, width, height);
	}

	private double actualHeight()
	{
		return this.height / this.scale;
	}

	private double actualWidth()
	{
		return this.width / this.scale;
	}

	/** Centers the Camera around these two Entities.
	 * 
	 * @param entity1
	 * @param entity2
	 * @param map - The Map they evolve in. */
	public void centerOn(Entity entity1, Entity entity2, Map map)
	{
		if (entity1 == null)
		{
			this.centerOn(entity2, map);
			return;
		}
		if (entity2 == null)
		{
			this.centerOn(entity1, map);
			return;
		}

		double[] bounds = new double[4];
		bounds[0] = Math.min(entity1.getX(), entity2.getX());
		bounds[1] = Math.min(entity1.getY(), entity2.getY());
		bounds[2] = Math.max(entity1.getX() + entity1.getWidth(), entity2.getX() + entity2.getWidth());
		bounds[3] = Math.max(entity1.getY() + entity1.getHeight(), entity2.getY() + entity2.getHeight());

		double distanceX = bounds[2] - bounds[0] + entity1.getWidth() + entity2.getWidth(), distanceY = bounds[3] - bounds[1] + entity1.getHeight()
				+ entity2.getHeight();
		if (distanceY > this.height)
		{
			this.scale = this.height / distanceY;
		}
		if (distanceX > this.width)
		{
			if (distanceY > this.height) this.scale = Math.min(this.scale, this.width / distanceX);
			else this.scale = this.width / distanceX;
		}

		// double maxX = this.maxXOffset(map), maxY = this.maxYOffset(map);
		if (distanceY <= this.height && distanceX <= this.width) this.scale = 1;

		double middleX = (bounds[0] + bounds[2]) / 2;
		double middleY = (bounds[1] + bounds[3]) / 2;

		this.xOffset = middleX - this.actualWidth() / 2;
		this.yOffset = middleY - this.actualHeight() / 2;

		/* if (this.xOffset < 0) this.xOffset = 0; if (this.yOffset < 0) this.yOffset = 0;
		 * 
		 * if (this.xOffset > maxX) this.xOffset = maxX; if (this.yOffset > maxY) this.yOffset = maxY; */
	}

	/** Centers this Camera on the given Entity.
	 * 
	 * @param entity - The Entity to center on.
	 * @param map - The Map containing the Entity. */
	public void centerOn(Entity entity, Map map)
	{
		if (entity == null) return;
		float[] pos = entity.getCenter();
		this.xOffset = pos[0] - this.actualWidth() / 2;
		this.yOffset = pos[1] - this.actualHeight() / 2;
	}

	public double getHeight()
	{
		return this.height / this.scale;
	}

	/** @return The scale, i.e. how much zoomed out the image is. */
	public double getScale()
	{
		return this.scale;
	}

	public double getWidth()
	{
		return this.width / this.scale;
	}

	/** @return The X Offset, i.e. the horizontal distance from the topleft corner. */
	public double getXOffset()
	{
		return this.xOffset;
	}

	/** @return The Y Offset, i.e. the vertical distance from the topleft corner. */
	public double getYOffset()
	{
		return this.yOffset;
	}

	/** @param map - The Map to draw.
	 * @return The maximum X Offset. */
	public double maxXOffset(Map map)
	{
		return Math.max(0, map.width * Map.TILESIZE - this.actualWidth());
	}

	/** @param map - The Map to draw.
	 * @return The maximum Y Offset. */
	public double maxYOffset(Map map)
	{
		return Math.max(0, map.height * Map.TILESIZE - this.actualHeight());
	}

	public Rectangle rectangle()
	{
		return new Rectangle((int) this.getXOffset(), (int) this.getYOffset(), (int) (this.width / this.scale), (int) (this.height / this.scale));
	}

}
