package net.watc4.game.states.cutscene;

import java.awt.Graphics2D;
import java.util.Stack;

import net.watc4.game.Game;
import net.watc4.game.states.GameState;
import net.watc4.game.states.State;
import net.watc4.game.utils.FileUtils;

/** Used when the Game displays a cutscene, thus explaining the game lore without gameplay. */
public class CutsceneState extends State
{
	/** @param game - The GameState to use for this Cutscene.
	 * @param cutsceneName - The path to the Cutscene file.
	 * @return The Cutscene created from the file. */
	public static State createFrom(GameState game, String cutsceneName)
	{
		CutsceneState cutscene = new CutsceneState(game);
		String[] data = FileUtils.readFileAsStringArray("res/cutscene/" + cutsceneName + ".txt");
		for (String line : data)
		{
			if (line.startsWith("Text")) cutscene.addEvent(new TextEvent(cutscene, line.substring("Text=".length())));
			if (line.startsWith("Cutscene"))
			{
				String[] values = line.split("\t");
				cutscene.addEvent(new ChangeCutsceneEvent(cutscene, values[1], values[2]));
			}
			if (line.startsWith("Move"))
			{
				String[] values = line.split("\t");
				cutscene.addEvent(new EntityMovementEvent(cutscene, Integer.parseInt(values[1]), Float.parseFloat(values[2]), Float.parseFloat(values[3])));
			}
		}
		if (cutscene.events.size() > 0) cutscene.events.peek().begin();
		return cutscene;
	}

	/** Contains all events to execute during this Cutscene. */
	protected Stack<CutsceneEvent> events;

	/** The GameState used to render the environment. */
	public GameState gameState;

	/** Creates a new Cutscene.
	 * 
	 * @param gameState - The GameState used to render the environment. */
	public CutsceneState(GameState gameState)
	{
		this.gameState = gameState;
		this.gameState.isInCutscene = true;
		this.events = new Stack<CutsceneEvent>();
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
	public void render(Graphics2D g)
	{
		this.gameState.render(g);
		if (!this.events.isEmpty()) this.events.peek().render(g);
	}

	@Override
	public void update()
	{
		while (!this.events.isEmpty() && this.events.peek().isOver())
		{
			this.events.pop().finish();
			if (!this.events.isEmpty()) this.events.peek().begin();
		}

		if (this.events.isEmpty())
		{
			Game.getGame().setCurrentState(this.gameState, false);
			this.gameState.isInCutscene = false;
		} else this.events.peek().update();

		this.gameState.update();
	}

}
