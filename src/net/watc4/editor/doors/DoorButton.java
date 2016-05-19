package net.watc4.editor.doors;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import net.watc4.game.display.Sprite;

@SuppressWarnings("serial")
public class DoorButton extends JButton implements Comparable<DoorButton>
{
	private JLabel[] infos = new JLabel[6]; // 0 : info maps 1 : Sprite Lumi 2 : coordonnees Lumi 3 : Sprite Pattou 4 : coordonnees Pattou	5 : UUID
	protected String map1, map2;
	protected int UUID = 0, LumiX, LumiY, PattouX, PattouY;

	private void init()
	{
		setLayout(null);
		setPreferredSize(new Dimension(600, 100));
		removeAll();
		infos[0] = new JLabel(map1+"   vers   "+map2);
		infos[0].setBounds(50,48,300,24);
		infos[0].setFont(infos[0].getFont().deriveFont(24f));
		add(infos[0]);
		JLabel lumiEye = new JLabel(new ImageIcon(Sprite.LUMI_EYE.getImage()));
		lumiEye.setBounds(8, 8, 16, 16);
		infos[1] = new JLabel(new ImageIcon(Sprite.LUMI.getImage()));
		infos[1].setLayout(null);
		infos[1].setBounds(400,14,32,32);
		infos[1].add(lumiEye);
		add(infos[1]);
		infos[2] = new JLabel("("+LumiX+","+LumiY+")");
		infos[2].setFont(infos[2].getFont().deriveFont(18f));
		infos[2].setBounds(440, 12, 150, 32);
		add(infos[2]);
		infos[3] = new JLabel(new ImageIcon(Sprite.PATTOU_IDLE_RIGHT1.getImage()));
		infos[3].setBounds(400,54,32,32);
		add(infos[3]);
		infos[4] = new JLabel("("+PattouX+","+PattouY+")");
		infos[4].setBounds(440, 52, 150, 32);
		infos[4].setFont(infos[4].getFont().deriveFont(18f));
		add(infos[4]);
		infos[5] = new JLabel("Porte n\u00B0"+(UUID-400));
		infos[5].setBounds(20, 10, 100, 20);
		infos[5].setFont(infos[5].getFont().deriveFont(18f));
		add(infos[5]);
		if(!(this instanceof AddDoorButton))
		{
			addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					try
					{
						DoorValues dialog = new DoorValues(DoorButton.this);
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
	
	public DoorButton(String map1, int UUID, String map2, int lumiX, int lumiY, int pattouX, int pattouY)
	{
		this.map1 = map1;
		this.map2 = map2;
		this.UUID = UUID;
		this.LumiX = lumiX;
		this.LumiY = lumiY;
		this.PattouX = pattouX;
		this.PattouY = pattouY;
		init();
	}
	
	public DoorButton(){
		
	}

	@Override
	public int compareTo(DoorButton db)
	{
		return ((Integer)(this.UUID)).compareTo(db.UUID);
	}
	
	public String getMap1()
	{
		return map1;
	}

	public String getMap2()
	{
		return map2;
	}

	public int getLumiX()
	{
		return LumiX;
	}

	public int getLumiY()
	{
		return LumiY;
	}

	public int getPattouX()
	{
		return PattouX;
	}

	public int getPattouY()
	{
		return PattouY;
	}

	public int getUUID()
	{
		return UUID;
	}
}
