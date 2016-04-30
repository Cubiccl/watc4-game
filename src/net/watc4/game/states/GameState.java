package net.watc4.game.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import net.watc4.game.Game;
import net.watc4.game.display.Camera;
import net.watc4.game.display.TextRenderer;
import net.watc4.game.entity.EntityLumi;
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

	/** Creates the GameState.
	 * 
	 * @param mapName - The (file) name of the Map to use.
	 * @return The created GameState. */
	public static GameState createNew(String mapName)
	{
		instance = new GameState(mapName);
		instance.getMap().lightManager.update();
		return instance;
	}

	/** @return The instance of the Game. */
	public static GameState getInstance()
	{
		return instance;
	}

	/** The Camera determining which part of the Map to render. */
	public final Camera camera;
	/** The Light Player. */
	public EntityLumi entityLumi;
	/** The Shadow Player. */
	public EntityPattou entityPattou;
	/** True if this State is in a Cutscene, thus the user cannot interact with this State. */
	public boolean isInCutscene;
	/** The world they evolve into. */
	private Map map;
	/** The name of the current Map. */
	private String mapName;

	/** Creates the GameState.
	 * 
	 * @param mapName - The (file) name of the Map to use. */
	public GameState(String mapName)
	{
		this.mapName = mapName;
		this.camera = new Camera();
		this.map = Map.createFrom(this.mapName, this);
	}

	/** Draws a Red overlay. It becomes more opaque the more damage the player takes.
	 * 
	 * @param g - The Graphics required to draw. */
	private void drawDamage(Graphics g)
	{
		int life = this.entityPattou.getHealth() * 255 / EntityPlayer.MAX_HEALTH;
		if (life >= 255 || life < 0) return;
		g.setColor(new Color(200, 50, 50, 255 - life));
		g.fillRect(0, 0, Camera.WIDTH, Camera.HEIGHT);
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
		if (!this.isInCutscene)
		{
			if (keyID == KeyEvent.VK_ESCAPE) Game.getGame().setCurrentState(new PauseMenuState(this), false);
		}
	}

	@Override
	public void render(Graphics g)
	{
		this.camera.centerOn(this.entityLumi, this.entityPattou, this.map);
		((Graphics2D) g).scale(this.camera.getScale(), this.camera.getScale());
		g.translate((int) -this.camera.getXOffset(), (int) -this.camera.getYOffset());
		this.map.render(g);
		this.entityPattou.render(g);
		((Graphics2D) g).scale(1 / this.camera.getScale(), 1 / this.camera.getScale());
		g.translate((int) this.camera.getXOffset(), (int) this.camera.getYOffset());
		this.drawDamage(g);

		if (GameSettings.debugMode)
		{
			g.setColor(Color.DARK_GRAY);
			TextRenderer.setFontSize(15);
			int size = 40;
			int y = 2;
			TextRenderer.drawString(g, "Lumi: " + this.entityLumi.getX() + ", " + this.entityLumi.getY(), 0, y * size);
			++y;
			TextRenderer.drawString(g, "Pattou: " + this.entityPattou.getX() + ", " + this.entityPattou.getY(), 0, y * size);
			++y;
			TextRenderer.drawString(g, "Lumi HP: " + this.entityLumi.getHealth() + "/" + EntityPlayer.MAX_HEALTH, 0, y * size);
			++y;
			TextRenderer.drawString(g, "Pattou HP: " + this.entityPattou.getHealth() + "/" + EntityPlayer.MAX_HEALTH, 0, y * size);
			++y;
			if (GameSettings.godMode) TextRenderer.drawString(g, "God mode (F2) ON", 0, y * size);
			else TextRenderer.drawString(g, "God mode (F2) OFF", 0, y * size);
			++y;
			if (GameSettings.lightMode) TextRenderer.drawString(g, "Light mode (F3) ON", 0, y * size);
			else TextRenderer.drawString(g, "Light mode (F3) OFF", 0, y * size);
			++y;
			if (GameSettings.drawHitboxes) TextRenderer.drawString(g, "Hitbox mode (F4) ON", 0, y * size);
			else TextRenderer.drawString(g, "Hitbox mode (F4) OFF", 0, y * size);

		}
	}

	/** Resets the Game & Map as they were created. */
	public void reset()
	{
		this.map = Map.createFrom(this.mapName, this);
		this.map.lightManager.update();
	}

	@Override
	public void update()
	{
		this.map.update();
	}
}
