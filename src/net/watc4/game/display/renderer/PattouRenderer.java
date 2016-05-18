package net.watc4.game.display.renderer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.entity.EntityPattou;

/** Renders Pattou. */
public class PattouRenderer extends EntityRenderer
{
	/** Different animations to use. */
	private Animation idleRight, idleLeft, walkingRight, walkingLeft, ladder;

	/** True if last movement was towards left. */

	public PattouRenderer(EntityPattou entity)
	{
		super(entity);
		this.idleRight = new Animation(35, Sprite.PATTOU_IDLE_RIGHT1, Sprite.PATTOU_IDLE_RIGHT2);
		this.idleLeft = new Animation(35, Sprite.PATTOU_IDLE_LEFT1, Sprite.PATTOU_IDLE_LEFT2);
		this.walkingRight = new Animation(10, Sprite.PATTOU_MOVING_RIGHT1, Sprite.PATTOU_MOVING_RIGHT2, Sprite.PATTOU_MOVING_RIGHT3,
				Sprite.PATTOU_MOVING_RIGHT4, Sprite.PATTOU_MOVING_RIGHT5, Sprite.PATTOU_MOVING_RIGHT6);
		this.walkingLeft = new Animation(10, Sprite.PATTOU_MOVING_LEFT1, Sprite.PATTOU_MOVING_LEFT2, Sprite.PATTOU_MOVING_LEFT3, Sprite.PATTOU_MOVING_LEFT4,
				Sprite.PATTOU_MOVING_LEFT5, Sprite.PATTOU_MOVING_LEFT6);
		this.ladder = new Animation(25, Sprite.PATTOU_LADDER1, Sprite.PATTOU_LADDER2, Sprite.PATTOU_LADDER3, Sprite.PATTOU_LADDER4);
		this.ladder.dispose();
	}

	/** Useful for the level editor */
	public Animation getAnimation()
	{
		return idleLeft;
	}

	@Override
	public void render(Graphics2D g)
	{
		BufferedImage image = this.animation.getImage();
		final int JUMP_ANIMATION_SPEED = 7;
		if (this.entity.isOnLadder())
		{
			if (this.entity.getYSpeed() != 0 || this.entity.getXSpeed() != 0) this.ladder.update();
			image = this.ladder.getImage();
		} else if (!this.entity.placeFree(0, 1))
		{
			if (this.entity.getXSpeed() > 0) image = this.walkingRight.getImage();
			else if (this.entity.getXSpeed() < 0) image = this.walkingLeft.getImage();
			else if (this.entity.getDirection() > 0) image = this.idleRight.getImage();
			else image = this.idleLeft.getImage();
		} else
		{
			if (this.entity.getDirection() > 0)
				if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 0) image = Sprite.PATTOU_JUMPING_RIGHT1.getImage();
				else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 1) image = Sprite.PATTOU_JUMPING_RIGHT2.getImage();
				else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 2) image = Sprite.PATTOU_JUMPING_RIGHT3.getImage();
				else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 3) image = Sprite.PATTOU_JUMPING_RIGHT4.getImage();
				else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 4) image = Sprite.PATTOU_JUMPING_RIGHT5.getImage();
				else image = Sprite.PATTOU_JUMPING_RIGHT6.getImage();
			else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 0) image = Sprite.PATTOU_JUMPING_LEFT1.getImage();
			else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 1) image = Sprite.PATTOU_JUMPING_LEFT2.getImage();
			else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 2) image = Sprite.PATTOU_JUMPING_LEFT3.getImage();
			else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 3) image = Sprite.PATTOU_JUMPING_LEFT4.getImage();
			else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 4) image = Sprite.PATTOU_JUMPING_LEFT5.getImage();
			else image = Sprite.PATTOU_JUMPING_LEFT6.getImage();

		}

		g.drawImage(image, (int) (this.entity.getX() - (32 - this.entity.getWidth()) / 2), (int) (this.entity.getY() - (32 - this.entity.getHeight()) / 2),
				null);
	}
}
