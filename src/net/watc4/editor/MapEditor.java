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
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.watc4.editor.cutscene.EventChooser;
import net.watc4.editor.cutscene.EventLabel;
import net.watc4.editor.cutscene.EventLabelCutscene;
import net.watc4.editor.cutscene.EventLabelMove;
import net.watc4.editor.cutscene.EventLabelText;
import net.watc4.editor.doors.AddDoorButton;
import net.watc4.editor.doors.DoorButton;
import net.watc4.editor.doors.DoorValues;
import net.watc4.editor.entity.EntityValues;
import net.watc4.editor.tiles.CreateMap;
import net.watc4.editor.tiles.TileLabel;
import net.watc4.game.Game;
import net.watc4.game.display.AnimationManager;
import net.watc4.game.display.Sprite;
import net.watc4.game.entity.Entity;
import net.watc4.game.entity.EntityCutscene;
import net.watc4.game.entity.EntityRegistry;
import net.watc4.game.map.TileRegistry;
import net.watc4.game.utils.FileUtils;
import net.watc4.game.utils.lore.LoreManager;

import org.eclipse.wb.swing.FocusTraversalOnArray;

public class MapEditor extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static final int MODE_TILES = 0;
	private static final int MODE_ENTITY = 1;
	private static final int MODE_CHARACTERS = 2;
	private static final int MODE_CUTSCENE = 3;
	private static final int MODE_DOORS = 4;
	private static final int MODE_ENDINGS = 5;
	private int mode;
	private Game g;
	private JPanel contentPane;
	private Toolkit tool = Toolkit.getDefaultToolkit();
	private GridBagConstraints gbc = new GridBagConstraints();
	private JPanel mapView = new JPanel(), cutsceneView = new JPanel(), doorsView = new JPanel(), endingsView = new JPanel();
	private JScrollPane scrollMap = new JScrollPane(), scrollCutscene, scrollDoors, scrollEndings, scrollTileRegistry, scrollEntityRegistry;
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenuItem createMapMenu = new JMenuItem("Nouveau"), mntmTester, mntmOuvrirUneCarte, mntmEnregistrer, mntmEnregistrerSous, mntmQuitter;
	private ArrayList<EventLabel> eventList = new ArrayList<EventLabel>();
	private ArrayList<DoorButton> doorList = new ArrayList<DoorButton>();
	private TileLabel[][] tilemap;
	private TileLabel[] tileChoice, entityChoice;
	private int selectedTile, selectedEntity;
	private JPanel tilesMenu, entityMenu, characterMenu, cutsceneMenu, doorsMenu, endingsMenu;
	private JLabel lblTiles = new JLabel("Tuiles :"), lblEntityName = new JLabel(), lblEntity, label_2, lblSelectedEntity;
	private JPanel tileRegistry = new JPanel(), entityRegistry = new JPanel();
	private JLabel lblTileSelected = new JLabel("Tuile s\u00E9lectionn\u00E9e :");
	private JLabel selectedTileLabel = new JLabel(), selectedEntityLabel = new JLabel();
	private JTextField fieldPattouX, fieldPattouY, fieldLumiX, fieldLumiY;
	private JRadioButton radioPattou, radioLumi;
	private JLabel focusPattou, focusLumi, lumiEyes, lblSelected, lblPattouX, lblPattouY, lblLumiX, lblLumiY;
	private int lblSelectedTileIndex = -1, lblSelectedEntityIndex = -1;
	private JButton btnRemovePattou, btnRemoveLumi;
	private JButton[] cutsceneOptions;
	private JButton[] ajoutCutscene;
	private JTabbedPane menu;
	public final static Color black1 = new Color(25, 25, 25), black2 = new Color(50, 50, 50), black3 = new Color(80, 80, 80), white = new Color(255, 255, 255);
	private final static JFileChooser fc = new JFileChooser(), sceneC = new JFileChooser();
	private boolean exists = false;
	private static String[] fileHeader = new String[]
	{ "width = ", "height = ", "lumiSpawnX = ", "lumiSpawnY = ", "pattouSpawnX = ", "pattouSpawnY = ", "tiles =" };

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

	public void colorStyle()
	{
		setColor(this.contentPane, black1, white);
		// MenuBar
		setColor(menuBar, black2, white);
		setColor(createMapMenu, black2, white);
		setColor(mntmTester, black2, white);
		setColor(mntmOuvrirUneCarte, black2, white);
		setColor(mntmEnregistrer, black2, white);
		setColor(mntmEnregistrerSous, black2, white);
		setColor(mntmQuitter, black2, white);
		// Menu
		for (int i = 0; i < this.menu.getComponentCount(); i++)
			setColor(this.menu.getComponent(i), black2, white);
		setColor((Component) tilesMenu.getParent(), white, black1);
		// TilesMenu
		setColor(lblTiles, black2, white);
		setColor(tileRegistry, black3, white);
		setColor(lblTileSelected, black2, white);
		setColor(selectedTileLabel, black2, white);
		setColor(mapView, black1, null);
		for (int i = 0; i < scrollTileRegistry.getHorizontalScrollBar().getComponents().length; i++)
			setColor(scrollTileRegistry.getHorizontalScrollBar().getComponent(i), black3, white);
		// EntityMenu
		setColor(lblEntityName, black2, white);
		setColor(entityRegistry, black3, white);
		setColor(selectedEntityLabel, black2, white);
		setColor(lblEntity, black2, white);
		setColor(label_2, black2, white);
		setColor(lblSelectedEntity, black2, white);
		// CharactersMenu
		setColor(radioPattou, black2, white);
		setColor(radioLumi, black2, white);
		setColor(lblPattouX, black2, white);
		setColor(lblPattouY, black2, white);
		setColor(lblLumiX, black2, white);
		setColor(lblLumiY, black2, white);
		setColor(btnRemoveLumi, black3, white);
		setColor(btnRemovePattou, black3, white);
		// Cutscene Menu
		setColor(cutsceneView, black2, white);
		for (int i = 0; i < cutsceneOptions.length; i++)
			setColor(cutsceneOptions[i], black3, white);
			// addButtons colores dans putAddButtons()
		// Ending Menu
		setColor(endingsView,black2, white);
		// TODO
	}

	public void setColor(Component c, Color back, Color fore)
	{
		if (c != null)
		{
			if (back != null) c.setBackground(back);
			if (fore != null) c.setForeground(fore);
		}
	}

	public ArrayList<DoorButton> getDoorList()
	{
		return doorList;
	}

	public void createDoorList()
	{
		AddDoorButton adb = new AddDoorButton();
		adb.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				try
				{
					DoorValues dialog = new DoorValues(new AddDoorButton());
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		doorList.add(adb);
		String[] lines = FileUtils.readFileAsStringArray("res/lore.txt");
		int i = 1;
		while (!lines[i].equals("endings ="))
		{
			String[] values = lines[i].split("\t");
			doorList.add(new DoorButton(values[0], Integer.parseInt(values[1]), values[2], Integer.parseInt(values[3]), Integer.parseInt(values[4]), Integer
					.parseInt(values[5]), Integer.parseInt(values[6])));
			i++;
		}
		doorList.sort(null);
		updateDoorList();
	}

	public void updateDoorList()
	{
		doorList.sort(null);
		doorsView.removeAll();
		for (int i = 0; i < doorList.size(); i++)
		{
			doorsView.add(doorList.get(i));
		}
		doorsView.updateUI();
	}

	public void updateScrollCutscene()
	{
		cutsceneView.setPreferredSize(new Dimension(tool.getScreenSize().width - 80, eventList.size() * 110));
		scrollCutscene.updateUI();
	}

	public int detectButtonSource(Object o)
	{
		for (int i = 0; i < ajoutCutscene.length; i++)
		{
			if (o.equals(ajoutCutscene[i])) return i;
		}
		return -1;
	}

	public void putAddButtons()
	{
		ajoutCutscene = new JButton[eventList.size() + 1];
		for (int i = 0; i < ajoutCutscene.length; i++)
		{
			ajoutCutscene[i] = new JButton(new ImageIcon(Sprite.PLUS.getImage()));
			ajoutCutscene[i].setBounds(160, 100 * i + 13, 20, 20);
			cutsceneView.add(ajoutCutscene[i]);
			setColor(ajoutCutscene[i],black3,white);
			ajoutCutscene[i].addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					int i = detectButtonSource(e.getSource());
					try
					{
						EventChooser dialog = new EventChooser(i);
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setVisible(true);
					} catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
			});
		}
	}

	public ArrayList<EventLabel> getEventlist()
	{
		return eventList;
	}

	public void setEventList(ArrayList<EventLabel> list)
	{
		eventList = list;
	}

	public void updateEventList()
	{
		cutsceneView.removeAll();
		initCutsceneOptions();
		for (int i = 0; i < eventList.size(); i++)
		{
			eventList.get(i).setBounds(20, eventList.get(i).getHeight() * i + 20, eventList.get(i).getWidth(), eventList.get(i).getHeight());
			cutsceneView.add(eventList.get(i));
			eventList.get(i).updatePosition(i + 1);
			eventList.get(i).updateButtons();
		}
		putAddButtons();
		cutsceneView.updateUI();
	}

	public void initCutsceneOptions()
	{
		for (int i = 0; i < cutsceneOptions.length; i++)
		{
			cutsceneOptions[i].setBounds(MapEditor.getFrames()[0].getWidth() - 280, 50 + i * 40, 160, 25);
			cutsceneView.add(cutsceneOptions[i]);
		}
		cutsceneOptions[0].addMouseListener(new MouseAdapter() // Creer
				{
					@Override
					public void mouseClicked(MouseEvent e)
					{
						try
						{
							eventList.clear();
							EventChooser dialog = new EventChooser(0);
							dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
							dialog.setVisible(true);
						} catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				});
		cutsceneOptions[1].addMouseListener(new MouseAdapter() // Ouvrir
				{
					@Override
					public void mouseClicked(MouseEvent arg0)
					{
						int returnVal = sceneC.showOpenDialog(MapEditor.getFrames()[0]);
						if (returnVal == JFileChooser.APPROVE_OPTION)
						{
							File cutsceneFile = sceneC.getSelectedFile();
							openCutsceneFile(cutsceneFile);
							MapEditor.getFrames()[0].setTitle("");
						}
					}
				});
		cutsceneOptions[2].addMouseListener(new MouseAdapter() // Enregistrer
				{
					@SuppressWarnings("deprecation")
					@Override
					public void mouseClicked(MouseEvent arg0)
					{
						int returnVal = sceneC.showSaveDialog(MapEditor.getFrames()[0]);
						if (returnVal == JFileChooser.APPROVE_OPTION)
						{
							try
							{
								createCutsceneFile(sceneC.getSelectedFile());
								Date d = new Date();
								MapEditor.getFrames()[0].setTitle("Sc\u00E8ne enregistr\u00E9e \u00E0 " + d.getHours() + ":" + d.getMinutes() + ":"
										+ d.getSeconds());
							} catch (IOException e1)
							{
								e1.printStackTrace();
							}
						}
					}
				});
	}

	public void removeCharacters()
	{
		fieldPattouX.setText("");
		fieldPattouY.setText("");
		fieldLumiX.setText("");
		fieldLumiY.setText("");
	}

	public void createTiles(int width, int height)
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
		exists = false;
		mapView.updateUI();
	}

	public void getTilesFromRegistry()
	{

		tileChoice = new TileLabel[TileRegistry.getTiles().size()];
		for (int i = 0; i < tileChoice.length; i++)
		{
			tileChoice[i] = new TileLabel();
			tileChoice[i].setLayout(null);
			tileChoice[i].setPreferredSize(new Dimension(32, 32));
			if (TileRegistry.getTileFromId(i).sprite == null)
			{
				tileChoice[i].setIcon(new ImageIcon(Sprite.TILE_WALL.getImage()));
			} else tileChoice[i].setIcon(new ImageIcon(TileRegistry.getTiles().get(i).sprite.getImage()));
			tileChoice[i].setId(i);
			tileChoice[i].setVisible(true);
			gbc.gridx = i;
			tileRegistry.add(tileChoice[i], gbc);
			addTileSelector(i);
		}
	}

	public void getEntityFromRegistry()
	{
		entityChoice = new TileLabel[EntityRegistry.getEntities().size()];
		for (int i = 0; i < entityChoice.length; i++)
		{
			entityChoice[i] = new TileLabel();
			entityChoice[i].setLayout(null);
			entityChoice[i].setPreferredSize(new Dimension(32, 32));

			try
			{
				entityChoice[i].setEn((Entity) (EntityRegistry.getDefaultConstructor(i)).newInstance());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException | InvocationTargetException e)
			{
				System.out.println((EntityRegistry.getDefaultConstructor(i)).toString());
				e.printStackTrace();
			}
			if (entityChoice[i].getEn() instanceof EntityCutscene)
			{
				entityChoice[i].setIcon(new ImageIcon(Sprite.CUTSCENE.getImage()));
			} else if (entityChoice[i].getEn().getRenderer() == null)
			{
				entityChoice[i].setIcon(new ImageIcon(Sprite.UNKNOWN.getImage()));
			} else entityChoice[i].setIcon(new ImageIcon(entityChoice[i].getEn().getRenderer().getAnimation().getImage()));
			entityChoice[i].setVisible(true);
			gbc.gridx = i;
			entityRegistry.add(entityChoice[i], gbc);
			addEntitySelector(i);
		}
	}

	public void addTileUpdater(int i, int j)
	{
		tilemap[i][j].addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent ev)
			{
				TileLabel tl = tilemap[i][j];
				if (mode == MapEditor.MODE_TILES)
				{
					if (ev.getButton() == MouseEvent.BUTTON3)
					{
						if (TileRegistry.getTileFromId(tl.getId()).maxData > 0)
						{
							byte d;
							if (tl.getData() == TileRegistry.getTileFromId(tl.getId()).maxData) d = 0;
							else d = (byte) (tl.getData() + 1);
							tl.setData(d);
							tl.setIcon(new ImageIcon(TileRegistry.getTileFromId(tl.getId()).getSprite(null, 0, 0, tl.getData())));
							tl.updateUI();
						}
					} else
					{
						if (TileRegistry.getTileFromId(selectedTile).sprite == null) tl.setIcon(new ImageIcon(Sprite.TILE_WALL.getImage()));
						else tl.setIcon(new ImageIcon(TileRegistry.getTileFromId(selectedTile).getSprite(null, 0, 0, tl.getData())));
						tl.setId(selectedTile);
					}
				} else if (mode == MapEditor.MODE_ENTITY)
				{
					if (ev.getButton() == MouseEvent.BUTTON3 && tl.getEn() != null && tl.getEntityValues().length <= 3)
					{
						tl.setEn(null);
						tl.setEntityValues(null);
						tl.removeAll();
						tl.updateUI();
					} else if (ev.getButton() == MouseEvent.BUTTON3 && tl.getEn() != null && tl.getEntityValues().length > 3)
					{
						try
						{
							EntityValues dialog = new EntityValues(tl);
							dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
							dialog.setVisible(true);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
					} else
					{
						if (tl.getEn() != null) tl.removeAll();
						try
						{
							tl.setEn(entityChoice[selectedEntity].getEn().getClass().newInstance());
							tl.setEnId(selectedEntity);
							tl.getEn().setX(i);
							tl.getEn().setY(j);
							tl.initValues();
						} catch (InstantiationException | IllegalAccessException e)
						{
							e.printStackTrace();
						}
						JLabel icon = new JLabel();
						if (tl.getEn() instanceof EntityCutscene)
						{
							icon = new JLabel(new ImageIcon(Sprite.CUTSCENE.getImage()));
						} else if (tl.getEn().getRenderer() == null) icon.setIcon(new ImageIcon(Sprite.UNKNOWN.getImage()));
						else icon.setIcon(new ImageIcon(tl.getEn().getRenderer().getAnimation().getImage()));
						icon.setBounds(0, 0, 32, 32);
						tl.add(icon);
						tl.updateUI();
					}
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
						tl.add(focusPattou);
						tl.updateUI();

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
						tl.add(focusLumi);
						tl.updateUI();
					}
				}
			}
		});
	}

	public void addTileSelector(int i)
	{
		tileChoice[i].addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent arg0)
			{

				selectedTile = i;
				if (TileRegistry.getTileFromId(i).sprite == null) selectedTileLabel.setIcon(new ImageIcon(Sprite.TILE_WALL.getImage()));
				else selectedTileLabel.setIcon(new ImageIcon(TileRegistry.getTileFromId(selectedTile).sprite.getImage()));
				if (lblSelectedTileIndex >= 0)
				{
					tileChoice[lblSelectedTileIndex].removeAll();
					tileChoice[lblSelectedTileIndex].updateUI();
				}
				lblSelectedTileIndex = i;
				tileChoice[i].add(lblSelected);
				tileChoice[i].updateUI();
			}
		});
	}

	public void addEntitySelector(int i)
	{
		entityChoice[i].addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent arg0)
			{

				selectedEntity = i;
				if (entityChoice[selectedEntity].getEn().getRenderer() == null)
				{
					selectedEntityLabel.setIcon(new ImageIcon(Sprite.UNKNOWN.getImage()));
				} else selectedEntityLabel.setIcon(new ImageIcon(entityChoice[selectedEntity].getEn().getRenderer().getAnimation().getImage()));
				if (lblSelectedEntityIndex >= 0)
				{
					entityChoice[lblSelectedEntityIndex].removeAll();
					entityChoice[lblSelectedEntityIndex].updateUI();
				}
				lblSelectedEntityIndex = i;
				entityChoice[i].add(lblSelected);
				entityChoice[i].updateUI();
				String name = entityChoice[i].getEn().getClass().getSimpleName();
				lblEntityName.setText(name.substring(6, name.length()));
			}
		});
	}

	public boolean openCutsceneFile(File cutsceneFile)
	{
		if (!(cutsceneFile.getAbsolutePath().endsWith(".txt") || cutsceneFile.getAbsolutePath().endsWith(".TXT"))) cutsceneFile = new File(
				cutsceneFile.getAbsolutePath() + ".txt");

		String[] lines = FileUtils.readFileAsStringArray(cutsceneFile.getAbsolutePath());

		for (int i = 0; i < lines.length; i++)
		{
			if (!(lines[i].startsWith("Text=") || lines[i].startsWith("Cutscene=") || lines[i].startsWith("Move=")))
			{
				System.err.println("Fichier incompatible.");
				return false;
			}
		}

		ArrayList<EventLabel> fileEventList = new ArrayList<EventLabel>();

		for (int i = 0; i < lines.length; i++)
		{
			String[] split = lines[i].split("\t");
			switch (lines[i].split("=")[0])
			{
				case "Cutscene":
				{
					fileEventList.add(new EventLabelCutscene(split[1], split[2]));
					break;
				}
				case "Text":
				{
					fileEventList.add(new EventLabelText(split[1]));
					break;
				}
				case "Move":
				{
					fileEventList.add(new EventLabelMove(Integer.valueOf(split[1]), Boolean.valueOf(split[2]), Integer.valueOf(split[3]), Integer
							.valueOf(split[4])));
					break;
				}
				default:
					break;
			}
		}

		eventList = fileEventList;
		updateEventList();
		updateScrollCutscene();
		return true;
	}

	public boolean openMapFile(File mapFile)
	{
		String[] lines = FileUtils.readFileAsStringArray(mapFile.getAbsolutePath());

		for (int i = 0; i < fileHeader.length; i++)
		{
			if (!lines[i].startsWith(fileHeader[i]))
			{
				System.err.println("Fichier incompatible.");
				return false;
			}
		}
		int info[] = new int[6]; // width, height, lumiSpawnX, lumiSpawnY, pattouSpawnX and pattouSpawnY
		for (int i = 0; i < info.length; i++)
		{
			info[i] = Integer.valueOf(lines[i].split(" = ")[1]);
		}
		String[] values;
		String[][] ids = new String[info[0]][info[1]];
		for (int y = 0; y < info[1]; y++)
		{
			values = lines[y + 7].split("\t");
			for (int x = 0; x < info[0]; x++)
			{
				ids[x][y] = values[x];
			}
		}

		tilemap = new TileLabel[info[0]][info[1]];
		mapView.removeAll();
		for (int i = 0; i < tilemap.length; i++)
		{
			for (int j = 0; j < tilemap[0].length; j++)
			{
				tilemap[i][j] = new TileLabel();
				tilemap[i][j].setLayout(null);
				tilemap[i][j].setPreferredSize(new Dimension(32, 32));
				if (ids[i][j].indexOf(",") > 0)
				{
					tilemap[i][j].setId(Integer.valueOf(ids[i][j].split(",")[0]));
					tilemap[i][j].setData(Byte.valueOf(ids[i][j].split(",")[1]));
				} else tilemap[i][j].setId(Integer.valueOf(ids[i][j]));
				if (TileRegistry.getTileFromId(tilemap[i][j].getId()).sprite == null)
				{
					tilemap[i][j].setIcon(new ImageIcon(Sprite.TILE_WALL.getImage()));
				} else tilemap[i][j].setIcon(new ImageIcon(TileRegistry.getTileFromId(tilemap[i][j].getId()).getSprite(null, 0, 0, tilemap[i][j].getData())));
				tilemap[i][j].setVisible(true);
				gbc.gridx = i;
				gbc.gridy = j;
				mapView.add(tilemap[i][j], gbc);
				addTileUpdater(i, j);
			}
		}

		final int X = 1, Y = 2;
		for (int i = 8 + info[1]; i < lines.length; i++)
		{
			String[] entityValues = lines[i].split(" ");
			Entity en = null;
			Object[] arguments = new Object[entityValues.length];
			arguments[0] = null;
			Constructor<Entity> constructor = EntityRegistry.getConstructor(Integer.valueOf(entityValues[0]));

			for (int j = 1; j < entityValues.length; j++)
			{
				if (constructor.getParameters()[j].getType().toString().equals("int")) arguments[j] = Integer.parseInt(entityValues[j]);
				else if (constructor.getParameters()[j].getType().toString().equals("float")) arguments[j] = Float.parseFloat(entityValues[j]);
				else if (constructor.getParameters()[j].getType().toString().equals("boolean")) arguments[j] = Boolean.parseBoolean(entityValues[j]);
				else arguments[j] = entityValues[j];
			}

			try
			{
				en = (Entity) EntityRegistry.getConstructor(Integer.valueOf(entityValues[0])).newInstance(arguments);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e)
			{
				e.printStackTrace();
				System.out.println(EntityRegistry.getConstructor(Integer.valueOf(entityValues[0])));
			}
			tilemap[Integer.valueOf(entityValues[X])][Integer.valueOf(entityValues[Y])].setEnId(Integer.valueOf(entityValues[0]));
			tilemap[Integer.valueOf(entityValues[X])][Integer.valueOf(entityValues[Y])].setEn(en);
			tilemap[Integer.valueOf(entityValues[X])][Integer.valueOf(entityValues[Y])].setEntityValues(entityValues);
			JLabel icon;
			if (en instanceof EntityCutscene)
			{
				icon = new JLabel(new ImageIcon(Sprite.CUTSCENE.getImage()));
			} else if (en.getRenderer() == null)
			{
				icon = new JLabel(new ImageIcon(Sprite.UNKNOWN.getImage()));
			} else icon = new JLabel(new ImageIcon(en.getRenderer().getAnimation().getImage()));
			icon.setBounds(0, 0, 32, 32);
			tilemap[Integer.valueOf(entityValues[X])][Integer.valueOf(entityValues[Y])].add(icon);
			tilemap[Integer.valueOf(entityValues[X])][Integer.valueOf(entityValues[Y])].updateUI();
		}
		mapView.updateUI();

		if (info[2] >= 0 && info[3] >= 0)
		{
			fieldLumiX.setText(String.valueOf(info[2]));
			fieldLumiY.setText(String.valueOf(info[3]));
			focusLumi.add(lumiEyes);
			tilemap[info[2]][info[3]].add(focusLumi);
			tilemap[info[2]][info[3]].updateUI();
		}
		if (info[4] >= 0 && info[5] >= 0)
		{
			fieldPattouX.setText(String.valueOf(info[4]));
			fieldPattouY.setText(String.valueOf(info[5]));
			tilemap[info[4]][info[5]].add(focusPattou);
			tilemap[info[4]][info[5]].updateUI();
		}
		exists = true;
		return true;
	}

	public boolean createCutsceneFile(File cutsceneFile) throws IOException
	{
		String path = cutsceneFile.getAbsolutePath();
		if (!(path.endsWith(".txt") || path.endsWith(".TXT"))) path += ".txt";
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path)));
		for (int i = 0; i < eventList.size(); i++)
		{
			switch (eventList.get(i).getClass().getSimpleName().replace("EventLabel", ""))
			{
				case "Cutscene":
				{
					pw.println("Cutscene=\t" + ((EventLabelCutscene) eventList.get(i)).getCutsceneText() + "\t"
							+ ((EventLabelCutscene) eventList.get(i)).getMapText());
					break;
				}
				case "Text":
				{
					pw.println("Text=\t" + ((EventLabelText) eventList.get(i)).getTextArea());
					break;
				}
				case "Move":
				{
					pw.println("Move=\t" + ((EventLabelMove) eventList.get(i)).getValueFromWhichField(0) + "\t"
							+ ((EventLabelMove) eventList.get(i)).getRelativeValue() + "\t" + ((EventLabelMove) eventList.get(i)).getValueFromWhichField(1)
							+ "\t" + ((EventLabelMove) eventList.get(i)).getValueFromWhichField(2));
					break;
				}
				default:
					break;
			}
		}
		pw.close();
		return true;
	}

	public boolean createMapFile(File mapFile) throws IOException
	{
		String path = mapFile.getAbsolutePath();
		if (!(path.endsWith(".txt") || path.endsWith(".TXT"))) path += ".txt";
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path)));
		int[] info = new int[6];
		info[0] = tilemap.length;
		info[1] = tilemap[0].length;
		if (fieldLumiX.getText().equals("")) info[2] = -1;
		else info[2] = Integer.valueOf(fieldLumiX.getText());
		if (fieldLumiY.getText().equals("")) info[3] = -1;
		else info[3] = Integer.valueOf(fieldLumiY.getText());
		if (fieldPattouX.getText().equals("")) info[4] = -1;
		else info[4] = Integer.valueOf(fieldPattouX.getText());
		if (fieldPattouY.getText().equals("")) info[5] = -1;
		else info[5] = Integer.valueOf(fieldPattouY.getText());
		for (int i = 0; i < fileHeader.length - 1; i++)
		{
			pw.println(new String(fileHeader[i] + info[i]));
		}
		pw.println(fileHeader[6]);

		for (int i = 0; i < tilemap[0].length; i++)
		{

			for (int j = 0; j < tilemap.length - 1; j++)
			{
				if (tilemap[j][i].getData() != 0)
				{
					pw.print(tilemap[j][i].getId() + "," + tilemap[j][i].getData() + "\t");
				} else pw.print(tilemap[j][i].getId() + "\t");
			}
			pw.println(tilemap[i][tilemap[i].length - 1].getId());
		}

		pw.println("entities =");

		for (int i = 0; i < tilemap[0].length; i++)
		{
			for (int j = 0; j < tilemap.length; j++)
			{
				if (tilemap[j][i].getEntityValues() != null)
				{
					for (int h = 0; h < tilemap[j][i].getEntityValues().length; h++)
					{
						pw.print(tilemap[j][i].getEntityValues()[h] + " ");
						if (h == tilemap[j][i].getEntityValues().length - 1) pw.println();
					}
				}
			}
		}

		exists = true;
		pw.close();
		return true;
	}

	/** Create the frame. */
	public MapEditor()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 120, 650, 600);
		menuBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		LoreManager.createLore();
		Sprite.createMainSprites();
		AnimationManager.create();
		TileRegistry.createTiles();
		EntityRegistry.createEntities();
		getEntityFromRegistry();
		getTilesFromRegistry();

		lblSelected = new JLabel();
		lblSelected.setBounds(11, 11, 10, 10);
		lblSelected.setBackground(new Color(0, 0, 255));
		lblSelected.setOpaque(true);
		focusPattou = new JLabel();
		focusPattou.setBounds(0, 0, 32, 32);
		focusPattou.setIcon(new ImageIcon(Sprite.PATTOU_IDLE_RIGHT[0].getImage()));
		focusLumi = new JLabel();
		focusLumi.setBounds(0, 0, 32, 32);
		focusLumi.setIcon(new ImageIcon(Sprite.LUMI.getImage()));
		lumiEyes = new JLabel(new ImageIcon(Sprite.LUMI_EYE.getImage()));
		lumiEyes.setBounds(0, 0, 32, 32);

		FileFilter txtOnly = new FileNameExtensionFilter("Fichier texte", "txt");
		fc.addChoosableFileFilter(txtOnly);
		fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
		fc.setCurrentDirectory(new File("res/maps/"));
		sceneC.addChoosableFileFilter(txtOnly);
		sceneC.removeChoosableFileFilter(sceneC.getAcceptAllFileFilter());
		sceneC.setCurrentDirectory(new File("res/cutscene/"));

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
					MapEditor.this.setTitle("");
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

		menuBar.add(createMapMenu);

		mntmTester = new JMenuItem("Tester");
		mntmTester.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{

				if (tilemap == null)
				{
					JOptionPane.showMessageDialog(null, "Veuillez cr\u00E9er une carte.", null, JOptionPane.ERROR_MESSAGE);
				} else
				{
					try
					{
						createMapFile(new File("res/maps/editorTest.txt"));
						EventQueue.invokeLater(new Runnable()
						{
							public void run()
							{
								try
								{
									g = new Game("editorTest");
									Game.setInstance(g);
									g.window.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
								} catch (Exception e)
								{}
							}
						});
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		});
		mntmTester.setToolTipText("Lance le jeu avec la carte en cours d'\u00E9dition.");
		mntmTester.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mntmTester.setMaximumSize(new Dimension(80, 32767));
		menuBar.add(mntmTester);

		mntmOuvrirUneCarte = new JMenuItem("Ouvrir");
		mntmOuvrirUneCarte.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				int returnVal = fc.showOpenDialog(MapEditor.this);
				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					File mapFile = fc.getSelectedFile();
					openMapFile(mapFile);
					MapEditor.this.setTitle("");
				}
			}
		});
		mntmOuvrirUneCarte.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mntmOuvrirUneCarte.setToolTipText("Ouvrez une carte d\u00E9j\u00E0 existante");
		mntmOuvrirUneCarte.setMaximumSize(new Dimension(70, 32767));
		menuBar.add(mntmOuvrirUneCarte);

		mntmEnregistrer = new JMenuItem("Enregistrer");
		mntmEnregistrer.addMouseListener(new MouseAdapter()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (tilemap == null)
				{
					JOptionPane.showMessageDialog(null, "Veuillez cr\u00E9er une carte.", null, JOptionPane.ERROR_MESSAGE);
				} else if (!exists)
				{
					int returnVal = fc.showSaveDialog(MapEditor.this);
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						try
						{
							createMapFile(fc.getSelectedFile());
							Date d = new Date();
							MapEditor.this.setTitle("Enregistr\u00E9 \u00E0 " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds());
						} catch (IOException e1)
						{
							e1.printStackTrace();
						}
					}
				} else try
				{
					createMapFile(fc.getSelectedFile());
					Date d = new Date();
					MapEditor.this.setTitle("Enregistr\u00E9 \u00E0 " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds());
				} catch (IOException e1)
				{
					e1.printStackTrace();
				}

			}
		});
		mntmEnregistrer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mntmEnregistrer.setMaximumSize(new Dimension(90, 32767));
		mntmEnregistrer.setToolTipText("Sauvegardez votre carte au format texte");
		menuBar.add(mntmEnregistrer);

		mntmEnregistrerSous = new JMenuItem("Enregistrer sous");
		mntmEnregistrerSous.setMaximumSize(new Dimension(110, 32767));
		mntmEnregistrerSous.addMouseListener(new MouseAdapter()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (tilemap == null)
				{
					JOptionPane.showMessageDialog(null, "Veuillez cr\u00E9er une carte.", null, JOptionPane.ERROR_MESSAGE);
				} else
				{
					int returnVal = fc.showSaveDialog(MapEditor.this);
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						try
						{
							createMapFile(fc.getSelectedFile());
							Date d = new Date();
							MapEditor.this.setTitle("Enregistr\u00E9 \u00E0 " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds());
						} catch (IOException e1)
						{
							e1.printStackTrace();
						}
					}
				}
			}
		});
		mntmEnregistrerSous.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menuBar.add(mntmEnregistrerSous);

		mntmQuitter = new JMenuItem("     Quitter");
		mntmQuitter.setMaximumSize(new Dimension(80, 32767));
		mntmQuitter.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				System.exit(0);
			}
		});
		mntmQuitter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menuBar.add(mntmQuitter);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		menu = new JTabbedPane();
		contentPane.add(menu, BorderLayout.NORTH);
		selectedTileLabel.setIcon(new ImageIcon(TileRegistry.getTileFromId(selectedTile).sprite.getImage()));
		tilesMenu = new JPanel();
		tilesMenu.setPreferredSize(new Dimension(620, 90));
		menu.add(tilesMenu, "Tuiles");
		tilesMenu.setMinimumSize(new Dimension(100, 10));
		tilesMenu.setLayout(null);
		lblTiles.setBounds(18, 23, 46, 14);

		tilesMenu.add(lblTiles);

		scrollTileRegistry = new JScrollPane();
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

		entityMenu = new JPanel();
		menu.add(entityMenu, "Entit\u00E9s");
		entityMenu.setLayout(null);
		entityMenu.setPreferredSize(new Dimension(620, 90));
		entityMenu.setMinimumSize(new Dimension(100, 10));

		lblEntity = new JLabel("Entit\u00E9s :");
		lblEntity.setBounds(18, 23, 46, 14);
		entityMenu.add(lblEntity);

		scrollEntityRegistry = new JScrollPane();
		scrollEntityRegistry.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollEntityRegistry.setPreferredSize(new Dimension(150, 34));
		scrollEntityRegistry.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollEntityRegistry.setBounds(74, 15, 300, 50);
		entityMenu.add(scrollEntityRegistry);

		scrollEntityRegistry.setViewportView(entityRegistry);
		GridBagLayout gbl_entityRegistry = new GridBagLayout();
		entityRegistry.setPreferredSize(new Dimension(32 * entityChoice.length, 32));
		entityRegistry.setLayout(gbl_entityRegistry);

		lblSelectedEntity = new JLabel("Entit\u00E9 s\u00E9lectionn\u00E9e :");
		lblSelectedEntity.setBounds(430, 23, 116, 14);
		entityMenu.add(lblSelectedEntity);

		label_2 = new JLabel();
		label_2.setBounds(555, 15, 32, 32);
		entityMenu.add(label_2);

		lblEntityName.setBounds(459, 49, 100, 16);
		entityMenu.add(lblEntityName);

		characterMenu = new JPanel();
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

		lblPattouX = new JLabel("X :");
		lblPattouX.setBounds(18, 40, 19, 16);
		characterMenu.add(lblPattouX);

		fieldPattouX = new JTextField();
		fieldPattouX.setEditable(false);
		fieldPattouX.setBounds(40, 38, 28, 20);
		characterMenu.add(fieldPattouX);
		fieldPattouX.setColumns(10);

		lblPattouY = new JLabel("Y :");
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

		lblLumiX = new JLabel("X :");
		lblLumiX.setBounds(328, 40, 19, 16);
		characterMenu.add(lblLumiX);

		fieldLumiX = new JTextField();
		fieldLumiX.setEditable(false);
		fieldLumiX.setBounds(350, 38, 28, 20);
		characterMenu.add(fieldLumiX);
		fieldLumiX.setColumns(10);

		lblLumiY = new JLabel("Y :");
		lblLumiY.setBounds(398, 40, 19, 16);
		characterMenu.add(lblLumiY);

		fieldLumiY = new JTextField();
		fieldLumiY.setEditable(false);
		fieldLumiY.setBounds(420, 38, 28, 20);
		characterMenu.add(fieldLumiY);
		fieldLumiY.setColumns(10);

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
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]
		{ tilesMenu, lblTiles, scrollTileRegistry, tileRegistry, menu, mapView, lblTileSelected, selectedTileLabel, entityMenu, lblEntity,
				scrollEntityRegistry, lblSelectedEntity, label_2, characterMenu, radioPattou, lblPattouX, fieldPattouX, lblPattouY, fieldPattouY, separator,
				radioLumi, lblLumiX, fieldLumiX, lblLumiY, fieldLumiY, btnRemovePattou, btnRemoveLumi, scrollMap }));
		btnRemoveLumi.setBounds(508, 35, 73, 26);
		characterMenu.add(btnRemoveLumi);

		cutsceneMenu = new JPanel();
		cutsceneMenu.setPreferredSize(new Dimension(620, 90));
		cutsceneMenu.setMinimumSize(new Dimension(100, 10));
		cutsceneMenu.setLayout(null);
		BorderLayout bl = new BorderLayout();
		cutsceneMenu.setLayout(bl);
		menu.add(cutsceneMenu, "Sc\u00E8nes");

		scrollCutscene = new JScrollPane(cutsceneView);
		cutsceneMenu.add(scrollCutscene, BorderLayout.CENTER);
		cutsceneView.setLayout(null);
		scrollCutscene.getVerticalScrollBar().setUnitIncrement(6);
		scrollCutscene.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		cutsceneOptions = new JButton[]
		{ new JButton("Cr\u00E9er"), new JButton("Ouvrir"), new JButton("Enregistrer") };

		doorsMenu = new JPanel();
		doorsMenu.setPreferredSize(new Dimension(620, 90));
		doorsMenu.setMinimumSize(new Dimension(100, 10));
		doorsMenu.setLayout(null);
		BorderLayout bl2 = new BorderLayout();
		doorsMenu.setLayout(bl2);
		menu.add(doorsMenu, "Portes");

		scrollDoors = new JScrollPane(doorsView);
		doorsMenu.add(scrollDoors, BorderLayout.CENTER);
		GridLayout gl = new GridLayout(0, 1);
		doorsView.setLayout(gl);
		createDoorList();
		scrollDoors.getVerticalScrollBar().setUnitIncrement(6);
		scrollDoors.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		// TODO
		endingsMenu = new JPanel();
		endingsMenu.setPreferredSize(new Dimension(620, 90));
		endingsMenu.setMinimumSize(new Dimension(100, 10));
		endingsMenu.setLayout(null);
		BorderLayout bl3 = new BorderLayout();
		endingsMenu.setLayout(bl3);
		menu.add(endingsMenu, "Fins");

		scrollEndings = new JScrollPane(endingsView);
		endingsMenu.add(scrollEndings, BorderLayout.CENTER);
		GridLayout gl2 = new GridLayout(0, 1);
		doorsView.setLayout(gl2);
		scrollEndings.getVerticalScrollBar().setUnitIncrement(6);
		scrollEndings.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		// TODO

		contentPane.add(scrollMap, BorderLayout.CENTER);
		scrollMap.setViewportView(mapView);
		scrollMap.getVerticalScrollBar().setUnitIncrement(6);
		scrollMap.getHorizontalScrollBar().setUnitIncrement(6);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.rowWeights = new double[] {};
		gbl_panel.columnWeights = new double[] {};
		mapView.setLayout(gbl_panel);

		menu.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent arg0)
			{
				Component selectedCmp = menu.getSelectedComponent();
				if (selectedCmp == tilesMenu) mode = MapEditor.MODE_TILES;
				else if (selectedCmp == entityMenu) mode = MapEditor.MODE_ENTITY;
				else if (selectedCmp == characterMenu) mode = MapEditor.MODE_CHARACTERS;
				else if (selectedCmp == cutsceneMenu)
				{
					mode = MapEditor.MODE_CUTSCENE;
					menu.setSize(new Dimension(MapEditor.this.getWidth() - 26, MapEditor.this.getHeight() - 72));
					updateEventList();
					initCutsceneOptions();
				} else if (selectedCmp == doorsMenu)
				{
					mode = MapEditor.MODE_DOORS;
					menu.setSize(new Dimension(MapEditor.this.getWidth() - 26, MapEditor.this.getHeight() - 72));
				} else if (selectedCmp == endingsMenu)
				{
					mode = MapEditor.MODE_ENDINGS;
					menu.setSize(new Dimension(MapEditor.this.getWidth() - 26, MapEditor.this.getHeight() - 72));
				}
			}
		});

		colorStyle();

	}
}
