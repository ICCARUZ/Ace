package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;

public class BuildMenu extends Menu
{
	public int[] location;
	public Structure selectedStructure;
	Unit[] buildables;
	
	public void show()
	{
		/*
		// Fetch selected unit and unit at cursor location
		location = Skirmish.players[Skirmish.currentPlayerID].camera.screenToBoard(UserInput.getMouseX(), UserInput.getMouseY());
		selectedStructure = Skirmish.players[Skirmish.currentPlayerID].currentStructure;
		*/
		
		// Set up menu items depeding on the context in which the menu was called
		menuItems = new ArrayList<String>();
		this.buildables = selectedStructure.getBuildableUnits();
		
		for(Unit u : buildables)
		{
			menuItems.add(u.name);
		}
										
		// Set up menu draw dimensions based on size
		buttonWidth = menuWidth - (padding * 2);
		menuHeight = menuItems.size() * buttonHeight + padding * 2 + (menuItems.size() - 1) * buttonPadding;
				
		// Set location
		//this.x = Gdx.input.getX();
		//this.y = Gdx.graphics.getHeight() - Gdx.input.getY() - this.menuHeight;
		
		this.x = 10;
		this.y = Gdx.graphics.getHeight() - 50;
						
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
	
	public void update()
	{
		// Only update the menu if active, otherwise check to see if we should open the menu
		if(!open)
		{
			if(UserInput.isMouseButtonPressed(Buttons.LEFT)) 
			{
				// Fetch selected unit and unit at cursor location
				location = Skirmish.players[Skirmish.currentPlayerID].camera.screenToBoard(UserInput.getMouseX(), UserInput.getMouseY());
				selectedStructure = Skirmish.players[Skirmish.currentPlayerID].currentStructure;
				
				if(selectedStructure != null && selectedStructure.buildingType == Structure.BuildingType.PRODUCTION && Skirmish.currentPlayerID == selectedStructure.ownerIndex)
				{				
					Skirmish.players[Skirmish.currentPlayerID].camera.focusLost = true;
					open = true;
					show();
				}
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
	
	public void selectItem(int index) 
	{
		if(index < 0 || index >= buildables.length || buildables[index].cost > Skirmish.getCurrentPlayer().resourceCount)
			return;
		
		Skirmish.map.addUnit(buildables[index].createInstance(selectedStructure.ownerIndex), location[0], location[1], false);
		Skirmish.players[Skirmish.currentPlayerID].camera.focusLost = false;
		Skirmish.getCurrentPlayer().resourceCount -= buildables[index].cost;
		open = false;
	}

	void init() 
	{
		this.title = "BUILD:";
		this.buttonHeight = 25;
	}	
}
