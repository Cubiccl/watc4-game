package net.watc4.game.entity;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.watc4.game.map.Chunk;
import net.watc4.game.map.Map;
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
	/** The map these Entities are in. */
	public final Map map;

	public EntityManager(Map map)
	{
		this.map = map;
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
			if (entity2.isSolid || entity.isSolid) return false;
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
		HashSet<Chunk> containers = this.getContainingChunks(entity);
		HashSet<Entity> candidates = new HashSet<Entity>(), colliding = new HashSet<Entity>();
		for (Chunk chunk : containers)
			candidates.addAll(this.division.get(chunk));

		candidates.remove(entity);
		for (Entity candidate : candidates)
			if (candidate.collidesWith(entity, dx, dy)) colliding.add(candidate);
		return colliding.toArray(new Entity[colliding.size()]);
	}

	/** @param entity - The target Entity.
	 * @return The Chunks this Entity occupies. */
	private HashSet<Chunk> getContainingChunks(Entity entity)
	{
		HashSet<Chunk> containers = new HashSet<Chunk>();
		if (!entity.hasMoved)
		{
			for (Chunk chunk : this.division.keySet())
				if (this.division.get(chunk).contains(entity)) containers.add(chunk);

			return containers;
		}
		Chunk current = this.map.getChunk(entity.getX(), entity.getY());
		containers.add(current);
		current = this.map.getChunk(entity.getX() + entity.getWidth(), entity.getY());
		containers.add(current);
		current = this.map.getChunk(entity.getX(), entity.getY() + entity.getHeight());
		containers.add(current);
		current = this.map.getChunk(entity.getX() + entity.getWidth(), entity.getY() + entity.getHeight());
		containers.add(current);
		return containers;
	}

	/** @return The Entity list */
	public ArrayList<Entity> getEntities()
	{
		return this.entities;
	}

	/** @param UUID - The UUID of the target Entity.
	 * @return The Entity with the input UUID. */
	public Entity getEntityByUUID(int UUID)
	{
		for (Entity entity : this.entities)
			if (entity.UUID == UUID) return entity;
		return null;
	}

	/** Adds a Chunk to the Chunk Manager.
	 * 
	 * @param chunk - The new Chunk. */
	public void registerChunk(Chunk chunk)
	{
		this.division.put(chunk, new HashSet<Entity>());
	}

	/** Adds a new Entity to track.
	 * 
	 * @param entity - The new Entity. */
	public void registerEntity(Entity entity)
	{
		this.entities.add(entity);
		if (entity instanceof ILightSource) map.lightManager.add(entity);
		entity.hasMoved = true;
		this.replaceEntity(entity);
	}

	@Override
	public void render(Graphics2D g)
	{
		for (Entity entity : this.entities)
			if (this.shouldRender(entity)) entity.render(g);
	}

	/** Places the Entity in the correct Chunk(s).
	 * 
	 * @param entity - The Entity to place. */
	private void replaceEntity(Entity entity)
	{
		this.clearEntityChunks(entity);
		HashSet<Chunk> chunks = this.getContainingChunks(entity);
		int x, y;
		Chunk c;
		for (Chunk chunk : chunks)
		{
			x = chunk.xPos;
			y = chunk.yPos;
			chunks.add(chunk);
			c = this.map.getChunk(x - Chunk.SIZE, y);
			if (c != null) this.division.get(c).add(entity);
			c = this.map.getChunk(x + Chunk.SIZE, y);
			if (c != null) this.division.get(c).add(entity);
			c = this.map.getChunk(x, y - Chunk.SIZE);
			if (c != null) this.division.get(c).add(entity);
			c = this.map.getChunk(x, y + Chunk.SIZE);
			if (c != null) this.division.get(c).add(entity);
		}
	}

	/** @param entity - The target Entity.
	 * @return True if the target Entity should render (i.e. it belongs to a rendered Chunk) */
	public boolean shouldRender(Entity entity)
	{
		HashSet<Chunk> chunks = this.getContainingChunks(entity);
		for (Chunk chunk : chunks)
			if (chunk.shouldRender()) return true;

		return false;
	}

	/** @param entity - The target Entity.
	 * @return True if the target Entity should update (i.e. it belongs to a updated Chunk) */
	public boolean shouldUpdate(Entity entity)
	{
		HashSet<Chunk> chunks = this.getContainingChunks(entity);
		for (Chunk chunk : chunks)
			if (chunk.shouldUpdate()) return true;

		return false;
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
					if (!testing.colliding.contains(entity.UUID)) testing.onCollisionWith(entity);
					this.colliding.add(entity);
				} else
				{
					if (testing.colliding.contains(entity.UUID)) testing.onCollisionEndWith(entity);
					this.colliding.remove(entity);
				}
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
			if (this.shouldUpdate(entity)) entity.update();

	}

}
