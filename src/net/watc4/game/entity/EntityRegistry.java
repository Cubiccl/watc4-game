package net.watc4.game.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import net.watc4.game.map.Map;

/** Registers all Entity types and spawns required Entities. */
public final class EntityRegistry
{

	/** Contains description of the Entity's parameters by class. */
	private static HashMap<Class<? extends Entity>, String[]> arguments;

	/** Sorts Entities by ID. */
	private static HashMap<Integer, Class<? extends Entity>> entities;

	/** Creates and registers all Entities. */
	public static void createEntities()
	{
		entities = new HashMap<Integer, Class<? extends Entity>>();
		arguments = new HashMap<Class<? extends Entity>, String[]>();
		registerEntity(0, EntityLumi.class, new String[]
		{ "UUID", "unsigned int", "X", "unsigned int", "Y", "unsigned int" });
		registerEntity(1, EntityPattou.class, new String[]
		{ "UUID", "unsigned int", "X", "unsigned int", "Y", "unsigned int" });
		registerEntity(2, EntityBattery.class, new String[]
		{ "UUID", "unsigned int", "X", "unsigned int", "Y", "unsigned int", "Buffer", "unsigned int", "Max Power", "unsigned int" });
		registerEntity(3, EntityCutscene.class, new String[]
		{ "UUID", "unsigned int", "X", "unsigned int", "Y", "unsigned int", "Tile Width", "unsigned int", "Tile Heigth", "unsigned int", "Cutscene Name",
				"string" });
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
		arguments[1] = Integer.parseInt(values[1]);
		arguments[2] = Float.parseFloat(values[2]) * Map.TILESIZE;
		arguments[3] = Float.parseFloat(values[3]) * Map.TILESIZE;

		Constructor<Entity> constructor = getConstructor(Integer.parseInt(values[0]));

		for (int i = 4; i < arguments.length; i++)
		{
			if (constructor.getParameters()[i].getType().toString().equals("int")) arguments[i] = Integer.parseInt(values[i]);
			else if (constructor.getParameters()[i].getType().toString().equals("float")) arguments[i] = Float.parseFloat(values[i]);
			else arguments[i] = values[i];
		}

		return spawnEntity(map, id, arguments);
	}

	/** @param id - The ID of the Entity.
	 * @return The Constructor for the given Entity. */
	@SuppressWarnings("unchecked")
	public static Constructor<Entity> getConstructor(int id)
	{
		return (Constructor<Entity>) entities.get(id).getConstructors()[1];
	}

	/** @param id - The ID of the Entity.
	 * @return The default Constructor (without arguments) for the given Entity. */
	@SuppressWarnings("unchecked")
	public static Constructor<Entity> getDefaultConstructor(int id)
	{
		return (Constructor<Entity>) entities.get(id).getConstructors()[0];
	}

	/** @return The list of Entity definitions sorted by ID. (0 -> size-1) */
	public static HashMap<Class<? extends Entity>, String[]> getDefinitions()
	{
		return arguments;
	}

	/** @return The list of Entity types, sorted by ID. (0 -> size-1) */
	public static HashMap<Integer, Class<? extends Entity>> getEntities()
	{
		return entities;
	}

	/** Registers the target Entity.
	 * 
	 * @param id - The ID of the Entity.
	 * @param entityClass - The Class of the Entity to register.
	 * @param parameters - Parameters to input when creating an Entity in the Editor. */
	private static void registerEntity(int id, Class<? extends Entity> entityClass, String... parameters)
	{
		if (!entities.containsKey(id)) entities.put(id, entityClass);
		if (!arguments.containsKey(entityClass)) arguments.put(entityClass, parameters);
	}

	/** Spawns an Entity.
	 * 
	 * @param map - The Map to spawn the Entity in.
	 * @param id - The ID of the Entity to spawn.
	 * @param arguments - The Arguments to spawn the Entity. Always start with UUID, GameState, xPos, yPos. See theEntity's constructor for additionnal arguments.
	 * @return The spawned Entity. */
	public static Entity spawnEntity(Map map, int id, Object... arguments)
	{
		if (entities.containsKey(id)) try
		{
			Entity entity = null;
			for (@SuppressWarnings("rawtypes")
			Constructor constructor : entities.get(id).getConstructors())
				if (constructor.getParameterCount() == arguments.length) entity = (Entity) constructor.newInstance(arguments);
			map.entityManager.registerEntity(entity);
			return entity;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
