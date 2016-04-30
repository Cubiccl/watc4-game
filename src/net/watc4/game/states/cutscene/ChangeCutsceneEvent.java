package net.watc4.game.states.cutscene;

import net.watc4.game.Game;
import net.watc4.game.states.GameState;

/** This Event changes the current Cutscene and Map. */
public class ChangeCutsceneEvent extends CutsceneEvent
{
	/** The name of the new Cutscene to change to. */
	private String cutsceneName;
	/** The name of the new Map to change to. */
	private String mapName;

	/** @param cutscene - The Cutscene this Event belongs to.
	 * @param cutsceneName - The name of the new Cutscene to change to.
	 * @param mapName - The name of the new Map to change to. */
	public ChangeCutsceneEvent(CutsceneState cutscene, String cutsceneName, String mapName)
	{
		super(cutscene);
		this.cutsceneName = cutsceneName;
		this.mapName = mapName;
	}

	@Override
	public void begin()
	{
		super.begin();
		Game.getGame().setCurrentState(CutsceneState.createFrom(GameState.createNew(this.mapName), this.cutsceneName));
	}

	@Override
	public boolean isOver()
	{
		return true;
	}

}
