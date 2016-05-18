package net.watc4.game.entity.lightEntity;

import net.watc4.game.entity.EntityBattery;
import net.watc4.game.entity.ILightSource;
import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;

public class EntityFluorescent extends EntityBattery implements ILightSource
{
	private final int MIN_LIGHT_INTENSITY = (int)(Map.TILESIZE / 1.2);

	private int maxLightIntensity;

	private int lightIntensity;

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
	}

	@Override
	public int getLightIntensity()
	{
		return this.lightIntensity;
	}
	
	@Override
	 public void update(){
		super.update();
		this.lightIntensity = (int)(this.power * (maxLightIntensity - MIN_LIGHT_INTENSITY) + MIN_LIGHT_INTENSITY); 
	}

}
