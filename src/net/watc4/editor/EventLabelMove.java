package net.watc4.editor;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class EventLabelMove extends EventLabel
{
	private static JLabel[] info = new JLabel[3];
	private static String[] infoStr = new String[]
	{ "UUID :", "X :", "Y :" };
	private static JTextField[] fields = new JTextField[3];

	public void init()
	{
		for (int i = 0; i < info.length; i++)
		{
			info[i] = new JLabel(infoStr[i]);
			info[i].setBounds(20 + 80 * i, 50, 40, 20);
			if (fields[i] == null) fields[i] = new JTextField();
			if (i == 0) fields[i].setBounds(60 + 80 * i, 50, 30, 20);
			else fields[i].setBounds(40 + 80 * i, 50, 30, 20);
			this.add(info[i]);
			this.add(fields[i]);
		}
	}

	public EventLabelMove()
	{
		super();
		init();
	}

	public EventLabelMove(int UUID, int x, int y)
	{
		super();
		fields[0] = new JTextField(UUID);
		fields[1] = new JTextField(x);
		fields[2] = new JTextField(y);
		init();
	}

}
