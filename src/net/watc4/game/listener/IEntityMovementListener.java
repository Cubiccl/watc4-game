package net.watc4.game.listener;

import net.watc4.game.entity.Entity;

/** Listens for Entity movement. */
public interface IEntityMovementListener
{

	/** Called after an Entity moves.
	 * 
	 * @param entity - The moving Entity. */
	public void onEntityMove(Entity entity);

}
