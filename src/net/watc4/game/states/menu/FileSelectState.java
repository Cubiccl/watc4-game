package net.watc4.game.states.menu;

import net.watc4.game.Game;
import net.watc4.game.states.GameState;
import net.watc4.game.utils.FileUtils;
import net.watc4.game.utils.lore.LoreManager;

public class FileSelectState extends MenuState
{
	public static final int NEW = 0, DELETE = 1, BACK = 2;

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
		for (int i = 0; i < LoreManager.saves.length; ++i)
			this.addButton(new Button(i + 2, "File " + (i + 1) + " : " + LoreManager.saves[i]));
		this.addButton(this.newFile = new Button(NEW, "New File"));
		this.addButton(this.deleteFile = new Button(DELETE, "Delete"));
		this.addButton(new Button(BACK, "Back to Menu"));
	}

	@Override
	protected void performAction(Button selected)
	{
		if (selected.id == NEW)
		{
			LoreManager.newSave();
			FileUtils.saveSaves();
			Game.getGame().setCurrentState(GameState.createNew(LoreManager.firstMap));
		}
		if (selected.id == DELETE)
		{
			this.deleting = !this.deleting;
			this.updateMode();
			this.setSelected(0);
		}
		if (selected.id == BACK) Game.getGame().setCurrentState(new MainMenuState());

		if (selected.id > BACK)
		{
			if (this.deleting)
			{
				LoreManager.deleteSave(selected.id - 3);
				this.empty();
				this.createButtons();
				this.deleting = false;
				this.updateMode();
			} else Game.getGame().setCurrentState(GameState.createNew(LoreManager.saves[selected.id - 3]));
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
