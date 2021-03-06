package net.watc4.game.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import net.watc4.game.entity.lightEntity.EntityFluorescent;
import net.watc4.game.map.Map;

/** Registers all Entity types and spawns required Entities. */
public final class EntityRegistry
{

	/** Contains description of the Entity's parameters by class. */
	private static HashMap<Class<? extends Entity>, String[]> arguments;

	/** Sorts Entities by ID. */
	private static HashMap<Integer, Class<? extends Entity>> entities;

	/** Creates and registers all Entities. <br/>
	 * The description of boolean parameters needs to be : <br/>
	 * "<b>description</b>"+"<b>\t</b>"+"name for the value <b>0</b>"+"-"+"name for the value <b>1</b>" */
	public static void createEntities()
	{
		entities = new HashMap<Integer, Class<? extends Entity>>();
		arguments = new HashMap<Class<? extends Entity>, String[]>();
		registerEntity(0, EntityLumi.class, new String[]
		{ "X", "int", "Y", "int", "UUID", "unsigned int 000 - 099" });
		registerEntity(1, EntityPattou.class, new String[]
		{ "X", "int", "Y", "int", "UUID", "unsigned int 100 - 199" });
		registerEntity(2, EntityBattery.class, new String[]
		{ "X", "unsigned int", "Y", "unsigned int", "UUID", "unsigned int 200 - 299", "ChargeTime", "float", "UnchargeTime", "float" });
		registerEntity(3, EntityCutscene.class, new String[]
		{ "X", "unsigned int", "Y", "unsigned int", "UUID", "unsigned int 300 - 399", "Tile Width", "unsigned int", "Tile Heigth", "unsigned int",
				"Cutscene Name", "string" });
		registerEntity(4, EntityDoor.class, "X", "unsigned int", "Y", "unsigned int", "UUID", "unsigned int 400 - 499", "Tile Width", "unsigned int",
				"Tile Heigth", "unsigned int");
		registerEntity(5, EntityFluorescent.class, new String[]
		{ "X", "unsigned int", "Y", "unsigned int", "UUID", "unsigned int 500 - 599", "ChargeTime", "float", "UnchargeTime", "float", "Intensity max", "int" });
		registerEntity(6, EntityEndLevel.class, "X", "unsigned int", "Y", "unsigned int", "UUID", "unsigned int 600 - 699", "Tile Width", "unsigned int",
				"Tile Heigth", "unsigned int", "Player	Lumi-Pattou", "boolean");
		registerEntity(7, EntitySpikePlatform.class, "X", "unsigned int", "Y", "unsigned int", "UUID", "unsigned int 700 - 799", "ChargeTime", "float",
				"UnchargeTime", "float");
		registerEntity(8, EntityRunaway.class, "X", "unsigned int", "Y", "unsigned int", "UUID", "unsigned int 800 - 899", "Run X", "unsigned int", "Run Y",
				"unsigned int");
		registerEntity(9, EntityEyes.class, "X", "int", "Y", "int", "UUID", "unsigned int 900 - 999");
		registerEntity(10, EntityHides.class, "X", "int", "Y", "int", "UUID", "unsigned int 1000 - 1099");
		registerEntity(11, EntityVoid.class, "X", "unsigned int", "Y", "unsigned int", "UUID", "unsigned int 800 - 899", "Tile Width", "unsigned int",
				"Tile Height", "unsigned int");
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
		arguments[3] = Integer.parseInt(values[3]);

		Constructor<Entity> constructor = getConstructor(Integer.parseInt(values[0]));

		for (int i = 4; i < arguments.length; i++)
		{
			if (constructor.getParameters()[i].getType().toString().equals("int")) arguments[i] = Integer.parseInt(values[i]);
			else if (constructor.getParameters()[i].getType().toString().equals("float")) arguments[i] = Float.parseFloat(values[i]);
			else if (constructor.getParameters()[i].getType().toString().equals("boolean")) arguments[i] = Boolean.parseBoolean(values[i]);
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
