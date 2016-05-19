package net.watc4.editor.tiles;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.watc4.editor.ErrorDialog;
import net.watc4.editor.MapEditor;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreateMap extends JDialog
{
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField widthField, heightField;
	private JLabel lblVeuillezChoisirLa, lblLargeur, lblHauteur;
	private JPanel buttonPane;
	private JButton okButton, cancelButton;
	@SuppressWarnings("unused")
	private ErrorDialog ed;
	private MapEditor mapEd = (MapEditor) MapEditor.getFrames()[0];

	public void colorStyle()
	{
		MapEditor.setColor(MapEditor.black2, MapEditor.white, contentPanel, buttonPane, lblVeuillezChoisirLa, lblLargeur, lblHauteur);
		MapEditor.setColor(MapEditor.black3, MapEditor.white, okButton, cancelButton);
	}

	/** Create the dialog. */
	public CreateMap()
	{
		setBounds(100, 100, 320, 200);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		lblVeuillezChoisirLa = new JLabel("Veuillez choisir la taille de la carte :");
		lblVeuillezChoisirLa.setBounds(10, 11, 284, 14);
		contentPanel.add(lblVeuillezChoisirLa);

		lblLargeur = new JLabel("Largeur : ");
		lblLargeur.setBounds(10, 50, 60, 14);
		contentPanel.add(lblLargeur);

		widthField = new JTextField("20");
		widthField.setBounds(80, 47, 86, 20);
		contentPanel.add(widthField);
		widthField.setColumns(10);

		lblHauteur = new JLabel("Hauteur : ");
		lblHauteur.setBounds(10, 83, 60, 14);
		contentPanel.add(lblHauteur);

		heightField = new JTextField("15");
		heightField.setBounds(80, 80, 86, 20);
		contentPanel.add(heightField);
		heightField.setColumns(10);
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Cr\u00E9er");
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

						if (wf <= 0 || hf <= 0) ed = new ErrorDialog("Veuillez remplir les champs correctement.");
						else
						{
							mapEd.createTiles(wf, hf);
							mapEd.removeCharacters();
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Annuler");
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
			colorStyle();
		}
	}
}
