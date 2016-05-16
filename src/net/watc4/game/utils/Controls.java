package net.watc4.game.utils;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Controls {
	
	private InputManager inputManager;
	public ArrayList<KeyAction> controlsList;
	
	public KeyAction p_left;
	public KeyAction p_right;
	public KeyAction p_up;
	public KeyAction p_down;
	public KeyAction p_jump;
	
	public KeyAction l_left;
	public KeyAction l_right;
	public KeyAction l_up;
	public KeyAction l_down;
	
	public KeyAction exit;

	public Controls() {
		
		/*exit = new KeyAction("exit", KeyAction.DETECT_INITIAL_PRESS_ONLY);*/
		
		controlsList = new ArrayList<KeyAction>();
		
		p_left = new KeyAction("p_left"); 
		p_right = new KeyAction("p_right");
		p_up = new KeyAction("p_up"); 
		p_down = new KeyAction("p_down");
		p_jump = new KeyAction("p_jump", KeyAction.DETECT_INITIAL_PRESS_ONLY);
		
		l_left = new KeyAction("l_left"); 
		l_right = new KeyAction("l_right");
		l_up = new KeyAction("l_up"); 
		l_down = new KeyAction("l_down");
		
		listControls();
        
	}
	
	public Controls(InputManager inputManager) 
	{
		/*exit = new KeyAction("exit", KeyAction.DETECT_INITIAL_PRESS_ONLY);*/
		
		this.inputManager = inputManager;
		
		controlsList = new ArrayList<KeyAction>();
		
		p_left = new KeyAction("p_left"); 
		p_right = new KeyAction("p_right");
		p_up = new KeyAction("p_up"); 
		p_down = new KeyAction("p_down");
		p_jump = new KeyAction("p_jump");
		
		l_left = new KeyAction("l_left"); 
		l_right = new KeyAction("l_right");
		l_up = new KeyAction("l_up"); 
		l_down = new KeyAction("l_down");
		
		this.listControls();
		this.CreateDefaultControls();
		
	}
	
	public void CreateDefaultControls()
	{
		/*Game.getGame().getInputManager().mapToKey(exit, KeyEvent.VK_ESCAPE);*/
		
		inputManager.mapToKey(p_left, KeyEvent.VK_J);
		inputManager.mapToKey(p_right, KeyEvent.VK_L);
		inputManager.mapToKey(p_up, KeyEvent.VK_I);
		inputManager.mapToKey(p_down, KeyEvent.VK_K);
		inputManager.mapToKey(p_jump, KeyEvent.VK_SPACE);
		
		inputManager.mapToKey(l_left, KeyEvent.VK_Q);
		inputManager.mapToKey(l_right, KeyEvent.VK_D);
		inputManager.mapToKey(l_up, KeyEvent.VK_Z);
		inputManager.mapToKey(l_down, KeyEvent.VK_S);
	}
	
	public void listControls()
	{
		controlsList.clear();
		
		controlsList.add(p_left);
		controlsList.add(p_right);
		controlsList.add(p_up);
		controlsList.add(p_down);
		controlsList.add(p_jump);
		
		controlsList.add(l_left);
		controlsList.add(l_right);
		controlsList.add(l_up);
		controlsList.add(l_down);
	}

}