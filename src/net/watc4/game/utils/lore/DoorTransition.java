package net.watc4.game.utils.lore;

import net.watc4.game.Game;
import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;

/** Tells where to go when a players goes through a Door. */
public class DoorTransition
{

	/** The position of the players. */
	public final int lumiX, lumiY, pattouX, pattouY;
	/** The Map to load. */
	public final String map;
	/** The Map the Door is from. */
	public final String mapDestination;
	/** The Door's UUID. */
	public final int UUID;

	public DoorTransition(int UUID, String map, String mapDestination, int lumiX, int lumiY, int pattouX, int pattouY)
	{
		this.UUID = UUID;
		this.map = map;
		this.mapDestination = mapDestination;
		this.lumiX = lumiX;
		this.lumiY = lumiY;
		this.pattouX = pattouX;
		this.pattouY = pattouY;
	}

	/** Applies this transition. */
	public void apply()
	{
		GameState game = GameState.createNew(this.mapDestination);
		if (game.hasLumi) game.entityLumi.setPosition(this.lumiX * Map.TILESIZE, this.lumiY * Map.TILESIZE);
		if (game.hasPattou) game.entityPattou.setPosition(this.pattouX * Map.TILESIZE, this.pattouY * Map.TILESIZE);
		Game.getGame().setCurrentState(game);
	}

}
