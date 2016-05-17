package net.watc4.game.entity;

import net.watc4.game.entity.ai.AI;
import net.watc4.game.states.GameState;

/** Entity controller by the user */
public abstract class EntityPlayer extends Entity
{
	public abstract class AIPlayer extends AI
	{
		public AIPlayer(EntityPlayer entity)
		{
			super(entity);
		}

		/** @return True if this Player is moving down. */
		public boolean down()
		{
			if (this.destination != null) return this.destination[1] > this.entity.getY() && Math.abs(this.entity.getY() - this.destination[1]) > EPSILON;
			return false;
		}

		/** @return True if this Player is moving left. */
		public boolean left()
		{
			if (this.destination != null) return this.destination[0] < this.entity.getX() && Math.abs(this.entity.getX() - this.destination[0]) > EPSILON;
			return false;
		}

		/** @return True if this Player is moving right. */
		public boolean right()
		{
			if (this.destination != null) return this.destination[0] > this.entity.getX() && Math.abs(this.entity.getX() - this.destination[0]) > EPSILON;
			return false;
		}

		/** @return True if this Player is moving up. */
		public boolean up()
		{
			if (this.destination != null) return this.destination[1] < this.entity.getY() && Math.abs(this.entity.getY() - this.destination[1]) > EPSILON;
			return false;
		}

	}

	/** The maximum health of the players. */
	public static final int MAX_HEALTH = 20;

	/** The health points of this Player. */
	protected int health;

	/** Creates a new Entity Player without parameters. Useful for the level editor. */
	public EntityPlayer()
	{
		this(null, 0, 0, 0);
	}

	/** Creates a new EntityPlayer
	 * 
	 * @param game - A reference to the GameState.
	 * @param UUID - The Unique Universal IDentifier of this Entity.
	 * @param xPos - Its x position.
	 * @param yPos - Its y position. */
	public EntityPlayer(GameState game, float xPos, float yPos, int UUID)
	{
		super(game, xPos, yPos, UUID);
		this.health = MAX_HEALTH;
	}

	/** @return The health points of this Player. */
	public int getHealth()
	{
		return this.health;
	}

}
