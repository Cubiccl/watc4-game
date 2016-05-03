package net.watc4.game.states.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import net.watc4.game.display.Camera;
import net.watc4.game.display.TextRenderer;
import net.watc4.game.states.State;

/** A State with different buttons to select. */
public abstract class MenuState extends State
{

	/** Contains all the buttons of this Menu. */
	private ArrayList<Button> buttons;
	/** The id of the currently selected button. */
	private int selected;
	/** The Title of this Menu. */
	protected String title;

	/** Creates a new MenuState.
	 * 
	 * @param title - The Title of this Menu. */
	public MenuState(String title)
	{
		this.title = title;
		this.buttons = new ArrayList<Button>();
		this.createButtons();
	}

	/** Adds a Button to this Menu.
	 * 
	 * @param button - The Button to add. */
	public void addButton(Button button)
	{
		if (!this.buttons.contains(button)) this.buttons.add(button);
		this.replaceButtons();
		if (this.buttons.size() == 1) button.isSelected = true;
	}

	/** Creates the Buttons of this Menu. */
	protected abstract void createButtons();

	/** @return The id of the currently selected Button. */
	public int getSelected()
	{
		return this.selected;
	}

	@Override
	public void onKeyPressed(int keyID)
	{
		super.onKeyPressed(keyID);
		if (keyID == KeyEvent.VK_UP) this.setSelected(this.selected - 1);
		if (keyID == KeyEvent.VK_DOWN) this.setSelected(this.selected + 1);
		if (keyID == KeyEvent.VK_ENTER) this.performAction(this.buttons.get(this.selected));
	}

	/** Called when the User selects a Button.
	 * 
	 * @param selected - The Button that is selected. */
	protected abstract void performAction(Button selected);

	/** Removes a Button from this Menu.
	 * 
	 * @param button - The Button to remove. */
	public void removeButton(Button button)
	{
		if (this.buttons.contains(button)) this.buttons.remove(button);
		this.replaceButtons();
	}

	@Override
	public void render(Graphics g)
	{
		super.render(g);
		
		TextRenderer.setFontSize(40);
		g.setColor(Color.DARK_GRAY);
		TextRenderer.drawStringCentered(g, this.title, Camera.WIDTH / 2, Camera.HEIGHT / 4);
		TextRenderer.setFontSize(30);

		for (Button button : this.buttons)
			button.render(g);
	}

	/** Places the buttons correctly on the Screen. Called when a Button is added or removed. */
	private void replaceButtons()
	{
		for (int i = 0; i < this.buttons.size(); i++)
		{
			this.buttons.get(i).xPosition = Camera.WIDTH / 2;
			this.buttons.get(i).yPosition = Camera.HEIGHT / 2 + i * (TextRenderer.getFontHeight() + 10);
		}
	}

	/** Changes the currently selected Button.
	 * 
	 * @param selected - The ID of the button to select. */
	public void setSelected(int selected)
	{
		this.buttons.get(this.selected).isSelected = false;
		this.selected = selected;
		if (this.selected < 0) this.selected = this.buttons.size() - 1;
		if (this.selected == this.buttons.size()) this.selected = 0;
		this.buttons.get(this.selected).isSelected = true;
	}

	@Override
	public void update()
	{}

}
