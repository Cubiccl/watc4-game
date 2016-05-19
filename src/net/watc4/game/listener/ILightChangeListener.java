package net.watc4.game.listener;

import net.watc4.game.entity.EntityLumi;

/** Listens for Lumi's Light changes. */
public interface ILightChangeListener
{

	/** Called after Lumi's Light changes.
	 * 
	 * @param lumi - Lumi. */
	public void onLightChange(EntityLumi lumi);

}
