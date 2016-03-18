package net.watc4.game.entity;

import java.awt.Graphics;
import java.util.ArrayList;

import net.watc4.game.GameObject;

/** Manages all Entities of the Game. */
public class EntityManager implements GameObject
{

	/** List of all Entities. */
	private ArrayList<Entity> entities;

	public EntityManager()
	{
		this.entities = new ArrayList<Entity>();
	}

	/** Adds a new Entity to track.
	 * 
	 * @param entity - The new Entity. */
	public void registerEntity(Entity entity)
	{
		this.entities.add(entity);
	}

	/** Removes a new Entity to track.
	 * 
	 * @param entity - The Entity to remove. */
	public void unregisterEntity(Entity entity)
	{
		this.entities.remove(entity);
	}

	@Override
	public void render(Graphics g)
	{
		for (Entity entity : this.entities)
			entity.render(g);

	}

	@Override
	public void update()
	{
		for (Entity entity : this.entities)
			entity.update();
	}

}