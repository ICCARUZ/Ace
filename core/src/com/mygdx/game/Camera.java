package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Camera 
{
	Map map;
	
	float x;
	float y;
	
	int viewportWidth;
	int viewportHeight;
	
	float rx, ry;
	int sx, sy;
	int tileRenderWidth;
	int tileRenderHeight;
	
	int ownerIndex;
	
	int[] mousePos = {0, 0}; // board position of mouse
	
	boolean focusLost = false;
	
	public Camera(Map map)
	{
		this.map = map;
		
		this.x = (map.width * map.getTileSize())/2;
		this.y = (-map.height * map.getTileSize())/2;
		
		this.viewportWidth = Gdx.graphics.getWidth();
		this.viewportHeight = Gdx.graphics.getHeight();		
	}
	
	public void update()
	{
		if(focusLost)
			return;
		
		mousePos = screenToBoard(UserInput.getMouseX(), UserInput.getMouseY());
		
		if(Gdx.input.isButtonPressed(Buttons.MIDDLE))
		{
			x -= Gdx.input.getDeltaX();
			y += Gdx.input.getDeltaY();
		}
	}
	
	public void render(SpriteBatch batch)
	{
		rx = viewportWidth/2 - x;
		ry = viewportHeight/2 - y;
		sx = 0;
		sy = 0;
		
		tileRenderWidth = map.width;
		tileRenderHeight = map.height;
				
		map.render(batch, rx, ry, sx, sy, tileRenderWidth, tileRenderHeight);
		
		/* camera debug info
		batch.begin();
		if(mousePos != null)
			Ace.font.draw(batch, mousePos[0] + ", " + mousePos[1], 20, 20);
		Ace.font.draw(batch, rx + ", " + ry, 20, 40);
		Ace.font.draw(batch, (rx + map.width * 16) + ", " + (ry + map.height * 16), 20, 60);
		Ace.font.draw(batch, UserInput.getMouseX() + ", " + (Gdx.graphics.getHeight() - UserInput.getMouseY()), 20, 80);
		batch.end();
		*/
	}
	
	public int[] screenToBoard(int targetX, int targetY)
	{		
		int resultX = (int)(targetX - rx)/map.getTileSize();
		int resultY = (int)(targetY + ry - viewportHeight)/map.getTileSize();
		if(map.contains(resultX, resultY))
			return new int[]{resultX, resultY};
		return null;
	}
	
	public boolean isMouseOnMap()
	{
		return UserInput.getMouseX() >= rx && UserInput.getMouseX() < map.width * 16 && Gdx.graphics.getHeight() - UserInput.getMouseY() >= ry && Gdx.graphics.getHeight() - UserInput.getMouseY() < ry + map.height * 16;
	}
}
