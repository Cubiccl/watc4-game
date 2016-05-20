package net.watc4.game.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.display.renderer.EntityRenderer;
import net.watc4.game.entity.ai.WalkerAI;
import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;

/** Entity that runs to a location when lit up. */
public class EntityRunaway extends Entity
{
	class RunawayRenderer extends EntityRenderer
	{
		private Animation stillLeft, stillRight, runLeft, runRight;

		public RunawayRenderer(Entity entity)
		{
			super(entity);
			this.stillLeft = new Animation(30, Sprite.RUNAWAY_STILL_LEFT);
			this.stillRight = new Animation(30, Sprite.RUNAWAY_STILL_RIGHT);
			this.runLeft = new Animation(10, Sprite.RUNAWAY_RUN_LEFT);
			this.runRight = new Animation(10, Sprite.RUNAWAY_RUN_RIGHT);
		}

		public void clear()
		{
			this.stillLeft.dispose();
			this.stillRight.dispose();
			this.runLeft.dispose();
			this.runRight.dispose();
		}

		@Override
		public void render(Graphics2D g)
		{
			BufferedImage image = this.stillLeft.getImage();
			EntityRunaway runaway = (EntityRunaway) this.entity;
			if (runaway.state == State.RUNAWAY)
			{
				if (this.entity.direction == 1) image = this.runRight.getImage();
				else image = this.runLeft.getImage();
			} else
			{
				if (this.entity.direction == 1) image = this.stillRight.getImage();
				else image = this.stillLeft.getImage();
			}
			g.drawImage(image, (int) this.entity.getX(), (int) this.entity.getY(), null);
		}
	}

	enum State
	{
		RUNAWAY,
		STANDING;
	}

	/** The destination coordinates. */
	private int runX, runY;
	/** This entity's current State. */
	private State state;

	public EntityRunaway()
	{
		this(null, 0, 0, 0, 0, 0);
	}

	public EntityRunaway(GameState game, float xPos, float yPos, int UUID, int runX, int runY)
	{
		super(game, xPos, yPos, UUID);
		this.state = State.STANDING;
		this.setAffectedByLight(true);
		this.ai = new WalkerAI(this);
		this.runX = runX * Map.TILESIZE;
		this.runY = runY * Map.TILESIZE;
		this.setRenderer(new RunawayRenderer(this));
	}

	@Override
	public void kill()
	{
		super.kill();
		((RunawayRenderer) this.getRenderer()).clear();
	}

	@Override
	public void onEnterLight(EntityLumi lumi)
	{
		super.onEnterLight(lumi);
		if (this.state == State.STANDING) this.state = State.RUNAWAY;
		this.ai.setDestination(this.runX, this.runY);
	}

}
