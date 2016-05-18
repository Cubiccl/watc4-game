package net.watc4.game.utils.lore;

import net.watc4.game.Game;
import net.watc4.game.states.GameState;
import net.watc4.game.states.cutscene.CutsceneState;

/** Defines what to do after the end of a Map or Cutscene. */
public class Ending
{
	public static final byte MAP = 0, CUTSCENE = 1, NULL = 2;

	public final String name;
	public final byte type;

	public Ending(byte type, String name)
	{
		this.type = type;
		this.name = name;
	}

	/** Applies this Ending. */
	public void apply(GameState currentGame)
	{
		switch (this.type)
		{
			case MAP:
				Game.getGame().setCurrentState(GameState.createNew(this.name));
				break;

			case CUTSCENE:
				Game.getGame().setCurrentState(CutsceneState.createFrom(currentGame, this.name));
				break;

			default:
				Game.getGame().setCurrentState(currentGame, false);
				break;
		}
	}

}
