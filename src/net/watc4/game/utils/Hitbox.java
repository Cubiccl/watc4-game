package net.watc4.game.utils;

import net.watc4.game.states.GameState;
import static net.watc4.game.map.Map.TILESIZE;

/** */
public class Hitbox
{
	/** height and width of the hitbox (starting from the point (x,y) */
	float height, width;
	/** Coordinates of the starting point of the hitbox */
	float x, y;

	/** @param height of hitbox
	 * @param width of the hitbox
	 * @param x coordinate of the hitbox
	 * @param y coordinate of the hitbox */
	public Hitbox(float height, float width, float x, float y)
	{
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
	}

	public void setPosition(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	/** Check if the top left corner of the hitbox collide with a solid Tile
	 * 
	 * @return true if it collide, false if not */
	public boolean topLeftCornerContact()
	{
		GameState gamestate = GameState.getInstance();
		float halfWidth = this.width / 2, halfHeight = this.height / 2;
		return gamestate.getMap().getTileAt((int) (this.x - halfWidth) / TILESIZE, (int) (this.y - halfHeight) / TILESIZE).isSolid();
	}

	/** Check if the top right corner of the hitbox collide with a solid Tile
	 * 
	 * @return true if it collide, false if not */
	public boolean topRightCornerContact()
	{
		GameState gamestate = GameState.getInstance();
		float halfWidth = this.width / 2, halfHeight = this.height / 2;
		return gamestate.getMap().getTileAt((int) (this.x + halfWidth) / TILESIZE, (int) (this.y - halfHeight) / TILESIZE).isSolid();
	}

	/** Check if the bot left corner of the hitbox collide with a solid Tile
	 * 
	 * @return true if it collide, false if not */
	public boolean botLeftCornerContact()
	{
		GameState gamestate = GameState.getInstance();
		float halfWidth = this.width / 2, halfHeight = this.height / 2;
		return gamestate.getMap().getTileAt((int) (this.x - halfWidth) / TILESIZE, (int) (this.y + halfHeight) / TILESIZE).isSolid();
	}

	/** Check if the bot right corner of the hitbox collide with a solid Tile
	 * 
	 * @return true if it collide, false if not */
	public boolean botRightCornerContact()
	{
		GameState gamestate = GameState.getInstance();
		float halfWidth = this.width / 2, halfHeight = this.height / 2;
		return gamestate.getMap().getTileAt((int) (this.x + halfWidth) / TILESIZE, (int) (this.y + halfHeight) / TILESIZE).isSolid();
	}

	/** Check if both of the top corner collide with a solid tile
	 * 
	 * @return true if it collide, false if not */
	public boolean topContact()
	{
		return topLeftCornerContact() && topRightCornerContact();
	}

	/** Check if both of the left corner collide with a solid tile
	 * 
	 * @return true if it collide, false if not */
	public boolean leftContact()
	{
		return topLeftCornerContact() && botLeftCornerContact();
	}

	/** Check if both of the right corner collide with a solid tile
	 * 
	 * @return true if it collide, false if not */
	public boolean rightContact()
	{
		return topRightCornerContact() && botRightCornerContact();
	}

	/** Check if both of the bot corner collide with a solid tile
	 * 
	 * @return true if it collide, false if not */
	public boolean botContact()
	{
		return botLeftCornerContact() && botRightCornerContact();
	}

	/** test if a hitbox put in argument collides with the hitbox
	 * 
	 * @param hitbox we want to test
	 * @return true if there's a colliding, false if not */
	public boolean collidesWith(Hitbox hitbox)
	{
		return ((hitbox.x <= this.x && this.x <= hitbox.x + hitbox.width) || (hitbox.x <= this.x + this.width && this.x + this.width <= hitbox.x + hitbox.width))
				&& ((hitbox.y <= this.y && this.y <= hitbox.y + hitbox.height) || (hitbox.y <= this.y + this.height && this.y + this.height <= hitbox.y
						+ hitbox.height));

	}

	/** test if a point is contained by the hitbox
	 * 
	 * @param x coordinate of the point
	 * @param y coordinate of the point
	 * @return true if the point is contained, false if not */
	public boolean contains(int x, int y)
	{
		return (this.x <= x && x <= this.x + this.width) && (this.y <= y && y <= this.y + this.height);

	}
}
