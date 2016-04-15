package net.watc4.game.entity;

import net.watc4.game.Game;
import net.watc4.game.display.renderer.PattouRenderer;
import net.watc4.game.states.GameOverState;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.GameSettings;
import net.watc4.game.utils.GameUtils;

/** Second player : must stay in the shadows and is affected by gravity. */
public class EntityPattou extends EntityPlayer
{
	private static final float JUMP_SPEED = 8;

	private static final float MOVE_SPEED = 3;

	/** True if Pattou can jump */
	private boolean canJump;

	/** Time, in UPS, for the begin of the jump */
	private int jumpingTime;

	public EntityPattou(float xPos, float yPos, GameState game)
	{
		super(xPos, yPos, game);
		this.setRenderer(new PattouRenderer(this));
		this.width = 20;
		this.height = 32;
		this.canJump = true;
		this.jumpingTime = 0;
		this.direction = -1;
	}

	public int getJumpingTime()
	{
		return this.jumpingTime;
	}

	/** Kill Pattou and lumi - reset Pattou Spawn and lumi Spawn - reset Hitbox at Pattou Spawn and Lumi Spawn */
	public void kill()
	{
		Game.getGame().setCurrentState(new GameOverState(this.game));
	}

	/** Checks for movement input and applies it. */
	private void manageInput()
	{
		int move = 0;
		boolean jumpPressed = Game.getGame().isKeyPressed(GameUtils.PATTOU_JUMP);
		if (Game.getGame().isKeyPressed(GameUtils.PATTOU_LEFT)) move--;
		if (Game.getGame().isKeyPressed(GameUtils.PATTOU_RIGHT)) move++;
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
		if (!jumpPressed)
		{
			this.ySpeed = Math.max(ySpeed, -JUMP_SPEED / 4);
		}
	}

	@Override
	public void update()
	{
		this.manageInput();

		if (!GameSettings.godMode && GameState.getInstance().entityLumi.isInLight(this)) --this.health;
		else
		{
			++this.health;
			if (this.health > MAX_HEALTH) this.health = MAX_HEALTH;
		}
		if (this.health < 0) this.kill();
		super.update();
	}

}
