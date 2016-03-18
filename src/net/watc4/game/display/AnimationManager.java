package net.watc4.game.display;

import java.awt.Graphics;
import java.util.ArrayList;

import net.watc4.game.GameObject;

/** Manages all Animations. When an Animation is created it is registered. Unregister an Animation when you don't need it anymore. */
public class AnimationManager implements GameObject
{
	/** All Animations currently used. */
	private ArrayList<Animation> animations;

	public AnimationManager()
	{
		this.animations = new ArrayList<Animation>();
	}

	/** Registers a new Animation. Will be updated automatically.
	 * 
	 * @param animation */
	public void registerAnimation(Animation animation)
	{
		if (!this.animations.contains(animation)) this.animations.add(animation);
	}

	/** Has nothing to render. */
	@Override
	@Deprecated
	public void render(Graphics g)
	{}

	/** Unregisters the Animation. Called when the Animation is disposed.
	 * 
	 * @see Animation#dispose()
	 * @param animation */
	public void unregisterAnimation(Animation animation)
	{
		this.animations.remove(animation);
	}

	@Override
	public void update()
	{
		for (Animation animation : this.animations)
			animation.update();
	}

}
