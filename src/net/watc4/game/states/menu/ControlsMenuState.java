package net.watc4.game.states.menu;

import java.awt.event.KeyEvent;

import net.watc4.game.Game;

/** Screen where the user can change control settings */
public class ControlsMenuState extends MenuState
{

	private static final int P_LEFT = 0;
	private static final int P_RIGHT = 1;
	private static final int P_UP = 2;
	private static final int P_DOWN = 3;
	private static final int P_JUMP = 4;

	private static final int L_LEFT = 5;
	private static final int L_RIGHT = 6;
	private static final int L_UP = 7;
	private static final int L_DOWN = 8;

	private static final int DEFAULT_CONTROLS = 9;
	private static final int OK = 10;

	private int[] actions =
	{ P_LEFT, P_RIGHT, P_UP, P_DOWN, P_JUMP, L_LEFT, L_RIGHT, L_UP, L_DOWN };
	private String[] texts =
	{ "P_LEFT : ", "P_RIGHT : ", "P_UP : ", "P_DOWN : ", "P_JUMP : ", "L_LEFT : ", "L_RIGHT : ", "L_UP : ", "L_DOWN : " };

	private boolean waiting_for_input = false;

	public ControlsMenuState()
	{
		super("Controls");
	}

	/*/** Resets all changes made to the Settings. Called when the user selects Cancel. private void abortChanges() {
	 * 
	 * } */

	@Override
	protected void createButtons()
	{

		this.addButton(new Button(P_LEFT, "P_LEFT : "));
		this.addButton(new Button(P_RIGHT, "P_RIGHT : "));
		this.addButton(new Button(P_UP, "P_UP : "));
		this.addButton(new Button(P_DOWN, "P_DOWN : "));
		this.addButton(new Button(P_JUMP, "P_JUMP : "));

		this.addButton(new Button(L_LEFT, "L_LEFT : "));
		this.addButton(new Button(L_RIGHT, "L_RIGHT : "));
		this.addButton(new Button(L_UP, "L_UP : "));
		this.addButton(new Button(L_DOWN, "L_DOWN : "));

		this.addButton(new Button(OK, "OK"));
		this.addButton(new Button(DEFAULT_CONTROLS, "DEFAULT"));

	}

	@Override
	protected void performAction(Button selected)
	{

		if ((selected.id != OK) && (selected.id != DEFAULT_CONTROLS))
		{
			selected.text = texts[selected.id] + "_";
			waiting_for_input = true;
		}

		if (selected.id == OK)
		{
			Game.getGame().setCurrentState(new MainMenuState());
		}

		if (selected.id == DEFAULT_CONTROLS)
		{
			Game.getGame().setCurrentState(new MainMenuState());
		}

	}

	@Override
	public void onKeyPressed(int keyID)
	{

		if ((waiting_for_input) && ((getSelected() != OK) && (getSelected() != DEFAULT_CONTROLS)))
		{
			waiting_for_input = false;
			for (int i = 0; i < actions.length; i++)
			{
				if (getSelected() == actions[i])
				{
					super.getButtons().get(i).text = texts[i] + KeyEvent.getKeyText(keyID);
					Game.getGame().getInputManager().clearMap(Game.getGame().getControls().controlsList.get(i));
					Game.getGame().getInputManager().mapToKey(Game.getGame().getControls().controlsList.get(i), keyID);
				}
			}

		} else super.onKeyPressed(keyID);

	}

}
