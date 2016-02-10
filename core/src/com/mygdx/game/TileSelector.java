package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TileSelector extends HUDElement
{
	public boolean isVisible = true;
	public int selection = 0;
	public float x, y;
	public float width, height;
	
	public TileSelector()
	{
		init();
	}
	
	void init() 
	{
		
	}
	
	public void update()
	{
		if(UserInput.isMouseButtonPressed(0))
		{
		}
	}
	
	public void render(SpriteBatch batch)
	{
		// Draw window
		
		// Draw window title
				
		// Draw contents for selected element type
	}
	
	public boolean isMouseOnWindow()
	{
		return UserInput.getMouseX() < this.x + this.width;
	}	
}
