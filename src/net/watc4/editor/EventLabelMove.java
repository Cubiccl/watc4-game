package net.watc4.editor;

import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class EventLabelMove extends EventLabel
{
	private JLabel[] info = new JLabel[3];
	private String[] infoStr = new String[]
	{ "UUID :", "X :", "Y :" };
	private JTextField[] fields = new JTextField[3];

	public void init()
	{
		for (int i = 0; i < info.length; i++)
		{
			info[i] = new JLabel(infoStr[i]);
			
			if(i==2) info[i].setBounds(80 * i, 50, 40, 20);
			else info[i].setBounds(20 + 80 * i, 50, 40, 20);
			
			if (fields[i] == null) fields[i] = new JTextField();
			fields[i].setBounds(60 + 60 * i, 52, 30, 20);
			
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
		fields[0] = new JTextField(String.valueOf(UUID));
		fields[1] = new JTextField(String.valueOf(x));
		fields[2] = new JTextField(String.valueOf(y));
		init();
	}

}
