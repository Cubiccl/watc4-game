package net.watc4.editor.doors;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class AddDoorButton extends DoorButton
{
	private JLabel add = new JLabel("Ajouter une porte...");

	public AddDoorButton()
	{
		map1 = "";
		UUID = 400;
		map2 = "";
		LumiX = 0;
		LumiY = 0;
		PattouX = 0;
		PattouY = 0;
		setLayout(null);
		setPreferredSize(new Dimension(600, 100));
		add.setFont(add.getFont().deriveFont(20f));
		add.setBounds(200, 34, 400, 32);
		add(add);
		setBackground(new Color(50,50,50));
		add.setForeground(new Color(255,255,255));
	}

}
