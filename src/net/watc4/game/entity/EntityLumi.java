package net.watc4.game.entity;

import net.watc4.game.Game;
import net.watc4.game.display.renderer.LumiRenderer;
import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.GameUtils;

/** First Player : can fly and spreads light. */
public class EntityLumi extends EntityPlayer
{

	/** Lumi's AI. */
	public class AILumi extends AIPlayer
	{
		public AILumi(EntityLumi entity)
		{
			super(entity);
		}

		@Override
		public boolean down()
		{
			if (!this.entity.game.isInCutscene) return Game.getGame().isKeyPressed(GameUtils.LUMI_DOWN);
			return super.down();
		}

		@Override
		public boolean left()
		{
			if (!this.entity.game.isInCutscene) return Game.getGame().isKeyPressed(GameUtils.LUMI_LEFT);
			return super.left();
		}

		@Override
		public boolean right()
		{
			if (!this.entity.game.isInCutscene) return Game.getGame().isKeyPressed(GameUtils.LUMI_RIGHT);
			return super.right();
		}

		@Override
		public boolean up()
		{
			if (!this.entity.game.isInCutscene) return Game.getGame().isKeyPressed(GameUtils.LUMI_UP);
			return super.up();
		}
	}

	public static final int LIGHT_INTENSITY = 400;
	private static final int MOVE_SPEED = 5;

	public EntityLumi(GameState game, float xPos, float yPos)
	{
		super(game, xPos, yPos);
		this.hasGravity = false;
		this.setRenderer(new LumiRenderer(this));
		this.ai = new AILumi(this);
	}
	
	public EntityLumi()
	{
		this(null, 0, 0);
	}

	/** @param entity - The Entity to test.
	 * @return True if the input Entity is in the light of Lumi. */
	public boolean isInLight(Entity entity)
	{
		return this.isInLight(entity.getCenter()[0], entity.getCenter()[1]);
	}

	/** @param x - The X Coordinate.
	 * @param y - The Y Coordinate.
	 * @return True if the given coordinates are in the light emitted by Lumi. */
	public boolean isInLight(float x0, float y0)
	{
		if (Math.sqrt(Math.pow(this.getCenter()[0] - x0, 2) + Math.pow(this.getCenter()[1] - y0, 2)) > LIGHT_INTENSITY) return false;

		int dx = (int) Math.abs(this.getX() + this.getWidth() / 2 - x0);
		int dy = (int) Math.abs(this.getY() + this.getHeight() / 2 - y0);
		int x = (int) x0;
		int y = (int) y0;
		int n = 1 + dx + dy;
		int x_inc = (this.getX() > x0) ? 1 : -1;
		int y_inc = (this.getY() > y0) ? 1 : -1;
		int error = dx - dy;
		dx *= 2;
		dy *= 2;

		for (; n > 0; --n)
		{
			if (this.game.getMap().getTileAt(x / Map.TILESIZE, y / Map.TILESIZE).isOpaque) return false;

			if (error > 0)
			{
				x += x_inc;
				error -= dy;
			} else
			{
				y += y_inc;
				error += dx;
			}
		}
		return true;
	}

	public void kill()
	{
		this.setPosition(this.game.getMap().lumiSpawnX, this.game.getMap().lumiSpawnY);
	}

	/** Checks for movement input and applies it. */
	private void manageInput()
	{
		boolean up = ((AILumi) this.ai).up(), down = ((AILumi) this.ai).down(), left = ((AILumi) this.ai).left(), right = ((AILumi) this.ai).right();
		float hMove = 0;
		float vMove = 0;

		if (up) vMove--;
		if (down) vMove++;
		if (left) hMove--;
		if (right) hMove++;

		boolean doubleInput = (up && right) || (up && left) || (down && right) || (down && left);
		float multiplier = 1;
		if (doubleInput) multiplier = 0.7f;
		this.ySpeed = vMove * MOVE_SPEED * multiplier;
		this.xSpeed = hMove * MOVE_SPEED * multiplier;
	}

	@Override
	public void setPosition(float x, float y)
	{
		super.setPosition(x, y);
		this.game.getMap().lightManager.update();
	}

	@Override
	public void update()
	{
		boolean wasMoving = this.ySpeed != 0 || this.xSpeed != 0;
		super.update();
		this.manageInput();
		if (this.game.isInCutscene) this.ai.update();

		if (wasMoving || this.ySpeed != 0 || this.xSpeed != 0) this.game.getMap().lightManager.update();
	}

}
