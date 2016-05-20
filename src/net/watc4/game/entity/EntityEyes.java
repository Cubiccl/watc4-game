package net.watc4.game.entity;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.display.renderer.EntityRenderer;
import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;

/** Changes state when Pattou approaches. */
public class EntityEyes extends Entity
{

	enum State
	{
		CLOSED,
		OPEN;
	}

	public static final int DETECTION = 5 * Map.TILESIZE;

	private State state;

	public EntityEyes()
	{
		this(null, 0, 0, 0);
	}

	public EntityEyes(GameState game, float xPos, float yPos, int UUID)
	{
		super(game, xPos, yPos, UUID);
		this.game.entityPattou.addMovementListener(this);
		this.state = State.CLOSED;
		this.hasGravity = false;
		this.setRenderer(new EntityRenderer(this, null));
	}

	@Override
	public void onEntityMove(Entity entity)
	{
		super.onEntityMove(entity);
		if (entity == this.game.entityPattou)
		{
			int distance = (int) this.distanceTo(entity);
			if (distance <= DETECTION && this.state == State.CLOSED)
			{
				this.state = State.OPEN;
				this.getRenderer().setAnimation(new Animation(10, Sprite.EYES_OPEN));
				this.getRenderer().getAnimation().loopsOnce = true;
			}
			if (distance > DETECTION && this.state == State.OPEN)
			{
				this.state = State.CLOSED;
				this.getRenderer().setAnimation(new Animation(10, Sprite.EYES_CLOSE));
				this.getRenderer().getAnimation().loopsOnce = true;
			}
		}
	}
}
