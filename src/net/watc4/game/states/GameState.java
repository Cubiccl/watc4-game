package net.watc4.game.states;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import net.watc4.game.Game;
import net.watc4.game.entity.EntityLumi;
import net.watc4.game.entity.EntityManager;
import net.watc4.game.entity.EntityPattou;
import net.watc4.game.map.Map;
import net.watc4.game.states.menu.PauseMenuState;

/** Represents the main game engine. */
public class GameState extends State
{
	/** The instance of the Game State. */
	private static GameState instance;

	/** @return The instance of the Game. */
	public static GameState getInstance()
	{
		if (instance == null)
		{
			instance = new GameState();
			instance.getMap().lightManager.update();
		}
		return instance;
	}

	/** The Light Player. */
	public final EntityLumi entityLumi;
	/** Manages Entities in this Game. */
	public final EntityManager entityManager;
	/** The Shadow Player. */
	public final EntityPattou entityPattou;
	/** The world they evolve into. */
	private Map map;

	/** Creates the GameState. */
	public GameState()
	{
		this.entityManager = new EntityManager();
		this.map = new Map("res/maps/map2.txt");

		this.entityLumi = new EntityLumi(this.map.lumiSpawnX, this.map.lumiSpawnY, this);
		this.entityPattou = new EntityPattou(this.map.pattouSpawnX, this.map.pattouSpawnY, this);
	}

	/** @return The <code>Map</code>. */
	public Map getMap()
	{
		return this.map;
	}

	@Override
	public void onKeyPressed(int keyID)
	{
		super.onKeyPressed(keyID);
		if (keyID == KeyEvent.VK_ESCAPE) Game.getGame().setCurrentState(new PauseMenuState(this));
	}

	@Override
	public void render(Graphics g)
	{
		this.map.render(g);
		this.entityManager.render(g);
	}

	@Override
	public void update()
	{
		this.map.update();
		this.entityManager.update();
	}
}
