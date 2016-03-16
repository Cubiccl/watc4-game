package net.watc4.game.entity;

import java.awt.Graphics;

import net.watc4.game.GameObject;
import net.watc4.game.states.GameState;

/** Represents a moving object in the world. i.e. A monster, a moving block, etc. */
public abstract class Entity implements GameObject
{
	/** Reference to the GameState. */
	protected GameState game;
	/** Its x and y positions. */
	protected int xPos, yPos;

	/** Creates a new Entity.
	 * 
	 * @param xPos - Its x position.
	 * @param yPos - Its y position.
	 * @param game - A reference to the GameState. */
	public Entity(int xPos, int yPos, GameState game)
	{
		this.xPos = xPos;
		this.yPos = yPos;
		game.registerEntity(this);
	}

	/** @return The X position of this Entity. */
	public int getX()
	{
		return this.xPos;
	}

	/** @return The Y position of this Entity. */
	public int getY()
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
	{}

}
