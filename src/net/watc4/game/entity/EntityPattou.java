package net.watc4.game.entity;

import static net.watc4.game.GameUtils.ACCELERATION;
import static net.watc4.game.GameUtils.MAX_SPEED;
import net.watc4.game.GameUtils;
import net.watc4.game.Main;
import net.watc4.game.display.renderer.PattouRenderer;
import net.watc4.game.map.Map;
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

	public EntityPattou(float xPos, float yPos, GameState game)
	{
		super(xPos, yPos, game);
		this.jumpTime = 1;
		this.setRenderer(new PattouRenderer(this));
		this.hitbox.setSize(20, 31);
	}

	/** Kill Pattou and lumi - reset Pattou Spawn and lumi Spawn - reset Hitbox at Pattou Spawn and Lumi Spawn */
	public void kill()
	{
		GameState gameState = new GameState();
		Map map = new GameState().getMap();

		gameState.entityLumi.setPosition(map.lumiSpawnX, map.lumiSpawnY);
		gameState.entityLumi.hitbox.setPosition(map.lumiSpawnX, map.lumiSpawnY);
		this.setPosition(map.pattouSpawnX, map.pattouSpawnY);
		this.hitbox.setPosition(map.pattouSpawnX, map.pattouSpawnY);

	}

	/** Checks for movement input and applies it. */
	private void manageInput()
	{
		boolean jump = Main.isKeyPressed(GameUtils.PATTOU_JUMP);
		if (this.ySpeed >= 0 || this.onGround())
		{
			this.jumpTime = 0;
			this.isJumping = false;
		}
		if (Main.isKeyPressed(GameUtils.PATTOU_LEFT)) this.xSpeed -= ACCELERATION;
		if (Main.isKeyPressed(GameUtils.PATTOU_RIGHT)) this.xSpeed += ACCELERATION;
		if (this.xSpeed > MAX_SPEED) this.xSpeed = MAX_SPEED;
		if (this.xSpeed < -MAX_SPEED) this.xSpeed = -MAX_SPEED;

		if (jump && this.canJump)
		{
			this.isJumping = true;
			this.canJump = false;
			this.onGround = false;
		}
		if (jump && this.isJumping)
		{
			this.ySpeed -= JUMP_SPEED / this.jumpTime;
			if (this.ySpeed < -MAX_SPEED) this.ySpeed = -MAX_SPEED;
			++this.jumpTime;
		}
		if (!jump && this.onGround) this.canJump = true;
	}

	@Override
	public void update()
	{
		super.update();
		this.manageInput();
		this.onGround = this.onGround();
	}

}
