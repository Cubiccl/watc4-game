package net.watc4.game.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils
{
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
}
