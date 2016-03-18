package net.watc4.game.display.renderer;

import java.awt.Graphics;

import net.watc4.game.GameObject;
import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.entity.Entity;

/** Draws an Entity depending on its state. */
public class EntityRenderer implements GameObject
{
	/** The Animation used to draw. */
	private Animation animation;
	/** The Entity to draw. */
	protected Entity entity;

	/** Creates a Renderer with the default Unknown sprite.
	 * 
	 * @param entity - The Entity to draw. */
	public EntityRenderer(Entity entity)
	{
		this(entity, new Animation(Sprite.UNKNOWN));
	}

	/** Creates a Renderer.
	 * 
	 * @param entity - The Entity to draw.
	 * @param animation - The default animation. */
	public EntityRenderer(Entity entity, Animation animation)
	{
		this.entity = entity;
		this.animation = animation;
	}

	@Override
	public void render(Graphics g)
	{
		g.drawImage(this.animation.getImage(), (int) this.entity.getX(), (int) this.entity.getY(), null);
	}

	/** Changes the current Animation.
	 * 
	 * @param animation - The new Animation to use. */
	public void setAnimation(Animation animation)
	{
		if (this.animation != null) this.animation.dispose();
		this.animation = animation;
		if (this.animation != null) this.animation.register();
	}

	/** EntityRenderers don't update. */
	@Override
	@Deprecated
	public void update()
	{}

}