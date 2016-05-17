package net.watc4.game.states.menu;

import net.watc4.game.Game;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.FileUtils;

public class FileSelectState extends MenuState
{
	public static final int NEW = 0, DELETE = 1;

	private boolean deleting;
	private Button newFile, deleteFile;

	public FileSelectState()
	{
		super("File Select");
		this.deleting = false;
	}

	@Override
	protected void createButtons()
	{
		for (int i = 0; i < FileUtils.saves.length; ++i)
			this.addButton(new Button(i + 2, "File " + (i + 1) + " : " + FileUtils.saves[i]));
		this.addButton(this.newFile = new Button(NEW, "New File"));
		this.addButton(this.deleteFile = new Button(DELETE, "Delete"));
	}

	@Override
	protected void performAction(Button selected)
	{
		if (selected.id == NEW)
		{
			FileUtils.newSave();
			FileUtils.saveSaves();
			Game.getGame().setCurrentState(GameState.createNew("map2"));
		}
		if (selected.id == DELETE)
		{
			this.deleting = !this.deleting;
			this.updateMode();
			this.setSelected(0);
		}
		if (selected.id > DELETE)
		{
			if (this.deleting)
			{
				FileUtils.deleteSave(selected.id - 2);
				this.empty();
				this.createButtons();
				this.deleting = false;
				this.updateMode();
			} else Game.getGame().setCurrentState(GameState.createNew(FileUtils.saves[selected.id - 2]));
		}
	}

	private void updateMode()
	{
		if (this.deleting)
		{
			this.removeButton(this.newFile);
			this.title = "Delete File";
			this.deleteFile.text = "Cancel";
		} else
		{
			this.addButton(this.newFile);
			this.title = "File Select";
			this.deleteFile.text = "Delete";
		}
	}

}
