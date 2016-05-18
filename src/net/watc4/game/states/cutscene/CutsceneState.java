package net.watc4.game.states.cutscene;

import java.awt.Graphics2D;
import java.util.Stack;

import net.watc4.game.entity.EntityCutscene;
import net.watc4.game.states.GameState;
import net.watc4.game.states.State;
import net.watc4.game.utils.FileUtils;
import net.watc4.game.utils.lore.LoreManager;

/** Used when the Game displays a cutscene, thus explaining the game lore without gameplay. */
public class CutsceneState extends State
{
	/** @param entityCutscene - The Entity that started this Cutscene.
	 * @return The created Cutscene. */
	public static CutsceneState createFrom(EntityCutscene entity)
	{
		CutsceneState cutscene = createFrom(entity.game, entity.cutsceneName);
		cutscene.entity = entity;
		return cutscene;
	}

	/** @param game - The GameState to use for this Cutscene.
	 * @param cutsceneName - The path to the Cutscene file.
	 * @return The Cutscene created from the file. */
	public static CutsceneState createFrom(GameState game, String cutsceneName)
	{
		CutsceneState cutscene = new CutsceneState(game, cutsceneName);
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
				cutscene.addEvent(new EntityMovementEvent(cutscene, Integer.parseInt(values[1]), Boolean.parseBoolean(values[2]), Float.parseFloat(values[3]),
						Float.parseFloat(values[4])));
			}
		}
		if (cutscene.events.size() > 0) cutscene.events.peek().begin();
		return cutscene;
	}

	/** The Entity that started this Cutscene. */
	private EntityCutscene entity;
	/** Contains all events to execute during this Cutscene. */
	protected Stack<CutsceneEvent> events;
	/** The GameState used to render the environment. */
	public GameState gameState;
	/** This Cutscene's name. */
	public final String name;

	/** Creates a new Cutscene.
	 * 
	 * @param gameState - The GameState used to render the environment. */
	public CutsceneState(GameState gameState, String name)
	{
		this.gameState = gameState;
		this.name = name;
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
	public void renderHud(Graphics2D g, int x, int y, int width, int height)
	{
		super.renderHud(g, x, y, width, height);
		this.gameState.renderHud(g, x, y, width, height);
		if (!this.events.isEmpty()) this.events.peek().renderHud(g, x, y, width, height);
	}

	@Override
	public void update()
	{
		if (this.entity != null)
		{
			this.entity.kill();
			this.entity = null;
		}
		while (!this.events.isEmpty() && this.events.peek().isOver())
		{
			this.events.pop().finish();
			if (!this.events.isEmpty()) this.events.peek().begin();
		}

		if (this.events.isEmpty())
		{
			this.gameState.isInCutscene = false;
			LoreManager.activateEnding(this.name, this.gameState);
		} else this.events.peek().update();

		this.gameState.update();
	}

}
