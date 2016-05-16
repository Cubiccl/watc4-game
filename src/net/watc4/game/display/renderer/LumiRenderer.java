package net.watc4.game.display.renderer;

import java.awt.Graphics2D;

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
	public void render(Graphics2D g)
	{
		super.render(g);
		g.drawImage(Sprite.LUMI_EYE.getImage(), (int) (this.entity.getX() + 8 + this.entity.getXSpeed()),
				(int) (this.entity.getY() + 8 + this.entity.getYSpeed()), null);
	}
}
