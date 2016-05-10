package net.watc4.game.entity;

import net.watc4.game.Game;
import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;
import net.watc4.game.states.cutscene.CutsceneState;

/** If a Player collides with this Entity, a Cutscene is triggered. */
public class EntityCutscene extends Entity
{
	/** The name of the Cutscene (file name). */
	public final String cutsceneName;

	public EntityCutscene()
	{
		this(null, 0, 0, 0, 0, 0, null);
	}

	/** Creates a new Entity Cutscene.
	 * 
	 * @param game - Its GameState.
	 * @param xPos - Its X Position.
	 * @param yPos - Its Y Position.
	 * @param tileWidth - Its width (in tiles.)
	 * @param tileHeight - Its height (in tiles.)
	 * @param cutsceneName - The name of the Cutscene it will trigger if collided with. */
	public EntityCutscene(GameState game, int UUID, float xPos, float yPos, int tileWidth, int tileHeight, String cutsceneName)
	{
		super(game, UUID, xPos, yPos);
		this.cutsceneName = cutsceneName;
		this.width = tileWidth * Map.TILESIZE;
		this.height = tileHeight * Map.TILESIZE;
		this.hasGravity = false;
		this.setRenderer(null);
	}

	@Override
	public void onCollisionWith(Entity entity)
	{
		super.onCollisionWith(entity);
		if (entity instanceof EntityPattou || entity instanceof EntityLumi) Game.getGame().setCurrentState(
				CutsceneState.createFrom(this.game, this.cutsceneName), false);
	}
}
