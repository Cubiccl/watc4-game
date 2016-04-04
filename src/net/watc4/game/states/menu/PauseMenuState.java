package net.watc4.game.states.menu;

import net.watc4.game.Game;
import net.watc4.game.states.GameState;

/** Used when the Player pause the Game while in the GameState. */
public class PauseMenuState extends MenuState
{
	public static final int CONTINUE = 0, MAINMENU = 1;

	/** Reference to the paused GameState. */
	private GameState gameState;

	public PauseMenuState(GameState gameState)
	{
		super("Pause");
		this.background = gameState;
		this.gameState = gameState;
	}

	@Override
	protected void createButtons()
	{
		this.addButton(new Button(CONTINUE, "Continue Game"));
		this.addButton(new Button(MAINMENU, "Exit to Main Menu"));
	}

	@Override
	protected void performAction(Button selected)
	{
		if (selected.id == CONTINUE) Game.getGame().setCurrentState(this.gameState);
		if (selected.id == MAINMENU) Game.getGame().setCurrentState(new MainMenuState());
	}

}
