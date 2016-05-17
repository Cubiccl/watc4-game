package net.watc4.game.states.cutscene;

import net.watc4.game.entity.Entity;
import net.watc4.game.map.Map;

/** Moves an Entity to given coordinates. */
public class EntityMovementEvent extends CutsceneEvent
{

	/** The Entity to move. */
	private Entity entity;
	/** True if the destination is relative to the Entity's current position. */
	private boolean relative;
	/** The destination of the Entity. */
	private float x, y;

	/** Creates a new Entity Movement Event.
	 * 
	 * @param cutscene - The Cutscene it belongs to.
	 * @param UUID - The UUID of the Entity to move.
	 * @param relative - True if the destination is relative to the Entity's current position.
	 * @param x - The X destination (in tiles)
	 * @param y - The Y destination (in tiles) */
	public EntityMovementEvent(CutsceneState cutscene, int UUID, boolean relative, float x, float y)
	{
		super(cutscene);
		this.entity = this.cutscene.gameState.getMap().entityManager.getEntityByUUID(UUID);
		this.relative = relative;
		this.x = x * Map.TILESIZE;
		this.y = y * Map.TILESIZE;
	}

	@Override
	public void begin()
	{
		super.begin();
		if (this.entity != null && this.entity.ai != null)
		{
			if (this.relative)
			{
				this.x += this.entity.getX();
				this.y += this.entity.getY();
			}
			this.entity.ai.setDestination(this.x, this.y);
		}
	}

	@Override
	public void finish()
	{
		super.finish();
		if (this.entity != null && this.entity.ai != null)
		{
			this.entity.setPosition(this.x, this.y);
			this.entity.ai.cancelDestination();
		}
	}

	@Override
	public boolean isOver()
	{
		return this.entity == null || this.entity.ai == null || this.entity.ai.destinationReached();
	}

}
