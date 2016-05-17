package net.watc4.editor;

import java.io.File;

import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class EventLabelCutscene extends EventLabel
{
	private JComboBox<String> cutscene, map;

	public String getCutsceneText()
	{
		return (String)cutscene.getSelectedItem();
	}

	public String getMapText()
	{
		return (String)map.getSelectedItem();
	}

	public void init()
	{
		cutscene = new JComboBox<String>(getCutsceneList());
		map = new JComboBox<String>(getMapList());
		cutscene.setBounds(20, 50, 100, 20);
		map.setBounds(130, 50, 100, 20);
		this.add(cutscene);
		this.add(map);
	}

	public EventLabelCutscene()
	{
		super();
		init();
	}

	public EventLabelCutscene(String cutscene, String map)
	{
		super();
		init();
		this.cutscene.setSelectedItem(cutscene);
		this.map.setSelectedItem(map);
	}

	public static String[] getCutsceneList()
	{
		File direc = new File("res/cutscene/");
		String[] res = direc.list();
		for (int i = 0; i < res.length; i++)
		{
			res[i] = res[i].split(".txt")[0];
		}
		return res;
	}

	public static String[] getMapList()
	{
		File direc = new File("res/maps/");
		String[] temp = direc.list();
		for (int i = 0; i < temp.length; i++)
		{
			temp[i] = temp[i].split(".txt")[0];
		}
		String[] res = new String[temp.length - 1];
		boolean found = false;
		for (int i = 0; i < res.length; i++)
		{
			if (temp[i].equals("editorTest"))
			{
				found = true;
			}
			if (found)
			{
				res[i] = temp[i + 1];
			} else res[i] = temp[i];
		}
		return res;
	}

}
