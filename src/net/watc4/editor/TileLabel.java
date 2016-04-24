package net.watc4.editor;

import javax.swing.JLabel;

import net.watc4.game.entity.Entity;

public class TileLabel extends JLabel
{
	private static final long serialVersionUID = 1L;
	private int id = 2;
	private Entity en;

	public Entity getEn()
	{
		return en;
	}

	public void setEn(Entity en)
	{
		this.en = en;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
}
