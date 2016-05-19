package net.watc4.editor.doors;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.watc4.editor.ErrorDialog;
import net.watc4.editor.MapEditor;
import net.watc4.game.display.Sprite;
import net.watc4.game.utils.FileUtils;

@SuppressWarnings("serial")
public class DoorValues extends JDialog
{

	private final JPanel contentPanel = new JPanel(), buttonPane;
	private JTextField fieldLumiX;
	private JTextField fieldPattouX;
	private JTextField fieldLumiY;
	private JTextField fieldPattouY;
	private JLabel versLabel, labelLumiX, labelLumiY, labelPattouX, labelPattouY, labelLumi, labelPattou, labelUUID;
	private JButton buttonPlacerLumi, buttonPlacerPattou, okButton, cancelButton, supprButton;
	private String[] mapList;
	private JTextField fieldUUID;
	private MapEditor mapEd = ((MapEditor) MapEditor.getFrames()[0]);
	private ArrayList<DoorButton> doorsList = mapEd.getDoorList();
	private JComboBox<String> map1comboBox, map2comboBox;
	private DoorButton dbtn;
	@SuppressWarnings("unused")
	private ErrorDialog ed;
	private String mapDestination;

	public void colorStyle()
	{
		MapEditor.setColor(MapEditor.black2, MapEditor.white, contentPanel, buttonPane, versLabel, labelLumiX, labelLumiY, labelPattouX, labelPattouY,
				labelUUID);
		MapEditor.setColor(MapEditor.black3, MapEditor.white, buttonPlacerLumi, buttonPlacerPattou, okButton, supprButton, cancelButton, map1comboBox,
				map2comboBox);
	}

