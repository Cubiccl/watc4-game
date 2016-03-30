package net.watc4.game;

import java.awt.event.KeyEvent;

/** Contains various constants and methods useful for the game. */
public final class GameUtils
{

	/** Entity physics constants.
	 * <ul>
	 * <li>ACCELERATION: The speed at which an Entity's speed increases.</li>
	 * <li>DECELERATION: The speed at which an Entity's speed decreases.</li>
	 * <li>GRAVITY: The speed at which an Entity's height is affected by gravity.</li>
	 * <li>MAX_SPEED: The maximum speed an Entity can reach by itself.</li>
	 * <li>MAX_SPEED: The real maximum speed of an Entity (can be reached by gravity or interactions with environment).</li>
	 * </ul> */
	public static final float ACCELERATION = 2, DECELERATION = 1, GRAVITY = 0.8f, MAX_SPEED = 5, REAL_MAX_SPEED = 10;

	/** The current Frames Per Second. */
	public static int currentFPS = 60;
	/** The current Updates Per Second. */
	public static int currentUPS = 20;

	/** Lumi's Controls:
	 * <ul>
	 * <li>UP : Z</li>
	 * <li>DOWN : S</li>
	 * <li>LEFT : Q</li>
	 * <li>RIGHT : D</li>
	 * </ul> */
	public static final int LUMI_UP = KeyEvent.VK_Z, LUMI_DOWN = KeyEvent.VK_S, LUMI_LEFT = KeyEvent.VK_Q, LUMI_RIGHT = KeyEvent.VK_D;

	/** The name of the Game. */
	public static final String NAME = "WATC4";

	/** Pattou's Controls:
	 * <ul>
	 * <li>UP : I</li>
	 * <li>DOWN : J</li>
	 * <li>LEFT : K</li>
	 * <li>RIGHT : L</li>
	 * <li>JUMP : Space bar</li>
	 * </ul> */
	public static final int PATTOU_LEFT = KeyEvent.VK_J, PATTOU_RIGHT = KeyEvent.VK_L, PATTOU_JUMP = KeyEvent.VK_SPACE;

}
