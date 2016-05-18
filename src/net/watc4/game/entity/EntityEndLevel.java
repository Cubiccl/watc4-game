package net.watc4.game.entity;

import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;

/** Represents the End of the Level for a player. */
public class EntityEndLevel extends Entity
{
	public static final boolean LUMI = false, PATTOU = true;

	/** The Player this End of Level is for. */
	public final boolean player;

	public EntityEndLevel()
	{
		this(null, 0, 0, 0, 0, 0, LUMI);
	}

	public EntityEndLevel(GameState game, float xPos, float yPos, int UUID, int width, int height, boolean player)
	{
		super(game, xPos, yPos, UUID);
		this.player = player;
		this.width = width * Map.TILESIZE;
		this.height = height * Map.TILESIZE;
		this.hasGravity = false;
		this.setRenderer(null);
	}

}
