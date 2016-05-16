package net.watc4.game.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import net.watc4.game.display.renderer.EntityRenderer;
import net.watc4.game.entity.ai.AI;
import net.watc4.game.map.Map;
import net.watc4.game.map.Tile;
import net.watc4.game.map.TileRegistry;
import net.watc4.game.map.tiles.TileLadder;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.GameSettings;
import net.watc4.game.utils.GameUtils;
import net.watc4.game.utils.IRender;
import net.watc4.game.utils.IUpdate;
import net.watc4.game.utils.geometry.Hitbox;
import net.watc4.game.utils.geometry.RectangleHitbox;

/** Represents a moving object in the world. i.e. A monster, a moving block, etc. */
public abstract class Entity implements IRender, IUpdate
{
	public static final float DEFAULT_SIZE = 32;
	private static final float GRAVITY = 0.4f;

	/** Defines this Entity's behavior. */
	public AI ai;
	/** The direction that the entity is facing 1:Right, -1:Left */
	protected int direction;
	/** Reference to the GameState. */
	public final GameState game;
	/** True if this Entity is affected by Gravity. False if it flies. */
	protected boolean hasGravity;
	/** True if this Entity moved last update. */
	public boolean hasMoved;
	/** True if this Entity is solid, as if it were a Solid Tile. */
	protected boolean isSolid;
	/** True if this Entity is standing on a Ladder. */
	protected boolean onLadder;
	/** Renders the Entity onto the screen. */
	private EntityRenderer renderer;
	/** Unique Universal IDentifier for this Entity. Each Entity should have a unique ID in a map. 0 is reserved for Lumi and 1 for Pattou. <br />
	 * For better comprehension, please always use Entity ID * 100 + whatever. Example : Battery can be from 200 to 299, and Cutscene from 300 to 399. */
	public final int UUID;
	/** The width/height of the entity. */
	protected int width, height;
	/** Its x and y positions. Topleft of the Entity. */
	private float xPos, yPos;
	/** Its x and y speed. */
	protected float xSpeed, ySpeed;

	/** Creates a new Entity without parameters. Useful for the level editor. */
	public Entity()
	{
		this(null, 0, 0, 0);
	}

	/** Creates a new Entity.
	 * 
	 * @param game - A reference to the GameState.
	 * @param UUID - The Unique Universal IDentifier of this Entity.
	 * @param xPos - Its x position.
	 * @param yPos - Its y position. */
	public Entity(GameState game, float xPos, float yPos, int UUID)
	{
		this.game = game;
		this.UUID = UUID;
		this.xPos = xPos;
		this.yPos = yPos;
		this.xSpeed = 0;
		this.ySpeed = 0;
		this.hasGravity = true;
		this.isSolid = false;
		this.renderer = new EntityRenderer(this);
		this.width = (int) DEFAULT_SIZE;
		this.height = (int) DEFAULT_SIZE;
	}

	/** @param entity - The Entity to test.
	 * @return The angle in degrees (=orientation) of the segment between this and the given Entity. Determines where the Entity is compared to this one.<br />
	 *         0/360 is Up, 90 is Right, 180 is Down, 270 is Left. */
	public double angleTo(Entity entity)
	{
		return (Math.atan2(entity.getCenter()[1] - this.getCenter()[1], entity.getCenter()[0] - this.getCenter()[0]) + Math.PI / 2) * 180 / Math.PI;
	}

	/** @param entity - The Entity to test.
	 * @return True if the given Entity collides with this Entity. */
	public boolean collidesWith(Entity entity)
	{
		return this.collidesWith(entity, 0, 0);
	}

	/** @param entity - The Entity to test.
	 * @param dx - Its future offset.
	 * @param dy - Its future offset.
	 * @return True if the given Entity would collide with this Entity if it were to move to the given offsets.. */
	public boolean collidesWith(Entity entity, float dx, float dy)
	{
		return this.hitbox().collidesWith(entity.hitbox(dx, dy));
	}

	/** test if a point is contained by the hitbox
	 * 
	 * @param x coordinate of the point
	 * @param y coordinate of the point
	 * @return true if the point is contained, false if not */
	public boolean contains(int x, int y)
	{
		return this.hitbox().contains(new Point(x, y));
	}

	/** @param entity - Another Entity.
	 * @return The distance between this and a given Entity. */
	public double distanceTo(Entity entity)
	{
		return Math.sqrt(Math.pow(this.getCenter()[0] - entity.getCenter()[0], 2) + Math.pow(this.getCenter()[1] - entity.getCenter()[1], 2));
	}

	/** @param direction - The Direction to look at.
	 * @return The Tile adjacent to the occupied, in the given direction. Defaults to the Occupied Tile.
	 * @see GameUtils#UP */
	public Tile getAdjacentTile(int direction)
	{
		int x = (int) this.getCenter()[0] / Map.TILESIZE, y = (int) this.getCenter()[1] / Map.TILESIZE;
		if (direction == GameUtils.UP) --y;
		if (direction == GameUtils.DOWN) ++y;
		if (direction == GameUtils.LEFT) --x;
		if (direction == GameUtils.RIGHT) ++x;
		return this.game.getMap().getTileAt(x, y);
	}

	/** @return The coordinates of the Center of this Entity. */
	public float[] getCenter()
	{
		return new float[]
		{ this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2 };
	}

