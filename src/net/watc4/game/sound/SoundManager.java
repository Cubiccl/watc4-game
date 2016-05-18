package net.watc4.game.sound;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class SoundManager extends JFrame
{

	/*
	public static void main(String[] args)
	{
		SoundBank.creat_All_Sound();
		SoundManager sound = new SoundManager();
		sound.play("bigmap2");
	}
*/
	public SoundManager()
	{}

	public AudioInputStream setup_Sound(String id)
	{
		Sound sound = SoundBank.getSound_from_ID(id);
		try
		{
			return AudioSystem.getAudioInputStream(new File(sound.getName() + sound.getFormat()));
		} catch (UnsupportedAudioFileException | IOException e)
		{
			e.printStackTrace();
		}
		return null;

	}

	public void play(String id){
		if (SoundBank.getSound_from_ID(id).isLoopable()) play_loop(id);
		else play_once(id);
	}
	
	/** play a sound once */
	public void play_once(String id)
	{
		try
		{
			Clip clip = AudioSystem.getClip();
			try
			{
				clip.open(setup_Sound(id));
				clip.start();
			} catch (LineUnavailableException  | IOException e)
			{
				e.printStackTrace();
			}
		} catch (LineUnavailableException e1)
		{
			e1.printStackTrace();
		}

	}

	/** play a sound in loop. */
	public void play_loop(String id)
	{
		try
		{
			Clip clip = AudioSystem.getClip();	
			clip.open(setup_Sound(id));
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.start();
			
		} catch (LineUnavailableException | IOException  e)
		{
			e.printStackTrace();
		}
	}

	/** stop a sound */
	public void stop_clip(Clip clip)
	{
		clip.stop();
		clip = null;
	}

	/** set the volume to the minimum 0% */
	public void mute(Clip clip)
	{
		FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		volume.setValue(volume.getMinimum());
	}

	/** @return the value of the volume */
	public float get_Volume(Clip clip)
	{
		FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		return volume.getValue();
	}

	/** set the volume to a given value
	 * 
	 * @param new_Volume a value between 0 and 100 */
	public void set_Volume(float vol,Clip clip)
	{
		FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		if (vol > 98.0) volume.setValue(volume.getMaximum());
		else
		{
			float max = (float) Math.pow(10.0, volume.getMaximum() / 20.0), min = (float) Math.pow(10.0, volume.getMinimum() / 20.0);
			vol = (float) (20 * Math.log10((vol / 100) * (max + min)) / Math.log10(10));
			volume.setValue(vol);
		}
	}

	/** boost the volume by 3db of the maximum volume */
	public void Volume_up(Clip clip)
	{
		FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float max = (float) Math.pow(10.0, volume.getMaximum() / 20.0), min = (float) Math.pow(10.0, volume.getMinimum() / 20.0);
		float vol = (float) Math.pow(10.0, volume.getValue() / 20.0);
		if (vol > (19 * (max - min) / 20)) volume.setValue(volume.getMaximum());

		else
		{
			vol = (float) (20 * Math.log10(vol + ((max - min) / 20)) / Math.log10(10));
			volume.setValue(vol);
		}
	}

	/** boost the volume by 3db of the maximum volume */
	public void Volume_down(Clip clip)
	{
		FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float max = (float) Math.pow(10.0, volume.getMaximum() / 20.0), min = (float) Math.pow(10.0, volume.getMinimum() / 20.0);
		float vol = (float) Math.pow(10.0, volume.getValue() / 20.0);
		if (vol < ((max - min) / 20)) volume.setValue(volume.getMinimum());
		else
		{
			vol = (float) (20 * Math.log10(vol - ((max - min) / 20)) / Math.log10(10));
			volume.setValue(vol);
		}
	}
}
