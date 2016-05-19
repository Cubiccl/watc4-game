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
	private JTextField textField;
	private MapEditor mapEd = ((MapEditor) MapEditor.getFrames()[0]);
	private ArrayList<DoorButton> doorsList = mapEd.getDoorList();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			DoorValues dialog = new DoorValues(0);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DoorValues(int i)
	{
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		mapList = FileUtils.getMapList();
		
		JComboBox<String> map1comboBox = new JComboBox<String>(mapList);
		map1comboBox.setBounds(30, 35, 120, 20);
		contentPanel.add(map1comboBox);
		
		JComboBox<String> map2comboBox = new JComboBox<String>(mapList);
		map2comboBox.removeItem(map1comboBox.getSelectedItem());
		map1comboBox.removeItem(map2comboBox.getSelectedItem());
		map2comboBox.setBounds(30, 170, 120, 20);
		contentPanel.add(map2comboBox);
		
		JLabel versLabel = new JLabel("vers");
		versLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		versLabel.setBounds(65, 95, 70, 20);
		contentPanel.add(versLabel);
		
		fieldLumiX = new JTextField();
		fieldLumiX.setBounds(230, 112, 25, 20);
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
		fieldPattouX.setEditable(false);
		contentPanel.add(fieldPattouX);
		
		JLabel labelPattouX = new JLabel("X :");
		labelPattouX.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labelPattouX.setBounds(209, 191, 17, 14);
		contentPanel.add(labelPattouX);
		
		fieldLumiY = new JTextField();
		fieldLumiY.setColumns(10);
		fieldLumiY.setBounds(288, 112, 25, 20);
		fieldLumiY.setEditable(false);
		contentPanel.add(fieldLumiY);
		
		JLabel labelLumiY = new JLabel("Y :");
		labelLumiY.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labelLumiY.setBounds(267, 113, 17, 14);
		contentPanel.add(labelLumiY);
		
		fieldPattouY = new JTextField();
		fieldPattouY.setColumns(10);
		fieldPattouY.setBounds(288, 190, 25, 20);
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
		labelPattou.setIcon(new ImageIcon(Sprite.PATTOU_IDLE_RIGHT1.getImage()));
		contentPanel.add(labelPattou);
		
		JButton buttonPlacerLumi = new JButton("Placer");
		buttonPlacerLumi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		buttonPlacerLumi.setBounds(335, 109, 87, 26);
		contentPanel.add(buttonPlacerLumi);
		
		JButton buttonPlacerPattou = new JButton("Placer");
		buttonPlacerPattou.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		buttonPlacerPattou.setBounds(335, 187, 87, 26);
		contentPanel.add(buttonPlacerPattou);
		
		JLabel lblUuid = new JLabel("UUID (400-499) :");
		lblUuid.setBounds(230, 25, 100, 16);
		contentPanel.add(lblUuid);
		
		textField = new JTextField();
		textField.setBounds(335, 23, 40, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton btnSupprimer = new JButton("Supprimer");
				btnSupprimer.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						doorsList.remove(i);
						mapEd.updateDoorList();
						dispose();
					}
				});
				buttonPane.add(btnSupprimer);
			}
			{
				JButton cancelButton = new JButton("Annuler");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						DoorValues.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
