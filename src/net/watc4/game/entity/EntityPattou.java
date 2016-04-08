package net.watc4.game.entity;

import static net.watc4.game.GameUtils.ACCELERATION;
import static net.watc4.game.GameUtils.MAX_SPEED;
import net.watc4.game.Game;
import net.watc4.game.GameUtils;
import net.watc4.game.display.renderer.PattouRenderer;
import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;

/** Second player : must stay in the shadows and is affected by gravity. */
public class EntityPattou extends EntityPlayer
{
	private static final float JUMP_SPEED = 6;
	
	private static final float MOVE_SPEED = 3;

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
		this.width = 20;
		this.height = 32;
	}

	/** Kill Pattou and lumi - reset Pattou Spawn and lumi Spawn - reset Hitbox at Pattou Spawn and Lumi Spawn */
	public void kill()
	{
		Map map = this.game.getMap();

		this.game.entityLumi.setPosition(map.lumiSpawnX, map.lumiSpawnY);
		this.setPosition(map.pattouSpawnX, map.pattouSpawnY);

	}

	/** Checks for movement input and applies it. */
	private void manageInput()
	{ 
		int move = 0;
		boolean jump = Game.getGame().isKeyPressed(GameUtils.PATTOU_JUMP);
		if (Game.getGame().isKeyPressed(GameUtils.PATTOU_LEFT)) move--;
		if (Game.getGame().isKeyPressed(GameUtils.PATTOU_RIGHT)) move++;
		xSpeed = move * MOVE_SPEED;
		
		if (Game.getGame().isKeyPressed(GameUtils.PATTOU_JUMP)) ySpeed = -JUMP_SPEED;
		
		/*
		boolean jump = Game.getGame().isKeyPressed(GameUtils.PATTOU_JUMP);
		if (this.ySpeed >= 0 || this.onGround())
		{
			this.jumpTime = 0;
			this.isJumping = false;
		}
		if (Game.getGame().isKeyPressed(GameUtils.PATTOU_LEFT)) this.xSpeed -= ACCELERATION;
		if (Game.getGame().isKeyPressed(GameUtils.PATTOU_RIGHT)) this.xSpeed += ACCELERATION;
		if (this.xSpeed > MAX_SPEED) this.xSpeed = MAX_SPEED;
		if (this.xSpeed < -MAX_SPEED) this.xSpeed = -MAX_SPEED;

		if (!jump && this.onGround()) this.canJump = true;
		if (jump && this.canJump && this.onGround())
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

		if (jump) this.canJump = false;*/
	} 

	@Override
	public void update()
	{
		this.manageInput();
		
		if (GameState.getInstance().entityLumi.isInLight(this)) --this.health;
		if (this.health < 0) this.kill();
		super.update();
	}

}
