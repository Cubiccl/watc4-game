package net.watc4.game.entity.lightEntity;

import java.awt.Point;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.display.renderer.EntityRenderer;
import net.watc4.game.entity.EntityBattery;
import net.watc4.game.entity.ILightSource;
import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.geometry.CircleHitbox;
import net.watc4.game.utils.geometry.Hitbox;

public class EntityFluorescent extends EntityBattery implements ILightSource
{
	private int lightIntensity;

	private int maxLightIntensity;

	private final int MIN_LIGHT_INTENSITY = (int) (Map.TILESIZE / 1.2);

	public EntityFluorescent()
	{
		this(null, 0, 0, 0, 3f, 30f, 300);
	}

	public EntityFluorescent(GameState game, float xPos, float yPos, int UUID, float chargeTime, float unchargeTime, int maxLightIntensity)
	{
		super(game, xPos, yPos, UUID, chargeTime, unchargeTime);
		this.maxLightIntensity = maxLightIntensity;
		this.lightIntensity = MIN_LIGHT_INTENSITY;
		this.hasGravity = true;
		this.isMoveable = true;
		this.setRenderer(new EntityRenderer(this, new Animation(Sprite.FLUORESCENT)));
	}

	@Override
	public int getLightIntensity()
	{
		return this.lightIntensity;
	}

	@Override
	public Hitbox hitbox(double dx, double dy)
	{
		return new CircleHitbox(new Point((int) (this.getX() + dx + 16), (int) (this.getY() + dy + 16)), 14);
	}

	@Override
	public void update()
	{
		super.update();
		int previousIntensity = this.lightIntensity;
		this.lightIntensity = (int) (this.power * (maxLightIntensity - MIN_LIGHT_INTENSITY) + MIN_LIGHT_INTENSITY);
		if (previousIntensity != this.lightIntensity) this.game.getMap().lightManager.update();
	}

}
