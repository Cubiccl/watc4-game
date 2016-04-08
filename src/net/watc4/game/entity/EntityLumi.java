package net.watc4.game.entity;

import net.watc4.game.Game;
import net.watc4.game.GameUtils;
import net.watc4.game.display.renderer.LumiRenderer;
import net.watc4.game.states.GameState;

/** First Player : can fly and spreads light. */
public class EntityLumi extends EntityPlayer
{
	private final int MOVE_SPEED = 8;
	
	public EntityLumi(float xPos, float yPos, GameState game)
	{
		super(xPos, yPos, game);
		this.hasGravity = false;
		this.setRenderer(new LumiRenderer(this));
	}

	/** @param x - The X Coordinate.
	 * @param y - The Y Coordinate.
	 * @return True if the given coordinates are in the light emitted by Lumi. */
	public boolean isInLight(float x, float y)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public void kill()
	{
		this.setPosition(this.game.getMap().lumiSpawnX, this.game.getMap().lumiSpawnY);
	}

	/** Checks for movement input and applies it. */
	private void manageInput()
	{
		boolean up = Game.getGame().isKeyPressed(GameUtils.LUMI_UP);
		boolean down = Game.getGame().isKeyPressed(GameUtils.LUMI_DOWN);
		boolean left = Game.getGame().isKeyPressed(GameUtils.LUMI_LEFT);
		boolean right = Game.getGame().isKeyPressed(GameUtils.LUMI_RIGHT);
		float hMove = 0;
		float vMove = 0;
		
		if (up) vMove--;
		if (down) vMove++;
		if (left) hMove--;
		if (right) hMove ++;
		
		boolean doubleInput = (up && right) || (up && left) || (down && right) || (down && left);
		float multiplier = 1;
		if (doubleInput) multiplier = 0.7f;
		this.ySpeed = vMove * MOVE_SPEED * multiplier;
		this.xSpeed = hMove * MOVE_SPEED * multiplier;
	}

	@Override
	public void setPosition(int x, int y)
	{
		super.setPosition(x, y);
		this.game.getMap().lightManager.update();
	}

	@Override
	public void update()
	{
		super.update();
		this.manageInput();

		if (this.ySpeed != 0 || this.xSpeed != 0) this.game.getMap().lightManager.update();
	}

}
