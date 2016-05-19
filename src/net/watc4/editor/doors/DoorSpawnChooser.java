package net.watc4.editor.doors;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import net.watc4.editor.MapEditor;
import net.watc4.editor.tiles.TileLabel;
import net.watc4.game.display.Sprite;
import net.watc4.game.entity.Entity;
import net.watc4.game.entity.EntityCutscene;
import net.watc4.game.entity.EntityRegistry;
import net.watc4.game.map.TileRegistry;
import net.watc4.game.utils.FileUtils;

@SuppressWarnings("serial")
public class DoorSpawnChooser extends JDialog
{

	private final JPanel contentPanel = new JPanel();
	private TileLabel[][] tilemap;
	private GridBagLayout gbl_panel = new GridBagLayout();
	private JPanel mapView, buttonPane;
	private JButton okButton, cancelButton;
	private JScrollPane scrollMap;
	private GridBagConstraints gbc = new GridBagConstraints();
	private boolean character;
	private int PattouX, PattouY, LumiX, LumiY;
	private static String[] fileHeader = new String[]
	{ "width = ", "height = ", "lumiSpawnX = ", "lumiSpawnY = ", "pattouSpawnX = ", "pattouSpawnY = ", "tiles =" };
	private JLabel focusPattou, focusLumi, lumiEyes;
	
	public void colorStyle()
	{
		MapEditor.setColor(MapEditor.black2, MapEditor.white, contentPanel, mapView, buttonPane, scrollMap, scrollMap.getVerticalScrollBar(), scrollMap.getHorizontalScrollBar());
		MapEditor.setColor(MapEditor.black3, MapEditor.white, okButton, cancelButton);
	}
	
	public void addTileUpdater(int i, int j)
	{
		tilemap[i][j].addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent ev)
			{
				TileLabel tl = tilemap[i][j];

				if (character)
				{
					if ((PattouX != 0) || (PattouY != 0))
					{
						int oldX = PattouX;
						int oldY = PattouY;
						tilemap[oldX][oldY].remove(focusPattou);
						tilemap[oldX][oldY].updateUI();
					}
					PattouX = i;
					PattouY = j;
					tl.add(focusPattou);
					tl.updateUI();

				} else
				{
					if ((LumiX != 0) || (LumiY != 0))
					{
						int oldX = LumiX;
						int oldY = LumiY;
						tilemap[oldX][oldY].remove(focusLumi);
						tilemap[oldX][oldY].updateUI();
					}
					LumiX = i;
					LumiY = j;
					tl.add(focusLumi);
					tl.updateUI();
				}
			}
		});
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
		return true;
	}

	/** Create the dialog. */
	public DoorSpawnChooser(String mapfile, boolean chara, int lumiX, int lumiY, int pattouX, int pattouY)
	{
		character = chara;
		LumiX = lumiX;
		LumiY = lumiY;
		PattouX = pattouX;
		PattouY = pattouY;
		focusPattou = new JLabel();
		focusPattou.setBounds(0, 0, 32, 32);
		focusPattou.setIcon(new ImageIcon(Sprite.PATTOU_IDLE_RIGHT[0].getImage()));
		focusLumi = new JLabel();
		focusLumi.setBounds(0, 0, 32, 32);
		focusLumi.setIcon(new ImageIcon(Sprite.LUMI.getImage()));
		lumiEyes = new JLabel(new ImageIcon(Sprite.LUMI_EYE.getImage()));
		lumiEyes.setBounds(0, 0, 32, 32);
		focusLumi.add(lumiEyes);
		setModal(true);
		setBounds(100, 100, 600, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			scrollMap = new JScrollPane();
			contentPanel.add(scrollMap, BorderLayout.CENTER);
			{
				mapView = new JPanel();
				scrollMap.setViewportView(mapView);
				scrollMap.getVerticalScrollBar().setUnitIncrement(6);
				scrollMap.getHorizontalScrollBar().setUnitIncrement(6);
				gbl_panel.columnWidths = new int[]
				{ 0 };
				gbl_panel.rowHeights = new int[]
				{ 0 };
				gbl_panel.columnWeights = new double[]
				{ Double.MIN_VALUE };
				gbl_panel.rowWeights = new double[]
				{ Double.MIN_VALUE };
				mapView.setLayout(gbl_panel);
			}
		}
		openMapFile(new File("res/maps/" + mapfile + ".txt"));
		if (PattouX + PattouY != 0)
		{
			tilemap[PattouX][PattouY].add(focusPattou);
			tilemap[PattouX][PattouY].updateUI();
		}
		if (LumiX + LumiY != 0)
		{
			tilemap[LumiX][LumiY].add(focusLumi);
			tilemap[LumiX][LumiY].updateUI();
		}
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
						if (character)
						{
							if(DoorValues.getWindows()[2] instanceof DoorValues)
							((DoorValues) DoorValues.getWindows()[2]).setSpawnPoint(PattouX, PattouY, character);
							else ((DoorValues) DoorValues.getWindows()[3]).setSpawnPoint(PattouX, PattouY, character);
						} else
						{
							if(DoorValues.getWindows()[2] instanceof DoorValues)
							((DoorValues) DoorValues.getWindows()[2]).setSpawnPoint(LumiX, LumiY, character);
							else ((DoorValues) DoorValues.getWindows()[3]).setSpawnPoint(PattouX, PattouY, character);
						}
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Annuler");
				cancelButton.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent arg0)
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
