package net.watc4.game.states;

import java.awt.Graphics;

import net.watc4.game.GameObject;
import net.watc4.game.entity.EntityPlayer;
import net.watc4.game.map.Map;

public class GameState implements GameObject
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

	@Override
	public void render(Graphics g)
	{}

	@Override
	public void update()
	{}
}
