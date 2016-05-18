package net.watc4.editor.cutscene;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class EventLabelText extends EventLabel
{
	private JScrollPane js;
	private JTextArea textArea;

	public String getTextArea()
	{
		if (textArea.getText().equals("")) return textArea.getText();
		else return "null";
	}

	public void init(String text)
	{
		this.textArea = new JTextArea(text);
		this.textArea.setBounds(0, 0, 200, 90);
		js = new JScrollPane(textArea);
		js.setBounds(20, 45, 200, 40);
		this.add(js);
	}

	public EventLabelText()
	{
		super();
		init("");
	}

	public EventLabelText(String text)
	{
		super();
		init(text);
	}
}
