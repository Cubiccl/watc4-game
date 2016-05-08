package net.watc4.game.states.menu;

import net.watc4.game.Game;
import net.watc4.game.utils.GameSettings;

public class VideoMenuState extends MenuState {
	
	private static final int OK = 0, CANCEL = 1, RESOLUTION = 2;

	private int previousResolution;

	public VideoMenuState()
	{
		super("Video settings");
		this.previousResolution = GameSettings.resolution;
	}

	/** Resets all changes made to the Settings. Called when the user selects Cancel. */
	private void abortChanges()
	{
		GameSettings.resolution = this.previousResolution;
	}

	/** Confirms and applies all changes made to the Settings. Called when the user selects OK. */
	private void confirmChanges()
	{
		if (this.previousResolution != GameSettings.resolution) Game.getGame().window.applyResolution();
	}

	@Override
	protected void createButtons()
	{
		this.addButton(new Button(RESOLUTION, "Resolution: " + GameSettings.getResolution()));
		this.addButton(new Button(OK, "OK"));
		this.addButton(new Button(CANCEL, "Cancel"));
	}

	@Override
	protected void performAction(Button selected)
	{
		if (selected.id == RESOLUTION)
		{
			GameSettings.cycleResolution();
			selected.text = "Resolution: " + GameSettings.getResolution();
		}
		if (selected.id == OK)
		{
			this.confirmChanges();
			Game.getGame().setCurrentState(new MainMenuState());
		}
		if (selected.id == CANCEL)
		{
			this.abortChanges();
			Game.getGame().setCurrentState(new MainMenuState());
		}
	}

}
