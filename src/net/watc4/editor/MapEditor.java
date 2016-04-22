package net.watc4.editor;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javafx.scene.input.MouseDragEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.watc4.game.display.AnimationManager;
import net.watc4.game.display.Sprite;
import net.watc4.game.map.TileRegistry;

public class MapEditor extends JFrame
{

	private JPanel contentPane;
	private static GridBagConstraints gbc = new GridBagConstraints();
	private static JPanel mapView = new JPanel();
	private static JScrollPane scrollPane = new JScrollPane();
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenuItem createMapMenu = new JMenuItem("Cr\u00E9er une carte");
	private static TileLabel[][] tilemap;
	private static TileLabel[] tileChoice;
	private static int selectedTile;
	private final JPanel menu = new JPanel();
	private final JLabel lblTiles = new JLabel("Tuiles :");
	private final static JPanel tileRegistry = new JPanel();
	private final JLabel lblTileSelected = new JLabel("Tuile s\u00E9lectionn\u00E9e :");
	private final static JLabel selectedTileLabel = new JLabel();

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

	public static void createTiles(int width, int height)
	{
		tilemap = new TileLabel[width][height];
		mapView.removeAll();
		for (int i = 0; i < tilemap.length; i++)
		{
			for (int j = 0; j < tilemap[0].length; j++)
			{
				tilemap[i][j] = new TileLabel();
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
			tileChoice[i].setPreferredSize(new Dimension(32, 32));
			tileChoice[i].setIcon(new ImageIcon(Sprite.TILE_WALL.getImage()));
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
			public void mouseClicked(MouseEvent me)
			{
					tilemap[i][j].setIcon(new ImageIcon(TileRegistry.getTileFromId(selectedTile).sprite.getImage()));
					tilemap[i][j].setId(selectedTile);
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
		selectedTileLabel.setIcon(new ImageIcon(TileRegistry.getTileFromId(selectedTile).sprite.getImage()));

		setJMenuBar(menuBar);
		createMapMenu.setMaximumSize(new Dimension(100, 100));
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
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(mapView);
		scrollPane.getVerticalScrollBar().setUnitIncrement(6);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(6);
		GridBagLayout gbl_panel = new GridBagLayout();
		mapView.setLayout(gbl_panel);

		contentPane.add(menu, BorderLayout.NORTH);
		menu.setPreferredSize(new Dimension(10, 80));
		menu.setMinimumSize(new Dimension(100, 10));
		menu.setLayout(null);
		lblTiles.setBounds(18, 23, 46, 14);

		menu.add(lblTiles);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setPreferredSize(new Dimension(150, 34));
		scrollPane_1.setBounds(74, 15, 300, 50);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane_1.getHorizontalScrollBar().setUnitIncrement(6);
		menu.add(scrollPane_1);

		GridBagLayout gbl_panel2 = new GridBagLayout();
		scrollPane_1.setViewportView(tileRegistry);
		tileRegistry.setPreferredSize(new Dimension(32 * tileChoice.length, 32));
		tileRegistry.setLayout(gbl_panel2);
		lblTileSelected.setBounds(430, 23, 116, 14);

		menu.add(lblTileSelected);
		selectedTileLabel.setBounds(555, 15, 32, 32);

		menu.add(selectedTileLabel);
	}
}
