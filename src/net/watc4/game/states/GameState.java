package net.watc4.game.states;

import java.awt.Graphics;

import net.watc4.game.GameObject;
import net.watc4.game.entity.Entity;
import net.watc4.game.entity.EntityManager;
import net.watc4.game.entity.EntityPlayer;
import net.watc4.game.map.Map;
import net.watc4.game.utils.FileUtils;

/** Represents the main game engine. */
public class GameState implements GameObject
{
	/** The Light Player. */
	private EntityPlayer entityLumi;
	/**
	 * 
	 */
	EntityManager entityManager;
	/** The Shadow Player. */
	private EntityPlayer entityPattou;
	/** The world they evolve into. */
	private Map map;

	/** Creates the GameState. */
	public GameState()
	{
		this.entityManager = new EntityManager();
		this.map = FileUtils.createMap("res/maps/testmap.txt");
		this.entityLumi = new EntityPlayer(this.map.lumiSpawnX, this.map.lumiSpawnY, this);
		this.entityPattou = new EntityPlayer(this.map.pattouSpawnX, this.map.pattouSpawnY, this);
	}

	/** @return The <code>Map</code>. */
	public Map getMap()
	{
		return this.map;
	}

	public void registerEntity(Entity animation)
	{
		this.entityManager.registerEntity(animation);
	}

	@Override
	public void render(Graphics g)
	{
		this.map.render(g);
		this.entityManager.render(g);
	}

	public void unregisterEntity(Entity animation)
	{
		this.entityManager.unregisterEntity(animation);
	}

	@Override
	public void update()
	{
		this.map.update();
		this.entityManager.update();
	}
}
