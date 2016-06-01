package net.watc4.editor.tiles;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import net.watc4.game.entity.Entity;
import net.watc4.game.map.Map;
import net.watc4.game.map.TileRegistry;

public class TileLabel extends JLabel
{
	private static final long serialVersionUID = 1L;
	private byte data = 0;
	private int xmap, ymap, id = 0;
	private Entity en;
	private int enId;
	private Object[] entityValues;
	private boolean tested = false;

	public TileLabel(int i, int j)
	{
		setLayout(null);
		setPreferredSize(new Dimension(32, 32));
		setXY(i * Map.TILESIZE, j * Map.TILESIZE);
	}

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
			else entityValues[i] = "null";
		}
	}

	public void updateUI()
	{
		super.updateUI();
		if (TileRegistry.getTileFromId(id).sprite != null) this.setIcon(new ImageIcon(TileRegistry.getTileFromId(id).sprite.getImage()));
	}

	public byte getData()
	{
		return data;
	}

	public void setData(byte data)
	{
		this.data = data;
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

	public int getX()
	{
		return xmap;
	}

	public void setX(int x)
	{
		this.xmap = x;
	}

	public int getY()
	{
		return ymap;
	}

	public void setY(int y)
	{
		this.ymap = y;
	}

	public void setXY(int x, int y)
	{
		this.xmap = x;
		this.ymap = y;
	}

	public boolean isTested()
	{
		return tested;
	}

	public void setTested(boolean tested)
	{
		this.tested = tested;
	}

}