	public void updateLore() throws IOException
	{
		String[] oldLore = FileUtils.readFileAsStringArray("res/lore.txt");
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("res/lore.txt")));
		pw.println("doors =");
		for (int i = 1; i < doorsList.size(); i++)
		{
			DoorButton db = doorsList.get(i);
			pw.println(db.map1 + "\t" + db.UUID + "\t" + db.map2 + "\t" + db.LumiX + "\t" + db.LumiY + "\t" + db.PattouX + "\t" + db.PattouY);
		}
		int j = 0;
		do
		{
			j++;
		} while (!oldLore[j].equals("endings ="));
		for (; j < oldLore.length; j++)
		{
			pw.println(oldLore[j]);
		}
		pw.close();
	}

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

	public int checkFields(int fieldUUID, String mapDestinationName)
	{
		if (!(mapDestinationName.equals(mapDestination))) { return 1; }
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
			if (fieldUUID <= 400 || fieldUUID > 499) { return 2; }
			if (UUIDs.get(j) == fieldUUID && UUIDs.get(j) != dbtnUUID) { return 3; }
		}
		return 0;
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

		mapDestination = (String) map2comboBox.getSelectedItem();

		versLabel = new JLabel("vers");
		versLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		versLabel.setBounds(65, 95, 70, 20);
		contentPanel.add(versLabel);

		fieldLumiX = new JTextField();
		fieldLumiX.setBounds(230, 112, 25, 20);
		fieldLumiX.setText(String.valueOf(dbtn.getLumiX()));
		fieldLumiX.setEditable(false);
		contentPanel.add(fieldLumiX);
		fieldLumiX.setColumns(10);

		labelLumiX = new JLabel("X :");
		labelLumiX.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labelLumiX.setBounds(209, 113, 17, 14);
		contentPanel.add(labelLumiX);

		fieldPattouX = new JTextField();
		fieldPattouX.setColumns(10);
		fieldPattouX.setBounds(230, 190, 25, 20);
		fieldPattouX.setText(String.valueOf(dbtn.getPattouX()));
		fieldPattouX.setEditable(false);
		contentPanel.add(fieldPattouX);

		labelPattouX = new JLabel("X :");
		labelPattouX.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labelPattouX.setBounds(209, 191, 17, 14);
		contentPanel.add(labelPattouX);

		fieldLumiY = new JTextField();
		fieldLumiY.setColumns(10);
		fieldLumiY.setBounds(288, 112, 25, 20);
		fieldLumiY.setText(String.valueOf(dbtn.getLumiY()));
		fieldLumiY.setEditable(false);
		contentPanel.add(fieldLumiY);

		labelLumiY = new JLabel("Y :");
		labelLumiY.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labelLumiY.setBounds(267, 113, 17, 14);
		contentPanel.add(labelLumiY);

		fieldPattouY = new JTextField();
		fieldPattouY.setColumns(10);
		fieldPattouY.setBounds(288, 190, 25, 20);
		fieldPattouY.setText(String.valueOf(dbtn.getPattouY()));
		fieldPattouY.setEditable(false);
		contentPanel.add(fieldPattouY);

		labelPattouY = new JLabel("Y :");
		labelPattouY.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labelPattouY.setBounds(267, 191, 17, 14);
		contentPanel.add(labelPattouY);

		labelLumi = new JLabel("");
		labelLumi.setBounds(255, 64, 32, 32);
		labelLumi.setLayout(null);
		labelLumi.setIcon(new ImageIcon(Sprite.LUMI.getImage()));
		JLabel labelLumiEyes = new JLabel("");
		labelLumiEyes.setBounds(8, 8, 16, 16);
		labelLumiEyes.setIcon(new ImageIcon(Sprite.LUMI_EYE.getImage()));
		labelLumi.add(labelLumiEyes);
		contentPanel.add(labelLumi);

		labelPattou = new JLabel("");
		labelPattou.setBounds(255, 147, 32, 32);
		labelPattou.setIcon(new ImageIcon(Sprite.PATTOU_IDLE_RIGHT[0].getImage()));
		contentPanel.add(labelPattou);

		buttonPlacerLumi = new JButton("Placer");
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
					mapDestination = (String) map2comboBox.getSelectedItem();
				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		buttonPlacerLumi.setBounds(335, 109, 87, 26);
		contentPanel.add(buttonPlacerLumi);

		buttonPlacerPattou = new JButton("Placer");
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
					mapDestination = (String) map2comboBox.getSelectedItem();
				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		buttonPlacerPattou.setBounds(335, 187, 87, 26);
		contentPanel.add(buttonPlacerPattou);

		labelUUID = new JLabel("UUID (401-499) :");
		labelUUID.setBounds(230, 25, 100, 16);
		contentPanel.add(labelUUID);

		fieldUUID = new JTextField();
		fieldUUID.setBounds(335, 23, 40, 20);
		fieldUUID.setText(String.valueOf(dbtn.getUUID()));
		contentPanel.add(fieldUUID);
		fieldUUID.setColumns(10);
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent e)
					{
						int check = checkFields(Integer.valueOf(fieldUUID.getText()), (String) map2comboBox.getSelectedItem());
						if (check == 0)
						{
							doorsList.remove(dbtn);
							doorsList.add(new DoorButton(((String) map1comboBox.getSelectedItem()), Integer.valueOf(fieldUUID.getText()),
									((String) map2comboBox.getSelectedItem()), Integer.valueOf(fieldLumiX.getText()), Integer.valueOf(fieldLumiY.getText()),
									Integer.valueOf(fieldPattouX.getText()), Integer.valueOf(fieldPattouY.getText())));
							mapEd.updateDoorList();
							try
							{
								updateLore();
							} catch (IOException e1)
							{
								e1.printStackTrace();
							}
							dispose();
						} else
						{
							switch (check)
							{
								case 1:
									ed = new ErrorDialog("La carte de destination a \u00E9t\u00E9 modifi\u00E9e", "depuis le placement des personnages.");
									break;
								case 2:
									ed = new ErrorDialog("Veuillez remplir le champ UUID correctement.");
									break;
								case 3:
									ed = new ErrorDialog("Cet UUID est d\u00E9j\u00E0 utilis\u00E9.");
									break;
								default:
									break;
							}
						}
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				supprButton = new JButton("Supprimer");
				supprButton.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent e)
					{
						doorsList.remove(dbtn);
						mapEd.updateDoorList();
						dispose();
					}
				});
				buttonPane.add(supprButton);
				if (dbtn instanceof AddDoorButton)
				{
					buttonPane.remove(supprButton);
				}
			}
			{
				cancelButton = new JButton("Annuler");
				cancelButton.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent arg0)
					{
						DoorValues.this.dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
			colorStyle();
		}
	}
}
