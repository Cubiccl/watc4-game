package net.watc4.editor.cutscene;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.watc4.editor.MapEditor;
import net.watc4.game.utils.FileUtils;

@SuppressWarnings("serial")
public class EventChooser extends JDialog
{

	private final JPanel contentPanel = new JPanel();
	private static JComboBox<String> eventComboBox;
	private MapEditor mapEd = (MapEditor) MapEditor.getFrames()[0];
	private JLabel labelText1, labelText2;
	private JPanel buttonPane;
	private JButton okButton, cancelButton;

	public void colorStyle()
	{
		MapEditor.setColor(MapEditor.black2, MapEditor.white, contentPanel, labelText1, labelText2, buttonPane);
		MapEditor.setColor(MapEditor.black3, MapEditor.white, eventComboBox, okButton, cancelButton);
	}

	public static EventLabel getChoice()
	{
		switch ((String) eventComboBox.getSelectedItem())
		{
			case "Cutscene":
				return new EventLabelCutscene();
			case "Move":
				return new EventLabelMove();
			case "Text":
				return new EventLabelText();
			default:
				return null;
		}
	}

	/** Create the dialog. */
	public EventChooser(int pos)
	{
		setModal(true);
		setBounds(300, 300, 250, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		labelText1 = new JLabel("Veuillez choisir un type d'\u00E9v\u00E8nement");
		labelText1.setBounds(10, 30, 220, 20);
		contentPanel.add(labelText1);
		{
			labelText2 = new JLabel(" parmi la liste ci-dessous :");
			labelText2.setBounds(41, 57, 158, 14);
			contentPanel.add(labelText2);
		}

		eventComboBox = new JComboBox<String>(FileUtils.getEventList());
		eventComboBox.setBounds(60, 82, 120, 20);
		contentPanel.add(eventComboBox);
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent arg0)
					{

						ArrayList<EventLabel> newList = new ArrayList<EventLabel>(), oldList = mapEd.getEventlist();
						for (int i = 0; i < oldList.size() + 1; i++)
						{
							if (i < pos) newList.add(oldList.get(i));
							else if (i == pos) newList.add(getChoice());
							else newList.add(oldList.get(i - 1));
						}
						mapEd.setEventList(newList);
						mapEd.updateEventList();
						mapEd.updateScrollCutscene();
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent arg0)
					{
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			colorStyle();
		}
	}
}
