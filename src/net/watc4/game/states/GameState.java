package net.watc4.game.states;

import net.watc4.game.entity.EntityPlayer;
import net.watc4.game.map.Map;

public class GameState
{
	private EntityPlayer entityLumi;
	private EntityPlayer entityPattou;
	private Map map;

	public GameState()
	{
		this.entityLumi = new EntityPlayer();
		this.entityPattou = new EntityPlayer();
		this.map = new Map();
	}

	public Map getMap()
	{
		return this.map;
	}
}
