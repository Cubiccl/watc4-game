package net.watc4.game.utils;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

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
	public static final float ACCELERATION = 2, DECELERATION = 1, MAX_SPEED = 5, REAL_MAX_SPEED = 40;

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
	public static final int PATTOU_UP = KeyEvent.VK_I, PATTOU_DOWN = KeyEvent.VK_K, PATTOU_LEFT = KeyEvent.VK_J, PATTOU_RIGHT = KeyEvent.VK_L,
			PATTOU_JUMP = KeyEvent.VK_SPACE;

	/** Directions:
	 * <ul>
	 * <li>UP = 0</li>
	 * <li>DOWN = 1</li>
	 * <li>LEFT = 2</li>
	 * <li>RIGHT = 3</li>
	 * </ul> */
	public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;

	/** Convert a double into an int */
	public static int toInt(double f)
	{
		return (f - (double) ((int) f) < 0.5) ? (int) f : (int) f + 1;
	}

	/** Draw an arrow, useful for debug */
	void drawArrow(Graphics2D g, int x1, int y1, int x2, int y2)
	{

		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx * dx + dy * dy);
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		at.concatenate(AffineTransform.getRotateInstance(angle));
		g.transform(at);

		// Draw horizontal arrow starting in (0, 0)
		g.drawLine(0, 0, len, 0);
		g.fillPolygon(new int[]
		{ len, len - 6, len - 6, len }, new int[]
		{ 0, -6, 6, 0 }, 4);
	}

}
