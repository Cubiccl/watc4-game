package net.watc4.game.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import javafx.geometry.Point2D;
import net.watc4.game.entity.EntityLumi;
import net.watc4.game.map.Chunk;
import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.FileUtils;
import net.watc4.game.utils.GameSettings;
import net.watc4.game.utils.IRender;
import net.watc4.game.utils.IUpdate;
import net.watc4.game.utils.Vector;

/** Calculates and displays shadows. */
public class LightManager implements IRender, IUpdate
{
	private static int[] toIntArray(List<Integer> list)
	{
		int[] res = new int[list.size()];
		for (int i = 0; i < res.length; i++)
		{
			res[i] = list.get(i);
		}
		return res;
	}

	private Map map;
	/** List of points to link */
	private TreeMap<Double, Point2D> endPoints;
	/** The field of view of Lumi */
	private BufferedImage lumiLight;

	private HashSet<Vector> viewWallSet, wallSet;

	private HashSet<Point2D> tmpEndPoints;

	/** The shadows to draw. */
	private BufferedImage shadows;

	public LightManager(Map map)
	{
		this.map = map;
		this.endPoints = new TreeMap<>();
		this.tmpEndPoints = new HashSet<>();
		this.viewWallSet = new HashSet<>();
		this.wallSet = new HashSet<>();
		try
		{
			this.lumiLight = ImageIO.read(new File("res/textures/lumiLight.png"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public HashSet<Vector> getVectorSet()
	{
		return this.wallSet;
	}

	@Override
	public void render(Graphics g)
	{
		this.updateShadows();
		g.drawImage(this.shadows, 0, 0, null);

		g.setColor(Color.GREEN);
		for (Vector v : wallSet)
		{
			g.fillOval((int) v.getPosition().getX() - 3, (int) v.getPosition().getY() - 3, 6, 6);
		}

		g.setColor(Color.RED);
		for (Point2D p : tmpEndPoints)
		{
			g.fillOval((int) p.getX() - 3, (int) p.getY() - 3, 6, 6);
		}
	}

	public void setWalls(HashSet<Vector> wallSet)
	{
		this.wallSet = wallSet;
	}

	@Override
	public void update()
	{
		this.endPoints.clear();

		EntityLumi lumi = GameState.getInstance().entityLumi;
		Point2D lightPosition = new Point2D(GameState.getInstance().entityLumi.getX() + GameState.getInstance().entityLumi.getWidth() / 2,
				GameState.getInstance().entityLumi.getY() + GameState.getInstance().entityLumi.getHeight() / 2);

		// Create the fieldOfView Rectangle
		viewWallSet.clear();
		for (int x = -1; x < 2; x+=2)
		for (int y = -1; y < 2; y+=2)
			viewWallSet.add(new Vector(new Point2D(lightPosition.getX() + x * lumi.LIGHT_INTENSITY, lightPosition.getY() + y* lumi.LIGHT_INTENSITY),
				new Point2D(0.00001 + (-x - y)*lumi.LIGHT_INTENSITY,(x-y) * lumi.LIGHT_INTENSITY)));

		// Add concerned walls
		wallSet.clear();
		for (Vector viewWall : viewWallSet)
		{
			Chunk chunk = this.map.getChunk((float) viewWall.getPosition().getX(), (float) viewWall.getPosition().getY());
			if (chunk != null) wallSet.addAll(chunk.getWallSet());
		}		
		
		// Find tmpEndPoints
		tmpEndPoints.clear();
		for (Vector viewWall : viewWallSet)
		{
			tmpEndPoints.addAll(viewWall.allIntersection(wallSet));
		}
		for (Vector endPoint : wallSet)
		{
			if (endPoint.getPosition().getX() >= lightPosition.getX() - lumi.LIGHT_INTENSITY
					&& endPoint.getPosition().getX() <= lightPosition.getX() + lumi.LIGHT_INTENSITY
					&& endPoint.getPosition().getY() >= lightPosition.getY() - lumi.LIGHT_INTENSITY
					&& endPoint.getPosition().getY() <= lightPosition.getY() + lumi.LIGHT_INTENSITY)
				tmpEndPoints.add(endPoint.getPosition());
				
				}

		// Find true endPoints
		wallSet.addAll(viewWallSet);
		for (Point2D endPoint : tmpEndPoints)
		{
			double cos = ((endPoint.getX() - lightPosition.getX())
					/ (Math.sqrt((endPoint.getX() - lightPosition.getX()) * (endPoint.getX() - lightPosition.getX())
							+ (endPoint.getY() - lightPosition.getY()) * (endPoint.getY() - lightPosition.getY()))));
			double angle = (endPoint.getY() - lightPosition.getY() > 0) ? (float) Math.acos(cos) : (float) -Math.acos(cos);
			Point2D tmpPoint1 = new Vector(lightPosition, new Point2D(Math.cos(angle - 0.00001), Math.sin(angle - 0.00001))).intersect(wallSet);
			Point2D tmpPoint2 = new Vector(lightPosition, new Point2D(Math.cos(angle + 0.00001), Math.sin(angle + 0.00001))).intersect(wallSet);
			if (tmpPoint1 != null) endPoints.put(angle - 0.00001, tmpPoint1);
			if (tmpPoint2 != null) endPoints.put(angle + 0.00001, tmpPoint2);
		}
	}

	/** Updates the shadows to draw. */
	private void updateShadows()
	{
		EntityLumi lumi = GameState.getInstance().entityLumi;
		Camera camera = GameState.getInstance().getCamera();
		this.shadows = new BufferedImage(map.width * Map.TILESIZE, map.height * Map.TILESIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D shadowsG = (Graphics2D) this.shadows.getGraphics();
		shadowsG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		List<Integer> stockTriangleX = new ArrayList<Integer>();
		List<Integer> stockTriangleY = new ArrayList<Integer>();
		shadowsG.setColor(Color.BLUE);
		for (Entry<Double, Point2D> entry : endPoints.entrySet())
		{
			stockTriangleX.add(FileUtils.toInt(entry.getValue().getX()));
			stockTriangleY.add(FileUtils.toInt(entry.getValue().getY()));
			shadowsG.fillOval(FileUtils.toInt(entry.getValue().getX()) - 3, FileUtils.toInt(entry.getValue().getY()) - 3, 6, 6);
		}
		int[] triangleX = new int[stockTriangleX.size()];
		triangleX = toIntArray(stockTriangleX);
		int[] triangleY = new int[stockTriangleY.size()];
		triangleY = toIntArray(stockTriangleY);

		Area hole = new Area(new Polygon(triangleX, triangleY, triangleX.length));
		Area polygon = new Area(new Rectangle(FileUtils.toInt(camera.getXOffset()), FileUtils.toInt(camera.getYOffset()),
				FileUtils.toInt(camera.width / camera.getScale()), FileUtils.toInt(camera.height / camera.getScale())));
		Area view = new Area(new Ellipse2D.Float(lumi.getX() + lumi.getWidth() / 2 - lumi.LIGHT_INTENSITY,
				lumi.getY() + lumi.getHeight() / 2 - lumi.LIGHT_INTENSITY, lumi.LIGHT_INTENSITY * 2, lumi.LIGHT_INTENSITY * 2));

		if (!GameSettings.lightMode)
		{
			hole.intersect(view);
			polygon.subtract(hole);
			shadowsG.setColor(Color.BLACK);
			shadowsG.fill(polygon);
			shadowsG.drawImage(lumiLight, FileUtils.toInt(lumi.getX() + (lumi.getWidth() - lumiLight.getWidth()) / 2),
					FileUtils.toInt(lumi.getY() + (lumi.getHeight() - lumiLight.getHeight()) / 2), null);

		} else
		{
			shadowsG.setColor(Color.RED);
			// hole.intersect(view);
			polygon.subtract(hole);
			shadowsG.draw(polygon);
			shadowsG.setColor(Color.GREEN);
			shadowsG.draw(new Area(new Rectangle(FileUtils.toInt(lumi.getX() + lumi.getWidth() / 2 - lumi.LIGHT_INTENSITY),
					FileUtils.toInt(lumi.getY() + lumi.getHeight() / 2 - lumi.LIGHT_INTENSITY), lumi.LIGHT_INTENSITY * 2, lumi.LIGHT_INTENSITY * 2)));

		}
	}

	public void registerChunk(Chunk chunk)
	{

	}
}
