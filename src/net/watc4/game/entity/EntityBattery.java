package net.watc4.game.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.display.renderer.EntityRenderer;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.GameSettings;

/** An Entity that charges when in the light of Lumi. */
public class EntityBattery extends Entity
{
	/** Time in sec to fully charge/uncharge the battery */
	private float chargeTime, unchargeTime;
	/** Represents how long this Battery has been in the Light. */
	protected float power;

	public EntityBattery()
	{
		this(null, 0, 0, 0, 0.7f, 5f);
	}

	public EntityBattery(GameState game, float xPos, float yPos, int UUID, float chargeTime, float unchargeTime)
	{
		super(game, xPos, yPos, UUID);
		this.power = 0;
		this.hasGravity = false;
		this.isSolid = true;
		this.chargeTime = chargeTime;
		this.unchargeTime = unchargeTime;
		this.setRenderer(new EntityRenderer(this, new Animation(Sprite.BATTERY)));
	}

	@Override
	public void render(Graphics2D g)
	{
		super.render(g);
		if (GameSettings.lightMode)
		{
			g.setColor(Color.RED);
			g.fillRect((int) getX() + 2, (int) getY() + 2, 28, 7);
			g.setColor(Color.GREEN);
			g.fillRect((int) getX() + 2, (int) getY() + 2, (int) (28 * this.power), 7);
		}
	}

	@Override
	public void update()
	{
		super.update();
		if (this.game.hasLumi && this.game.entityLumi.isInLight(this)) this.power = (this.power >= 1) ? 1 : 0.0167f / chargeTime + this.power;
		else this.power = (this.power <= 0) ? 0 : this.power - 0.0167f / unchargeTime;
	}

}
