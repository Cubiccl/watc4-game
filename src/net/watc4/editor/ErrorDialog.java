package net.watc4.editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class ErrorDialog extends JDialog
{

	private final JPanel contentPanel = new JPanel();
	private JPanel buttonPane;
	private JButton okButton;
	private JLabel[] message;

	public void colorStyle()
	{
		MapEditor.setColor(MapEditor.black2, MapEditor.white, contentPanel, buttonPane);
		MapEditor.setColor(MapEditor.black2, MapEditor.white, message);
		MapEditor.setColor(MapEditor.black3, MapEditor.white, okButton);
	}

	/** Create the dialog. */
	public ErrorDialog(String... text)
	{
		setModal(true);
		setBounds(500, 300, 400, 130);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		message = new JLabel[text.length];
		for(int i = 0; i < message.length; i++)
			{
			message[i] = new JLabel(text[i]);
			contentPanel.add(message[i]);
			}
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		colorStyle();
		try
		{
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setVisible(true);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
