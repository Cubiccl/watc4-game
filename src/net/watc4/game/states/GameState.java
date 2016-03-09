package net.watc4.game.states;

import java.awt.Graphics;

import net.watc4.game.GameObject;
import net.watc4.game.entity.EntityPlayer;
import net.watc4.game.map.Map;

public class GameState implements GameObject
/** Represents the main game engine. */
public class GameState
{
	/** The Light Player. */
	private EntityPlayer entityLumi;
	/** The Shadow Player. */
	private EntityPlayer entityPattou;
	/** The world they evolve into. */
	private Map map;

	/** Creates the GameState. */
	public GameState()
	{
		this.entityLumi = new EntityPlayer();
		this.entityPattou = new EntityPlayer();
		this.map = new Map();
	}

	/** @return The <code>Map</code>. */
	public Map getMap()
	{
		return this.map;
	}

	@Override
	public void render(Graphics g)
	{}

	@Override
	public void update()
	{}
}
