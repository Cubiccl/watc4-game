package net.watc4.game.entity;

import net.watc4.game.states.GameState;

/** Entity controller by the user */
public abstract class EntityPlayer extends Entity
{
	/** The maximum health of the players. */
	public static final int MAX_HEALTH = 20;

	/** The health points of this Player. */
	protected int health;

	/** Creates a new EntityPlayer
	 * 
	 * @param xPos - Its x position.
	 * @param yPos - Its y position.
	 * @param game - A reference to the GameState. */
	public EntityPlayer(GameState game, float xPos, float yPos)
	{
		super(game, xPos, yPos);
		this.health = MAX_HEALTH;
	}

	/** @return The health points of this Player. */
	public int getHealth()
	{
		return this.health;
	}

}
