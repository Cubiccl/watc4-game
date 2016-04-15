package net.watc4.game.states.cutscene;

import java.awt.Graphics;
import java.util.Stack;

import net.watc4.game.Game;
import net.watc4.game.states.GameState;
import net.watc4.game.states.State;

/** Used when the Game displays a cutscene, thus explaining the game lore without gameplay. */
public class CutsceneState extends State
{
	/** Contains all events to execute during this Cutscene. */
	protected Stack<CutsceneEvent> events;
	/** The GameState used to render the environment. */
	public final GameState gameState;

	/** Creates a new Cutscene.
	 * 
	 * @param gameState - The GameState used to render the environment. */
	public CutsceneState(GameState gameState)
	{
		this.gameState = gameState;
		this.events = new Stack<CutsceneEvent>();
		this.addEvent(new TextEvent(this, "Press Space to continue", true));
		this.addEvent(new TextEvent(this, "This is a Cutscene"));
	}

	/** Adds an Event at the end of this Cutscene.
	 * 
	 * @param event - The new Event. */
	public void addEvent(CutsceneEvent event)
	{
		this.events.insertElementAt(event, 0);
	}

	@Override
	public void onKeyPressed(int keyID)
	{
		super.onKeyPressed(keyID);
		if (!this.events.isEmpty()) this.events.peek().onKeyPressed(keyID);
	}

	@Override
	public void render(Graphics g)
	{
		this.gameState.render(g);
		if (!this.events.isEmpty()) this.events.peek().render(g);
	}

	@Override
	public void update()
	{
		while (!this.events.isEmpty() && this.events.peek().isOver())
		{
			this.events.pop();
			if (!this.events.isEmpty()) this.events.peek().begin();
		}
		if (this.events.isEmpty()) Game.getGame().setCurrentState(this.gameState);
		else this.events.peek().update();
	}

}
