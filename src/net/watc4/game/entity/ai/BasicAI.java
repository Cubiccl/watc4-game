package net.watc4.game.entity.ai;

import net.watc4.game.entity.Entity;
import net.watc4.game.map.TileRegistry;
import net.watc4.game.utils.GameUtils;

public class BasicAI extends AI
{

	public BasicAI(Entity entity)
	{
		super(entity);
	}

	/** @return True if this Player is moving down. */
	public boolean down()
	{
		if (this.destination != null) return this.destination[1] > this.entity.getY() && Math.abs(this.entity.getY() - this.destination[1]) > EPSILON;
		return false;
	}

	/** @return True if this Player is moving left. */
	public boolean left()
	{
		if (this.destination != null) return this.destination[0] < this.entity.getX() && Math.abs(this.entity.getX() - this.destination[0]) > EPSILON;
		return false;
	}

	/** @return True if this Player is moving right. */
	public boolean right()
	{
		if (this.destination != null) return this.destination[0] > this.entity.getX() && Math.abs(this.entity.getX() - this.destination[0]) > EPSILON;
		return false;
	}

	/** @return True if this Player is moving up. */
	public boolean up()
	{
		if (this.destination != null) return this.destination[1] < this.entity.getY() && Math.abs(this.entity.getY() - this.destination[1]) > EPSILON;
		return false;
	}

	@Override
	public void update()
	{
		super.update();
		boolean up = this.up(), down = this.down(), left = this.left(), right = this.right();

		float hMove = 0;
		float vMove = 0;

		if (up) vMove--;
		if (down) vMove++;
		if (left) hMove--;
		if (right) hMove++;

		if (down && this.entity.getAdjacentTile(GameUtils.DOWN) == TileRegistry.LADDER_TOP)
		{
			this.entity.onLadder = true;
			if (!this.entity.placeFree(0, 1)) this.entity.onLadder = false;
		}

		boolean doubleInput = (up && right) || (up && left) || (down && right) || (down && left);
		float multiplier = 1;
		if (doubleInput) multiplier = 0.7f;
		this.entity.ySpeed = vMove * this.entity.moveSpeed * multiplier;
		this.entity.xSpeed = hMove * this.entity.moveSpeed * multiplier;
	}

}
