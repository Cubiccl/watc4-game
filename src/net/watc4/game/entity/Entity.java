package net.watc4.game.entity;

import java.awt.Graphics;

import net.watc4.game.display.renderer.EntityRenderer;
import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.IRender;
import net.watc4.game.utils.IUpdate;

/** Represents a moving object in the world. i.e. A monster, a moving block, etc. */
public abstract class Entity implements IRender, IUpdate
{
	public static final float DEFAULT_SIZE = 32;

	private static final float GRAVITY = 0.4f;
	/** The direction that the entity is facing 1:Rigth, -1:Left */
	protected int direction;
	/** Reference to the GameState. */
	public final GameState game;
	/** True if this Entity is affected by Gravity. False if it flies. */
	protected boolean hasGravity;
	/** Renders the Entity onto the screen. */
	private EntityRenderer renderer;
	/** The width/height of the entity. */
	protected int width, height;
	/** Its x and y positions. Topleft of the Entity. */
	private float xPos, yPos;
	/** Its x and y speed. */
	protected float xSpeed, ySpeed;

	/** Creates a new Entity.
	 * 
	 * @param xPos - Its x position.
	 * @param yPos - Its y position.
	 * @param game - A reference to the GameState. */
	public Entity(float xPos, float yPos, GameState game)
	{
		this.xPos = xPos;
		this.yPos = yPos;
		this.xSpeed = 0;
		this.ySpeed = 0;
		this.hasGravity = true;
		this.game = game;
		this.game.entityManager.registerEntity(this);
		this.renderer = new EntityRenderer(this);
		this.width = (int) DEFAULT_SIZE;
		this.height = (int) DEFAULT_SIZE;
	}

	/** test if a hitbox put in argument collides with the hitbox
	 * 
	 * @param hitbox we want to test
	 * @return true if there's a colliding, false if not */
	public boolean collidesWith(Entity entity)
	{
		return ((entity.xPos <= this.xPos && this.xPos <= entity.xPos + entity.width) || (entity.xPos <= this.xPos + this.width && this.xPos + this.width <= entity.xPos
				+ entity.width))
				&& ((entity.yPos <= this.yPos && this.yPos <= entity.yPos + entity.height) || (entity.yPos <= this.yPos + this.height && this.yPos
						+ this.height <= entity.yPos + entity.height));

	}

	/** test if a point is contained by the hitbox
	 * 
	 * @param x coordinate of the point
	 * @param y coordinate of the point
	 * @return true if the point is contained, false if not */
	public boolean contains(int x, int y)
	{
		return (this.xPos <= x && x <= this.xPos + this.width) && (this.yPos <= y && y <= this.yPos + this.height);

	}

	public int getDirection()
	{
		return this.direction;
	}

	public float getHeight()
	{
		return this.height;
	}

	public float getWidth()
	{
		return this.width;
	}

	/** @return The X position of this Entity. */
	public float getX()
	{
		return this.xPos;
	}

	/** @return This Entity's horizontal speed. */
	public float getXSpeed()
	{
		return this.xSpeed;
	}

	/** @return The Y position of this Entity. */
	public float getY()
	{
		return this.yPos;
	}

	/** @return This Entity's vertical speed. */
	public float getYSpeed()
	{
		return this.ySpeed;
	}

	/** Destroys this Entity. */
	public void kill()
	{
		this.game.entityManager.unregisterEntity(this);
	}

	/** @param dx - delta x
	 * @param dy - delta y
	 * @return true if the entity doesn't collide with a solid tile at {@linkplain Entity#xPos xPos} + dx, {@linkplain Entity#yPos yPos} + dy */
	public boolean placeFree(float dx, float dy)
	{
		int tileXStart = (int) ((xPos + dx) / Map.TILESIZE), tileYStart = (int) ((yPos + dy) / Map.TILESIZE);
		int tileXEnd = (int) ((xPos + dx + width - 1) / Map.TILESIZE + 1);
		int tileYEnd = (int) ((yPos + dy + height - 1) / Map.TILESIZE + 1);
		for (int x = tileXStart; x < tileXEnd; ++x)
		{
			for (int y = tileYStart; y < tileYEnd; ++y)
				if (game.getMap().getTileAt(x, y).isSolid) return false;
		}
		return true;
	}

	@Override
	public void render(Graphics g)
	{
		this.renderer.render(g);
	}

	public void setPosition(int x, int y)
	{
		this.xSpeed = 0;
		this.ySpeed = 0;
		this.xPos = x;
		this.yPos = y;
	}

	/** Changes this Entity's renderer.
	 * 
	 * @param renderer - The new Renderer to use. */
	public void setRenderer(EntityRenderer renderer)
	{
		this.renderer.setAnimation(null);
		this.renderer = renderer;
	}

	@Override
	public void update()
	{
		// this.testForCollisions();

		if (hasGravity)
		{
			ySpeed += GRAVITY;
		}

		if (!placeFree(xSpeed, 0))
		{
			while (placeFree(Math.signum(xSpeed), 0))
				xPos += Math.signum(xSpeed);
			xSpeed = 0;
		}

		if (!placeFree(0, ySpeed))
		{
			while (placeFree(0, Math.signum(ySpeed)))
				yPos += Math.signum(ySpeed);
			ySpeed = 0;
		}
		xPos += xSpeed;
		yPos += ySpeed;
	}
}
