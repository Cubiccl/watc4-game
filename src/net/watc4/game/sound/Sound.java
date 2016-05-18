package net.watc4.game.sound;

public class Sound
{
	private String id;
	private String name;
	private String format;
	private boolean loopable;
	
	Sound(String id, String name, String format, boolean loopable)
	{
		this.setId(id);
		this.setName(name);
		this.setFormat(format);
		this.setLoopable(loopable);
	}

	/** @return the name of the sound */
	public String getName()
	{
		return name;
	}

	/** @param set the name of the sound */
	public void setName(String name)
	{
		this.name = name;
	}

	/** @return the format of the sound */
	public String getFormat()
	{
		return format;
	}

	/** @param set the format of the sound */
	public void setFormat(String format)
	{
		this.format = format;
	}

	/** @return true if the sound must be played in loop */
	public boolean isLoopable()
	{
		return loopable;
	}

	/** @param set if the sound must be played in loop */
	public void setLoopable(boolean loopable)
	{
		this.loopable = loopable;
	}

	/** @return the id of the sound */
	public String getId()
	{
		return id;
	}

	/** @param set the if of a sound */
	public void setId(String id)
	{
		this.id = id;
	}

	

}
