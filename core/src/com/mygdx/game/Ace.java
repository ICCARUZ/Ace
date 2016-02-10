package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ace extends ApplicationAdapter 
{
	SpriteBatch batch;
	Texture img;
	
	public static Skirmish skirmish;
	public static MainMenu mainMenu;
	public static BitmapFont font;
	public static GameState currentState = GameState.MAINMENU;
	
	public static enum GameState
	{
		MAINMENU,
		SKIRMISH,
		EDITOR
	}
		
	public void create () 
	{		
		font = new BitmapFont();
		font.setColor(Color.WHITE);				
		batch = new SpriteBatch();
		
		HUD.init();
		mainMenu = new MainMenu();
		mainMenu.init();
	}

	public void render () 
	{				
		update();
		
		if(currentState == GameState.MAINMENU)
			mainMenu.render(batch);
		else
		if(currentState == GameState.SKIRMISH)
			skirmish.render(batch);
		else
		if(currentState == GameState.EDITOR)
			return; // update editor
	}
	
	public void update()
	{		
		Animation.updateAll();
		
		if(currentState == GameState.MAINMENU)
			mainMenu.update();
		else
		if(currentState == GameState.SKIRMISH)
			skirmish.update();
		else
		if(currentState == GameState.EDITOR)
			return; // update editor
	}
}
