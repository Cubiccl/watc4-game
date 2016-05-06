package net.watc4.game.entity.ai;

import net.watc4.game.entity.Entity;
import net.watc4.game.utils.IUpdate;

/** Artificial Intelligence : Defines the behavior of an Entity. */
public abstract class AI implements IUpdate
{
	/** Allowed distance between the coordinates and the destination, because getting the exact coordinates is almost impossible. */
	public static final int EPSILON = 2;

	/** Coordinates this AI wants to go to. null if doesn't want to move. */
	protected float[] destination;
	/** The Entity to control. */
	protected final Entity entity;

	/** Creates a new AI.
	 * 
	 * @param entity - The Entity to control. */
	public AI(Entity entity)
	{
		this.entity = entity;
	}

	/** Forgets the current destination. */
	public void cancelDestination()
	{
		this.destination = null;
	}

	/** @return True if this Entity has reached its destination. (+- Epsilon, because an exact destination is almost impossible to reach. */
	public boolean destinationReached()
	{
		return Math.abs(this.entity.getX() - this.destination[0]) < EPSILON && Math.abs(this.entity.getY() - this.destination[1]) < EPSILON;
	}

	/** Changes this Entity's destination.
	 * 
	 * @param x - The new X coordinate.
	 * @param y - The new Y coordinate. */
	public void setDestination(float x, float y)
	{
		this.destination = new float[]
		{ x, y };
	}

	@Override
	public void update()
	{}

}
