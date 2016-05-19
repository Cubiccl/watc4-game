package net.watc4.game.entity;

import java.awt.Graphics2D;

import net.watc4.game.display.Sprite;
import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;

/** This Entity deals damage and turns into a platform when lit up. */
public class EntitySpikePlatform extends EntityBattery
{
	private enum State
	{
		PLATFORM,
		SPIKES;
	}

	private static final float SWITCH = 0.5f;

	/** The current behavior state. */
	private State state;

	public EntitySpikePlatform()
	{
		this(null, 0, 0, 0, 0, 0);
	}

	public EntitySpikePlatform(GameState game, float xPos, float yPos, int UUID, float chargeTime, float unchargeTime)
	{
		super(game, xPos, yPos, UUID, chargeTime, unchargeTime);
		this.state = State.SPIKES;
		this.isSolid = false;
		this.hasGravity = false;
	}

	@Override
	public void render(Graphics2D g)
	{
		int sprite = 0;
		if (this.power >= SWITCH / 2) ++sprite;
		if (this.power >= SWITCH) ++sprite;
		if (this.power >= SWITCH * 3 / 2) ++sprite;
		g.drawImage(Sprite.SPIKE_PLATFORM[sprite].getImage(), (int) this.getX(), (int) this.getY(), null);
	}

	@Override
	public void update()
	{
		super.update();

		if (this.power >= SWITCH && this.state == State.SPIKES)
		{
			this.state = State.PLATFORM;
			this.isSolid = true;
			this.height = Map.TILESIZE / 4;
		}
		if (this.power < SWITCH && this.state == State.PLATFORM)
		{
			this.state = State.SPIKES;
			this.isSolid = false;
			this.height = Map.TILESIZE;
		}

		if (this.state == State.SPIKES) for (int UUID : this.colliding)
		{
			Entity target = this.game.getMap().entityManager.getEntityByUUID(UUID);
			if (target != this) target.dealDamage(2);
		}
	}
}
