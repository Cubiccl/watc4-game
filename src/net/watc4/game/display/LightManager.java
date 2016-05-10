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

import javafx.geometry.Point2D;

import javax.imageio.ImageIO;

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

	private Vector[] viewWallSet;

	private HashSet<Vector> wallSet;

	private HashSet<Point2D> tmpEndPoints;

	/** The shadows to draw. */
	private BufferedImage shadows;

	public LightManager(Map map)
	{
		this.map = map;
		this.endPoints = new TreeMap<>();
		this.tmpEndPoints = new HashSet<>();
		this.viewWallSet = new Vector[4];
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
		viewWallSet[0] = new Vector(new Point2D(lightPosition.getX() - lumi.LIGHT_INTENSITY, lightPosition.getY() - lumi.LIGHT_INTENSITY),
				new Point2D(2 * lumi.LIGHT_INTENSITY, 0));
		viewWallSet[1] = new Vector(new Point2D(lightPosition.getX() + lumi.LIGHT_INTENSITY, lightPosition.getY() - lumi.LIGHT_INTENSITY),
				new Point2D(0.00001, 2 * lumi.LIGHT_INTENSITY));
		viewWallSet[2] = new Vector(new Point2D(lightPosition.getX() - lumi.LIGHT_INTENSITY, lightPosition.getY() + lumi.LIGHT_INTENSITY),
				new Point2D(0.00001, -2 * lumi.LIGHT_INTENSITY));
		viewWallSet[3] = new Vector(new Point2D(lightPosition.getX() + lumi.LIGHT_INTENSITY, lightPosition.getY() + lumi.LIGHT_INTENSITY),
				new Point2D(-2 * lumi.LIGHT_INTENSITY, 0));

		// Add concerned walls
		wallSet.clear();
		Chunk TL = this.map.getChunk((float) viewWallSet[0].getPosition().getX(), (float) viewWallSet[0].getPosition().getY());
		int TLx = (TL != null) ? TL.xPos : 0;
		int TLy = (TL != null) ? TL.yPos : 0;
		Chunk TR = this.map.getChunk((float) viewWallSet[1].getPosition().getX(), (float) viewWallSet[1].getPosition().getY());
		int TRx = (TR != null) ? TR.xPos : map.chunks.length-1;
		Chunk BL = this.map.getChunk((float) viewWallSet[2].getPosition().getX(), (float) viewWallSet[2].getPosition().getY());
		int BLy = (BL != null) ? BL.yPos : map.chunks[0].length-1;
		for (int x = TLx; x <= TRx; x++)
			for (int y = TLy; y <= BLy; y++)
				wallSet.addAll(map.getChunk(x * Chunk.SIZE, y * Chunk.SIZE).getWallSet());

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
		for (int i = 0; i < viewWallSet.length; i++)
			wallSet.add(viewWallSet[i]);
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
		for (Entry<Double, Point2D> entry : endPoints.entrySet())
		{
			stockTriangleX.add(FileUtils.toInt(entry.getValue().getX()));
			stockTriangleY.add(FileUtils.toInt(entry.getValue().getY()));
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
			hole.intersect(view);
			polygon.subtract(hole);
			shadowsG.draw(polygon);
		}
	}
}