package Core;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;

import Units.Unit;

public class Player 
{
	public String name;
	public ArrayList<Unit> units;
	public Unit selected;	
	public Map board;
	public GameContainer gc;
	public int[] selectedTile;
	public Camera playerCam;
	public int playerNumber;
	
	public Player(GameContainer gc, Map board, String name, int playerNumber)
	{
		this.name = name;
		this.playerNumber = playerNumber;
		units = new ArrayList<Unit>();
		selected = null;
		this.board = board;
		this.gc = gc;
		playerCam = new Camera(gc, board, this);
		selectedTile = playerCam.screenToBoard((int)playerCam.x, (int)playerCam.y);
	}

	public void setSelection(Unit u)
	{
		selected = u;
				
		if(u != null)
			u.updateReachable();
	}
	
	public Unit getSelection()
	{
		return selected;
	}
	
	public void moveSelection(int[] position)
	{
		if(selected.owner == this && selected.canMove)
		{
			selected.move(position);
		}
	}
	
	public void startTurn()
	{
		selected = null;
		for(Unit u : units)
		{
			u.canAct = true;
			u.canMove = true;
		}
	}
}
