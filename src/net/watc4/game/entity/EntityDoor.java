package net.watc4.game.entity;

import net.watc4.game.Game;
import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;

/** When Pattou collides with this and presses UP, it will take it to a new Map. */
public class EntityDoor extends Entity
{
	/** The spawn coordinates of Lumi and Pattou in the new map. */
	private final int lumiX, lumiY, pattouX, pattouY;
	/** The Map to switch to. */
	private final String mapName;
	
	public EntityDoor()
	{
		this(null, 0, 0, 0, 0, 0, "", 0, 0, 0, 0);
	}

	public EntityDoor(GameState game, float xPos, float yPos, int UUID, int tileWidth, int tileHeight, String mapName, int lumiX, int lumiY, int pattouX,
			int pattouY)
	{
		super(game, xPos, yPos, UUID);
		this.width = tileWidth * Map.TILESIZE;
		this.height = tileHeight * Map.TILESIZE;
		this.mapName = mapName;
		this.lumiX = lumiX;
		this.lumiY = lumiY;
		this.pattouX = pattouX;
		this.pattouY = pattouY;
		this.setRenderer(null);
	}

	/** Activates this Door. */
	public void activate()
	{
		GameState game = GameState.createNew(this.mapName);
		game.entityLumi.setPosition(this.lumiX * Map.TILESIZE, this.lumiY * Map.TILESIZE);
		game.entityPattou.setPosition(this.pattouX * Map.TILESIZE, this.pattouY * Map.TILESIZE);
		Game.getGame().setCurrentState(game);
	}
}
