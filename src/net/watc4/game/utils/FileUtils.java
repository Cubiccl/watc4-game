package net.watc4.game.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import net.watc4.game.map.Map;
import net.watc4.game.map.TileRegistry;

public class FileUtils
{

	/** @param url Path to the map file.
	 * @return The corresponding <code>Map<code> */
	public static Map createMap(String url)
	{

		String[] mapText = readFileAsStringArray(url);

		int info[] = new int[6]; // width, height, lumiSpawnX, lumiSpawnY, pattouSpawnX and pattouSpawnY

		for (int i = 0; i < info.length; i++)
		{
			info[i] = Integer.valueOf(mapText[i].split(" = ")[1]);
		}

		Map map = new Map(info[0], info[1], info[2] * Map.TILESIZE, info[3] * Map.TILESIZE, info[4] * Map.TILESIZE, info[5] * Map.TILESIZE);
		String[] values; // Tiles values temporarily stored per line from the map file

		for (int i = 0; i < info[0]; i++)
		{
			values = mapText[i + 7].split("\t");

			for (int j = 0; j < info[1]; j++)
			{
				System.out.println(Integer.valueOf(values[j]));
				map.setTileAt(j, i, TileRegistry.getTileFromId(Integer.valueOf(values[j])));
			}
		}

		return map;
	}

	/** @param url Path to the map file.
	 * @return <b>String[]</b> containing the file line per line. */
	public static String[] readFileAsStringArray(String url)
	{
		ArrayList<String> list = new ArrayList<String>();

		try
		{
			File f = new File(url);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			try
			{
				String line = br.readLine();

				while (line != null)
				{
					list.add(line);
					line = br.readLine();
				}

				br.close();
				fr.close();

			} catch (IOException exception)
			{
				System.out.println("Reading error : " + exception.getMessage());
			}
		} catch (FileNotFoundException exception)
		{
			System.out.println("File doesn't exists.");
		}

		return list.toArray(new String[0]);

	}
}
