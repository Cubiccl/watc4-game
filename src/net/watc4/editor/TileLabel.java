package net.watc4.editor;

import javax.swing.JLabel;

import net.watc4.game.entity.Entity;

public class TileLabel extends JLabel
{
	private static final long serialVersionUID = 1L;
	private int id = 2;
	private Entity en;
	private int enId;
	private Object[] entityValues;

	public void initValues()
	{
		entityValues = new Object[en.getClass().getConstructors()[1].getParameters().length];
		entityValues[0] = enId;
		entityValues[1] = (int) en.getX();
		entityValues[2] = (int) en.getY();

		for (int i = 3; i < entityValues.length; i++)
		{
			if (en.getClass().getConstructors()[1].getParameters()[i].getType().toString().equals("int")) entityValues[i] = 0;
			else if (en.getClass().getConstructors()[1].getParameters()[i].getType().toString().equals("float")) entityValues[i] = 0;
			else entityValues[i] = null;
		}
	}

	public Object[] getEntityValues()
	{
		return entityValues;
	}

	public void setEntityValues(Object[] values)
	{
		this.entityValues = values;
	}

	public int getEnId()
	{
		return enId;
	}

	public void setEnId(int enId)
	{
		this.enId = enId;
	}

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
