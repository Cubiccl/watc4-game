package net.watc4.game.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import javafx.geometry.Point2D;
import net.watc4.game.GameObject;
import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.FileUtils;
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
	private HashSet<Vector> wallSet;
	/** The shadows to draw. */
	private BufferedImage shadows;
	/** Width, in pixel of the light manager */
	private int width;

	public LightManager(Map map)
	{
		this.width = map.width * Map.TILESIZE;
		this.height = map.height * Map.TILESIZE;
		this.wallSet = map.getWallSet();
		this.endPoints = new TreeMap<>();
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

	public HashSet<Vector> getVectorSet()
	{
		return this.wallSet;
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

		Iterator<Vector> it = wallSet.iterator();
		while (it.hasNext())
		{
			Vector vector = (Vector) it.next();

			double cos = ((vector.getPosition().getX() - lightPosition.getX())
					/ (Math.sqrt((vector.getPosition().getX() - lightPosition.getX()) * (vector.getPosition().getX() - lightPosition.getX())
							+ (vector.getPosition().getY() - lightPosition.getY()) * (vector.getPosition().getY() - lightPosition.getY()))));
			double angle = (vector.getPosition().getY() - lightPosition.getY() > 0) ? (float) Math.acos(cos) : (float) -Math.acos(cos);
			endPoints.put(angle - 0.00001, new Vector(lightPosition, new Point2D(Math.cos(angle - 0.00001), Math.sin(angle - 0.00001))).intersect(wallSet));
			endPoints.put(angle, new Vector(lightPosition, new Point2D(Math.cos(angle), Math.sin(angle))).intersect(wallSet));
			endPoints.put(angle + 0.00001, new Vector(lightPosition, new Point2D(Math.cos(angle + 0.00001), Math.sin(angle + 0.00001))).intersect(wallSet));
			
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
			stockTriangleX.add(FileUtils.toInt(entry.getValue().getX()));
			stockTriangleY.add(FileUtils.toInt(entry.getValue().getY()));
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
