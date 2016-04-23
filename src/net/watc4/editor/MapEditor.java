package net.watc4.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.watc4.game.display.AnimationManager;
import net.watc4.game.display.Sprite;
import net.watc4.game.map.TileRegistry;

public class MapEditor extends JFrame
{
	private static final int MODE_TILES = 0;
	private static final int MODE_ENTITY = 1;
	private static final int MODE_CHARACTERS = 2;
	private static int mode;
	private static JPanel contentPane;
	private static GridBagConstraints gbc = new GridBagConstraints();
	private static JPanel mapView = new JPanel();
	private static JScrollPane scrollMap = new JScrollPane();
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenuItem createMapMenu = new JMenuItem("Nouveau");
	private static TileLabel[][] tilemap;
	private static TileLabel[] tileChoice;
	private static int selectedTile;
	private final JPanel tilesMenu = new JPanel();
	private final JLabel lblTiles = new JLabel("Tuiles :");
	private final static JPanel tileRegistry = new JPanel();
	private final JLabel lblTileSelected = new JLabel("Tuile s\u00E9lectionn\u00E9e :");
	private final static JLabel selectedTileLabel = new JLabel();
	private static JTextField fieldPattouX, fieldPattouY, fieldLumiX, fieldLumiY;
	private static JRadioButton radioPattou, radioLumi;
	private static JLabel focusPattou, focusLumi, lumiEyes, lblSelected;
	private static int lblSelectedIndex = -1;
	private static JButton btnRemovePattou, btnRemoveLumi;
	{
		lblSelected = new JLabel();
		lblSelected.setBounds(11, 11, 10, 10);
		lblSelected.setBackground(new Color(0, 0, 255));
		lblSelected.setOpaque(true);
		focusPattou = new JLabel();
		focusPattou.setBounds(0, 0, 32, 32);
		focusPattou.setIcon(new ImageIcon(Sprite.PATTOU_IDLE_RIGHT1.getImage()));
		focusLumi = new JLabel();
		focusLumi.setBounds(0, 0, 32, 32);
		focusLumi.setIcon(new ImageIcon(Sprite.LUMI.getImage()));
		lumiEyes = new JLabel(new ImageIcon(Sprite.LUMI_EYE.getImage()));
		lumiEyes.setBounds(0, 0, 32, 32);
	}

	/** Launch the application. */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					MapEditor frame = new MapEditor();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public static void removeCharacters()
	{
		fieldPattouX.setText("");
		fieldPattouY.setText("");
		fieldLumiX.setText("");
		fieldLumiY.setText("");
	}

	public static void createTiles(int width, int height)
	{
		tilemap = new TileLabel[width][height];
		mapView.removeAll();
		for (int i = 0; i < tilemap.length; i++)
		{
			for (int j = 0; j < tilemap[0].length; j++)
			{
				tilemap[i][j] = new TileLabel();
				tilemap[i][j].setLayout(null);
				tilemap[i][j].setPreferredSize(new Dimension(32, 32));
				tilemap[i][j].setIcon(new ImageIcon(Sprite.TILE_WALL.getImage()));
				tilemap[i][j].setVisible(true);
				gbc.gridx = i;
				gbc.gridy = j;
				mapView.add(tilemap[i][j], gbc);
				addTileUpdater(i, j);
			}
		}

		mapView.updateUI();
	}

	public static void getTilesFromRegistry()
	{

		tileChoice = new TileLabel[TileRegistry.getTiles().size()];
		for (int i = 0; i < tileChoice.length; i++)
		{
			tileChoice[i] = new TileLabel();
			tileChoice[i].setLayout(null);
			tileChoice[i].setPreferredSize(new Dimension(32, 32));
			tileChoice[i].setIcon(new ImageIcon(TileRegistry.getTiles().get(i).sprite.getImage()));
			tileChoice[i].setId(i);
			tileChoice[i].setVisible(true);
			gbc.gridx = i;
			tileRegistry.add(tileChoice[i], gbc);
			addTileSelector(i);
		}
	}

