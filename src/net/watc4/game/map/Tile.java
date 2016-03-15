package net.watc4.game.map;

import java.awt.Graphics;

import net.watc4.game.GameObject;
import net.watc4.game.display.Animation;

public class Tile implements GameObject
{
	int id;
	Animation sprite;

	Tile(int id, Animation sprite)
	{
		this.id = id;
		this.sprite = sprite;
	}

	public int getId()
	{
		return this.id;
	}

	@Override
	public void render(Graphics g)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void update()
	{
		// TODO Auto-generated method stub

	}

}
