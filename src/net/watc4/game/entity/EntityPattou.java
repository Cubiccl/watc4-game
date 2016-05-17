package net.watc4.game.entity;

import net.watc4.game.Game;
import net.watc4.game.display.renderer.PattouRenderer;
import net.watc4.game.map.Map;
import net.watc4.game.map.Tile;
import net.watc4.game.map.TileRegistry;
import net.watc4.game.map.tiles.TileLadder;
import net.watc4.game.states.GameOverState;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.GameSettings;
import net.watc4.game.utils.GameUtils;

/** Second player : must stay in the shadows and is affected by gravity. */
public class EntityPattou extends EntityPlayer
{

	/** Lumi's AI. */
	public class AIPattou extends AIPlayer
	{

		private boolean passedWall = false;
		private float wall = -1;

		public AIPattou(EntityPattou entity)
		{
			super(entity);
		}

		@Override
		public boolean down()
		{
			if (!this.entity.game.isInCutscene) return Game.getGame().getControls().p_down.isPressed();
			return super.down();
		}

		/** @return True if this AI jumps. */
		public boolean jump()
		{
			if (!this.entity.game.isInCutscene) return Game.getGame().getControls().p_jump.isPressed();
			if (this.destination == null) return false;
			if (Math.abs(this.destination[0] - this.entity.getX()) < Map.TILESIZE * 2) return false;

			boolean left = this.left(), right = this.right();

			Tile tile = null;
			if (left)
			{
				tile = this.entity.getAdjacentTile(GameUtils.LEFT);
			} else if (right)
			{
				tile = this.entity.getAdjacentTile(GameUtils.RIGHT);
			}

			if (tile != null && tile.isSolid)
			{
				if (left) this.wall = ((this.entity.getCenter()[0] / Map.TILESIZE) * Map.TILESIZE) - 1;
				if (right) this.wall = ((this.entity.getCenter()[0] / Map.TILESIZE) * Map.TILESIZE) + 2;
				this.passedWall = false;
			}

			if (this.wall != -1 && !this.passedWall && ((left && this.entity.getCenter()[0] < this.wall) || (right && this.entity.getCenter()[0] > this.wall)))
			{
				this.passedWall = true;
				this.wall = -1;
			}
			return (this.wall != -1) && !this.passedWall;
		}

		@Override
		public boolean left()
		{
			if (!this.entity.game.isInCutscene) return Game.getGame().getControls().p_left.isPressed();
			return super.left();
		}

		@Override
		public boolean right()
		{
			if (!this.entity.game.isInCutscene) return Game.getGame().getControls().p_right.isPressed();
			return super.right();
		}

		@Override
		public boolean up()
		{
			if (!this.entity.game.isInCutscene) return Game.getGame().getControls().p_up.isPressed();
			return super.up();
		}
	}

	private static final float JUMP_SPEED = 8, LADDER_JUMP_SPEED = 6;
	private static final float MOVE_SPEED = 3, LADDER_SPEED = 1.7f;

	/** True if Pattou can jump */
	private boolean canJump;
	/** The Door Pattou is standing in. null if none. */
	private EntityDoor door;
	/** Time, in UPS, for the begin of the jump */
	private int jumpingTime;

	public EntityPattou()
	{
		this(null, 0, 0);
	}

	public EntityPattou(GameState game, float xPos, float yPos)
	{
		super(game, xPos, yPos, 1);
		this.setRenderer(new PattouRenderer(this));
		this.width = 20;
		this.height = 32;
		this.canJump = true;
		this.jumpingTime = 0;
		this.direction = -1;
		this.ai = new AIPattou(this);
	}

	public int getJumpingTime()
	{
		return this.jumpingTime;
	}

	/** Kill Pattou and lumi - reset Pattou Spawn and lumi Spawn - reset Hitbox at Pattou Spawn and Lumi Spawn */
	public void kill()
	{
		Game.getGame().setCurrentState(new GameOverState(this.game), false);
	}

	/** Checks for movement input and applies it. */
	private void manageInput()
	{
		int move = 0;
		boolean jumpPressed = ((AIPattou) this.ai).jump(), up = ((AIPattou) this.ai).up(), down = ((AIPattou) this.ai).down(), left = ((AIPattou) this.ai)
				.left(), right = ((AIPattou) this.ai).right();
		boolean ladderMovement = up || down;

		if (this.door != null && up && this.canJump)
		{
			this.door.activate();
			return;
		}
		if (left) move--;
		if (right) move++;
		if (ladderMovement && this.getOccupiedTile() instanceof TileLadder) this.onLadder = true;
		if (down && this.getAdjacentTile(GameUtils.DOWN) == TileRegistry.LADDER_TOP)
		{
			this.onLadder = true;
			if (!this.placeFree(0, 1)) this.onLadder = false;
			if (this.isOnLadder()) this.canJump = true;
		}
		if (this.isOnLadder())
		{
			if (up) this.ySpeed = -LADDER_SPEED;
			else if (down)
			{
				this.ySpeed = LADDER_SPEED;
				if (!this.placeFree(0, this.ySpeed)) this.onLadder = false;
			} else this.ySpeed = 0;
		}
		this.xSpeed = move * MOVE_SPEED;
		if (move != 0) this.direction = move;
		if (!placeFree(0, 1))
		{
			this.jumpingTime = 0;
			if (jumpPressed && canJump)
			{
				this.canJump = false;
				this.ySpeed = -JUMP_SPEED;
			}
			if (!jumpPressed) canJump = true;
		} else this.jumpingTime++;

		if (this.isOnLadder() && jumpPressed && !ladderMovement)
		{
			this.canJump = false;
			this.ySpeed = -LADDER_JUMP_SPEED;
			this.onLadder = false;
		}

		if (!jumpPressed && !this.isOnLadder())
		{
			this.ySpeed = Math.max(ySpeed, -JUMP_SPEED / 4);
		}
	}

	@Override
	public void onCollisionEndWith(Entity entity)
	{
		super.onCollisionEndWith(entity);
		if (entity instanceof EntityDoor) this.door = null;
	}

	@Override
	public void onCollisionWith(Entity entity)
	{
		super.onCollisionWith(entity);
		if (entity instanceof EntityDoor) this.door = (EntityDoor) entity;
	}

	@Override
	public void update()
	{
		this.manageInput();

		if (!GameSettings.godMode && this.game.entityLumi.isInLight(this)) --this.health;
		else
		{
			++this.health;
			if (this.health > MAX_HEALTH) this.health = MAX_HEALTH;
		}
		if (this.health < 0) this.kill();
		super.update();
	}

}
