package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Skirmish 
{
	public static Map map;
	public static Player[] players;
	public static int playerCount = 2;
	public static int currentPlayerID;
	public static state gameState;
	public static int currentTurn = 1;
	
	public Entity selection; // The currently selected unit or structure
	
	public static enum state
	{
		GAMEPLAY,
		TURNCHANGE,
		END
	}
		
	public void init(Map map)
	{
		gameState = state.TURNCHANGE;
		Skirmish.map = map;
		
		players = new Player[playerCount];
		for(int i = 0; i < playerCount; i++)
		{
			players[i] = new Player(i, map);
		}

		currentTurn = 1;
		currentPlayerID = 0;
		startTurn();
		
		map.addUnit(new Infantry(0), 8, 5);
		map.addUnit(new Infantry(1), 10, 10);
		map.addUnit(new Copter(0), 10, 5);
		map.addUnit(new Copter(1), 5, 10);
	}
	
	public void update()
	{		
		if(gameState == state.GAMEPLAY)
		{
			players[currentPlayerID].update();
			players[currentPlayerID].getCamera().update();
			HUD.update();
			
			// DEBUG
			if(UserInput.isKeyPressed(Input.Keys.Z))
			{
				int[] coord = players[currentPlayerID].camera.screenToBoard(UserInput.getMouseX(), UserInput.getMouseY());
				if(map.getUnit(coord[0], coord[1]) == null)
				{
					map.addUnit(new Infantry(currentPlayerID), coord[0], coord[1]);
				}
			}
		}
		else
		if(gameState == state.TURNCHANGE)
		{
			if(Gdx.input.justTouched())
				gameState = state.GAMEPLAY;
		}
		else
		if(gameState == state.END)
		{
			if(UserInput.isMouseButtonPressed(0))
			{
				Ace.currentState = Ace.GameState.MAINMENU;
			}
		}
	}
	
	public void render(SpriteBatch batch)
	{		
		if(gameState == state.GAMEPLAY)
		{
			// Render camera for the current player
			players[currentPlayerID].getCamera().render(batch);
			
			// Render the HUD
			HUD.render(batch);
		}
		else
		if(gameState == state.TURNCHANGE)
		{
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			batch.begin();
			
			Ace.font.draw(batch, "TURN " + currentTurn, Gdx.graphics.getWidth()/2 - 32, Gdx.graphics.getHeight()/2 + Ace.font.getCapHeight()*4);
			Ace.font.draw(batch, "PLAYER " + (currentPlayerID + 1) + " TURN", Gdx.graphics.getWidth()/2 - 60, Gdx.graphics.getHeight()/2 + Ace.font.getCapHeight());
			Ace.font.draw(batch, "CLICK TO CONTINUE", Gdx.graphics.getWidth()/2 - 77, Gdx.graphics.getHeight()/2 - Ace.font.getCapHeight());
			
			batch.end();
		}
		else
		if(gameState == state.END)
		{
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			batch.begin();
			
			Ace.font.draw(batch, "VICTORY!", Gdx.graphics.getWidth()/2 - 32, Gdx.graphics.getHeight()/2 + Ace.font.getCapHeight()*4);
			Ace.font.draw(batch, "PLAYER " + (currentPlayerID + 1) + " WINS!", Gdx.graphics.getWidth()/2 - 60, Gdx.graphics.getHeight()/2 + Ace.font.getCapHeight());
			Ace.font.draw(batch, "CLICK TO CONTINUE", Gdx.graphics.getWidth()/2 - 77, Gdx.graphics.getHeight()/2 - Ace.font.getLineHeight()*2);
			
			batch.end();
		}
	}

	public static Unit getUnit(int x, int y)
	{
		return map.getUnit(x, y);
	}
	
	public static int getDefense(int x, int y)
	{
		return map.getTerrain(x, y).defense;
	}
	
	public static void endTurn()
	{
		players[currentPlayerID].currentSelection = null;
		currentPlayerID = (currentPlayerID + 1)%players.length;
		if(currentPlayerID == 0)
			currentTurn++;
		gameState = state.TURNCHANGE;
		
		startTurn();
	}
	
	public static void startTurn()
	{
		Unit u;
		Structure s;
		for(int y = 0; y < map.height; y++)
		{
			for(int x = 0; x < map.width; x++)
			{
				u = map.getUnit(x, y);
				s = map.getStructure(x, y);
				if(u != null)
					u.startTurn();
				if(s != null)
					s.startTurn();
			}		
		}
	}
	
	public static Player getCurrentPlayer()
	{
		return players[currentPlayerID];
	}
	
	public static void setWinner(int winningPlayerIndex)
	{
		Skirmish.gameState = state.END;
		Skirmish.currentPlayerID = winningPlayerIndex; 
	}
}
