package net.watc4.game.entity;

import java.awt.Graphics;
import java.util.ArrayList;

import net.watc4.game.utils.IRender;
import net.watc4.game.utils.IUpdate;

/** Manages all Entities of the Game. */
public class EntityManager implements IRender, IUpdate
{

	/** List of all Entities. */
	private ArrayList<Entity> entities;

	public EntityManager()
	{
		this.entities = new ArrayList<Entity>();
	}

	/** @param entity - The Entity to move.
	 * @param dx - The x offset it will move to.
	 * @param dy - The y offset it will move to.
	 * @return True if the given Entity can move to the given offsets, i.e. there is no solid Entity at the target position. */
	public boolean canEntityMove(Entity entity, float dx, float dy)
	{
		Entity[] colliding = this.getCollisionsWith(entity, dx, dy);
		for (Entity entity2 : colliding)
			if (entity2.isSolid) return false;

		return true;
	}

	/** @param entity - The Entity to test.
	 * @return All Entities that collide with the given Entity. */
	public Entity[] getCollisionsWith(Entity entity)
	{
		return this.getCollisionsWith(entity, 0, 0);
	}

	/** @param entity - The Entity to test.
	 * @param dx - The x offset it will move to.
	 * @param dy - The y offset it will move to.
	 * @return All Entities that would collide with the given Entity if it were to move to the given offsets. */
	public Entity[] getCollisionsWith(Entity entity, float dx, float dy)
	{
		ArrayList<Entity> colliding = new ArrayList<Entity>();
		for (Entity entity2 : this.entities)
		{
			if (entity != entity2 && entity2.collidesWith(entity, dx, dy)) colliding.add(entity2);
		}
		return colliding.toArray(new Entity[colliding.size()]);
	}

	/** Adds a new Entity to track.
	 * 
	 * @param entity - The new Entity. */
	public void registerEntity(Entity entity)
	{
		this.entities.add(entity);
	}

	@Override
	public void render(Graphics g)
	{
		for (Entity entity : this.entities)
			entity.render(g);

	}

	/** Removes a new Entity to track.
	 * 
	 * @param entity - The Entity to remove. */
	public void unregisterEntity(Entity entity)
	{
		this.entities.remove(entity);
	}

	@Override
	public void update()
	{
		ArrayList<Entity> toTest = new ArrayList<Entity>();
		toTest.addAll(this.entities);
		while (!toTest.isEmpty())
		{
			for (int i = 1; i < toTest.size(); ++i)
			{
				if (toTest.get(0).collidesWith(toTest.get(i)))
				{
					toTest.get(0).onCollisionWith(toTest.get(i));
					toTest.get(i).onCollisionWith(toTest.get(0));
				}
			}
			toTest.remove(0);
		}

		for (Entity entity : this.entities)
			entity.update();

	}

}
