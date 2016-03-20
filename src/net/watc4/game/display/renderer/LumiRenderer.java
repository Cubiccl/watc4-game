package net.watc4.game.display.renderer;

import java.awt.Graphics;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.entity.EntityLumi;

/** Renders Lumi. */
public class LumiRenderer extends EntityRenderer
{

	public LumiRenderer(EntityLumi lumi)
	{
		super(lumi, new Animation(Sprite.LUMI));
	}

	@Override
	public void render(Graphics g)
	{
		super.render(g);
		g.drawImage(Sprite.LUMI_EYE.getImage(), (int) (this.entity.getX() + 8 + this.entity.getXSpeed()) - Sprite.LUMI.getImage().getWidth() / 2,
				(int) (this.entity.getY() + 8 + this.entity.getYSpeed()) - Sprite.LUMI.getImage().getHeight() / 2, null);
	}
}
