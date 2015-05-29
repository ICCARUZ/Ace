
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;

public class Player 
{
	String name;
	ArrayList<Unit> units;
	Unit selected;	
	Map board;
	GameContainer gc;
	int[] selectedTile;
	Camera playerCam;
	int playerNumber;
	
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
