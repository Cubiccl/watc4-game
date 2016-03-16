package net.watc4.game.entity;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import net.watc4.game.Main;
import net.watc4.game.display.Animation;
import net.watc4.game.display.Sprite;
import net.watc4.game.states.GameState;

/** Entity controller by the user */
public class EntityPlayer extends Entity
{
	private Animation animation;

	/** Creates a new EntityPlayer
	 * 
	 * @param xPos - Its x position.
	 * @param yPos - Its y position.
	 * @param game - A reference to the GameState. */
	public EntityPlayer(int xPos, int yPos, GameState game)
	{
		super(xPos, yPos, game);
		this.animation = new Animation(40, Sprite.PATTOU_IDLE1, Sprite.PATTOU_IDLE2);
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
