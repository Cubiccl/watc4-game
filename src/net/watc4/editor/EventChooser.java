package net.watc4.editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class EventChooser extends JDialog
{

	private final JPanel contentPanel = new JPanel();
	private static JComboBox<String> eventComboBox;
	private MapEditor mapEd = (MapEditor) MapEditor.getFrames()[0];
	
	public static String[] getEventList()
	{
		File direc = new File("src/net/watc4/editor/");
		String[] temp = direc.list();
		ArrayList<String> res = new ArrayList<String>();
		for (int i = 0; i < temp.length; i++)
		{
			if(temp[i].startsWith("EventLabel") && temp[i].length() > "EventLabel.java".length())
				res.add(temp[i].replace("EventLabel", "").replace(".java", ""));
		}
		return res.toArray(new String[res.size()]);
	}
	
	public static EventLabel getChoice(){
		switch ((String)eventComboBox.getSelectedItem())
		{
			case "Cutscene" : return new EventLabelCutscene();
			case "Move" : return new EventLabelMove();
			case "Text" : return new EventLabelText();
			default : return null;
		}
	}

	/**
	 * Create the dialog.
	 */
	public EventChooser(int pos)
	{
		setBounds(300, 300, 250, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblVeuillezChoisirUn = new JLabel("Veuillez choisir un type d'\u00E9v\u00E8nement");
		lblVeuillezChoisirUn.setBounds(10, 30, 220, 20);
		contentPanel.add(lblVeuillezChoisirUn);
		{
			JLabel lblParmiLaListe = new JLabel(" parmi la liste ci-dessous :");
			lblParmiLaListe.setBounds(41, 57, 158, 14);
			contentPanel.add(lblParmiLaListe);
		}
		
		eventComboBox = new JComboBox<String>(getEventList());
		eventComboBox.setBounds(60, 82, 120, 20);
		contentPanel.add(eventComboBox);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						
						ArrayList<EventLabel> newList = new ArrayList<EventLabel>(), oldList = mapEd.getEventlist();
						for(int i = 0; i < oldList.size()+1; i++)
						{
							if(i < pos) newList.add(oldList.get(i));
							else if(i == pos) newList.add(getChoice());
							else newList.add(oldList.get(i-1));
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
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
