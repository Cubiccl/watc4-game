package net.watc4.game.display.renderer;

import java.awt.Graphics;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.entity.EntityLumi;

/** Renders Lumi. */
public class LumiRenderer extends EntityRenderer
{
	/** Its eyes. */
	private Animation eyes;

	public LumiRenderer(EntityLumi lumi)
	{
		super(lumi, new Animation(Sprite.LUMI));
		this.eyes = new Animation(Sprite.LUMI_EYE);
	}

	@Override
	public void render(Graphics g)
	{
		super.render(g);
		g.drawImage(this.eyes.getImage(), (int) (this.entity.getX() + 8 + this.entity.getXSpeed()), (int) (this.entity.getY() + 8 + this.entity.getYSpeed()), null);
	}
}
