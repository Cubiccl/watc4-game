package net.watc4.game.display;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.watc4.game.GameObject;

/** Used to display Sprites. Can contain several Sprites that will cycle over time. */
public class Animation implements GameObject
{
	/** Index of the current Sprite to display. */
	private int currentSprite;
	/** The speed of this Animation. Represents the number of ticks between each Sprite. */
	public final int speed;
	/** List of all Sprites to display. */
	private Sprite[] sprites;
	/** Number of ticks the current Sprite has been displayed. */
	private int tick;

	/** Creates a new Animation.
	 * 
	 * @param sprites - The Sprites to cycle into.
	 * @param speed- The speed of the Animation. (ticks per Sprite) */
	public Animation(int speed, Sprite... sprites)
	{
		this.sprites = sprites;
		this.currentSprite = 0;
		this.tick = 0;
		this.speed = speed;
		this.register();
	}

	/** Creates a new Animation, with one Sprite.
	 * 
	 * @param sprite */
	public Animation(Sprite sprite)
	{
		this(0, sprite);
	}

	/** Unregisters the Animation from the Animation Manager. Call when you stop using the Animation. */
	public void dispose()
	{
		AnimationManager.unregisterAnimation(this);
	}

	/** @return The current Image to display. */
	public BufferedImage getImage()
	{
		return this.sprites[this.currentSprite].getImage();
	}

	/** Cycles through the Sprites.
	 * 
	 * @return The new Image to display. */
	public BufferedImage next()
	{
		this.currentSprite = (this.currentSprite + 1) % this.sprites.length;
		this.tick = 0;
		return this.getImage();
	}

	/** Registers this Animation into the AnimationManager. */
	public void register()
	{
		AnimationManager.registerAnimation(this);
	}

	/** Do not use render(Graphics) on this <code>Animation</code>. Use getImage() and render with the Object using this <code>Animation</code>.
	 * 
	 * @see Animation#getImage() */
	@Override
	@Deprecated
	public void render(Graphics g)
	{}

	@Override
	public void update()
	{
		if (this.speed > 0)
		{
			++this.tick;
			if (this.tick >= this.speed) this.next();
		}
	}

}
