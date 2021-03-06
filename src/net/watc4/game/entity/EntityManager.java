package net.watc4.game.entity;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.watc4.game.listener.IEntityMovementListener;
import net.watc4.game.map.Chunk;
import net.watc4.game.map.Map;
import net.watc4.game.utils.IRender;
import net.watc4.game.utils.IUpdate;

/** Manages all Entities of the Game. */
public class EntityManager implements IRender, IUpdate, IEntityMovementListener
{

	/** Divides Entities into the Chunks containing them. */
	private HashMap<Chunk, HashSet<Entity>> division;
	/** List of all Entities. */
	private ArrayList<Entity> entities;
	/** The map these Entities are in. */
	public final Map map;
	private HashSet<Entity> toDelete;

	public EntityManager(Map map)
	{
		this.map = map;
		this.entities = new ArrayList<Entity>();
		this.division = new HashMap<Chunk, HashSet<Entity>>();
		this.toDelete = new HashSet<Entity>();
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

	/** Calculates the new Chunks for the given Entity.
	 * 
	 * @param entity - The target Entity.
	 * @return The Chunks this Entity occupies. */
	private HashSet<Chunk> generateContainingChunks(Entity entity)
	{
		HashSet<Chunk> containers = new HashSet<Chunk>();
		Chunk current = this.map.getChunk(entity.getX(), entity.getY());
		if (current != null) containers.add(current);
		current = this.map.getChunk(entity.getX() + entity.getWidth(), entity.getY());
		if (current != null) containers.add(current);
		current = this.map.getChunk(entity.getX(), entity.getY() + entity.getHeight());
		if (current != null) containers.add(current);
		current = this.map.getChunk(entity.getX() + entity.getWidth(), entity.getY() + entity.getHeight());
		if (current != null) containers.add(current);
		return containers;
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
		HashSet<Chunk> chunks = new HashSet<Chunk>();
		for (Chunk chunk : this.division.keySet())
			if (this.division.get(chunk).contains(entity)) chunks.add(chunk);

		return chunks;
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

	@Override
	public void onEntityMove(Entity entity)
	{
		this.replaceEntity(entity);
		this.testForCollisions(entity);
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
		if (entity == null) return;
		this.entities.add(entity);
		if (entity instanceof ILightSource) map.lightManager.add(entity);
		entity.addMovementListener(this);
		this.onEntityMove(entity);
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
		HashSet<Chunk> chunks = this.generateContainingChunks(entity);
		int x, y;
		Chunk c;
		for (Chunk chunk : chunks)
		{
			if (chunk == null) entity.kill();
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

	/** Tests for and applies collisions on the given Entity.
	 * 
	 * @param entity - The Entity to test. */
	private void testForCollisions(Entity entity)
	{
		HashSet<Entity> toTest = new HashSet<Entity>();
		for (Chunk chunk : this.getContainingChunks(entity))
			for (Entity candidate : this.division.get(chunk))
				toTest.add(candidate);

		for (Entity candidate : toTest)
		{
			if (entity == candidate) continue;
			if (candidate.collidesWith(entity))
			{
				if (!candidate.colliding.contains(entity.UUID))
				{
					candidate.onCollisionWith(entity);
					entity.onCollisionWith(candidate);
				}
			} else
			{
				if (candidate.colliding.contains(entity.UUID))
				{
					candidate.onCollisionEndWith(entity);
					entity.onCollisionEndWith(candidate);
				}
			}
		}

	}

	/** Removes a new Entity to track.
	 * 
	 * @param entity - The Entity to remove. */
	public void unregisterEntity(Entity entity)
	{
		this.toDelete.add(entity);
	}

	@Override
	public void update()
	{
		for (Entity entity : this.entities)
			if (this.shouldUpdate(entity)) entity.update();
		
		for (Entity entity : this.toDelete)
		{
			this.entities.remove(entity);
			this.clearEntityChunks(entity);
		}
	}

	public void renderOverShadow(Graphics2D g)
	{
		for (Entity entity : this.entities)
		{
			if (entity.overShadow) entity.render(g);
		}
	}

}
