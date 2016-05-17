package net.watc4.game.states.menu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import net.watc4.game.display.TextRenderer;
import net.watc4.game.states.State;

/** A State with different buttons to select. */
public abstract class MenuState extends State
{

	/** Contains all the buttons of this Menu. */
	private ArrayList<Button> buttons;
	protected int offset = 0;
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

	public ArrayList<Button> getButtons()
	{
		return this.buttons;
	}

	/** @return The id of the currently selected Button. */
	public int getSelected()
	{
		return this.selected;
	}

	@Override
	public void onKeyPressed(int keyID)
	{
		super.onKeyPressed(keyID);
		if (keyID == KeyEvent.VK_UP)
		{
			this.setSelected(this.selected - 1);
			this.offset = Math.max(0, this.selected - 3);
		}
		if (keyID == KeyEvent.VK_DOWN)
		{
			this.setSelected(this.selected + 1);
			this.offset = Math.max(0, this.selected - 3);
		}
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
	public void renderHud(Graphics2D g, int x, int y, int width, int height)
	{
		super.renderHud(g, x, y, width, height);

		TextRenderer.setFontSize(60);
		g.setColor(Color.DARK_GRAY);
		TextRenderer.drawStringCentered(g, this.title, x + width / 2, y + height / 4);
		TextRenderer.setFontSize(45);

		for (int i = offset; i < this.buttons.size(); i++)
		{
			buttons.get(i).renderHud(g, x, y, width, height);
		}
	}

	/** Places the buttons correctly on the Screen. Called when a Button is added or removed. */
	protected void replaceButtons()
	{
		int m = 0;
		for (int i = offset; i < this.buttons.size(); i++)
		{
			this.buttons.get(i).yPosition = m * (TextRenderer.getFontHeight() + 10);
			m++;
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
