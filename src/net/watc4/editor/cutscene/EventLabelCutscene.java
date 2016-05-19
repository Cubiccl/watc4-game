package net.watc4.editor.cutscene;

import javax.swing.JComboBox;

import net.watc4.editor.MapEditor;
import net.watc4.game.utils.FileUtils;

@SuppressWarnings("serial")
public class EventLabelCutscene extends EventLabel
{
	private JComboBox<String> cutscene, map;

	public String getCutsceneText()
	{
		return (String) cutscene.getSelectedItem();
	}

	public String getMapText()
	{
		return (String) map.getSelectedItem();
	}

	public void init()
	{
		cutscene = new JComboBox<String>(FileUtils.getCutsceneList());
		map = new JComboBox<String>(FileUtils.getMapList());
		cutscene.setBounds(20, 50, 100, 20);
		map.setBounds(130, 50, 100, 20);
		this.add(cutscene);
		this.add(map);
		MapEditor.setColor(MapEditor.black3, MapEditor.white, cutscene, map);
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

}
