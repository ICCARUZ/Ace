package UI;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import Core.Gameplay;
import Units.Unit;
public class FieldMenu 
{
	Input input;	
	String[] elements;
	int x, y;
	int height;
	int width = 90;
	int selected;
	int[] mouseLocation;
	Unit selection;
	int tilex, tiley;
	

	public FieldMenu(GameContainer gc, int[] tilePosition) 
	{
		elements = new String[]{"END TURN"};
		
		if(tilePosition != null)
		{
			tilex = tilePosition[0];
			tiley = tilePosition[1];
			
			selection = Gameplay.players[Gameplay.activePlayer].getSelection();					
	
			if(selection != null && selection.owner == Gameplay.players[Gameplay.activePlayer])
			{
				if(selection.canMove && selection.canReach(tilex, tiley))
				{
					elements = new String[]{"MOVE", "END TURN"};
				}
				else
				if(selection.canAct && Gameplay.board.get(tilex, tiley) != null && selection.owner != Gameplay.board.get(tilex, tiley).owner)
				{
					elements = new String[]{"ATTACK", "END TURN"};
				}
			}			
		}
		
		input = gc.getInput();
		height = elements.length * 25;
		mouseLocation = new int[2];
		x = input.getMouseX();
		y = input.getMouseY();			
	}

	public void render(Graphics g)
	{		
		g.setColor(new Color(0,0,0, 180));
		g.fillRect(x, y, width, height);
		
		
		//HIGHLIGHT BUTTONS
		
		for(int i = 0; i < elements.length; i++)
		{
			g.setColor(new Color(255,255,255, 50));
			g.fillRect(x + 3, (y+2) + 25*i, width - 6, 21);
			g.setColor(Color.white);
			g.drawString(elements[i], x+6, (y+3) + 25*i);
		}
		//HIGHLIGHT SELECTED ELEMENT		
		if(isMouseInMenu())
		{
			selected = getMouseIndex();
			g.setColor(new Color(255,255,255, 70));
			g.fillRect(x+3, (y+2) + 25*selected, width - 6, 21);
		}
	}

	public void update(int delta)
	{
		mouseLocation[0] = input.getMouseX();
		mouseLocation[1] = input.getMouseY();	
		
		if(!isMouseInMenu())
		{
			if(input.isMousePressed(0) || input.isMousePressed(1))
				Gameplay.toggleMenu();
		}
		else
		{
			if(input.isMousePressed(0))
			{
				if(elements[selected].equals("MOVE"))
				{
					if(Gameplay.players[Gameplay.activePlayer] == selection.owner)
						Gameplay.players[Gameplay.activePlayer].getSelection().move(new int[]{tilex, tiley});
					Gameplay.toggleMenu();
				}
				else
				if(elements[selected].equals("ATTACK"))
				{
					Gameplay.players[Gameplay.activePlayer].getSelection().act(new int[]{tilex, tiley});
					Gameplay.toggleMenu();
				}
				else
				if(elements[selected].equals("END TURN"))
					Gameplay.swapTurn();
			}
		}
	}
	
	public boolean isMouseInMenu()
	{
		return (mouseLocation[0] >= x && mouseLocation[0] <= (x + width) && mouseLocation[1] >= y && mouseLocation[1] <= (y + height));
	}
	
	public int getMouseIndex()
	{
		int r = -1;
		for(int i = 0; i < elements.length; i++)
		{
			if(mouseLocation[0] >= x && mouseLocation[0] <= (x + width) && mouseLocation[1] >= y + 25 * i && mouseLocation[1] <= y + 25*(i+1))
				return i;
		}
		return r;
	} 
}
