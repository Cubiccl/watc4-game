package net.watc4.game.states.menu;

import net.watc4.game.Game;

/** Screen where the user can adjust settings */
public class SettingsMenuState extends MenuState
{
	public static final int CONTROLS = 0, VIDEO_SETTINGS = 1, BACK = 2;

	public SettingsMenuState()
	{
		super("Settings");
	}

	@Override
	protected void createButtons()
	{
		this.addButton(new Button(CONTROLS, "CONTROLS"));
		this.addButton(new Button(VIDEO_SETTINGS, "VIDEO SETTINGS"));
		this.addButton(new Button(BACK, "BACK"));
	}

	@Override
	protected void performAction(Button selected)
	{
		if (selected.id == CONTROLS)
		{
			Game.getGame().setCurrentState(new ControlsMenuState());
		}
		if (selected.id == VIDEO_SETTINGS)
		{
			Game.getGame().setCurrentState(new VideoMenuState());
		}
		if (selected.id == BACK)
		{
			Game.getGame().setCurrentState(new MainMenuState());
		}
	}

}
