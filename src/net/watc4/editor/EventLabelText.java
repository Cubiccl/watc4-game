package net.watc4.editor;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class EventLabelText extends EventLabel
{
	private static JTextArea text;

	public void init(){
		if(text == null) text = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(text);
		scrollPane.setBounds(20,45,200,40);
		text.setBounds(20,45,200,90);
		this.add(scrollPane);
	}
	
	public EventLabelText()
	{
		super();
		init();
	}

	public EventLabelText(String text)
	{
		super();
		this.text = new JTextArea(text);
		init();
	}
}
