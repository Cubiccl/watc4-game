package net.watc4.game.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.HashSet;

import net.watc4.game.Game;
import net.watc4.game.display.Animation;
import net.watc4.game.display.Background;
import net.watc4.game.display.Camera;
import net.watc4.game.display.Sprite;
import net.watc4.game.display.TextRenderer;
import net.watc4.game.entity.Entity;
import net.watc4.game.entity.EntityLumi;
import net.watc4.game.entity.EntityPattou;
import net.watc4.game.listener.IEntityMovementListener;
import net.watc4.game.listener.ILightChangeListener;
import net.watc4.game.map.Map;
import net.watc4.game.states.menu.PauseMenuState;
import net.watc4.game.utils.GameSettings;
import net.watc4.game.utils.lore.LoreManager;

/** Represents the main game engine. */
public class GameState extends State implements IEntityMovementListener
{

	/** Creates the GameState.
	 * 
	 * @param mapName - The (file) name of the Map to use.
	 * @return The created GameState. */
	public static GameState createNew(String mapName)
	{
		GameState instance = new GameState(mapName);
		instance.getMap().lightManager.update();
		return instance;
	}

	/** The Camera determining which part of the Map to render. */
	public final Camera camera;
	/** The Light Player. */
	public EntityLumi entityLumi;
	/** The Shadow Player. */
	public EntityPattou entityPattou;
	/** True if the given Player is used in this State. */
	public boolean hasLumi, hasPattou;
	/** True if this State is in a Cutscene, thus the user cannot interact with this State. */
	public boolean isInCutscene;
	/** Light Change Listeners. */
	private HashSet<ILightChangeListener> lightListeners;
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
		this.hasLumi = false;
		this.hasPattou = false;
		this.lightListeners = new HashSet<ILightChangeListener>();
		this.map = Map.createFrom(this.mapName, this);
		if (this.hasLumi) this.entityLumi.addMovementListener(this);
		this.setBackground(new Background(new Animation(Sprite.TILE_WALL), this));
	}

	/** @param listener - The Listener. Will be called when Lumi's Light changes. */
	public void addLightChangeListener(ILightChangeListener listener)
	{
		this.lightListeners.add(listener);
	}

	/** Draws a Red overlay. It becomes more opaque the more damage the player takes.
	 * 
	 * @param g - The Graphics required to draw. */
	private void drawDamage(Graphics g)
	{
		int life = 255;
		if (this.hasPattou) life = this.entityPattou.getHealth() * 255 / this.entityPattou.getMaxHealth();
		if (life >= 255 || life < 0) return;
		g.setColor(new Color(200, 50, 50, 255 - life));
		g.fillRect(0, 0, Camera.WIDTH, Camera.HEIGHT);
	}

	/** @return The <code>Camera</code>. */
	public Camera getCamera()
	{
		return this.camera;
	}

	/** @return The <code>Map</code>. */
	public Map getMap()
	{
		return this.map;
	}

	/** @return True if the current Level is over. */
	private boolean isLevelOver()
	{
		boolean over = true;
		if (this.hasLumi && !this.entityLumi.reachedEnd()) over = false;
		if (this.hasPattou && !this.entityPattou.reachedEnd()) over = false;
		return over;
	}

	@Override
	public void onEntityMove(Entity entity)
	{
		if (entity == this.entityLumi) for (ILightChangeListener listener : this.lightListeners)
			listener.onLightChange(this.entityLumi);
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
	public void onLoad()
	{
		Game.getGame().getSoundManager().play(this.mapName);
	}

	@Override
	public void onUnload()
	{
		Game.getGame().getSoundManager().clip_close(this.mapName);
	}

	/** @param listener - The Listener to remove. */
	public void removeLightChangeListener(ILightChangeListener listener)
	{
		this.lightListeners.remove(listener);
	}

	@Override
	public void render(Graphics2D g)
	{
		this.camera.centerOn(this.entityLumi, this.entityPattou, this.map);

		int xOffset = (int) this.camera.getXOffset(), yOffset = (int) this.camera.getYOffset();
		int width = (int) this.camera.getWidth(), height = (int) this.camera.getHeight();
		((Graphics2D) g).scale(this.camera.getScale(), this.camera.getScale());
		g.translate(-xOffset, -yOffset);

		super.render(g);
		this.map.render(g);
		if (this.hasPattou) this.entityPattou.render(g);

		g.setColor(Color.BLACK);
		if (xOffset < 0) g.fillRect(xOffset, yOffset, -xOffset, height);
		if (yOffset < 0) g.fillRect(xOffset, yOffset, width, -yOffset);
		g.fillRect(this.map.width * Map.TILESIZE, yOffset, xOffset, height);
		g.fillRect(xOffset, this.map.height * Map.TILESIZE, width, height);

		((Graphics2D) g).scale(1 / this.camera.getScale(), 1 / this.camera.getScale());
		g.translate(xOffset, yOffset);
		this.drawDamage(g);
	}

	@Override
	public void renderHud(Graphics2D g, int x, int y, int width, int height)
	{
		super.renderHud(g, x, y, width, height);

		if (GameSettings.debugMode)
		{
			g.setColor(Color.DARK_GRAY);
			TextRenderer.setFontSize(25);
			int size = 30;
			y += 2;
			if (this.hasLumi)
			{
				TextRenderer.drawString(g, "Lumi: " + this.entityLumi.getX() + ", " + this.entityLumi.getY(), x, y * size);
				++y;
			}
			if (this.hasPattou)
			{
				TextRenderer.drawString(g, "Pattou: " + this.entityPattou.getX() + ", " + this.entityPattou.getY(), x, y * size);
				++y;
			}
			if (this.hasLumi)
			{
				TextRenderer.drawString(g, "Lumi HP: " + this.entityLumi.getHealth() + "/" + this.entityLumi.getMaxHealth(), x, y * size);
				++y;
			}
			if (this.hasPattou)
			{
				TextRenderer.drawString(g, "Pattou HP: " + this.entityPattou.getHealth() + "/" + this.entityPattou.getMaxHealth(), x, y * size);
				++y;
			}
			if (GameSettings.godMode) TextRenderer.drawString(g, "God mode (F2) ON", x, y * size);
			else TextRenderer.drawString(g, "God mode (F2) OFF", x, y * size);
			++y;
			if (GameSettings.lightMode) TextRenderer.drawString(g, "Light mode (F3) ON", x, y * size);
			else TextRenderer.drawString(g, "Light mode (F3) OFF", x, y * size);
			++y;
			if (GameSettings.drawHitboxes) TextRenderer.drawString(g, "Hitbox mode (F4) ON", x, y * size);
			else TextRenderer.drawString(g, "Hitbox mode (F4) OFF", x, y * size);

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
		if (!this.isInCutscene && this.isLevelOver()) LoreManager.activateEnding(this.mapName, this);
	}
}
