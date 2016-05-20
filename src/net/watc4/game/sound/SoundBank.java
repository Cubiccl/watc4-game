package net.watc4.game.sound;

import java.util.HashMap;

public class SoundBank
{
	private static HashMap<String, Sound> sound_Bank;

	/** @return the sound_Bank */
	public static HashMap<String, Sound> getSound_Bank()
	{
		return sound_Bank;
	}

	/** @param id
	 * @return the sound at the given id */
	public static Sound getSound_from_ID(String id)
	{
		return sound_Bank.get(id);
	}

	
	/** Create the sound_Bank of all Sound */
	public static void creat_All_Sound()
	{
		sound_Bank = new HashMap<String, Sound>();
		add_Sound_to_Bank(new Sound("Level 1", "res/sound/Track1", ".wav", true));
	}

	/** add a sound to the SoundBank
	 * 
	 * @param sound */
	public static void add_Sound_to_Bank(Sound sound)
	{
		sound_Bank.put(sound.getId(), sound);
	}
}
