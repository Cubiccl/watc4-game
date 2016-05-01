package net.watc4.game.entity;

import net.watc4.game.states.GameState;

/** An Entity that charges when in the light of Lumi. */
public class EntityBattery extends Entity
{
	public static final int CHARGE_SPEED = 2, UNCHARGE_SPEED = 1;

	/** Represents the maximum amount of energy this battery can store above its maximum power. */
	public final int buffer;
	/** Represents the amount of energy this battery needs to be fully charged. */
	public final int maxPower;
	/** Represents how long this Battery has been in the Light. */
	private int power;

	public EntityBattery(GameState game, float xPos, float yPos, int maxPower, int buffer)
	{
		super(game, xPos, yPos);
		this.power = 0;
		this.maxPower = maxPower;
		this.buffer = buffer;
		this.hasGravity = false;
		this.isSolid = true;
	}
	
	public EntityBattery()
	{
		this(null,0,0,0,0);
	}

	/** @return The power of this Battery. */
	public int getPower()
	{
		return this.power;
	}

	/** @return In percent, the amount of energy this battery has compared to what it needs to be fully charged. */
	public int getPowerRatio()
	{
		return this.power * 100 / this.maxPower;
	}

	/** @return True if this Battery is fully charged. */
	public boolean isFullyCharged()
	{
		return this.power >= this.maxPower;
	}

	@Override
	public void update()
	{
		super.update();
		if (this.power < this.maxPower + this.buffer && this.game.entityLumi.isInLight(this)) this.power += CHARGE_SPEED;
		else if (this.power > 0) this.power -= UNCHARGE_SPEED;
		else this.power = 0;
		if (this.power > this.maxPower + this.buffer) this.power = this.maxPower + this.buffer;
	}

}
