package Core;

import java.io.FileNotFoundException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.FadeInTransition;

import UI.FieldMenu;
import Units.TestUnit;
import Units.Unit;


public class Gameplay extends BasicGameState
{
	public static Map board;
	public static int activePlayer;
	public static Player[] players;
	public static Input input;
	public static boolean inMenu;
	public static FieldMenu menu;
	public static StateBasedGame game; 
	public static GameContainer gc;
	
	public void init(GameContainer gc, StateBasedGame game) throws SlickException
	{
		Gameplay.gc = gc;
		try 
		{
			Gameplay.board = new Map("res/maps/testmap.txt");
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		inMenu = false;
		input = gc.getInput();
		players = new Player[2];
		players[0] = new Player(gc, board, "PLAYER ONE", 0);
		players[1] = new Player(gc, board, "PLAYER TWO", 1);
		Gameplay.game = game;
		board.add(new TestUnit(players[0]), 8, 5);
		board.add(new TestUnit(players[1]), 10, 10);
	}
	
	public void enter(GameContainer gc, StateBasedGame game) throws SlickException
	{
		inMenu = false;
		input.clearKeyPressedRecord();	
	}
	
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException
	{
		// Draw map
		players[activePlayer].playerCam.render(gc, g);
		
		// Draw selected unit info
		if(players[activePlayer].getSelection() != null)
		{
			if(players[activePlayer].getSelection().owner == players[activePlayer])
			{
				if(players[activePlayer].getSelection().canMove)
					players[activePlayer].getSelection().drawMoves(g, players[activePlayer].playerCam.rx, players[activePlayer].playerCam.ry, 0, 0);	
				else
				if(players[activePlayer].getSelection().canAct)
					players[activePlayer].getSelection().drawRangeOnly(g, players[activePlayer].playerCam.rx, players[activePlayer].playerCam.ry, 0, 0);	
			}
			else
			{
				players[activePlayer].getSelection().drawRangeEnemy(g, players[activePlayer].playerCam.rx, players[activePlayer].playerCam.ry, 0, 0);	
			}
		}
		
		// Draw units on map
		board.drawUnits(gc, g, players[activePlayer].playerCam.rx, players[activePlayer].playerCam.ry, 0, 0);
				
		// Draw info box for hovered-over units and terrain
		if(players[activePlayer].playerCam.isMouseOnMap())
		{
			// unit info
			if(board.get(players[activePlayer].playerCam.cursorPos[0], players[activePlayer].playerCam.cursorPos[1]) != null)
			{
				board.get(players[activePlayer].playerCam.cursorPos[0], players[activePlayer].playerCam.cursorPos[1]).drawStatusWindow();
			}
			
			// terrain info
			board.drawTerrainInfoWindow(g, players[activePlayer].playerCam.cursorPos[0], players[activePlayer].playerCam.cursorPos[1]);
		}
		
		// Consider drawing a menu
		if(inMenu)
			menu.render(g);			
	}
	
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException
	{
		if(inMenu)
		{
			menu.update(delta);
			return;
		}
		players[activePlayer].playerCam.update(gc,  delta);
		getMouseInput(delta);
		getKeyboardInput(delta);
	}	
	
	public void getKeyboardInput(int delta)
	{			
		for(int x = 0; x < board.getWidth(); x++)
			for(int y = 0; y < board.getHeight(); y++)
				if(board.get(x, y) !=  null)
					(board.get(x, y)).update(delta);
			
		if(input.isKeyPressed(Input.KEY_Z))
		{
			game.enterState(1, null, new FadeInTransition());
		}
		
		if(input.isKeyPressed(Input.KEY_ESCAPE))
		{
			game.enterState(2);
		}
	}
	
	public void getMouseInput(int delta)
	{
		// Left click select
		if(input.isMousePressed(0))
		{
			if(players[activePlayer].playerCam.isMouseOnMap())
				players[activePlayer].setSelection(board.get(players[activePlayer].playerCam.cursorPos[0], players[activePlayer].playerCam.cursorPos[1]));
		}
		
		// Right click menu
		if(input.isMousePressed(1))
		{
			toggleMenu();
		}
	}
	
	public static void swapTurn()
	{		
		for(Unit u : players[activePlayer].units)
		{
			u.canMove = true;
			u.canAct = true;
		}
		game.enterState(1, null, new FadeInTransition());
	}
	
	public static void toggleMenu()
	{
		if(!inMenu)
		{
			menu = new FieldMenu(gc, players[activePlayer].playerCam.cursorPos);
			inMenu = true;			
		}
		else
		{
			inMenu = false;
		}
	}
	
	public void spawnUnits()
	{
		
	}
	
	public int getID()
	{
		return 0;
	}
}
