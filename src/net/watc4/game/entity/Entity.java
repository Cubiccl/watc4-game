package net.watc4.game.entity;

import static net.watc4.game.GameUtils.DECELERATION;

import java.awt.Graphics;

import net.watc4.game.GameObject;
import net.watc4.game.GameUtils;
import net.watc4.game.display.renderer.EntityRenderer;
import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.Hitbox;

/** Represents a moving object in the world. i.e. A monster, a moving block, etc. */
public abstract class Entity implements GameObject
{
	public static final float DEFAULT_SIZE = 32;

	/** Reference to the GameState. */
	protected final GameState game;
	/** True if this Entity is affected by Gravity. False if it flies. */
	protected boolean hasGravity;
	protected Hitbox hitbox;
	/** True if the Entity is standing on the Ground. */
	protected boolean onGround;
	/** Renders the Entity onto the screen. */
	private EntityRenderer renderer;
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
		game.registerEntity(this);
		this.renderer = new EntityRenderer(this);
		this.hitbox = new Hitbox(DEFAULT_SIZE, DEFAULT_SIZE, this.xPos, this.yPos);
	}

	/** Applies current speed and modifies it according to the game physics. */
	private void applySpeed()
	{
		this.testForCollisions();
		this.hitbox.setPosition(this.xPos, this.yPos);

		if (this.xSpeed > 0)
		{
			this.xSpeed -= DECELERATION;
			if (this.xSpeed < 0) this.xSpeed = 0;
		}
		if (this.xSpeed < 0)
		{
			this.xSpeed += DECELERATION;
			if (this.xSpeed > 0) this.xSpeed = 0;
		}

		if (this.hasGravity)
		{
			this.ySpeed += GameUtils.GRAVITY;
			if (this.ySpeed > GameUtils.REAL_MAX_SPEED) this.ySpeed = GameUtils.REAL_MAX_SPEED;
		} else
		{
			if (this.ySpeed > 0)
			{
				this.ySpeed -= DECELERATION;
				if (this.ySpeed < 0) this.ySpeed = 0;
			}
			if (this.ySpeed < 0)
			{
				this.ySpeed += DECELERATION;
				if (this.ySpeed > 0) this.ySpeed = 0;
			}
		}
	}

	/** @return The Hitbox of this Entity. */
	public Hitbox getHitbox()
	{
		return this.hitbox;
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
		this.game.unregisterEntity(this);
	}

	/** @return true if the entity is on the ground, false if not */
	public boolean onGround()
	{
		return this.onGround;
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

	/** Tests if the Entity collides with a Tile and applies the collision. */
	private void testForCollisions()
	{
		float newX = this.xPos + this.xSpeed;
		float newY = this.yPos + this.ySpeed;

		int[] collision = GameState.getInstance().getMap().detectCollision(this, newX, this.yPos);
		if (collision != null)
		{
			if (this.xSpeed > 0) this.xPos = collision[0] * Map.TILESIZE - this.getHitbox().getWidth();
			else if (this.xSpeed < 0) this.xPos = (collision[0] + 1) * Map.TILESIZE;
			this.xSpeed = 0;
		} else this.xPos = newX;

		collision = GameState.getInstance().getMap().detectCollision(this, this.xPos, newY);
		if (collision != null)
		{
			if (this.ySpeed > 0)
			{
				this.yPos = collision[1] * Map.TILESIZE - this.getHitbox().getHeight();
				this.onGround = true;
			} else if (this.ySpeed < 0) this.yPos = (collision[1] + 1) * Map.TILESIZE;
			this.ySpeed = 0;
		} else this.yPos = newY;
	}

	@Override
	public void update()
	{
		this.applySpeed();
	}

}
