package net.watc4.game.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

import net.watc4.game.Game;
import net.watc4.game.GameObject;
import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;

public class LightManager implements GameObject
{
	/** Width, in pixel of the light manager */
	private int width;
	/** Height, in pixel of the light manager */
	private int height;
	/**	 */
	private ArrayList<float[]> segments;
	/**	 */
	private ArrayList<float[]> endPoints;

	public LightManager(Map map)
	{
		this.width = map.width * Map.TILESIZE;
		this.height = map.height * Map.TILESIZE;
		this.segments = new ArrayList<>();
		this.endPoints = new ArrayList<>();
		// Add the border of the map
		for (int i = 0; i < map.width; i++)
		{
			this.segments.add(new float[]
			{ 1 * i * Map.TILESIZE, 0, Map.TILESIZE, 0 });
			this.segments.add(new float[]
			{ (map.width * Map.TILESIZE) - (1 * i * Map.TILESIZE), map.height * Map.TILESIZE, -Map.TILESIZE, 0 });
		}
		for (int i = 0; i < map.height; i++)
		{
			this.segments.add(new float[]
			{ 0, (map.height * Map.TILESIZE) - (1 * i * Map.TILESIZE), 0, -Map.TILESIZE });
			this.segments.add(new float[]
			{ map.width * Map.TILESIZE, 1 * i * Map.TILESIZE, 0, Map.TILESIZE });
		}

		// Add borders
		for (int y = 0; y < map.height - 1; y++)
		{
			// Add the horizontal ones
			for (int x = 0; x < map.width - 1; x++)
			{
				if (map.getTileAt(x, y).isOpaque() && !map.getTileAt(x + 1, y).isOpaque()) this.segments.add(new float[]
				{ (x + 1) * Map.TILESIZE, y * Map.TILESIZE, 0, Map.TILESIZE });
				else if (!map.getTileAt(x, y).isOpaque() && map.getTileAt(x + 1, y).isOpaque()) this.segments.add(new float[]
				{ (x + 1) * Map.TILESIZE, (y + 1) * Map.TILESIZE, 0, -Map.TILESIZE });

			}
			// Add the vertical ones
			for (int x = 0; x < map.width; x++)
			{
				if (map.getTileAt(x, y).isOpaque() && !map.getTileAt(x, y + 1).isOpaque()) this.segments.add(new float[]
				{ (x + 1) * Map.TILESIZE, (y + 1) * Map.TILESIZE, -Map.TILESIZE, 0 });
				else if (!map.getTileAt(x, y).isOpaque() && map.getTileAt(x, y + 1).isOpaque()) this.segments.add(new float[]
				{ x * Map.TILESIZE, (y + 1) * Map.TILESIZE, Map.TILESIZE, 0 });
			}
		}

		// Add the last vertical ones
		for (int x = 0; x < map.width - 1; x++)
		{
			if (map.getTileAt(x, map.height - 1).isOpaque() && !map.getTileAt(x + 1, map.height - 1).isOpaque()) this.segments.add(new float[]
			{ (x + 1) * Map.TILESIZE, (map.height - 1) * Map.TILESIZE, 0, Map.TILESIZE });
			else if (!map.getTileAt(x, (map.height - 1)).isOpaque() && map.getTileAt(x + 1, (map.height - 1)).isOpaque()) this.segments.add(new float[]
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
			while (i < this.segments.size())
			{
				int index = 0;
				for (int j = 0; j < this.segments.size(); j++)
				{
					if (j != i) if (this.segments.get(i)[0] == this.segments.get(j)[0] + this.segments.get(j)[2]
							&& this.segments.get(i)[1] == this.segments.get(j)[1] + this.segments.get(j)[3])
					{
						segFind.add(this.segments.get(j));
						index = j;
					}
				}

				if (segFind.size() == 1) if (this.segments.get(i)[2] * segFind.get(0)[3] - segFind.get(0)[2] * this.segments.get(i)[3] == 0)
				{
					this.segments.get(index)[2] += this.segments.get(i)[2];
					this.segments.get(index)[3] += this.segments.get(i)[3];
					this.segments.remove(i);
					changed = true;

				}
				segFind.clear();
				i++;
			}
		}
	}

	public ArrayList<float[]> getSegment()
	{
		return this.segments;
	}

	@Override
	public void render(Graphics g)
	{
		BufferedImage lightMap = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		BufferedImage shadows = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		Graphics lightMapG = lightMap.getGraphics();
		Graphics shadowsG = shadows.getGraphics();
		int posX = (int)GameState.getInstance().entityLumi.getX();
		int posY = (int)GameState.getInstance().entityLumi.getY();
				
		lightMapG.setColor(Color.WHITE);
		shadowsG.setColor(Color.BLACK);
		lightMapG.fillRect(0, 0, this.width, this.height);
		shadowsG.fillRect(0, 0, this.width, this.height);
		
		lightMapG.setColor(Color.BLACK);
		for (int i = 0; i < this.endPoints.size(); i++)
		{
			int[]baseTriangleX = {posX, (int)this.endPoints.get(i%this.endPoints.size())[1],(int)this.endPoints.get((i+1)%this.endPoints.size())[1]};
			int[]baseTriangleY = {posY, (int)this.endPoints.get(i%this.endPoints.size())[2],(int)this.endPoints.get((i+1)%this.endPoints.size())[2]};
			lightMapG.fillPolygon(baseTriangleX, baseTriangleY, 3);
		}
		applyGrayscaleMaskToAlpha(shadows, lightMap);
		g.drawImage(shadows, 0, 0, null);

	}

	@Override
	public void update()
	{
		this.endPoints.clear();
		double cos = 0;
		float angle = 0;
		float rp_x = GameState.getInstance().entityLumi.getX();
		float rp_y = GameState.getInstance().entityLumi.getY();

		// Get the angle
		for (int i = 0; i < this.segments.size(); i++)
		{
			cos = ((this.segments.get(i)[0] - rp_x) / (Math
					.sqrt((double) ((this.segments.get(i)[0] - rp_x) * (this.segments.get(i)[0] - rp_x) + (this.segments.get(i)[1] - rp_y) * (this.segments.get(i)[1] - rp_y)))));
			angle = (this.segments.get(i)[1] - rp_y > 0) ? (float) Math.acos(cos) : (float) -Math.acos(cos);
			this.endPoints.add(new float[]
			{ angle, -1, -1 });
			this.endPoints.add(new float[]
			{ (float) (angle + 0.00001), -1, -1 });
			this.endPoints.add(new float[]
			{ (float) (angle - 0.00001), -1, -1 });
		}

		// Sort the angle
		this.endPoints.sort(new Comparator<float[]>()
		{
			@Override
			public int compare(float[] o1, float[] o2)
			{
				if (o1.length == 0) return 1;
				if (o2.length == 0) return -1;
				if (o1[0] < o2[0]) return -1;
				if (o1[0] >= o2[0]) return 1;
				return 0;
			}
		});

		// Get all the end points
		float cartesianProd;
		float ts, tr, tempTr;
		float rd_x, rd_y;
		float sp_x, sp_y, sd_x, sd_y;

		for (int i = 0; i < this.endPoints.size(); i++)
		{
			rd_x = (float) Math.cos(this.endPoints.get(i)[0]);
			rd_y = (float) Math.sin(this.endPoints.get(i)[0]);
			tr = Float.MAX_VALUE;
			for (int j = 0; j < segments.size(); j++)
			{
				sp_x = this.segments.get(j)[0];
				sp_y = this.segments.get(j)[1];
				sd_x = this.segments.get(j)[2];
				sd_y = this.segments.get(j)[3];
				cartesianProd = (float) ((sd_x * rd_y) - (sd_y * rd_x));

				if (cartesianProd != 0)
				{
					ts = (rd_x * (sp_y - rp_y) + rd_y * (rp_x - sp_x)) / cartesianProd;
					if (ts >= 0 && ts <= 1)
					{
						tempTr = (sp_x + sd_x * ts - rp_x) / rd_x;
						if (tempTr > 0 && tempTr < tr)

							tr = tempTr;

					}
				}
			}
			this.endPoints.get(i)[1] = rp_x + tr * rd_x;
			this.endPoints.get(i)[2] = rp_y + tr * rd_y;
		}
	}

	private void applyGrayscaleMaskToAlpha(BufferedImage image, BufferedImage mask)
	{
		int width = image.getWidth();
		int height = image.getHeight();

		int[] imagePixels = image.getRGB(0, 0, width, height, null, 0, width);
		int[] maskPixels = mask.getRGB(0, 0, width, height, null, 0, width);

		for (int i = 0; i < imagePixels.length; i++)
		{
			int color = imagePixels[i] & 0x00ffffff; // Mask preexisting alpha
			int alpha = maskPixels[i] << 24; // Shift blue to alpha
			imagePixels[i] = color | alpha;
		}
		image.setRGB(0, 0, width, height, imagePixels, 0, width);
	}
}
