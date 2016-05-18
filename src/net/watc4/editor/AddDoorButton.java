package net.watc4.editor;

import java.awt.Dimension;

import javax.swing.JLabel;

public class AddDoorButton extends DoorButton
{
	private JLabel add = new JLabel("Ajouter une porte...");

	public AddDoorButton(){
		setLayout(null);
		setPreferredSize(new Dimension(600,100));
		add.setFont(add.getFont().deriveFont(20f));
		add.setBounds(200, 34, 400, 32);
		add(add);
	}

}
