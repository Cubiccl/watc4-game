package net.watc4.editor.tiles;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.watc4.editor.ErrorDialog;
import net.watc4.editor.MapEditor;

public class CreateMap extends JDialog
{
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField widthField, heightField;
	private JLabel lblVeuillezChoisirLa, lblLargeur, lblHauteur;
	private JPanel buttonPane;
	private JButton okButton, cancelButton, parcourirButton;
	private JRadioButton choiceRadioButton;
	@SuppressWarnings("unused")
	private ErrorDialog ed;
	private MapEditor mapEd = (MapEditor) MapEditor.getFrames()[0];
	private JTextField filenameField;
	private JFileChooser fc;

	public void colorStyle()
	{
		MapEditor.setColor(MapEditor.black2, MapEditor.white, contentPanel, buttonPane, lblVeuillezChoisirLa, lblLargeur, lblHauteur, choiceRadioButton);
		MapEditor.setColor(MapEditor.black3, MapEditor.white, okButton, cancelButton, parcourirButton);
	}

	public boolean checkFields()
	{
		if (choiceRadioButton.isSelected())
		{
			return true;
		} else
		{
			int wf, hf;
			try
			{
				wf = Integer.valueOf(widthField.getText());
			} catch (NumberFormatException ex)
			{
				ed = new ErrorDialog("Veuillez remplir les champs correctement.");
				return false;
			}
			try
			{
				hf = Integer.valueOf(heightField.getText());
			} catch (NumberFormatException ex2)
			{
				ed = new ErrorDialog("Veuillez remplir les champs correctement.");
				return false;
			}
			if (wf <= 0 || hf <= 0)
			{
				ed = new ErrorDialog("Veuillez remplir les champs correctement.");
				return false;
			} else return true;
		}

	}

	/** Create the dialog. */
	public CreateMap()
	{
		setBounds(100, 100, 320, 300);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		fc = new JFileChooser();
		FileFilter imageOnly = new FileNameExtensionFilter("Fichier image", "png", "jpg", "jpeg");
		fc.addChoosableFileFilter(imageOnly);
		fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
		fc.setCurrentDirectory(new File("res/textures/maps/"));

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

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 108, 284, 5);
		contentPanel.add(separator);

		choiceRadioButton = new JRadioButton("A partir d'une image :");
		choiceRadioButton.setBounds(20, 120, 191, 23);
		contentPanel.add(choiceRadioButton);

		filenameField = new JTextField();
		filenameField.setBounds(10, 150, 284, 20);
		contentPanel.add(filenameField);
		filenameField.setColumns(10);

		parcourirButton = new JButton("Parcourir");
		parcourirButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				int returnVal = fc.showOpenDialog(CreateMap.this);
				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					filenameField.setText(fc.getSelectedFile().getPath());
					filenameField.updateUI();
					choiceRadioButton.setSelected(true);
				}
			}
		});
		parcourirButton.setBounds(196, 181, 98, 26);
		contentPanel.add(parcourirButton);
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

						if (checkFields())
						{
							try
							{
								if (choiceRadioButton.isSelected())
								{
									BufferedImage level = ImageIO.read(new File(filenameField.getText()));
									mapEd.createTiles(level.getWidth() / 32, level.getHeight() / 32, filenameField.getText());
								} else mapEd.createTiles(Integer.valueOf(widthField.getText()), Integer.valueOf(heightField.getText()), null);
							} catch (IOException e1)
							{
								e1.printStackTrace();
							}
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
