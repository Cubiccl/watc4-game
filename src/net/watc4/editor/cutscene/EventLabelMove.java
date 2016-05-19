package net.watc4.editor.cutscene;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class EventLabelMove extends EventLabel
{
	private JLabel[] info = new JLabel[3];
	private String[] infoStr = new String[]
	{ "UUID :", "X :", "Y :" };
	private JTextField[] fields = new JTextField[3];
	private JRadioButton relative;
	
	public boolean getRelativeValue(){
		return relative.isSelected();
	}

	public int getValueFromWhichField(int index)
	{
		if (index < 0 || index > 2) return -1;
		else if (fields[index].getText().equals("") || Integer.valueOf(fields[index].getText()) < 0) return 0;
		else return Integer.valueOf(fields[index].getText());
	}

	public void init()
	{
		for (int i = 0; i < info.length; i++)
		{
			info[i] = new JLabel(infoStr[i]);

			if (i == 2) info[i].setBounds(80 * i, 50, 40, 20);
			else info[i].setBounds(20 + 80 * i, 50, 40, 20);

			if (fields[i] == null) fields[i] = new JTextField();
			fields[i].setBounds(60 + 60 * i, 52, 30, 20);
			this.add(fields[i]);

			info[i].setForeground(Color.white);
			this.add(info[i]);
		}
		if (this.relative == null) relative = new JRadioButton("Relatif", false);
		this.relative.setBounds(100, 20, 80, 20);
		this.relative.setBackground(new Color(50,50,50));
		this.relative.setForeground(Color.white);
		this.add(this.relative);
	}

	public EventLabelMove()
	{
		super();
		init();
	}

	public EventLabelMove(int UUID, boolean relative, int x, int y)
	{
		super();
		fields[0] = new JTextField(String.valueOf(UUID));
		fields[1] = new JTextField(String.valueOf(x));
		fields[2] = new JTextField(String.valueOf(y));
		this.relative = new JRadioButton("Relatif", relative);
		init();
	}

}
