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

public class FileUtils
{

	/** Contains the Save Files. */
	public static String[] saves;

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
		saveSaves();
	}

	/** Loads the Save Files. */
	public static void loadSaves()
	{
		saves = readFileAsStringArray("res/saves.txt");
	}

	/** Creates a new Save. */
	public static void newSave()
	{
		String[] newSaves = new String[saves.length + 1];
		System.arraycopy(saves, 0, newSaves, 0, saves.length);
		newSaves[saves.length] = "map2";
		saves = newSaves;
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
			for (String save : saves)
				pw.println(save);
			pw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
