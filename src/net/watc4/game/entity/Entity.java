package net.watc4.game.entity;

import java.awt.Graphics;

import net.watc4.game.GameObject;
import net.watc4.game.GameUtils;
import net.watc4.game.states.GameState;
import static net.watc4.game.GameUtils.DECELERATION;

/** Represents a moving object in the world. i.e. A monster, a moving block, etc. */
public abstract class Entity implements GameObject
{
	/** Reference to the GameState. */
	protected final GameState game;
	/** True if this Entity is affected by Gravity. False if it flies. */
	protected boolean hasGravity;
	/** Its x and y positions. */
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
	}

	/** Applies current speed and modifies it according to the game physics. */
	private void applySpeed()
	{
		this.xPos += this.xSpeed;
		this.yPos += this.ySpeed;
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
			if (this.ySpeed < GameUtils.REAL_MAX_SPEED) this.ySpeed += GameUtils.GRAVITY;
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

	/** @return The X position of this Entity. */
	public float getX()
	{
		return this.xPos;
	}

	/** @return The Y position of this Entity. */
	public float getY()
	{
		return this.yPos;
	}

	/** Destroys this Entity. */
	public void kill()
	{
		this.game.unregisterEntity(this);
	}

	@Override
	public void render(Graphics g)
	{}

	@Override
	public void update()
	{
		this.applySpeed();
	}

}
