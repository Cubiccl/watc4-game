package net.watc4.game.entity;

import java.awt.Graphics;

import net.watc4.game.GameObject;
import net.watc4.game.states.GameState;

public abstract class Entity implements GameObject
{
	protected GameState game;
	protected int xPos, yPos;

	public Entity(int xPos, int yPos, GameState game)
	{
		this.xPos = xPos;
		this.yPos = yPos;
		game.registerEntity(this);
	}

	public int getX()
	{
		return this.xPos;
	}

	public int getY()
	{
		return this.yPos;
	}

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
