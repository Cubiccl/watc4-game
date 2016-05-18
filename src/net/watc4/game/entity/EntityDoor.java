package net.watc4.game.entity;

import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.display.renderer.EntityRenderer;
import net.watc4.game.map.Map;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.lore.LoreManager;

/** When Pattou collides with this and presses UP, it will take it to a new Map. */
public class EntityDoor extends Entity
{

	public EntityDoor()
	{
		this(null, 0, 0, 0, 0, 0);
	}

	public EntityDoor(GameState game, float xPos, float yPos, int UUID, int tileWidth, int tileHeight)
	{
		super(game, xPos, yPos, UUID);
		this.width = tileWidth * Map.TILESIZE;
		this.height = tileHeight * Map.TILESIZE;
		this.setRenderer(new EntityRenderer(this, new Animation(Sprite.DOOR)));
	}

	/** Activates this Door. */
	public void activate()
	{
		LoreManager.activateDoor(this.UUID, this.game.getMap().name);
	}
}
