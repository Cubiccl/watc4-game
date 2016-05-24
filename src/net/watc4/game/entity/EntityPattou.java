package net.watc4.game.entity;

import net.watc4.game.Game;
import net.watc4.game.display.renderer.PattouRenderer;
import net.watc4.game.entity.ai.BasicAI;
import net.watc4.game.entity.ai.WalkerAI;
import net.watc4.game.states.GameOverState;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.GameSettings;

/** Second player : must stay in the shadows and is affected by gravity. */
public class EntityPattou extends Entity
{

	/** Pattou's AI. */
	public class AIPattou extends WalkerAI
	{

		public AIPattou(EntityPattou entity)
		{
			super(entity);
		}

		@Override
		public boolean down()
		{
			if (!this.entity.game.isInCutscene) return Game.getGame().getControls().p_down.isPressed();
			return super.down();
		}

		/** @return True if this AI jumps. */
		public boolean jump()
		{
			if (!this.entity.game.isInCutscene) return Game.getGame().getControls().p_jump.isPressed();
			return super.jump();
		}

		@Override
		public boolean left()
		{
			if (!this.entity.game.isInCutscene) return Game.getGame().getControls().p_left.isPressed();
			return super.left();
		}

		@Override
		public boolean right()
		{
			if (!this.entity.game.isInCutscene) return Game.getGame().getControls().p_right.isPressed();
			return super.right();
		}

		@Override
		public boolean up()
		{
			if (!this.entity.game.isInCutscene) return Game.getGame().getControls().p_up.isPressed();
			return super.up();
		}
	}

	/** The Door Pattou is standing in. null if none. */
	private EntityDoor door;

	public EntityPattou()
	{
		this(null, 0, 0);
	}

	public EntityPattou(GameState game, float xPos, float yPos)
	{
		super(game, xPos, yPos - 50, 1);
		this.setRenderer(new PattouRenderer(this));
		this.width = 30;
		this.height = 80;
		this.direction = -1;
		this.ai = new AIPattou(this);
		this.setAffectedByLight(true);
		this.moveSpeed = 5.5f;
		this.overShadow = true;
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

	@Override
	public void onCollisionEndWith(Entity entity)
	{
		super.onCollisionEndWith(entity);
		if (entity instanceof EntityDoor) this.door = null;
	}

	@Override
	public void onCollisionWith(Entity entity)
	{
		super.onCollisionWith(entity);
		if (entity instanceof EntityDoor) this.door = (EntityDoor) entity;
	}

	/** @return True if this Player has reached the end of the level. */
	public boolean reachedEnd()
	{
		for (Integer uuid : this.colliding)
		{
			Entity entity = this.game.getMap().entityManager.getEntityByUUID(uuid);
			if (entity instanceof EntityEndLevel && ((EntityEndLevel) entity).player == EntityEndLevel.PATTOU) return true;
		}
		return false;
	}

	@Override
	public void update()
	{
		if (this.door != null && ((BasicAI) this.ai).up() && this.canJump)
		{
			this.door.activate();
			return;
		}

		this.ai.update();

		for (Entity entity : game.getMap().entityManager.getEntities())
			if (entity.isMoveable && this.collidesWith(entity, Math.signum(-this.xSpeed), 0))
			{
				if (!entity.placeFree(xSpeed, 0))
				{
					while (entity.placeFree(Math.signum(xSpeed), 0))
						entity.setX(entity.getX() + Math.signum(xSpeed));
					xSpeed = 0;
				} else entity.setX(entity.getX() + this.xSpeed);
				entity.onEntityMove(entity);
			}

		super.update();

		if (this.isInLight() && !GameSettings.godMode) this.dealDamage(1);
		else if (this.getHealth() < 20) this.heal(1);

	}

}
