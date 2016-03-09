package net.watc4.game.states;

import net.watc4.game.entity.EntityPlayer;
import net.watc4.game.map.Map;

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
}
