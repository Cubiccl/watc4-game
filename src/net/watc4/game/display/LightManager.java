package net.watc4.game.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.sun.xml.internal.bind.v2.model.util.ArrayInfoUtil;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import javafx.geometry.Point2D;
import net.watc4.game.GameObject;
import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.Vector;

/** Calculates and displays shadows. */
public class LightManager implements GameObject
{
	/** List of points to link */
	private TreeMap<Double, Point2D> endPoints;
	/** True if the shadows have changed, thus the shadows Image should be updated. */
	private boolean hasChanged;
	/** Height, in pixel of the light manager */
	private int height;
	/** List of segments stopping light */
	private HashSet<Vector> vectorSet;
	/** The shadows to draw. */
	private BufferedImage shadows;
	/** Width, in pixel of the light manager */
	private int width;

	public LightManager(Map map)
	{
		this.width = map.width * Map.TILESIZE;
		this.height = map.height * Map.TILESIZE;
		this.vectorSet = new HashSet<>((map.height * (2 * map.width + 1) + map.width) / 1, 3);
		this.endPoints = new TreeMap<>();
		// Add the externals borders of the map
		for (int i = 0; i < map.width; i++)
		{
			this.vectorSet.add(new Vector(1 * i * Map.TILESIZE, 0, Map.TILESIZE, 0));
			this.vectorSet.add(new Vector((map.width * Map.TILESIZE) - (1 * i * Map.TILESIZE), map.height * Map.TILESIZE, -Map.TILESIZE, 0));
		}
		for (int i = 0; i < map.height; i++)
		{
			this.vectorSet.add(new Vector(0, (map.height * Map.TILESIZE) - (1 * i * Map.TILESIZE), 0, -Map.TILESIZE));
			this.vectorSet.add(new Vector(map.width * Map.TILESIZE, 1 * i * Map.TILESIZE, 0, Map.TILESIZE));
		}

		// Add vectors
		for (int y = 0; y < map.height - 1; y++)
		{
			// Add the horizontal ones
			for (int x = 0; x < map.width - 1; x++)
			{
				if (map.getTileAt(x, y).isOpaque && !map.getTileAt(x + 1, y).isOpaque)
					this.vectorSet.add(new Vector((x + 1) * Map.TILESIZE, y * Map.TILESIZE, 0, Map.TILESIZE));
				else if (!map.getTileAt(x, y).isOpaque && map.getTileAt(x + 1, y).isOpaque)
					this.vectorSet.add(new Vector((x + 1) * Map.TILESIZE, (y + 1) * Map.TILESIZE, 0, -Map.TILESIZE));

			}
			// Add the vertical ones
			for (int x = 0; x < map.width; x++)
			{
				if (map.getTileAt(x, y).isOpaque && !map.getTileAt(x, y + 1).isOpaque)
					this.vectorSet.add(new Vector((x + 1) * Map.TILESIZE, (y + 1) * Map.TILESIZE, -Map.TILESIZE, 0));
				else if (!map.getTileAt(x, y).isOpaque && map.getTileAt(x, y + 1).isOpaque)
					this.vectorSet.add(new Vector(x * Map.TILESIZE, (y + 1) * Map.TILESIZE, Map.TILESIZE, 0));
			}
		}

		// Add the last horizontal ones
		for (int x = 0; x < map.width - 1; x++)
		{
			if (map.getTileAt(x, map.height - 1).isOpaque && !map.getTileAt(x + 1, map.height - 1).isOpaque)
				this.vectorSet.add(new Vector((x + 1) * Map.TILESIZE, (map.height - 1) * Map.TILESIZE, 0, Map.TILESIZE));
			else if (!map.getTileAt(x, (map.height - 1)).isOpaque && map.getTileAt(x + 1, (map.height - 1)).isOpaque)
				this.vectorSet.add(new Vector((x + 1) * Map.TILESIZE, ((map.height - 1) + 1) * Map.TILESIZE, 0, -Map.TILESIZE));

		}

		cleanVectorSet();

	}

	/** Applies a mask onto an image.
	 * 
	 * @param image - The Image to modify.
	 * @param mask - The mask to apply. */
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

