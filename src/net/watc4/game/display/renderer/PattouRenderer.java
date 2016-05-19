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
		this.idleRight = new Animation(35, Sprite.PATTOU_IDLE_RIGHT);
		this.idleLeft = new Animation(35, Sprite.PATTOU_IDLE_LEFT);
		this.walkingRight = new Animation(10, Sprite.PATTOU_MOVING_RIGHT);
		this.walkingLeft = new Animation(10, Sprite.PATTOU_MOVING_LEFT);
		this.ladder = new Animation(25, Sprite.PATTOU_LADDER);
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
			if (this.entity.getDirection() > 0) if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 0) image = Sprite.PATTOU_JUMPING_RIGHT[0]
					.getImage();
			else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 1) image = Sprite.PATTOU_JUMPING_RIGHT[1].getImage();
			else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 2) image = Sprite.PATTOU_JUMPING_RIGHT[2].getImage();
			else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 3) image = Sprite.PATTOU_JUMPING_RIGHT[3].getImage();
			else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 4) image = Sprite.PATTOU_JUMPING_RIGHT[4].getImage();
			else image = Sprite.PATTOU_JUMPING_RIGHT[5].getImage();
			else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 0) image = Sprite.PATTOU_JUMPING_LEFT[0].getImage();
			else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 1) image = Sprite.PATTOU_JUMPING_LEFT[1].getImage();
			else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 2) image = Sprite.PATTOU_JUMPING_LEFT[2].getImage();
			else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 3) image = Sprite.PATTOU_JUMPING_LEFT[3].getImage();
			else if (((EntityPattou) this.entity).getJumpingTime() / JUMP_ANIMATION_SPEED == 4) image = Sprite.PATTOU_JUMPING_LEFT[4].getImage();
			else image = Sprite.PATTOU_JUMPING_LEFT[5].getImage();

		}

		g.drawImage(image, (int) (this.entity.getX() - (32 - this.entity.getWidth()) / 2), (int) (this.entity.getY() - (32 - this.entity.getHeight()) / 2),
				null);
	}
}
