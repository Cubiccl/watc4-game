package net.watc4.game.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import net.watc4.game.map.Map;

/** Registers all Entity types and spawns required Entities. */
public final class EntityRegistry
{

	/** Sorts Entities by ID. */
	private static HashMap<Integer, Class<? extends Entity>> entities;

	/** Creates and registers all Entities. */
	public static void createEntities()
	{
		entities = new HashMap<Integer, Class<? extends Entity>>();
		registerEntity(0, EntityLumi.class);
		registerEntity(1, EntityPattou.class);
		registerEntity(2, EntityBattery.class);
	}

	/** Creates the adequate arguments then spawns an Entity.
	 * 
	 * @param map - The Map to spawn the Entity in.
	 * @param id - The ID of the Entity.
	 * @param values - The data from the map file.
	 * @return The spawned Entity. */
	public static Entity createEntity(Map map, int id, String[] values)
	{
		Object[] arguments = new Object[values.length];
		arguments[0] = map.game;
		arguments[1] = Float.parseFloat(values[1]) * Map.TILESIZE;
		arguments[2] = Float.parseFloat(values[2]) * Map.TILESIZE;
		switch (id)
		{
			case 2: // Battery
				arguments[3] = Integer.parseInt(values[3]);
				arguments[4] = Integer.parseInt(values[4]);
				break;

			default:
				break;
		}
		return spawnEntity(map, id, arguments);
	}

	/** Registers the target Entity.
	 * 
	 * @param id - The ID of the Entity.
	 * @param entityClass - The Class of the Entity to register. */
	private static void registerEntity(int id, Class<? extends Entity> entityClass)
	{
		if (!entities.containsKey(id)) entities.put(id, entityClass);
	}

	/** Spawns an Entity.
	 * 
	 * @param map - The Map to spawn the Entity in.
	 * @param id - The ID of the Entity to spawn.
	 * @param arguments - The Arguments to spawn the Entity. Always start with GameState, xPos, yPos. See theEntity's constructor for additionnal arguments.
	 * @return The spawned Entity. */
	public static Entity spawnEntity(Map map, int id, Object... arguments)
	{
		if (entities.containsKey(id)) try
		{
			Entity entity = (Entity) entities.get(id).getConstructors()[0].newInstance(arguments);
			map.entityManager.registerEntity(entity);
			return entity;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
