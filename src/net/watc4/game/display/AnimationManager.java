package net.watc4.game.display;

import java.util.ArrayList;

/** Manages all Animations. When an Animation is created it is registered. Unregister an Animation when you don't need it anymore. */
public class AnimationManager
{
	/** All Animations currently used. */
	private static ArrayList<Animation> animations;

	/** Creates the AnimationManager. */
	public static void create()
	{
		animations = new ArrayList<Animation>();
	}

	/** Registers a new Animation. Will be updated automatically.
	 * 
	 * @param animation */
	public static void registerAnimation(Animation animation)
	{
		if (!animations.contains(animation)) animations.add(animation);
	}

	/** Unregisters the Animation. Called when the Animation is disposed.
	 * 
	 * @see Animation#dispose()
	 * @param animation */
	public static void unregisterAnimation(Animation animation)
	{
		animations.remove(animation);
	}

	public static void update()
	{
		for (Animation animation : animations)
			animation.update();
	}

}
