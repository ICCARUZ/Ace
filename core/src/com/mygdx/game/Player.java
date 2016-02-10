package com.mygdx.game;

public class Player 
{
	int playerID;	
	Camera camera;
	Unit currentSelection;
	Structure currentStructure;
	int resourceCount = 0;
	
	public Player(int playerNumber, Map map)
	{
		this.playerID = playerNumber;
		this.camera = new Camera(map);
	}
	
	public void update()
	{
		this.currentStructure = null;
		
		if(camera.focusLost)
				return;
		
		if(UserInput.isMouseButtonPressed(0) && camera.mousePos != null)
		{
			selectUnit(camera.mousePos);
			if(currentSelection != null)
				currentSelection.updateReachable();
			else
			{
				currentStructure = Skirmish.map.getStructure(camera.mousePos[0], camera.mousePos[1]);
			}
		}
	}
	
	public void selectUnit(int x, int y)
	{
		if(currentSelection != null)
			currentSelection.setAnimation(Unit.Animations.IDLE);
		
		currentSelection = Skirmish.map.getUnit(x,y);
		if(currentSelection != null)
		{
			currentSelection.setAnimation(Unit.Animations.RUN);				
		}
	}
	
	public void selectUnit(int[] location)
	{
		if(location == null)
			return;
		selectUnit(location[0], location[1]);
	}
	
	public Unit getSelection()
	{
		return currentSelection;
	}
	
	public Camera getCamera()
	{
		return this.camera;
	}
}
