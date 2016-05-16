package net.watc4.editor;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import net.watc4.game.display.Sprite;

@SuppressWarnings("serial")
public abstract class EventLabel extends JLabel
{
	protected static JLabel type;
	protected static JButton[] options;
	protected static TitledBorder tb;
	protected int position;

	public void initBase()
	{
		this.setSize(new Dimension(300, 100));
		this.setLayout(null);
		type = new JLabel(translate(this.getClass().getSimpleName().replace("EventLabel", "")));
		type.setBounds(10, 20, 60, 20);
		this.add(type);
		options = new JButton[]
		{ new JButton(new ImageIcon(Sprite.ARROW_UP.getImage())), new JButton(new ImageIcon(Sprite.ARROW_DOWN.getImage())),
				new JButton(new ImageIcon(Sprite.CROSS.getImage())) };
		options[0].setBounds(245, 20, 20, 25);
		options[1].setBounds(245, 55, 20, 25);
		options[2].setBounds(270, 37, 20, 25);
		for (int i = 0; i < options.length; i++)
			this.add(options[i]);
		tb = BorderFactory.createTitledBorder(String.valueOf(position));
		this.setBorder(tb);
	}
	
	public void updatePosition(int i){
		this.position = i;
		tb.setTitle(String.valueOf(i));
	}

	public static String translate(String s)
	{
		switch (s)
		{
			case "Text":
				return "Texte :";
			case "Move":
				return "D\u00E9placer :";
			case "Cutscene":
				return "Sc\u00E8ne :";
			default:
				return "Ev\u00E8nement inconnu :";
		}
	}

	public EventLabel()
	{
		initBase();
	}

}