	public int getDirection()
	{
		return this.direction;
	}

	public float getHeight()
	{
		return this.height;
	}

	/** @return The Tile this Entity is currently occuping. */
	public Tile getOccupiedTile()
	{
		return this.getAdjacentTile(-1);
	}

	/** @return This Entity's EntityRenderer. */
	public EntityRenderer getRenderer()
	{
		return this.renderer;
	}

	public float getWidth()
	{
		return this.width;
	}

	/** @return The X position of this Entity. */
	public float getX()
	{
		return this.xPos;
	}

	/** @return This Entity's horizontal speed. */
	public float getXSpeed()
	{
		return this.xSpeed;
	}

	/** @return The Y position of this Entity. */
	public float getY()
	{
		return this.yPos;
	}

	/** @return This Entity's vertical speed. */
	public float getYSpeed()
	{
		return this.ySpeed;
	}

	/** @return This Entity's Hitbox. */
	public Hitbox hitbox()
	{
		return this.hitbox(0, 0);
	}

	/** @return This Entity's Hitbox, assuming that it had move by dx and dy. */
	public Hitbox hitbox(double dx, double dy)
	{
		return new RectangleHitbox((int) (this.getX() + dx), (int) (this.getY() + dy), (int) this.getWidth(), (int) this.getHeight());
	}

	/** @return True if this Entity is standing on a Ladder. */
	public boolean isOnLadder()
	{
		return this.onLadder;
	}

	/** Destroys this Entity. */
	public void kill()
	{
		this.game.getMap().entityManager.unregisterEntity(this);
	}

	/** Called when this Entity collides with the given Entity.
	 * 
	 * @param entity - The colliding entity.
	 * @see Entity#angleTo(Entity) angleTo(Entity) for positionning */
	public void onCollisionWith(Entity entity)
	{}

	/** @param dx - delta x
	 * @param dy - delta y
	 * @return true if the entity doesn't collide with a solid tile at {@linkplain Entity#xPos xPos} + dx, {@linkplain Entity#yPos yPos} + dy */
	public boolean placeFree(float dx, float dy)
	{
		int tileXStart = (int) ((this.xPos + dx) / Map.TILESIZE), tileYStart = (int) ((this.yPos + dy) / Map.TILESIZE);
		int tileXEnd = (int) ((this.xPos + dx + this.width - 1) / Map.TILESIZE + 1);
		int tileYEnd = (int) ((this.yPos + dy + this.height - 1) / Map.TILESIZE + 1);
		for (int x = tileXStart; x < tileXEnd; ++x)
		{
			for (int y = tileYStart; y < tileYEnd; ++y)
			{
				if (this.game.getMap().getTileAt(x, y).isSolid && this.game.getMap().getTileAt(x, y).hitbox(x, y).collidesWith(this.hitbox(dx, dy))) return false;
			}
		}

		// Test if on top of ladder
		if (dy > 0 && !this.onLadder && this.game.getMap().getTileAt((int) ((this.xPos + dx) / Map.TILESIZE), tileYEnd - 1) == TileRegistry.LADDER_TOP
				&& (this.yPos + dy + this.height - 1) % Map.TILESIZE < Map.TILESIZE / 6) return false;
		return this.game.getMap().entityManager.canEntityMove(this, dx, dy);
	}

	@Override
	public void render(Graphics2D g)
	{
		if (this.renderer != null) this.renderer.render(g);
		if (GameSettings.drawHitboxes)
		{
			if (this instanceof EntityCutscene) this.hitbox().render(g, Color.YELLOW);
			else this.hitbox().render(g, Color.BLUE);
		}
	}

	public void setPosition(float x, float y)
	{
		this.xSpeed = 0;
		this.ySpeed = 0;
		this.xPos = x;
		this.yPos = y;
		this.hasMoved = true;
	}

	/** Changes this Entity's renderer.
	 * 
	 * @param renderer - The new Renderer to use. */
	public void setRenderer(EntityRenderer renderer)
	{
		this.renderer.setAnimation(null);
		this.renderer = renderer;
	}

	public void setX(float xPos)
	{
		this.xPos = xPos;
	}

	public void setY(float yPos)
	{
		this.yPos = yPos;
	}

	@Override
	public void update()
	{
		float xPrev = this.xPos, yPrev = this.yPos;

		if (this.isOnLadder() && !(this.getOccupiedTile() instanceof TileLadder))
		{
			if (!(this.getAdjacentTile(GameUtils.DOWN) == TileRegistry.LADDER_TOP && this.getCenter()[1] % Map.TILESIZE > Map.TILESIZE / 2)) this.onLadder = false;
		}

		if (hasGravity && !this.onLadder)
		{
			ySpeed += GRAVITY;
		}

		if (!placeFree(xSpeed, 0))
		{
			while (placeFree(Math.signum(xSpeed), 0))
				xPos += Math.signum(xSpeed);
			xSpeed = 0;
		}

		if (!placeFree(0, ySpeed))
		{
			while (placeFree(0, Math.signum(ySpeed)))
				yPos += Math.signum(ySpeed);
			ySpeed = 0;
		}
		xPos += xSpeed;
		yPos += ySpeed;

		if (xPrev != this.xPos || yPrev != this.yPos) this.hasMoved = true;
	}
}
