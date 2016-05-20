package net.watc4.game.display;

import java.util.HashSet;

/** Manages all Animations. When an Animation is created it is registered. Unregister an Animation when you don't need it anymore. */
public class AnimationManager
{
	/** All Animations currently used. */
	private static HashSet<Animation> animations;
	private static HashSet<Animation> toRemove;

	/** Creates the AnimationManager. */
	public static void create()
	{
		animations = new HashSet<Animation>();
		toRemove = new HashSet<Animation>();
	}

	/** Registers a new Animation. Will be updated automatically.
	 * 
	 * @param animation */
	public static void registerAnimation(Animation animation)
	{
		animations.add(animation);
	}

	/** Unregisters the Animation. Called when the Animation is disposed.
	 * 
	 * @see Animation#dispose()
	 * @param animation */
	public static void unregisterAnimation(Animation animation)
	{
		toRemove.add(animation);
	}

	public static void update()
	{
		for (Animation animation : animations)
			animation.update();
		
		for (Animation animation : toRemove)
			animations.remove(animation);
		
		toRemove.clear();
	}

}
