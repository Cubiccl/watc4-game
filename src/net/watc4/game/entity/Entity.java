package net.watc4.game.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashSet;

import net.watc4.game.display.renderer.EntityRenderer;
import net.watc4.game.entity.ai.AI;
import net.watc4.game.entity.ai.BasicAI;
import net.watc4.game.listener.IEntityMovementListener;
import net.watc4.game.listener.ILightChangeListener;
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
public abstract class Entity implements IRender, IUpdate, IEntityMovementListener, ILightChangeListener
{

	public static final float DEFAULT_SIZE = 32;
	private static final float GRAVITY = 0.4f;

	/** Defines this Entity's behavior. */
	public AI ai;
	/** True if Pattou can jump */
	public boolean canJump;
	/** A list of the UUIDs this Entity is colliding with. */
	protected HashSet<Integer> colliding;
	/** The direction that the entity is facing 1:Right, -1:Left */
	public int direction;
	/** Reference to the GameState. */
	public final GameState game;
	/** True if this Entity is affected by Gravity. False if it flies. */
	protected boolean hasGravity;
	/** The health points of this Entity. */
	private int health;
	/** True if this Entity is affected by Lumi's light. */
	private boolean isAffectedByLight;
	/** True if this Entity is in Lumi's light. */
	private boolean isInLight;
	/** True if this Entity is invulnerable ; thus it will not take damage. It can still be killed. */
	protected boolean isInvulnerable;
	/** True if this Entity is moveable. */
	protected boolean isMoveable = false;
	/** True if this Entity is solid, as if it were a Solid Tile. */
	protected boolean isSolid;
	/** Time, in UPS, for the begin of the jump */
	public int jumpingTime;
	/** The maximum health of this Entity. */
	private int maxHealth;
	/** Listeners to this Entity's movement. */
	private HashSet<IEntityMovementListener> movementListeners;
	/** This Entity's movement speed. */
	public int moveSpeed;
	/** True if this Entity is standing on a Ladder. */
	public boolean onLadder;
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
	public float xSpeed, ySpeed;

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
		this.isAffectedByLight = false;
		this.isInvulnerable = false;
		this.moveSpeed = 4;
		this.canJump = true;
		this.jumpingTime = 0;
		this.setMaxHealth(20);
		this.heal(20);
		this.renderer = new EntityRenderer(this);
		this.width = (int) DEFAULT_SIZE;
		this.height = (int) DEFAULT_SIZE;
		this.colliding = new HashSet<Integer>();
		this.movementListeners = new HashSet<IEntityMovementListener>();
		this.ai = new BasicAI(this);
	}

	/** @param listener - The Listener. Will be called when this Entity moves. */
	public void addMovementListener(IEntityMovementListener listener)
	{
		this.movementListeners.add(listener);
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

	/** Deals damage to this Entity. If health reaches 0, this Entity dies.
	 * 
	 * @param damage - The amount of damage to deal. */
	public void dealDamage(int damage)
	{
		if (this.isInvulnerable) return;
		this.health -= damage;
		if (this.health <= 0)
		{
			this.health = 0;
			this.kill();
		}
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

	/** @return The health points of this Player. */
	public int getHealth()
	{
		return this.health;
	}

	public float getHeight()
	{
		return this.height;
	}

	/** @return This Entity's maximum health. */
	public int getMaxHealth()
	{
		return maxHealth;
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

	/** Restores health to this Entity. Will not go higher than its maximum health.
	 * 
	 * @param health - The amount of health to restore. */
	public void heal(int health)
	{
		this.health += health;
		if (this.health > this.maxHealth) this.health = this.maxHealth;
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

	/** @return True if this Entity is affected bu Lumi's light. */
	public boolean isAffectedByLight()
	{
		return isAffectedByLight;
	}

	/** @return True if this Entity is in Lumi's light. */
	public boolean isInLight()
	{
		return isInLight;
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

	/** Called when this Entity stops colliding with the given Entity.
	 * 
	 * @param entity - The colliding entity. */
	public void onCollisionEndWith(Entity entity)
	{
		this.colliding.remove(entity.UUID);
	}

	/** Called when this Entity starts colliding with the given Entity.
	 * 
	 * @param entity - The colliding entity.
	 * @see Entity#angleTo(Entity) angleTo(Entity) for positionning */
	public void onCollisionWith(Entity entity)
	{
		this.colliding.add(entity.UUID);
	}

	/** Called when this Entity enters Lumi's light.
	 * 
	 * @param lumi - Lumi. */
	public void onEnterLight(EntityLumi lumi)
	{}

	@Override
	public void onEntityMove(Entity entity)
	{
		if (entity == this)
		{
			for (IEntityMovementListener listener : this.movementListeners)
				listener.onEntityMove(this);
			if (this.isAffectedByLight()) this.updateLight(this.game.entityLumi);
		}
	}

	/** Called when this Entity exits Lumi's light.
	 * 
	 * @param lumi - Lumi. */
	public void onExitLight(EntityLumi lumi)
	{}

	@Override
	public void onLightChange(EntityLumi lumi)
	{
		this.updateLight(lumi);
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
				if (this.game.getMap().getTileAt(x, y).isSolid
						&& this.game.getMap().getTileAt(x, y).hitbox(this.game.getMap(), x, y, this.game.getMap().getDataAt(x, y))
								.collidesWith(this.hitbox(dx, dy))) return false;
			}
		}

		// Test if on top of ladder
		if (dy > 0 && !this.onLadder && this.game.getMap().getTileAt((int) ((this.xPos + dx) / Map.TILESIZE), tileYEnd - 1) == TileRegistry.LADDER_TOP
				&& (this.yPos + dy + this.height - 1) % Map.TILESIZE < Map.TILESIZE / 6) return false;
		return this.game.getMap().entityManager.canEntityMove(this, dx, dy);
	}

	/** @param listener - The Listener to remove. */
	public void removeMovementListener(IEntityMovementListener listener)
	{
		this.movementListeners.remove(listener);
	}

	@Override
	public void render(Graphics2D g)
	{
		if (this.renderer != null) this.renderer.render(g);
		if (GameSettings.drawHitboxes)
		{
			Color color = Color.BLUE;
			if (this instanceof EntityCutscene) color = Color.YELLOW;
			if (this instanceof EntityEndLevel) color = Color.GREEN;
			this.hitbox().render(g, color);
		}
	}

	/** @param isAffectedByLight - True if this Entity is affected by Lumi's light. */
	public void setAffectedByLight(boolean isAffectedByLight)
	{
		if (this.game == null) return;
		this.isAffectedByLight = isAffectedByLight;
		if (this.isAffectedByLight()) this.game.addLightChangeListener(this);
		else this.game.removeLightChangeListener(this);
	}

	/** Sets this Entity's maximum health.
	 * 
	 * @param maxHealth - The new maximum health. */
	public void setMaxHealth(int maxHealth)
	{
		this.maxHealth = maxHealth;
		this.health = this.maxHealth;
	}

	public void setPosition(float x, float y)
	{
		this.xSpeed = 0;
		this.ySpeed = 0;
		this.xPos = x;
		this.yPos = y;
		this.onEntityMove(this);
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

		if (this.ai != null) this.ai.update();

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
		xPos += xSpeed;

		if (!placeFree(0, ySpeed))
		{
			while (placeFree(0, Math.signum(ySpeed)))
				yPos += Math.signum(ySpeed);
			ySpeed = 0;
		}
		yPos += ySpeed;

		if (xPrev != this.xPos || yPrev != this.yPos) this.onEntityMove(this);

	}

	/** Calculates if this is in Lumi's light. */
	private void updateLight(EntityLumi lumi)
	{
		boolean wasInLight = this.isInLight;
		this.isInLight = lumi.isInLight(this);
		if (this.isInLight && !wasInLight) this.onEnterLight(lumi);
		if (!this.isInLight && wasInLight) this.onExitLight(lumi);
	}
}
