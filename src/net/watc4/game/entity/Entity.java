package net.watc4.game.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.HashSet;

import net.watc4.game.display.renderer.EntityRenderer;
import net.watc4.game.map.Chunk;
import net.watc4.game.map.Map;
import net.watc4.game.map.Tile;
import net.watc4.game.map.TileRegistry;
import net.watc4.game.map.tiles.TileLadder;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.GameSettings;
import net.watc4.game.utils.GameUtils;
import net.watc4.game.utils.IRender;
import net.watc4.game.utils.IUpdate;

/** Represents a moving object in the world. i.e. A monster, a moving block, etc. */
public abstract class Entity implements IRender, IUpdate
{
	public static final float DEFAULT_SIZE = 32;

	private static final float GRAVITY = 0.4f;
	/** The direction that the entity is facing 1:Rigth, -1:Left */
	protected int direction;
	/** Reference to the GameState. */
	public final GameState game;
	/** True if this Entity is affected by Gravity. False if it flies. */
	protected boolean hasGravity;
	/** True if this Entity is solid, as if it were a Solid Tile. */
	protected boolean isSolid;
	/** True if this Entity is standing on a Ladder. */
	protected boolean onLadder;
	/** Renders the Entity onto the screen. */
	private EntityRenderer renderer;
	/** The width/height of the entity. */
	protected int width, height;
	/** Its x and y positions. Topleft of the Entity. */
	private float xPos, yPos;
	/** Its x and y speed. */
	protected float xSpeed, ySpeed;

	/** Creates a new Entity.
	 * 
	 * @param xPos - Its x position.
	 * @param yPos - Its y position.
	 * @param game - A reference to the GameState. */
	public Entity(float xPos, float yPos, GameState game)
	{
		this.xPos = xPos;
		this.yPos = yPos;
		this.xSpeed = 0;
		this.ySpeed = 0;
		this.hasGravity = true;
		this.isSolid = false;
		this.game = game;
		this.game.entityManager.registerEntity(this);
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
		float x = entity.xPos + dx, y = entity.yPos + dy;
		return this.hitbox().intersects(new Rectangle((int) x, (int) y, (int) entity.getWidth(), (int) entity.getHeight()));
	}

	/** test if a point is contained by the hitbox
	 * 
	 * @param x coordinate of the point
	 * @param y coordinate of the point
	 * @return true if the point is contained, false if not */
	public boolean contains(int x, int y)
	{
		return this.hitbox().contains(x, y);
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

	/** @return The Chunk(s) that contain this Entity. */
	public Chunk[] getChunks()
	{
		HashSet<Chunk> containers = new HashSet<Chunk>();
		Chunk current = this.game.getMap().getChunk(this.getX(), this.getY());
		containers.add(current);
		current = this.game.getMap().getChunk(this.getX() + this.getWidth(), this.getY());
		containers.add(current);
		current = this.game.getMap().getChunk(this.getX(), this.getY() + this.getHeight());
		containers.add(current);
		current = this.game.getMap().getChunk(this.getX() + this.getWidth(), this.getY() + this.getHeight());
		containers.add(current);
		return containers.toArray(new Chunk[containers.size()]);
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
	private Rectangle hitbox()
	{
		return new Rectangle((int) this.getX(), (int) this.getY(), (int) this.getWidth(), (int) this.getHeight());
	}

	/** @return True if this Entity is standing on a Ladder. */
	public boolean isOnLadder()
	{
		return this.onLadder;
	}

	/** Destroys this Entity. */
	public void kill()
	{
		this.game.entityManager.unregisterEntity(this);
	}

	/** Called when this Entity collides with the given Entity.
	 * 
	 * @param entity - The colliding entity.
	 * @see Entity#angleTo(Entity) angleTo(Entity) for positionning */
	public void onCollisionWith(Entity entity)
	{
		System.out.println(entity.getClass().getName());
	}

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
				if (this.game.getMap().getTileAt(x, y).isSolid) return false;
			}
			// Test if on top of ladder
			if (dy > 0 && !this.onLadder && this.game.getMap().getTileAt(x, tileYEnd - 1) == TileRegistry.LADDER_TOP
					&& (this.yPos + dy + this.height - 1) % Map.TILESIZE < Map.TILESIZE / 6) return false;
		}

		return this.game.entityManager.canEntityMove(this, dx, dy);
	}

	@Override
	public void render(Graphics g)
	{
		this.renderer.render(g);
		if (GameSettings.drawHitboxes)
		{
			g.setColor(Color.BLUE);
			g.drawRect((int) this.getX(), (int) this.getY(), (int) this.getWidth(), (int) this.getHeight());
		}
	}

	public void setPosition(int x, int y)
	{
		this.xSpeed = 0;
		this.ySpeed = 0;
		this.xPos = x;
		this.yPos = y;
	}

	/** Changes this Entity's renderer.
	 * 
	 * @param renderer - The new Renderer to use. */
	public void setRenderer(EntityRenderer renderer)
	{
		this.renderer.setAnimation(null);
		this.renderer = renderer;
	}

	/** @return True if this Entity should render (i.e. it belongs to a rendered Chunk) */
	public boolean shouldRender()
	{
		Chunk[] chunks = this.getChunks();
		for (Chunk chunk : chunks)
			if (chunk.shouldRender()) return true;

		return false;
	}

	/** @return True if this Entity should update (i.e. it belongs to a updated Chunk) */
	public boolean shouldUpdate()
	{
		Chunk[] chunks = this.getChunks();
		for (Chunk chunk : chunks)
			if (chunk.shouldUpdate()) return true;

		return false;
	}

	@Override
	public void update()
	{
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
	}
}
