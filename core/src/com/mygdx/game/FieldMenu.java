package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;

public class FieldMenu extends Menu
{
	/*
	public Entity selectedEntity; // The Unit selected when the menu was opened
	public Entity otherEntity; // The Unit clicked on to open the menu
	public int[] location; // The map location the menu was opened at
	*/
	
	public Unit selectedUnit;
	public Unit otherUnit;
	public int[] location;
		
	public void update()
	{				
		// Only update the menu if active, otherwise check to see if we should open the menu
		if(!open)
		{
			if(UserInput.isMouseButtonPressed(Buttons.RIGHT)) 
			{
				Skirmish.players[Skirmish.currentPlayerID].camera.focusLost = true;
				open = true;
				show();
			}
			
			return;
		}
		
		super.update();
		
		// If we click outside the menu, close it
		if(UserInput.isMouseButtonPressed(Buttons.LEFT) || UserInput.isMouseButtonPressed(Buttons.RIGHT))
		{
			if(!isMouseOnMenu)
			{
				Skirmish.players[Skirmish.currentPlayerID].camera.focusLost = false;
				open = false;
			}
		}
	}	
	
	public void show()
	{						
		// Fetch selected unit and unit at cursor location
		location = Skirmish.players[Skirmish.currentPlayerID].camera.mousePos;
		selectedUnit = Skirmish.players[Skirmish.currentPlayerID].getSelection();
		if(location != null)
			otherUnit = Skirmish.map.getUnit(location[0], location[1]);
						
		// Set up menu items depeding on the context in which the menu was called
		menuItems = new ArrayList<String>();
				
		if(selectedUnit != null && selectedUnit.ownerIndex == Skirmish.currentPlayerID)
		{
			if(((Unit)selectedUnit).canMoveTo(location[0], location[1]))
			{
				menuItems.add("MOVE");
			}
			
			if(((Unit)selectedUnit).canAttack(location[0], location[1]))
			{
				menuItems.add("ATTACK");
			}
			
			if(((Unit)selectedUnit).canCapture(location[0], location[1]))
			{
				menuItems.add("CAPTURE");
			}
			
			if(((Unit)selectedUnit).canReinforce(location[0], location[1]))
			{
				menuItems.add("MERGE");
			}
			
			if(((Unit)selectedUnit).canAct /*&& selectedUnit.x == location[0] && selectedUnit.y == location[1]*/)
			{
				menuItems.add("WAIT");
			}
			
			if(!((Unit)selectedUnit).canAct)
			{
				menuItems.add("END TURN");
			}
		}
		else
		{
			menuItems.add("END TURN");	
		}

		
		// Set up menu draw dimensions based on size
		buttonWidth = menuWidth - (padding * 2);
		menuHeight = menuItems.size() * buttonHeight + padding * 2 + (menuItems.size() - 1) * buttonPadding;
		
		// Set location
		this.x = Gdx.input.getX();
		this.y = Gdx.graphics.getHeight() - Gdx.input.getY() - this.menuHeight;
				
		// Ensure that the menu is entirely on-screen
		if(this.x < 0)
			this.x += this.menuWidth;
		if(this.y < 0)
			this.y += this.menuHeight;
				
		if(this.x + this.menuWidth > Gdx.graphics.getWidth())
			this.x = Gdx.graphics.getWidth() - this.menuWidth;
		if(this.y + this.menuHeight > Gdx.graphics.getHeight())
			this.y -= this.menuHeight;
	}
	
	public void selectItem(int item)
	{		
		if(item < 0)
			return;
		
		switch(menuItems.get(item))
		{	
			case "MOVE": ((Unit)selectedUnit).move(location[0], location[1]); break;
			case "ATTACK": ((Unit)selectedUnit).attack(location[0], location[1]); break;
			case "MERGE": ((Unit)selectedUnit).reinforce(location[0], location[1]); break;
			case "CAPTURE": ((Unit)selectedUnit).capture(location[0], location[1]); break;
			case "WAIT": ((Unit)selectedUnit).canAct = false; ((Unit)selectedUnit).canMove = false; break;
			case "END TURN": Skirmish.endTurn(); break;
		}
		if(selectedUnit != null && !menuItems.get(item).equals("MOVE")) 
		{ 
			((Unit)selectedUnit).setAnimation(Unit.Animations.IDLE); 
		}
		Skirmish.players[Skirmish.currentPlayerID].camera.focusLost = false;
		open = false;
	}

	@Override
	void init() 
	{
		// Set up button height
		buttonHeight = 25;	
	}
}
