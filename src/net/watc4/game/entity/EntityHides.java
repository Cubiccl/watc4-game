package net.watc4.game.entity;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.display.renderer.EntityRenderer;
import net.watc4.game.states.GameState;

/** Entity that runs to a location when lit up. */
public class EntityHides extends Entity
{

	private Animation hiding;

	public EntityHides()
	{
		this(null, 0, 0, 0);
	}

	public EntityHides(GameState game, float xPos, float yPos, int UUID)
	{
		super(game, xPos, yPos, UUID);
		this.setAffectedByLight(true);
		this.isSolid = true;
		this.setRenderer(new EntityRenderer(this, new Animation(Sprite.HIDES[0])));
		this.width = 181;
		this.height = 95;
	}

	@Override
	public void kill()
	{
		super.kill();
		this.setRenderer(null);
	}

	@Override
	public void onEnterLight(EntityLumi lumi)
	{
		super.onEnterLight(lumi);
		this.hiding = new Animation(5, Sprite.HIDES);
		this.hiding.loopsOnce = true;
		this.setRenderer(new EntityRenderer(this, this.hiding));
	}

	@Override
	public void update()
	{
		super.update();
		if (this.hiding != null && this.hiding.isOver()) this.kill();
	}

}
