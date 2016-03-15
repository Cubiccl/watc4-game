package net.watc4.game.entity;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import net.watc4.game.Main;
import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.states.GameState;

public class EntityPlayer extends Entity
{
	private Animation animation;

	public EntityPlayer(int xPos, int yPos, GameState game)
	{
		super(xPos, yPos, game);
		this.animation = new Animation(10, Sprite.PATTOU_MOVING1, Sprite.PATTOU_MOVING2, Sprite.PATTOU_MOVING3, Sprite.PATTOU_MOVING4, Sprite.PATTOU_MOVING5,
				Sprite.PATTOU_MOVING6);
	}

	@Override
	public void render(Graphics g)
	{
		super.render(g);
		g.drawImage(this.animation.getImage(), this.getX(), this.getY(), null);
	}

	@Override
	public void update()
	{
		super.update();

		if (Main.isKeyPressed(KeyEvent.VK_UP)) this.yPos--;
		if (Main.isKeyPressed(KeyEvent.VK_DOWN)) this.yPos++;
		if (Main.isKeyPressed(KeyEvent.VK_RIGHT)) this.xPos++;
		if (Main.isKeyPressed(KeyEvent.VK_LEFT)) this.xPos--;
	}

}
