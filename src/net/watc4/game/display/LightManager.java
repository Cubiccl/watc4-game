package net.watc4.game.display;

import java.util.ArrayList;

import net.watc4.game.map.Map;

public class LightManager
{
	/**	 */
	private ArrayList<float[]> segments;

	public LightManager(Map map)
	{
		segments = new ArrayList<>();
		// Add the border of the map
		for (int i = 0; i < map.width; i++)
		{
			segments.add(new float[]
			{ 1 * i * Map.TILESIZE, 0, Map.TILESIZE, 0 });
			segments.add(new float[]
			{ (map.width * Map.TILESIZE) - (1 * i * Map.TILESIZE), map.height * Map.TILESIZE, -Map.TILESIZE, 0 });
		}
		for (int i = 0; i < map.height; i++)
		{
			segments.add(new float[]
			{ 0, (map.height * Map.TILESIZE) - (1 * i * Map.TILESIZE), 0, -Map.TILESIZE });
			segments.add(new float[]
			{ map.width * Map.TILESIZE, 1 * i * Map.TILESIZE, 0, Map.TILESIZE });
		}

		// Add borders
		for (int y = 0; y < map.height - 1; y++)
		{
			// Add the horizontal ones
			for (int x = 0; x < map.width - 1; x++)
			{
				if (map.getTileAt(x, y).isOpaque() && !map.getTileAt(x + 1, y).isOpaque()) segments.add(new float[]
				{ (x + 1) * Map.TILESIZE, y * Map.TILESIZE, 0, Map.TILESIZE });
				else if (!map.getTileAt(x, y).isOpaque() && map.getTileAt(x + 1, y).isOpaque()) segments.add(new float[]
				{ (x + 1) * Map.TILESIZE, (y + 1) * Map.TILESIZE, 0, -Map.TILESIZE });

			}
			// Add the vertical ones
			for (int x = 0; x < map.width; x++)
			{
				if (map.getTileAt(x, y).isOpaque() && !map.getTileAt(x, y + 1).isOpaque()) segments.add(new float[]
				{ (x + 1) * Map.TILESIZE, (y + 1) * Map.TILESIZE, -Map.TILESIZE, 0 });
				else if (!map.getTileAt(x, y).isOpaque() && map.getTileAt(x, y + 1).isOpaque()) segments.add(new float[]
				{ x * Map.TILESIZE, (y + 1) * Map.TILESIZE, Map.TILESIZE, 0 });
			}
		}

		// Add the last vertical ones
		for (int x = 0; x < map.width - 1; x++)
		{
			if (map.getTileAt(x, map.height - 1).isOpaque() && !map.getTileAt(x + 1, map.height - 1).isOpaque()) segments.add(new float[]
			{ (x + 1) * Map.TILESIZE, (map.height - 1) * Map.TILESIZE, 0, Map.TILESIZE });
			else if (!map.getTileAt(x, (map.height - 1)).isOpaque() && map.getTileAt(x + 1, (map.height - 1)).isOpaque()) segments.add(new float[]
			{ (x + 1) * Map.TILESIZE, ((map.height - 1) + 1) * Map.TILESIZE, 0, -Map.TILESIZE });

		}

		cleanSegments();

	}

	private void cleanSegments()
	{
		ArrayList<float[]> segFind = new ArrayList<>();
		boolean changed = true;
		while (changed)
		{
			int i = 0;
			changed = false;
			while (i < segments.size())
			{
				int index = 0;
				for (int j = 0; j < segments.size(); j++)
				{
					if (j != i)
						if (segments.get(i)[0] == segments.get(j)[0] + segments.get(j)[2] && segments.get(i)[1] == segments.get(j)[1] + segments.get(j)[3])
					{
						segFind.add(segments.get(j));
						index = j;
					}
				}

				if (segFind.size() == 1) if (segments.get(i)[2] * segFind.get(0)[3] - segFind.get(0)[2] * segments.get(i)[3] == 0)
				{
					segments.get(index)[2] += segments.get(i)[2];
					segments.get(index)[3] += segments.get(i)[3];
					segments.remove(i);
					changed = true;

				}
				segFind.clear();
				i++;
			}
		}
	}
	
	public ArrayList<float[]> getSegment(){
		return this.segments;
	}
}
