package net.watc4.game.utils.lore;

import java.util.HashMap;
import java.util.HashSet;

import net.watc4.game.states.GameState;
import net.watc4.game.utils.FileUtils;

/** Manages the links between maps, menus & cutscenes. */
public final class LoreManager
{
	/** List of all Door transitions. */
	private static HashSet<DoorTransition> doorTransitions;

	/** List of endings. */
	private static HashMap<String, Ending> endings;
	public static final String firstMap = "map2";
	/** Contains the Save Files. */
	public static String[] saves;

	/** Activates a Door.
	 * 
	 * @param UUID - The Door's UUID.
	 * @param mapName - The name of the Map it's from. */
	public static void activateDoor(int UUID, String mapName)
	{
		for (DoorTransition door : doorTransitions)
		{
			if (door.UUID == UUID && door.map.equals(mapName))
			{
				door.apply();
				return;
			}
		}
		System.out.println("Door not found : " + mapName + ", " + UUID);
	}

	/** Activates an Ending.
	 * 
	 * @param game - The current GameState.
	 * @param name - The name of the source. */
	public static void activateEnding(String name, GameState game)
	{
		if (endings.get(name) == null) System.out.println("Ending not found for : " + name);
		else endings.get(name).apply(game);
	}

	/** Creates the LoreManager. */
	public static void createLore()
	{
		FileUtils.loadSaves();
		doorTransitions = new HashSet<DoorTransition>();
		endings = new HashMap<String, Ending>();

		String[] data = FileUtils.readFileAsStringArray("res/lore.txt");
		int index = 1;
		while (index < data.length && !data[index].startsWith("endings ="))
		{
			String[] values = data[index].split("\t");
			doorTransitions.add(new DoorTransition(Integer.parseInt(values[1]), values[0], values[2], Integer.parseInt(values[3]), Integer.parseInt(values[4]),
					Integer.parseInt(values[5]), Integer.parseInt(values[6])));
			++index;
		}
		++index;

		while (index < data.length)
		{
			String[] values = data[index].split("\t");
			endings.put(values[0], new Ending(Byte.parseByte(values[1]), values[2]));
			++index;
		}
	}

	/** Deletes a Save.
	 * 
	 * @param index - The Index of the Save. */
	public static void deleteSave(int index)
	{
		if (saves.length == 0) return;
		String[] newSaves = new String[saves.length - 1];
		int i = 0;
		for (int j = 0; j < newSaves.length; j++)
		{
			if (index == i) continue;
			newSaves[j] = saves[i];
			++i;
		}
		saves = newSaves;
		FileUtils.saveSaves();
	}

	/** Creates a new Save. */
	public static void newSave()
	{
		String[] newSaves = new String[saves.length + 1];
		System.arraycopy(saves, 0, newSaves, 0, saves.length);
		newSaves[saves.length] = firstMap;
		saves = newSaves;
		FileUtils.saveSaves();
	}

}
