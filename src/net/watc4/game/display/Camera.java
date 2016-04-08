package net.watc4.game.display;

import net.watc4.game.Game;
import net.watc4.game.entity.Entity;
import net.watc4.game.map.Map;

/** Determines what part of the Map to draw on the Screen. */
public class Camera
{
	/** The Dimensions of the Camera. */
	public final int width, height;
	/** The offset of the Camera. Represents how far from the original topleft corner the camera is at. */
	private int xOffset, yOffset;

	public Camera()
	{
		this(Game.getGame().window.canvas.getWidth(), Game.getGame().window.canvas.getHeight());
	}

	public Camera(int width, int height)
	{
		this(0, 0, width, height);
	}

	public Camera(int xOffset, int yOffset, int width, int height)
	{
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
	}

	/** Centers the Camera around these two Entities.
	 * 
	 * @param entity1
	 * @param entity2
	 * @param map - The Map they evolve in. */
	public void centerOn(Entity entity1, Entity entity2, Map map)
	{
		int middleX = (int) ((entity1.getX() + entity1.getHitbox().getWidth() / 2 + entity2.getX() + entity2.getHitbox().getWidth() / 2) / 2);
		int middleY = (int) ((entity1.getY() + entity1.getHitbox().getHeight() / 2 + entity2.getY()) / 2 + entity2.getHitbox().getHeight() / 2);
		int maxX = this.maxXOffset(map), maxY = this.maxYOffset(map);

		this.xOffset = middleX - this.width / 2;
		this.yOffset = middleY - this.height / 2;

		if (this.xOffset < 0) this.xOffset = 0;
		if (this.yOffset < 0) this.yOffset = 0;

		if (this.xOffset > maxX) this.xOffset = maxX;
		if (this.yOffset > maxY) this.yOffset = maxY;
	}

	/** @return The X Offset, i.e. the horizontal distance from the topleft corner. */
	public int getXOffset()
	{
		return this.xOffset;
	}

	/** @return The Y Offset, i.e. the vertical distance from the topleft corner. */
	public int getYOffset()
	{
		return this.yOffset;
	}

	/** @param map - The Map to draw.
	 * @return The maximum X Offset. */
	private int maxXOffset(Map map)
	{
		return Math.max(0, map.width * Map.TILESIZE - this.width);
	}

	/** @param map - The Map to draw.
	 * @return The maximum Y Offset. */
	private int maxYOffset(Map map)
	{
		return Math.max(0, map.height * Map.TILESIZE - this.height);
	}

}
