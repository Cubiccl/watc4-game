package net.watc4.editor.doors;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.watc4.editor.MapEditor;
import net.watc4.game.display.Sprite;
import net.watc4.game.utils.FileUtils;

@SuppressWarnings("serial")
public class DoorValues extends JDialog
{

	private final JPanel contentPanel = new JPanel();
	private JTextField fieldLumiX;
	private JTextField fieldPattouX;
	private JTextField fieldLumiY;
	private JTextField fieldPattouY;
	private String[] mapList;
	private JTextField fieldUUID;
	private MapEditor mapEd = ((MapEditor) MapEditor.getFrames()[0]);
	private ArrayList<DoorButton> doorsList = mapEd.getDoorList();
	private JComboBox<String> map1comboBox, map2comboBox;
	private DoorButton dbtn;

	public void setSpawnPoint(int x, int y, boolean character)
	{
		if (character)
		{
			fieldPattouX.setText(String.valueOf(x));
			fieldPattouY.setText(String.valueOf(y));
		} else
		{
			fieldLumiX.setText(String.valueOf(x));
			fieldLumiY.setText(String.valueOf(y));
		}
	}

	public boolean checkUUID(int fieldUUID)
	{
		int dbtnUUID = dbtn.getUUID();
		ArrayList<Integer> UUIDs = new ArrayList<Integer>();
		String[] lines = FileUtils.readFileAsStringArray("res/lore.txt");
		int i = 1;
		while (!lines[i].equals("endings ="))
		{
			UUIDs.add(Integer.parseInt(lines[i].split("\t")[1]));
			i++;
		}
		UUIDs.sort(null);
		for (int j = 0; j < UUIDs.size(); j++)
		{
			if (fieldUUID <= 400 || fieldUUID > 499)
			{
				JOptionPane.showMessageDialog(null, "Veuillez remplir le champ UUID correctement.", null, JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if (UUIDs.get(j) == fieldUUID && UUIDs.get(j) != dbtnUUID)
			{
				JOptionPane.showMessageDialog(null, "Cet UUID est d\u00E9j\u00E0 utilis\u00E9.", null, JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;
	}

	/** Create the dialog. */
	public DoorValues(DoorButton db)
	{
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		dbtn = db;
		mapList = FileUtils.getMapList();

		map1comboBox = new JComboBox<String>(mapList);
		map1comboBox.setBounds(30, 35, 120, 20);
		map1comboBox.setSelectedItem(dbtn.getMap1());
		contentPanel.add(map1comboBox);

		map2comboBox = new JComboBox<String>(mapList);
		map2comboBox.setSelectedItem(dbtn.getMap2());
		map2comboBox.setBounds(30, 170, 120, 20);
		contentPanel.add(map2comboBox);

		JLabel versLabel = new JLabel("vers");
		versLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		versLabel.setBounds(65, 95, 70, 20);
		contentPanel.add(versLabel);

		fieldLumiX = new JTextField();
		fieldLumiX.setBounds(230, 112, 25, 20);
		fieldLumiX.setText(String.valueOf(dbtn.getLumiX()));
		fieldLumiX.setEditable(false);
		contentPanel.add(fieldLumiX);
		fieldLumiX.setColumns(10);

		JLabel labelLumiX = new JLabel("X :");
		labelLumiX.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labelLumiX.setBounds(209, 113, 17, 14);
		contentPanel.add(labelLumiX);

		fieldPattouX = new JTextField();
		fieldPattouX.setColumns(10);
		fieldPattouX.setBounds(230, 190, 25, 20);
		fieldPattouX.setText(String.valueOf(dbtn.getPattouX()));
		fieldPattouX.setEditable(false);
		contentPanel.add(fieldPattouX);

		JLabel labelPattouX = new JLabel("X :");
		labelPattouX.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labelPattouX.setBounds(209, 191, 17, 14);
		contentPanel.add(labelPattouX);

		fieldLumiY = new JTextField();
		fieldLumiY.setColumns(10);
		fieldLumiY.setBounds(288, 112, 25, 20);
		fieldLumiY.setText(String.valueOf(dbtn.getLumiY()));
		fieldLumiY.setEditable(false);
		contentPanel.add(fieldLumiY);

		JLabel labelLumiY = new JLabel("Y :");
		labelLumiY.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labelLumiY.setBounds(267, 113, 17, 14);
		contentPanel.add(labelLumiY);

		fieldPattouY = new JTextField();
		fieldPattouY.setColumns(10);
		fieldPattouY.setBounds(288, 190, 25, 20);
		fieldPattouY.setText(String.valueOf(dbtn.getPattouY()));
		fieldPattouY.setEditable(false);
		contentPanel.add(fieldPattouY);

		JLabel labelPattouY = new JLabel("Y :");
		labelPattouY.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labelPattouY.setBounds(267, 191, 17, 14);
		contentPanel.add(labelPattouY);

		JLabel labelLumi = new JLabel("");
		labelLumi.setBounds(255, 64, 32, 32);
		labelLumi.setLayout(null);
		labelLumi.setIcon(new ImageIcon(Sprite.LUMI.getImage()));
		JLabel labelLumiEyes = new JLabel("");
		labelLumiEyes.setBounds(8, 8, 16, 16);
		labelLumiEyes.setIcon(new ImageIcon(Sprite.LUMI_EYE.getImage()));
		labelLumi.add(labelLumiEyes);
		contentPanel.add(labelLumi);

		JLabel labelPattou = new JLabel("");
		labelPattou.setBounds(255, 147, 32, 32);
		labelPattou.setIcon(new ImageIcon(Sprite.PATTOU_IDLE_RIGHT[0].getImage()));
		contentPanel.add(labelPattou);

		JButton buttonPlacerLumi = new JButton("Placer");
		buttonPlacerLumi.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				try
				{
					DoorSpawnChooser frame = new DoorSpawnChooser((String) map2comboBox.getSelectedItem(), false, Integer.valueOf(fieldLumiX.getText()),
							Integer.valueOf(fieldLumiY.getText()), Integer.valueOf(fieldPattouX.getText()), Integer.valueOf(fieldPattouY.getText()));
					frame.setVisible(true);
				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		buttonPlacerLumi.setBounds(335, 109, 87, 26);
		contentPanel.add(buttonPlacerLumi);

		JButton buttonPlacerPattou = new JButton("Placer");
		buttonPlacerPattou.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				try
				{
					DoorSpawnChooser frame = new DoorSpawnChooser((String) map2comboBox.getSelectedItem(), true, Integer.valueOf(fieldLumiX.getText()), Integer
							.valueOf(fieldLumiY.getText()), Integer.valueOf(fieldPattouX.getText()), Integer.valueOf(fieldPattouY.getText()));
					frame.setVisible(true);
				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		buttonPlacerPattou.setBounds(335, 187, 87, 26);
		contentPanel.add(buttonPlacerPattou);

		JLabel labelUUID = new JLabel("UUID (401-499) :");
		labelUUID.setBounds(230, 25, 100, 16);
		contentPanel.add(labelUUID);

		fieldUUID = new JTextField();
		fieldUUID.setBounds(335, 23, 40, 20);
		fieldUUID.setText(String.valueOf(dbtn.getUUID()));
		contentPanel.add(fieldUUID);
		fieldUUID.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent e)
					{
						if (checkUUID(Integer.valueOf(fieldUUID.getText())))
						{
							doorsList.remove(dbtn);
							doorsList.add(new DoorButton(((String) map1comboBox.getSelectedItem()), Integer.valueOf(fieldUUID.getText()),
									((String) map2comboBox.getSelectedItem()), Integer.valueOf(fieldLumiX.getText()), Integer.valueOf(fieldLumiY.getText()),
									Integer.valueOf(fieldPattouX.getText()), Integer.valueOf(fieldPattouY.getText())));
							mapEd.updateDoorList();
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton btnSupprimer = new JButton("Supprimer");
				btnSupprimer.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent e)
					{
						doorsList.remove(dbtn);
						mapEd.updateDoorList();
						dispose();
					}
				});
				buttonPane.add(btnSupprimer);
				if (dbtn instanceof AddDoorButton)
				{
					buttonPane.remove(btnSupprimer);
				}
			}
			{
				JButton cancelButton = new JButton("Annuler");
				cancelButton.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent arg0)
					{
						DoorValues.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
