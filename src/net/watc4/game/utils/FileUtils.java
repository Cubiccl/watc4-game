package net.watc4.game.utils;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
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
	
	
	/** Convert a double into an int */
	public static int toInt(double f)
	{
		return (f - (double) ((int) f) < 0.5) ? (int) f : (int) f + 1;
	}
	
	/** Draw an arrow, useful for debug */
	void drawArrow(Graphics2D g, int x1, int y1, int x2, int y2) {
      
        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len, len-6, len-6, len},
                      new int[] {0, -6, 6, 0}, 4);
    }
}
