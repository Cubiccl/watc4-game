package net.watc4.editor.cutscene;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import net.watc4.editor.MapEditor;
import net.watc4.game.display.Sprite;

@SuppressWarnings("serial")
public abstract class EventLabel extends JLabel
{
	protected JLabel type;
	protected JButton[] options;
	protected TitledBorder tb;
	protected int position;
	private MapEditor mapEd = (MapEditor) MapEditor.getFrames()[0];

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
		
		MapEditor.setColor( MapEditor.black3, null, this);
		MapEditor.setColor(MapEditor.black4, null, options);
		MapEditor.setColor(null,MapEditor.white,type);
		tb.setTitleColor(MapEditor.white);
		
		options[0].addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				ArrayList<EventLabel> list = mapEd.getEventlist();
				EventLabel temp = list.get(position - 2);
				list.set(position - 2, EventLabel.this);
				list.set(position - 1, temp);
				mapEd.updateEventList();
				temp.updateButtons();
				EventLabel.this.updateButtons();
			}
		});
		options[1].addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				ArrayList<EventLabel> list = mapEd.getEventlist();
				EventLabel temp = list.get(position);
				list.set(position, EventLabel.this);
				list.set(position - 1, temp);
				mapEd.updateEventList();
				temp.updateButtons();
				EventLabel.this.updateButtons();
			}
		});
		options[2].addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				mapEd.getEventlist().remove(EventLabel.this);
				mapEd.updateEventList();
				for (int i = 0; i < mapEd.getEventlist().size(); i++)
				{
					mapEd.getEventlist().get(i).updateButtons();
				}
			}
		});
	}

	public void updatePosition(int i)
	{
		this.position = i;
		tb.setTitle(String.valueOf(i));
	}

	public void updateButtons()
	{
		if (position == 1 && position == mapEd.getEventlist().size())
		{
			options[0].setVisible(false);
			options[1].setVisible(false);
		}
		else if (position == 1)
		{
			options[0].setVisible(false);
			options[1].setVisible(true);
			options[1].setBounds(245, 37, 20, 25);
		}
		else if (position == mapEd.getEventlist().size())
		{
			options[1].setVisible(false);
			options[0].setVisible(true);
			options[0].setBounds(245, 37, 20, 25);
		}
		else
		{
			options[0].setVisible(true);
			options[1].setVisible(true);
			options[0].setBounds(245, 20, 20, 25);
			options[1].setBounds(245, 55, 20, 25);
		}
		updateUI();
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