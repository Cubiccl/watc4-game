package net.watc4.game.entity;

import java.awt.Graphics;

import net.watc4.game.states.GameState;

/** Entity controller by the user */
public class EntityPlayer extends Entity
{
	/** The health points of this Player. */
	protected int health;

	/** Creates a new EntityPlayer
	 * 
	 * @param xPos - Its x position.
	 * @param yPos - Its y position.
	 * @param game - A reference to the GameState. */
	public EntityPlayer(float xPos, float yPos, GameState game)
	{
		super(xPos, yPos, game);
		this.health = 20;
	}

	@Override
	public void render(Graphics g)
	{
		super.render(g);
	}

	@Override
	public void update()
	{
		super.update();
	}

}
