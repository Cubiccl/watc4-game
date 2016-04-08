package net.watc4.game.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import net.watc4.game.Game;
import net.watc4.game.display.Camera;
import net.watc4.game.display.TextRenderer;
import net.watc4.game.entity.EntityBattery;
import net.watc4.game.entity.EntityLumi;
import net.watc4.game.entity.EntityManager;
import net.watc4.game.entity.EntityPattou;
import net.watc4.game.entity.EntityPlayer;
import net.watc4.game.map.Map;
import net.watc4.game.states.menu.PauseMenuState;
import net.watc4.game.utils.GameSettings;

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

	/** The Camera determining which part of the Map to render. */
	private Camera camera;
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
		this.camera = new Camera();
		this.map = new Map("res/maps/map2.txt");

		this.entityLumi = new EntityLumi(this.map.lumiSpawnX, this.map.lumiSpawnY, this);
		this.entityPattou = new EntityPattou(this.map.pattouSpawnX, this.map.pattouSpawnY, this);
		new EntityBattery(2 * Map.TILESIZE, 3 * Map.TILESIZE, this, 5, 5);
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
		this.camera.centerOn(this.entityLumi, this.entityPattou, this.map);
		g.translate(-this.camera.getXOffset(), -this.camera.getYOffset());
		this.map.render(g);
		//this.entityManager.render(g);
		this.entityPattou.render(g);
		g.translate(this.camera.getXOffset(), this.camera.getYOffset());

		if (GameSettings.debugMode)
		{
			g.setColor(Color.DARK_GRAY);
			TextRenderer.setFontSize(15);
			int size = TextRenderer.getFontHeight();
			int y = 2;
			TextRenderer.drawString(g, "Lumi: " + this.entityLumi.getX() + ", " + this.entityLumi.getY(), 0, y * size);
			++y;
			TextRenderer.drawString(g, "Pattou: " + this.entityPattou.getX() + ", " + this.entityPattou.getY(), 0, y * size);
			++y;
			TextRenderer.drawString(g, "Lumi HP: " + this.entityLumi.getHealth() + "/" + EntityPlayer.MAX_HEALTH, 0, y * size);
			++y;
			TextRenderer.drawString(g, "Pattou HP: " + this.entityPattou.getHealth() + "/" + EntityPlayer.MAX_HEALTH, 0, y * size);
		}
	}

	@Override
	public void update()
	{
		this.map.update();
		this.entityManager.update();
	}
}
