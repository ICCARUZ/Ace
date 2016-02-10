package com.mygdx.game;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainMenu extends Menu
{
	Texture menuTexture;
	
	public void selectItem(int index) 
	{
		if(index == 0)
		{
			Map testMap = null;
			try 
			{
				testMap = new Map("../desktop/res/maps/testmap - Copy.txt");
			} catch (FileNotFoundException e) {System.err.println("INITIALIZATION FALED. COULD NOT FIND MAP FILE."); System.exit(0);}
			Ace.skirmish = new Skirmish();
			Ace.skirmish.init(testMap);
			Ace.currentState = Ace.GameState.SKIRMISH;
		}
		else
		if(index == 1)
		{
			// INITIALIZE EDITOR
		}
		else
		if(index == 2)
		{
			Gdx.app.exit(); // QUIT GAME
		}
	}
	
	public void render(SpriteBatch batch)
	{
		batch.begin();
		
		batch.draw(menuTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.end();
		
		super.render(batch);
	}

	public void init() 
	{				
		menuTexture = new Texture("./res/art/ui/titlegraphic.png");
		this.menuWidth = 130;
		this.buttonWidth = menuWidth - padding*2;
		buttonHeight = 25;
		open = true;
		x = Gdx.graphics.getWidth()/3;
		y = 0;
		// Create list of menu items
		menuItems = new ArrayList<String>();
		menuItems.add("START GAME");
		//menuItems.add("LOAD GAME");
		menuItems.add("MAP EDITOR");
		//menuItems.add("OPTIONS");
		menuItems.add("QUIT GAME");
		menuHeight = menuItems.size() * buttonHeight + padding * 2 + (menuItems.size() - 1) * buttonPadding;
	}	
}
