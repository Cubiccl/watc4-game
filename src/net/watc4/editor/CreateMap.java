package net.watc4.editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreateMap extends JDialog
{
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField widthField;
	private JTextField heightField;

	/** Create the dialog. */
	public CreateMap()
	{
		setBounds(100, 100, 320, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblVeuillezChoisirLa = new JLabel("Veuillez choisir la taille de la carte :");
		lblVeuillezChoisirLa.setBounds(10, 11, 284, 14);
		contentPanel.add(lblVeuillezChoisirLa);

		JLabel lblLargeur = new JLabel("Largeur : ");
		lblLargeur.setBounds(10, 50, 60, 14);
		contentPanel.add(lblLargeur);

		widthField = new JTextField("20");
		widthField.setBounds(80, 47, 86, 20);
		contentPanel.add(widthField);
		widthField.setColumns(10);

		JLabel lblHauteur = new JLabel("Hauteur : ");
		lblHauteur.setBounds(10, 83, 60, 14);
		contentPanel.add(lblHauteur);

		heightField = new JTextField("15");
		heightField.setBounds(80, 80, 86, 20);
		contentPanel.add(heightField);
		heightField.setColumns(10);

		JLabel lblErreur = new JLabel("Erreur !!");
		lblErreur.setBounds(210, 60, 46, 14);
		lblErreur.setVisible(false);
		contentPanel.add(lblErreur);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Cr\u00E9er");
				okButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{

						int wf, hf;
						try
						{
							wf = Integer.valueOf(widthField.getText());
						} catch (NumberFormatException ex)
						{
							wf = 0;
						}
						try
						{
							hf = Integer.valueOf(heightField.getText());
						} catch (NumberFormatException ex2)
						{
							hf = 0;
						}

						if (wf <= 0 || hf <= 0) lblErreur.setVisible(true);
						else
						{
							MapEditor.createTiles(wf, hf);
							MapEditor.removeCharacters();
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Annuler");
				cancelButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