	/** Merges adjacent segments into single ones. */
	private void cleanVectorSet()
	{
		boolean done = false;
		boolean manyVectorFound = false;
		while (!done)
		{
			Iterator<Vector> it = vectorSet.iterator();
			done = true;
			while (it.hasNext())
			{
				Vector targetVector = (Vector) it.next();
				Vector vectorFound = null;
				Iterator<Vector> jt = vectorSet.iterator();

				while (jt.hasNext() && !manyVectorFound)
				{
					Vector arrowVector = (Vector) jt.next();
					if (targetVector.getPosition().getX() == arrowVector.getPosition().getX() + arrowVector.getDirection().getX()
							&& targetVector.getPosition().getY() == arrowVector.getPosition().getY() + arrowVector.getDirection().getY())
						;
					{
						if (vectorFound != null) manyVectorFound = true;
						vectorFound = arrowVector;
					}
				}
				if (vectorFound == null) manyVectorFound = true;
				if (!manyVectorFound && targetVector.getDirection().getX() * vectorFound.getDirection().getY()
						- targetVector.getDirection().getY() * vectorFound.getDirection().getX() == 0)
				{
					vectorFound.setDirection(new Point2D(vectorFound.getDirection().getX() + targetVector.getDirection().getX(),
							vectorFound.getDirection().getY() + targetVector.getDirection().getY()));
					vectorSet.remove(targetVector);
					done = false;

				}
			}
		}
	}

	private int toInt(double f)
	{
		return (f - (double) ((int) f) < 0.5) ? (int) f : (int) f + 1;
	}

	public HashSet<Vector> getVectorSet()
	{
		return this.vectorSet;
	}

	@Override
	public void render(Graphics g)
	{
		if (this.hasChanged) this.updateShadows();
		g.drawImage(this.shadows, 0, 0, null);
	}

	private static int[] toIntArray(List<Integer> list)
	{
		int[] res = new int[list.size()];
		for (int i = 0; i < res.length; i++)
		{
			res[i] = list.get(i);
		}
		return res;
	}

	@Override
	public void update()
	{
		this.endPoints.clear();

		Point2D lightPosition = new Point2D(GameState.getInstance().entityLumi.getX() + GameState.getInstance().entityLumi.getHitbox().getWidth() / 2,
				GameState.getInstance().entityLumi.getY() + GameState.getInstance().entityLumi.getHitbox().getHeight() / 2);

		Iterator<Vector> it = vectorSet.iterator();
		while (it.hasNext())
		{
			Vector vector = (Vector) it.next();

			double cos = ((vector.getPosition().getX() - lightPosition.getX())
					/ (Math.sqrt((vector.getPosition().getX() - lightPosition.getX()) * (vector.getPosition().getX() - lightPosition.getX())
							+ (vector.getPosition().getY() - lightPosition.getY()) * (vector.getPosition().getY() - lightPosition.getY()))));
			double angle = (vector.getPosition().getY() - lightPosition.getY() > 0) ? (float) Math.acos(cos) : (float) -Math.acos(cos);
			endPoints.put(angle - 0.00001, new Vector(lightPosition, new Point2D(Math.cos(angle - 0.00001), Math.sin(angle - 0.00001))).intersect(vectorSet));
			endPoints.put(angle, new Vector(lightPosition, new Point2D(Math.cos(angle), Math.sin(angle))).intersect(vectorSet));
			endPoints.put(angle + 0.00001, new Vector(lightPosition, new Point2D(Math.cos(angle + 0.00001), Math.sin(angle + 0.00001))).intersect(vectorSet));
			
		}
		this.hasChanged = true;
	}

	/** Updates the shadows to draw. */
	private void updateShadows()
	{
		BufferedImage lightMap = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		this.shadows = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D lightMapG = (Graphics2D) lightMap.getGraphics();
		Graphics shadowsG = this.shadows.getGraphics();

		lightMapG.setColor(Color.WHITE);
		shadowsG.setColor(Color.BLACK);
		lightMapG.fillRect(0, 0, this.width, this.height);
		shadowsG.fillRect(0, 0, this.width, this.height);

		lightMapG.setColor(Color.BLACK);

		List<Integer> stockTriangleX = new ArrayList<Integer>();
		List<Integer> stockTriangleY = new ArrayList<Integer>();

		for (Entry<Double, Point2D> entry : endPoints.entrySet())
		{
			stockTriangleX.add(toInt(entry.getValue().getX()));
			stockTriangleY.add(toInt(entry.getValue().getY()));
		}
		int[] triangleX = new int[stockTriangleX.size()];
		triangleX = toIntArray(stockTriangleX);
		int[] triangleY = new int[stockTriangleY.size()];
		triangleY = toIntArray(stockTriangleY);

		lightMapG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		lightMapG.fillPolygon(triangleX, triangleY, triangleX.length);

		applyGrayscaleMaskToAlpha(this.shadows, lightMap);
		this.hasChanged = false;
	}
}
