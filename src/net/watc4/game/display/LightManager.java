package net.watc4.game.display;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.watc4.game.entity.Entity;
import net.watc4.game.entity.ILightSource;
import net.watc4.game.listener.IEntityMovementListener;
import net.watc4.game.map.Chunk;
import net.watc4.game.map.Map;
import net.watc4.game.utils.GameSettings;
import net.watc4.game.utils.GameUtils;
import net.watc4.game.utils.IRender;
import net.watc4.game.utils.IUpdate;
import net.watc4.game.utils.Vector;

/** Calculates and displays shadows. */
public class LightManager implements IRender, IUpdate, IEntityMovementListener
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

	private Area fieldOfView;

	private ArrayList<ILightSource> lightSources;

	private Map map;

	private HashSet<Vector> wallSet;

	public LightManager(Map map)
	{
		this.map = map;
		this.lightSources = new ArrayList<>();
		this.wallSet = new HashSet<>();
	}

	public void add(Entity entity)
	{
		ILightSource lightSource = (ILightSource) entity;
		this.lightSources.add(lightSource);
		entity.addMovementListener(this);
	}

	public HashSet<Vector> getVectorSet()
	{
		return this.wallSet;
	}

	@Override
	public void onEntityMove(Entity entity)
	{
		this.update();
	}

	@Override
	public void render(Graphics2D g)
	{
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Camera camera = this.map.game.getCamera();

		Area shadow = new Area(new Rectangle(GameUtils.toInt(camera.getXOffset()), GameUtils.toInt(camera.getYOffset()), GameUtils.toInt(camera.width
				/ camera.getScale()), GameUtils.toInt(camera.height / camera.getScale())));

		if (!GameSettings.lightMode)
		{
			shadow.subtract(this.fieldOfView);
			g.setColor(Color.BLACK);
			g.fill(shadow);

		} else
		{
			shadow.subtract(this.fieldOfView);
			g.setColor(Color.RED);
			g.draw(shadow);
		}
	}

	public void setWalls(HashSet<Vector> wallSet)
	{
		this.wallSet = wallSet;
	}

	@Override
	public void update()
	{
		this.fieldOfView = null;
		for (int i = 0; i < lightSources.size(); i++)
		{
			HashSet<Point2D> tmpEndPoints = new HashSet<>();
			Vector[] viewWallSet = new Vector[4];
			TreeMap<Double, Point2D> endPoints = new TreeMap<>();
			ILightSource lightSource = lightSources.get(i);
			Point2D lightPosition = new Point2D.Double(lightSource.getX() + lightSource.getWidth() / 2, lightSource.getY() + lightSource.getHeight() / 2);

			// Create the fieldOfView Rectangle
			viewWallSet[0] = new Vector(new Point2D.Double(lightPosition.getX() - lightSource.getLightIntensity(), lightPosition.getY()
					- lightSource.getLightIntensity()), new Point2D.Double(2 * lightSource.getLightIntensity(), 0));
			viewWallSet[1] = new Vector(new Point2D.Double(lightPosition.getX() + lightSource.getLightIntensity(), lightPosition.getY()
					- lightSource.getLightIntensity()), new Point2D.Double(0.00001, 2 * lightSource.getLightIntensity()));
			viewWallSet[2] = new Vector(new Point2D.Double(lightPosition.getX() - lightSource.getLightIntensity(), lightPosition.getY()
					+ lightSource.getLightIntensity()), new Point2D.Double(0.00001, -2 * lightSource.getLightIntensity()));
			viewWallSet[3] = new Vector(new Point2D.Double(lightPosition.getX() + lightSource.getLightIntensity(), lightPosition.getY()
					+ lightSource.getLightIntensity()), new Point2D.Double(-2 * lightSource.getLightIntensity(), 0));

			// Add concerned walls
			wallSet.clear();
			Chunk TL = this.map.getChunk((float) viewWallSet[0].getPosition().getX(), (float) viewWallSet[0].getPosition().getY());
			int TLx = (TL != null) ? TL.xPos : 0;
			int TLy = (TL != null) ? TL.yPos : 0;
			Chunk TR = this.map.getChunk((float) viewWallSet[1].getPosition().getX(), (float) viewWallSet[1].getPosition().getY());
			int TRx = (TR != null) ? TR.xPos : map.chunks.length - 1;
			Chunk BL = this.map.getChunk((float) viewWallSet[2].getPosition().getX(), (float) viewWallSet[2].getPosition().getY());
			int BLy = (BL != null) ? BL.yPos : map.chunks[0].length - 1;
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
				if (endPoint.getPosition().getX() >= lightPosition.getX() - lightSource.getLightIntensity()
						&& endPoint.getPosition().getX() <= lightPosition.getX() + lightSource.getLightIntensity()
						&& endPoint.getPosition().getY() >= lightPosition.getY() - lightSource.getLightIntensity()
						&& endPoint.getPosition().getY() <= lightPosition.getY() + lightSource.getLightIntensity()) tmpEndPoints.add(endPoint.getPosition());

			}

			// Find true endPoints
			for (int j = 0; j < viewWallSet.length; j++)
				wallSet.add(viewWallSet[j]);
			for (Point2D endPoint : tmpEndPoints)
			{
				double cos = ((endPoint.getX() - lightPosition.getX()) / (Math.sqrt((endPoint.getX() - lightPosition.getX())
						* (endPoint.getX() - lightPosition.getX()) + (endPoint.getY() - lightPosition.getY()) * (endPoint.getY() - lightPosition.getY()))));
				double angle = (endPoint.getY() - lightPosition.getY() > 0) ? (float) Math.acos(cos) : (float) -Math.acos(cos);
				Point2D tmpPoint1 = new Vector(lightPosition, new Point2D.Double(Math.cos(angle - 0.00001), Math.sin(angle - 0.00001))).intersect(wallSet);
				Point2D tmpPoint2 = new Vector(lightPosition, new Point2D.Double(Math.cos(angle + 0.00001), Math.sin(angle + 0.00001))).intersect(wallSet);
				if (tmpPoint1 != null) endPoints.put(angle - 0.00001, tmpPoint1);
				if (tmpPoint2 != null) endPoints.put(angle + 0.00001, tmpPoint2);
			}

			// Transform endPoints into an Area
			List<Integer> stockTriangleX = new ArrayList<Integer>();
			List<Integer> stockTriangleY = new ArrayList<Integer>();
			for (Entry<Double, Point2D> entry : endPoints.entrySet())
			{
				stockTriangleX.add(GameUtils.toInt(entry.getValue().getX()));
				stockTriangleY.add(GameUtils.toInt(entry.getValue().getY()));
			}
			int[] triangleX = new int[stockTriangleX.size()];
			triangleX = toIntArray(stockTriangleX);
			int[] triangleY = new int[stockTriangleY.size()];
			triangleY = toIntArray(stockTriangleY);

			Area tmpFieldOfView = new Area(new Polygon(triangleX, triangleY, triangleX.length));
			tmpFieldOfView.intersect(new Area(new Ellipse2D.Float(lightSource.getX() + lightSource.getWidth() / 2 - lightSource.getLightIntensity(),
					lightSource.getY() + lightSource.getHeight() / 2 - lightSource.getLightIntensity(), lightSource.getLightIntensity() * 2, lightSource
							.getLightIntensity() * 2)));
			if (this.fieldOfView == null) this.fieldOfView = tmpFieldOfView;
			else this.fieldOfView.add(tmpFieldOfView);
		}
	}
}