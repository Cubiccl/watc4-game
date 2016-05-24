package net.watc4.game.entity;

import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;

public class EntityVoid extends Entity
{

	public EntityVoid()
	{
		this(null, 0, 0, 0, 0, 0);
	}

	public EntityVoid(GameState game, float xPos, float yPos, int UUID, int width, int height)
	{
		super(game, xPos, yPos, UUID);
		this.width = width * Map.TILESIZE;
		this.height = height * Map.TILESIZE;
		this.hasGravity = false;
		this.setRenderer(null);
	}

	@Override
	public void update()
	{
		super.update();
		for (int UUID : colliding)
		{
			this.game.getMap().entityManager.getEntityByUUID(UUID).kill();
		}
	}

}
