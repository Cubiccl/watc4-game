package net.watc4.game.display;

import java.awt.image.BufferedImage;

import net.watc4.game.utils.IUpdate;

/** Used to display Sprites. Can contain several Sprites that will cycle over time. */
public class Animation implements IUpdate
{
	/** Index of the current Sprite to display. */
	private int currentSprite;
	/** True if this animation loops only once. */
	public boolean loopsOnce;
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
		this.loopsOnce = false;
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

	public boolean isOver()
	{
		return this.loopsOnce && this.currentSprite == this.sprites.length - 1;
	}

	/** Cycles through the Sprites.
	 * 
	 * @return The new Image to display. */
	public BufferedImage next()
	{
		this.currentSprite = (this.currentSprite + 1) % this.sprites.length;
		this.tick = 0;
		if (this.isOver()) this.dispose();
		return this.getImage();
	}

	/** Registers this Animation into the AnimationManager. */
	public void register()
	{
		AnimationManager.registerAnimation(this);
	}

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
