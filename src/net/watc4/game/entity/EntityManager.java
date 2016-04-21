package net.watc4.game.entity;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.watc4.game.map.Chunk;
import net.watc4.game.utils.IRender;
import net.watc4.game.utils.IUpdate;

/** Manages all Entities of the Game. */
public class EntityManager implements IRender, IUpdate
{

	/** List of Entities that currently collide. */
	private HashSet<Entity> colliding;
	/** Divides Entities into the Chunks containing them. */
	private HashMap<Chunk, HashSet<Entity>> division;
	/** List of all Entities. */
	private ArrayList<Entity> entities;

	public EntityManager()
	{
		this.entities = new ArrayList<Entity>();
		this.division = new HashMap<Chunk, HashSet<Entity>>();
		this.colliding = new HashSet<Entity>();
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

	/** Removes the Entity from the Chunk division.
	 * 
	 * @param entity - The Entity to remove. */
	private void clearEntityChunks(Entity entity)
	{
		for (Chunk chunk : this.division.keySet())
			this.division.get(chunk).remove(entity);
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
		Chunk[] chunks = entity.getChunks();
		HashSet<Entity> candidates = new HashSet<Entity>(), colliding = new HashSet<Entity>();
		for (Chunk chunk : chunks)
			candidates.addAll(this.division.get(chunk));

		candidates.remove(entity);
		for (Entity candidate : candidates)
			if (candidate.collidesWith(entity, dx, dy)) colliding.add(candidate);

		return colliding.toArray(new Entity[colliding.size()]);
	}

	/** Adds a new Entity to track.
	 * 
	 * @param entity - The new Entity. */
	public void registerEntity(Entity entity)
	{
		this.entities.add(entity);
		this.replaceEntity(entity);
	}

	@Override
	public void render(Graphics g)
	{
		for (Entity entity : this.entities)
			if (entity.shouldRender()) entity.render(g);
	}

	/** Places the Entity in the correct Chunk(s).
	 * 
	 * @param entity - The Entity to place. */
	private void replaceEntity(Entity entity)
	{
		this.clearEntityChunks(entity);
		for (Chunk chunk : entity.getChunks())
		{
			if (!this.division.containsKey(chunk)) this.division.put(chunk, new HashSet<Entity>());
			this.division.get(chunk).add(entity);
		}
	}

	/** Tests for and applies collisions between the given Entities.
	 * 
	 * @param entities - The Entities to test for. */
	private void testForCollisions(HashSet<Entity> entities)
	{
		HashSet<Entity> toTest = new HashSet<Entity>();
		for (Entity entity : entities)
			if (entity.hasMoved || this.colliding.contains(entity)) toTest.add(entity);

		for (Entity testing : toTest)
			for (Entity entity : entities)
			{
				if (entity == testing) continue;
				if (testing.collidesWith(entity))
				{
					testing.onCollisionWith(entity);
					this.colliding.add(entity);
				} else this.colliding.remove(entity);
			}

		for (Entity entity : toTest)
			entity.hasMoved = false;
	}

	/** Removes a new Entity to track.
	 * 
	 * @param entity - The Entity to remove. */
	public void unregisterEntity(Entity entity)
	{
		this.entities.remove(entity);
		this.clearEntityChunks(entity);
	}

	@Override
	public void update()
	{
		for (Entity entity : this.entities)
			if (entity.hasMoved) this.replaceEntity(entity);

		for (Chunk chunk : this.division.keySet())
			this.testForCollisions(this.division.get(chunk));

		for (Entity entity : this.entities)
			if (entity.shouldUpdate()) entity.update();

	}

}
