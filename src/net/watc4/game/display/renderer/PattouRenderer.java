package net.watc4.game.display.renderer;

import java.awt.Graphics;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.entity.EntityPattou;

/** Renders Pattou. */
public class PattouRenderer extends EntityRenderer
{
	/** Different animations to use. */
	private Animation idleRight, idleLeft, walkingRight, walkingLeft;
	/** True if last movement was towards left. */
	private boolean wasLeft;

	public PattouRenderer(EntityPattou entity)
	{
		super(entity);
		this.idleRight = new Animation(30, Sprite.PATTOU_IDLE_RIGHT1, Sprite.PATTOU_IDLE_RIGHT2);
		this.idleLeft = new Animation(30, Sprite.PATTOU_IDLE_LEFT1, Sprite.PATTOU_IDLE_LEFT2);
		this.walkingRight = new Animation(10, Sprite.PATTOU_MOVING_RIGHT1, Sprite.PATTOU_MOVING_RIGHT2, Sprite.PATTOU_MOVING_RIGHT3,
				Sprite.PATTOU_MOVING_RIGHT4, Sprite.PATTOU_MOVING_RIGHT5, Sprite.PATTOU_MOVING_RIGHT6);
		this.walkingLeft = new Animation(10, Sprite.PATTOU_MOVING_LEFT1, Sprite.PATTOU_MOVING_LEFT2, Sprite.PATTOU_MOVING_LEFT3, Sprite.PATTOU_MOVING_LEFT4,
				Sprite.PATTOU_MOVING_LEFT5, Sprite.PATTOU_MOVING_LEFT6);

		this.setAnimation(this.idleRight);
	}

	@Override
	public void render(Graphics g)
	{
		boolean isRight = this.entity.getXSpeed() > 0;
		boolean isJumping = this.entity.getYSpeed() < 0;
		boolean isFalling = this.entity.getYSpeed() > 0;

		if (isJumping)
		{
			if (isRight || !this.wasLeft) g.drawImage(Sprite.PATTOU_JUMPING_RIGHT.getImage(), (int) this.entity.getX(), (int) this.entity.getY(), null);
			else g.drawImage(Sprite.PATTOU_JUMPING_LEFT.getImage(), (int) this.entity.getX(), (int) this.entity.getY(), null);
		} else if (isFalling)
		{
			if (isRight || !this.wasLeft) g.drawImage(Sprite.PATTOU_FALLING_RIGHT.getImage(), (int) this.entity.getX(), (int) this.entity.getY(), null);
			else g.drawImage(Sprite.PATTOU_FALLING_LEFT.getImage(), (int) this.entity.getX(), (int) this.entity.getY(), null);
		} else if (isRight) g.drawImage(this.walkingRight.getImage(), (int) this.entity.getX(), (int) this.entity.getY(), null);
		else if (this.entity.getXSpeed() < 0) g.drawImage(this.walkingLeft.getImage(), (int) this.entity.getX(), (int) this.entity.getY(), null);
		else if (this.wasLeft) g.drawImage(this.idleLeft.getImage(), (int) this.entity.getX(), (int) this.entity.getY(), null);
		else super.render(g);

		this.wasLeft = (this.wasLeft && !isRight) || this.entity.getXSpeed() < 0;
	}
}