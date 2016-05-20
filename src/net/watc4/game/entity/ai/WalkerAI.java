package net.watc4.game.entity.ai;

import net.watc4.game.entity.Entity;
import net.watc4.game.map.Map;
import net.watc4.game.map.Tile;
import net.watc4.game.map.TileRegistry;
import net.watc4.game.map.tiles.TileLadder;
import net.watc4.game.utils.GameUtils;

public class WalkerAI extends BasicAI
{

	private boolean passedWall = false;
	private float wall = -1;

	public WalkerAI(Entity entity)
	{
		super(entity);
	}

	/** @return True if this AI jumps. */
	public boolean jump()
	{
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
	public void update()
	{
		int move = 0;
		boolean jumpPressed = this.jump(), up = this.up(), down = this.down(), left = this.left(), right = this.right();
		boolean ladderMovement = up || down;

		if (left) move--;
		if (right) move++;
		if (ladderMovement && this.entity.getOccupiedTile() instanceof TileLadder) this.entity.onLadder = true;
		if (down && this.entity.getAdjacentTile(GameUtils.DOWN) == TileRegistry.LADDER_TOP)
		{
			this.entity.onLadder = true;
			if (!this.entity.placeFree(0, 1)) this.entity.onLadder = false;
			if (this.entity.isOnLadder()) this.entity.canJump = true;
		}
		if (this.entity.isOnLadder())
		{
			if (up) this.entity.ySpeed = -this.entity.moveSpeed * 1.5f;
			else if (down)
			{
				this.entity.ySpeed = this.entity.moveSpeed * 1.5f;
				if (!this.entity.placeFree(0, this.entity.ySpeed)) this.entity.onLadder = false;
			} else this.entity.ySpeed = 0;
		}
		this.entity.xSpeed = move * this.entity.moveSpeed;
		if (move != 0) this.entity.direction = move;
		if (!this.entity.placeFree(0, 1))
		{
			this.entity.jumpingTime = 0;
			if (jumpPressed && this.entity.canJump)
			{
				this.entity.canJump = false;
				this.entity.ySpeed = -this.entity.moveSpeed * 2;
			}
			if (!jumpPressed) this.entity.canJump = true;
		} else this.entity.jumpingTime++;

		if (this.entity.isOnLadder() && jumpPressed && !ladderMovement)
		{
			this.entity.canJump = false;
			this.entity.ySpeed = -this.entity.moveSpeed * 3 / 2;
			this.entity.onLadder = false;
		}

		if (!jumpPressed && !this.entity.isOnLadder())
		{
			this.entity.ySpeed = Math.max(this.entity.ySpeed, -this.entity.moveSpeed / 4);
		}

	}
}
