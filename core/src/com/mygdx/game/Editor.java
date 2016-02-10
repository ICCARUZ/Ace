package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Editor 
{
	public Camera camera;
	public Map map;
	public String filename;
	public String mapname;
	TileSelector selector;
	int[] mouseGridLoc;
	int currentTeam;
	
	public void init()
	{
		openMap("skirmish.txt");
		this.camera = new Camera(map);
		this.selector = new TileSelector();
	}
	
	public void update()
	{
		selector.update();
		camera.update();
	}
	
	public void render(SpriteBatch batch)
	{
		camera.render(batch);
		
		if(selector.isVisible)
			selector.render(batch);
		
		getMouseInput();
		getKeyboardInput();
		
		/*
		if(camera.isMouseOnMap())
		{
			// draw terrain info		
		}
		*/
	}
	
	public void getMouseInput()
	{
		if(UserInput.isMouseButtonDown(0) && !selector.isMouseOnWindow() && camera.isMouseOnMap())
		{
			
		}
	}
	
	public void getKeyboardInput()
	{
		
	}
	
	public void openMap(String filename)
	{
		
	}
}
