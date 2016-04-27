package net.watc4.game.states.menu;

import net.watc4.game.Game;
import net.watc4.game.states.GameState;

/** The Main Menu. */
public class MainMenuState extends MenuState
{
	public static final int PLAY = 0, EXIT = 1, SETTINGS = 2;

	public MainMenuState()
	{
		super("WATC4");
	}

	@Override
	protected void createButtons()
	{
		this.addButton(new Button(PLAY, "Play"));
		this.addButton(new Button(SETTINGS, "Settings"));
		this.addButton(new Button(EXIT, "Exit Game"));
	}

	@Override
	protected void performAction(Button selected)
	{
		if (selected.id == PLAY) Game.getGame().setCurrentState(GameState.getInstance());
		if (selected.id == SETTINGS) Game.getGame().setCurrentState(new SettingsMenuState());
		if (selected.id == EXIT) Game.getGame().stop();
	}

}
