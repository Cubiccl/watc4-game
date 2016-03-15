package net.watc4.game.entity;

import java.awt.Graphics;
import java.util.ArrayList;

import net.watc4.game.GameObject;

public class EntityManager implements GameObject
{

	private ArrayList<Entity> entities;

	public EntityManager()
	{
		this.entities = new ArrayList<Entity>();
	}

	public void registerEntity(Entity animation)
	{
		this.entities.add(animation);
	}

	public void unregisterEntity(Entity animation)
	{
		this.entities.remove(animation);
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