	public static void addTileUpdater(int i, int j)
	{
		tilemap[i][j].addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent arg0)
			{
				if (mode == MapEditor.MODE_TILES)
				{
					tilemap[i][j].setIcon(new ImageIcon(TileRegistry.getTileFromId(selectedTile).sprite.getImage()));
					tilemap[i][j].setId(selectedTile);
				} else if (mode == MapEditor.MODE_ENTITY)
				{

				} else if (mode == MapEditor.MODE_CHARACTERS)
				{
					if (radioPattou.isSelected() & !radioLumi.isSelected())
					{
						if (!fieldPattouX.getText().equals("") || !fieldPattouY.getText().equals(""))
						{
							int oldX = Integer.valueOf(fieldPattouX.getText());
							int oldY = Integer.valueOf(fieldPattouY.getText());
							tilemap[oldX][oldY].remove(focusPattou);
							tilemap[oldX][oldY].updateUI();
						}
						fieldPattouX.setText(String.valueOf(i));
						fieldPattouY.setText(String.valueOf(j));
						tilemap[i][j].add(focusPattou);
						tilemap[i][j].updateUI();

					} else if (radioLumi.isSelected() & !radioPattou.isSelected())
					{
						if (!fieldLumiX.getText().equals("") || !fieldLumiY.getText().equals(""))
						{
							int oldX = Integer.valueOf(fieldLumiX.getText());
							int oldY = Integer.valueOf(fieldLumiY.getText());
							tilemap[oldX][oldY].remove(focusLumi);
							tilemap[oldX][oldY].updateUI();
						}
						fieldLumiX.setText(String.valueOf(i));
						fieldLumiY.setText(String.valueOf(j));
						focusLumi.add(lumiEyes);
						tilemap[i][j].add(focusLumi);
						tilemap[i][j].updateUI();
					}
				}
			}
		});
	}

	public static void addTileSelector(int i)
	{
		tileChoice[i].addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent arg0)
			{

				selectedTile = i;
				selectedTileLabel.setIcon(new ImageIcon(TileRegistry.getTileFromId(selectedTile).sprite.getImage()));
				if (lblSelectedIndex >= 0)
				{
					tileChoice[lblSelectedIndex].removeAll();
					tileChoice[lblSelectedIndex].updateUI();
				}
				lblSelectedIndex = i;
				tileChoice[i].add(lblSelected);
				tileChoice[i].updateUI();
			}
		});
	}

	/** Create the frame. */
	public MapEditor()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 120, 650, 600);
		menuBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		AnimationManager.create();
		TileRegistry.createTiles();
		getTilesFromRegistry();

		setJMenuBar(menuBar);
		createMapMenu.setToolTipText("Choisissez les dimensions de votre nouvelle carte");
		createMapMenu.setMaximumSize(new Dimension(80, 100));
		createMapMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		createMapMenu.setHorizontalTextPosition(SwingConstants.LEFT);
		createMapMenu.setHorizontalAlignment(SwingConstants.LEFT);
		createMapMenu.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				try
				{
					CreateMap dialog = new CreateMap();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

		menuBar.add(createMapMenu);

		JMenuItem mntmModifier = new JMenuItem("Modifier");
		mntmModifier.setToolTipText("Modifiez les dimensions de votre carte");
		mntmModifier.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mntmModifier.setMaximumSize(new Dimension(80, 32767));
		menuBar.add(mntmModifier);

		JMenuItem mntmOuvrirUneCarte = new JMenuItem("Ouvrir");
		mntmOuvrirUneCarte.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mntmOuvrirUneCarte.setToolTipText("Ouvrez une carte déjà existante");
		mntmOuvrirUneCarte.setMaximumSize(new Dimension(80, 32767));
		menuBar.add(mntmOuvrirUneCarte);

		JMenuItem mntmEnregistrer = new JMenuItem("Enregistrer");
		mntmEnregistrer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mntmEnregistrer.setMaximumSize(new Dimension(80, 32767));
		mntmEnregistrer.setToolTipText("Sauvegardez votre carte au format texte");
		menuBar.add(mntmEnregistrer);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JTabbedPane menu = new JTabbedPane();
		contentPane.add(menu, BorderLayout.NORTH);
		selectedTileLabel.setIcon(new ImageIcon(TileRegistry.getTileFromId(selectedTile).sprite.getImage()));
		tilesMenu.setPreferredSize(new Dimension(620, 90));
		menu.add(tilesMenu, "Tuiles");
		tilesMenu.setMinimumSize(new Dimension(100, 10));
		tilesMenu.setLayout(null);
		lblTiles.setBounds(18, 23, 46, 14);

		tilesMenu.add(lblTiles);

		JScrollPane scrollTileRegistry = new JScrollPane();
		scrollTileRegistry.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollTileRegistry.setPreferredSize(new Dimension(150, 34));
		scrollTileRegistry.setBounds(74, 15, 300, 50);
		scrollTileRegistry.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollTileRegistry.getHorizontalScrollBar().setUnitIncrement(6);
		tilesMenu.add(scrollTileRegistry);

		GridBagLayout gbl_panel2 = new GridBagLayout();
		scrollTileRegistry.setViewportView(tileRegistry);
		tileRegistry.setPreferredSize(new Dimension(32 * tileChoice.length, 32));
		tileRegistry.setLayout(gbl_panel2);
		lblTileSelected.setBounds(430, 23, 116, 14);

		tilesMenu.add(lblTileSelected);
		selectedTileLabel.setBounds(555, 15, 32, 32);
		tilesMenu.add(selectedTileLabel);

		JPanel entityMenu = new JPanel();
		menu.add(entityMenu, "Entités");
		entityMenu.setLayout(null);
		entityMenu.setPreferredSize(new Dimension(620, 90));
		entityMenu.setMinimumSize(new Dimension(100, 10));

		JLabel lblEntity = new JLabel("Entités :");
		lblEntity.setBounds(18, 23, 46, 14);
		entityMenu.add(lblEntity);

		JScrollPane scrollEntityRegistry = new JScrollPane();
		scrollEntityRegistry.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollEntityRegistry.setPreferredSize(new Dimension(150, 34));
		scrollEntityRegistry.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollEntityRegistry.setBounds(74, 15, 300, 50);
		entityMenu.add(scrollEntityRegistry);

		JLabel lblSelectedEntity = new JLabel("Entité sélectionnée :");
		lblSelectedEntity.setBounds(430, 23, 116, 14);
		entityMenu.add(lblSelectedEntity);

		JLabel label_2 = new JLabel();
		label_2.setBounds(555, 15, 32, 32);
		entityMenu.add(label_2);

		JPanel characterMenu = new JPanel();
		menu.add(characterMenu, "Personnages");
		characterMenu.setLayout(null);
		characterMenu.setPreferredSize(new Dimension(620, 90));
		characterMenu.setMinimumSize(new Dimension(100, 10));

		radioPattou = new JRadioButton("Pattou :");
		radioPattou.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (radioPattou.isSelected()) radioLumi.setSelected(false);
			}
		});
		radioPattou.setBounds(18, 8, 121, 24);
		characterMenu.add(radioPattou);

		JLabel lblPattouX = new JLabel("X :");
		lblPattouX.setBounds(18, 40, 19, 16);
		characterMenu.add(lblPattouX);

		fieldPattouX = new JTextField();
		fieldPattouX.setEditable(false);
		fieldPattouX.setBounds(40, 38, 28, 20);
		characterMenu.add(fieldPattouX);
		fieldPattouX.setColumns(10);

		JLabel lblPattouY = new JLabel("Y :");
		lblPattouY.setBounds(88, 40, 19, 16);
		characterMenu.add(lblPattouY);

		fieldPattouY = new JTextField();
		fieldPattouY.setEditable(false);
		fieldPattouY.setBounds(110, 38, 28, 20);
		characterMenu.add(fieldPattouY);
		fieldPattouY.setColumns(10);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(308, 0, 2, 90);
		characterMenu.add(separator);

		radioLumi = new JRadioButton("Lumi :");
		radioLumi.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (radioLumi.isSelected()) radioPattou.setSelected(false);
			}
		});
		radioLumi.setBounds(328, 8, 121, 24);
		characterMenu.add(radioLumi);

		JLabel lblLumiX = new JLabel("X :");
		lblLumiX.setBounds(328, 40, 19, 16);
		characterMenu.add(lblLumiX);

		fieldLumiX = new JTextField();
		fieldLumiX.setEditable(false);
		fieldLumiX.setBounds(350, 38, 28, 20);
		characterMenu.add(fieldLumiX);
		fieldLumiX.setColumns(10);

		JLabel lblLumiY = new JLabel("Y :");
		lblLumiY.setBounds(398, 40, 19, 16);
		characterMenu.add(lblLumiY);

		fieldLumiY = new JTextField();
		fieldLumiY.setEditable(false);
		fieldLumiY.setBounds(420, 38, 28, 20);
		characterMenu.add(fieldLumiY);
		fieldLumiY.setColumns(10);

		btnRemovePattou = new JButton("Retirer");
		btnRemovePattou.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (!fieldPattouX.getText().equals("") || !fieldPattouY.getText().equals(""))
				{
					int oldX = Integer.valueOf(fieldPattouX.getText());
					int oldY = Integer.valueOf(fieldPattouY.getText());
					tilemap[oldX][oldY].remove(focusPattou);
					tilemap[oldX][oldY].updateUI();
				}
				fieldPattouX.setText("");
				fieldPattouY.setText("");
			}
		});
		btnRemovePattou.setBounds(198, 35, 73, 26);
		characterMenu.add(btnRemovePattou);

		btnRemoveLumi = new JButton("Retirer");
		btnRemoveLumi.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (!fieldLumiX.getText().equals("") || !fieldLumiY.getText().equals(""))
				{
					int oldX = Integer.valueOf(fieldLumiX.getText());
					int oldY = Integer.valueOf(fieldLumiY.getText());
					tilemap[oldX][oldY].remove(focusLumi);
					tilemap[oldX][oldY].updateUI();
				}
				fieldLumiX.setText("");
				fieldLumiY.setText("");
			}
		});
		btnRemoveLumi.setBounds(508, 35, 73, 26);
		characterMenu.add(btnRemoveLumi);

		menu.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent arg0)
			{
				Component selectedCmp = menu.getSelectedComponent();
				if (selectedCmp == tilesMenu) mode = MapEditor.MODE_TILES;
				else if (selectedCmp == entityMenu) mode = MapEditor.MODE_ENTITY;
				else if (selectedCmp == characterMenu) mode = MapEditor.MODE_CHARACTERS;
			}
		});

		contentPane.add(scrollMap, BorderLayout.CENTER);
		scrollMap.setViewportView(mapView);
		scrollMap.getVerticalScrollBar().setUnitIncrement(6);
		scrollMap.getHorizontalScrollBar().setUnitIncrement(6);
		GridBagLayout gbl_panel = new GridBagLayout();
		mapView.setLayout(gbl_panel);

	}
}
