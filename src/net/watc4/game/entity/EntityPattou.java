package net.watc4.game.entity;

import static net.watc4.game.GameUtils.ACCELERATION;
import static net.watc4.game.GameUtils.MAX_SPEED;
import net.watc4.game.GameUtils;
import net.watc4.game.Main;
import net.watc4.game.display.renderer.PattouRenderer;
import net.watc4.game.states.GameState;

/** Second player : must stay in the shadows and is affected by gravity. */
public class EntityPattou extends EntityPlayer
{
	private static final float JUMP_SPEED = 10;

	/** True if the character can jump. */
	private boolean canJump;
	/** True if the character is jumping. */
	private boolean isJumping;
	/** The time it has been jumping. */
	private int jumpTime;
	/** True if it is standing on the ground. False if in the air. */
	private boolean onGround;

	public EntityPattou(int xPos, int yPos, GameState game)
	{
		super(xPos, yPos, game);
		this.jumpTime = 1;
		this.onGround = true;
		this.setRenderer(new PattouRenderer(this));
	}

	/** Checks for movement input and applies it. */
	private void manageInput()
	{
		boolean jump = Main.isKeyPressed(GameUtils.PATTOU_JUMP);
		if (this.onGround && !jump) this.canJump = true;
		if (this.canJump && jump) this.isJumping = true;
		if (!jump) this.isJumping = false;
		if (this.isJumping && jump)
		{
			if (this.ySpeed > -MAX_SPEED) this.ySpeed -= JUMP_SPEED / (this.jumpTime * 1.2);
			this.jumpTime++;
			this.canJump = false;
		}
		if (this.onGround) this.jumpTime = 1;
		if (this.ySpeed > 0) this.isJumping = false;
		if (Main.isKeyPressed(GameUtils.PATTOU_LEFT) && this.xSpeed > -MAX_SPEED) this.xSpeed -= ACCELERATION;
		if (Main.isKeyPressed(GameUtils.PATTOU_RIGHT) && this.xSpeed < MAX_SPEED) this.xSpeed += ACCELERATION;
		if (this.onGround && this.ySpeed > 0) this.ySpeed = 0;
	}

	@Override
	public void update()
	{
		super.update();
		this.manageInput();
		this.onGround = this.getY() >= 200;
	}

}
