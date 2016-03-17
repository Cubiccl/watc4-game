package net.watc4.game.utils;

/** */
public class Hitbox
{
	/** height and width of the hitbox (starting from the point (x,y)*/
	int height, width;
	/** Coordinates of the starting point of the hitbox*/
	int x, y;

	/** 
	 * @param height of hitbox
	 * @param width of the hitbox
	 * @param x coordinate of the hitbox
	 * @param y coordinate of the hitbox*/
	Hitbox(int height, int width, int x, int y)
	{
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
	}

	/** test if a hitbox put in argument collides with the hitbox 
	 * @param hitbox we want to test
	 * @return true if there's a colliding, false if not */
	public boolean collidesWith(Hitbox hitbox)
	{
		// don't ctrl+shift+f, it's nicer like this
		// JYA would be pride
		return ((hitbox.x <= this.x && this.x <= hitbox.x + hitbox.width)
				|| (hitbox.x <= this.x + this.width && this.x + this.width <= hitbox.x + hitbox.width))
				&& ((hitbox.y <= this.y && this.y <= hitbox.y + hitbox.height)
				|| (hitbox.y <= this.y + this.height && this.y + this.height <= hitbox.y + hitbox.height));

	}

	/** test if a point is contained by the hitbox
	 * @param x coordinate of the point
	 * @param y coordinate of the point
	 * @return true if the point is contained, false if not*/
	public boolean contains(int x, int y)
	{
		return (this.x <= x && x<= this.x+this.width) && (this.y <= y && y <= this.y+this.height);

	}
}
