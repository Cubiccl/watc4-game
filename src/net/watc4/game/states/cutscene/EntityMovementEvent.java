package net.watc4.game.states.cutscene;

import net.watc4.game.entity.Entity;
import net.watc4.game.map.Map;

/** Moves an Entity to given coordinates. */
public class EntityMovementEvent extends CutsceneEvent
{

	/** The Entity to move. */
	private Entity entity;
	/** The destination of the Entity. */
	private float x, y;

	/** Creates a new Entity Movement Event.
	 * 
	 * @param cutscene - The Cutscene it belongs to.
	 * @param entityID - The ID of the Entity to move.
	 * @param x - The X destination (in tiles)
	 * @param y - The Y destination (in tiles) */
	public EntityMovementEvent(CutsceneState cutscene, int entityID, float x, float y)
	{
		super(cutscene);
		this.entity = this.cutscene.gameState.entityLumi;
		this.x = x * Map.TILESIZE;
		this.y = y * Map.TILESIZE;
	}

	@Override
	public void begin()
	{
		super.begin();
		if (this.entity.ai != null) this.entity.ai.setDestination(this.x, this.y);
	}

	@Override
	public void finish()
	{
		super.finish();
		if (this.entity.ai != null)
		{
			this.entity.setPosition(this.x, this.y);
			this.entity.ai.cancelDestination();
		}
	}

	@Override
	public boolean isOver()
	{
		return this.entity.ai == null || this.entity.ai.destinationReached();
	}

}
