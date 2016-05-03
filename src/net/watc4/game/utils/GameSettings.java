package net.watc4.game.utils;

public final class GameSettings
{
	/** True if the debug mode is active. */
	public static boolean debugMode = false;

	/** True if hitboxes & chunks should be drawn. */
	public static boolean drawHitboxes = false;

	/** True if the god mode is active (Players will not take damage). */
	public static boolean godMode = false;

	/** True if the light mode is active (Players will see through shadows). */
	public static boolean lightMode = false;

	/** Available resolutions */
	public static final int FULLSCREEN = 0, R640 = 1, R960 = 2, R1280 = 3;

	/** The resolution mode. */
	public static int resolution = R960;

	public static void cycleResolution()
	{
		++resolution;
		if (resolution > 3) resolution = FULLSCREEN;
	}

	public static String getResolution()
	{
		switch (resolution)
		{
			case FULLSCREEN:
				return "Fullscreen";
				
			case R640:
				return "640 x 480";
				
			case R960:
				return "960 x 720";
				
			case R1280:
				return "1280 x 960";

			default:
				return "Unknown";
		}
	}

}
