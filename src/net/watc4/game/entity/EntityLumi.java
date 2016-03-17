package net.watc4.game.entity;

import static net.watc4.game.GameUtils.ACCELERATION;
import static net.watc4.game.GameUtils.MAX_SPEED;
import net.watc4.game.GameUtils;
import net.watc4.game.Main;
import net.watc4.game.states.GameState;

/** First Player : can fly and spreads light. */
public class EntityLumi extends EntityPlayer
{
	public EntityLumi(int xPos, int yPos, GameState game)
	{
		super(xPos, yPos, game);
		this.hasGravity = false;
	}

	/** Checks for movement input and applies it. */
	private void manageInput()
	{
		if (Main.isKeyPressed(GameUtils.LUMI_UP) && this.ySpeed > -MAX_SPEED) this.ySpeed -= ACCELERATION;
		if (Main.isKeyPressed(GameUtils.LUMI_DOWN) && this.ySpeed < MAX_SPEED) this.ySpeed += ACCELERATION;
		if (Main.isKeyPressed(GameUtils.LUMI_LEFT) && this.xSpeed > -MAX_SPEED) this.xSpeed -= ACCELERATION;
		if (Main.isKeyPressed(GameUtils.LUMI_RIGHT) && this.xSpeed < MAX_SPEED) this.xSpeed += ACCELERATION;
	}

	@Override
	public void update()
	{
		super.update();
		this.manageInput();
	}

}
