package net.watc4.game.entity;

import net.watc4.game.Game;
import net.watc4.game.display.renderer.PattouRenderer;
import net.watc4.game.map.TileRegistry;
import net.watc4.game.map.tiles.TileLadder;
import net.watc4.game.states.GameOverState;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.GameSettings;
import net.watc4.game.utils.GameUtils;

/** Second player : must stay in the shadows and is affected by gravity. */
public class EntityPattou extends EntityPlayer
{
	private static final float JUMP_SPEED = 8, LADDER_JUMP_SPEED = 6;

	private static final float MOVE_SPEED = 3, LADDER_SPEED = 1.7f;

	/** True if Pattou can jump */
	private boolean canJump;

	/** Time, in UPS, for the begin of the jump */
	private int jumpingTime;

	public EntityPattou(GameState game, float xPos, float yPos)
	{
		super(game, xPos, yPos);
		this.setRenderer(new PattouRenderer(this));
		this.width = 20;
		this.height = 32;
		this.canJump = true;
		this.jumpingTime = 0;
		this.direction = -1;
	}
	
	public EntityPattou(){
		this(null,0,0);
	}

	public int getJumpingTime()
	{
		return this.jumpingTime;
	}

	/** Kill Pattou and lumi - reset Pattou Spawn and lumi Spawn - reset Hitbox at Pattou Spawn and Lumi Spawn */
	public void kill()
	{
		Game.getGame().setCurrentState(new GameOverState(this.game), false);
	}

	/** Checks for movement input and applies it. */
	private void manageInput()
	{
		int move = 0;
		boolean jumpPressed = Game.getGame().isKeyPressed(GameUtils.PATTOU_JUMP);
		boolean ladderMovement = Game.getGame().isKeyPressed(GameUtils.PATTOU_UP) || Game.getGame().isKeyPressed(GameUtils.PATTOU_DOWN);
		if (Game.getGame().isKeyPressed(GameUtils.PATTOU_LEFT)) move--;
		if (Game.getGame().isKeyPressed(GameUtils.PATTOU_RIGHT)) move++;
		if (ladderMovement && this.getOccupiedTile() instanceof TileLadder) this.onLadder = true;
		if (Game.getGame().isKeyPressed(GameUtils.PATTOU_DOWN) && this.getAdjacentTile(GameUtils.DOWN) == TileRegistry.LADDER_TOP)
		{
			this.onLadder = true;
			if (!this.placeFree(0, 1)) this.onLadder = false;
			if (this.isOnLadder()) this.canJump = true;
		}
		if (this.isOnLadder())
		{
			if (Game.getGame().isKeyPressed(GameUtils.PATTOU_UP)) this.ySpeed = -LADDER_SPEED;
			else if (Game.getGame().isKeyPressed(GameUtils.PATTOU_DOWN))
			{
				this.ySpeed = LADDER_SPEED;
				if (!this.placeFree(0, this.ySpeed)) this.onLadder = false;
			} else this.ySpeed = 0;
		}
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

		if (this.isOnLadder() && jumpPressed && !ladderMovement)
		{
			this.canJump = false;
			this.ySpeed = -LADDER_JUMP_SPEED;
			this.onLadder = false;
		}

		if (!jumpPressed && !this.isOnLadder())
		{
			this.ySpeed = Math.max(ySpeed, -JUMP_SPEED / 4);
		}
	}

	@Override
	public void update()
	{
		if (!this.game.isInCutscene) this.manageInput();

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
