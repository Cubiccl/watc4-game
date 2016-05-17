package net.watc4.game.states.cutscene;

import java.awt.Color;
import java.awt.Graphics2D;

import net.watc4.game.display.TextRenderer;
import net.watc4.game.utils.GameUtils;

/** Displays text until the user skips it. */
public class TextEvent extends CutsceneEvent
{
	/** True if the text should be displayed on the bottom (top else). */
	private boolean bottom;
	/** True if the user skipped this text and is ready to go on. */
	private boolean passed;
	/** The text to show. */
	private String text;

	/** Creates a new Text Event.
	 * 
	 * @param cutscene - The parent Cutscene.
	 * @param text - The text to show. */
	public TextEvent(CutsceneState cutsceneState, String string)
	{
		this(cutsceneState, string, false);
	}

	/** Creates a new Text Event.
	 * 
	 * @param cutscene - The parent Cutscene.
	 * @param text - The text to show.
	 * @param bottom - True if the text should be displayed on the bottom (top else). */
	public TextEvent(CutsceneState cutsceneState, String text, boolean bottom)
	{
		super(cutsceneState);
		this.text = text;
		this.bottom = bottom;
		this.passed = false;
	}

	@Override
	public boolean isOver()
	{
		return this.passed;
	}

	@Override
	public void onKeyPressed(int keyID)
	{
		super.onKeyPressed(keyID);
		if (keyID == GameUtils.PATTOU_JUMP) this.passed = true;
	}

	@Override
	public void renderHud(Graphics2D g, int x, int y, int width, int height)
	{
		super.renderHud(g, x, y, width, height);
		final int offset = 5;
		y += offset;
		int boxHeight = offset * 6;
		if (this.bottom) y += height - boxHeight - offset * 2;

		g.setColor(new Color(50, 0, 50));
		g.fillRect(x + offset, y, width - offset * 4, boxHeight);
		g.setColor(new Color(150, 100, 150));
		g.drawRect(x + offset, y, width - offset * 4, boxHeight);

		TextRenderer.setFontSize(20);
		TextRenderer.drawStringCentered(g, this.text, x + width / 2, y + boxHeight / 3);
	}

}
