package net.watc4.game.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import net.watc4.game.utils.lore.LoreManager;

public class FileUtils
{

	/** Loads the Save Files. */
	public static void loadSaves()
	{
		LoreManager.saves = readFileAsStringArray("res/saves.txt");
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
					if (!line.startsWith("//")) list.add(line);
					line = br.readLine();
				}

				br.close();
				fr.close();

			} catch (IOException exception)
			{
				System.err.println("Reading error : " + exception.getMessage());
				exception.printStackTrace();
			}
		} catch (FileNotFoundException exception)
		{
			System.err.println("File doesn't exists.");
			exception.printStackTrace();
		}

		return list.toArray(new String[0]);

	}

	/** Saves the Save Files. */
	public static void saveSaves()
	{
		File file = new File("res/saves.txt");
		file.delete();
		try
		{
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			for (String save : LoreManager.saves)
				pw.println(save);
			pw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
