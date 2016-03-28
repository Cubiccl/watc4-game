package net.watc4.game.entity;

import static net.watc4.game.GameUtils.ACCELERATION;
import static net.watc4.game.GameUtils.MAX_SPEED;
import net.watc4.game.GameUtils;
import net.watc4.game.Main;
import net.watc4.game.display.renderer.LumiRenderer;
import net.watc4.game.states.GameState;

/** First Player : can fly and spreads light. */
public class EntityLumi extends EntityPlayer
{
	public EntityLumi(float xPos, float yPos, GameState game)
	{
		super(xPos, yPos, game);
		this.hasGravity = false;
		this.setRenderer(new LumiRenderer(this));
	}

	/** Checks for movement input and applies it. */
	private void manageInput()
	{
		boolean up = Main.isKeyPressed(GameUtils.LUMI_UP);
		boolean down = Main.isKeyPressed(GameUtils.LUMI_DOWN);
		boolean left = Main.isKeyPressed(GameUtils.LUMI_LEFT);
		boolean right = Main.isKeyPressed(GameUtils.LUMI_RIGHT);
		boolean doubleInput = (up && right) || (up && left) || (down && right) || (down && left);
		float multiplier = 1;
		if (doubleInput) multiplier = 0.7f;
		if (up && this.ySpeed > -MAX_SPEED) this.ySpeed -= ACCELERATION * multiplier;
		if (down && this.ySpeed < MAX_SPEED) this.ySpeed += ACCELERATION * multiplier;
		if (left && this.xSpeed > -MAX_SPEED) this.xSpeed -= ACCELERATION * multiplier;
		if (right && this.xSpeed < MAX_SPEED) this.xSpeed += ACCELERATION * multiplier;
	}

	@Override
	public void update()
	{
		super.update();
		this.manageInput();

		if (this.ySpeed != 0 || this.xSpeed != 0) GameState.getInstance().getMap().lightManager.update();
	}
	public void kill()
	{
		this.setPosition(GameState.getInstance().getMap().lumiSpawnX, GameState.getInstance().getMap().lumiSpawnY);
	}

}
