package Core;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class MainMenu extends BasicGameState 
{
	Input input;	
	String[] elements;
	int x, y;
	int height;
	int width = 120;
	int selected;
	int[] mouseLocation;
	Image graphic;
		
	public void init(GameContainer gc, StateBasedGame game) throws SlickException 
	{		
		graphic = new Image("res/art/ui/titlegraphic.png");
		elements = new String[]{"START GAME", "EDITOR", "EXIT"};			
		input = gc.getInput();
		height = elements.length * 25;
		mouseLocation = new int[2];
		x = (int)(gc.getWidth() * .4);
		y = (int)(gc.getHeight() * .7);					
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
	
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException 
	{		
		g.drawImage(graphic,  0,  0);	
		g.setColor(new Color(0, 0, 0, 128));
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
	
	public void enter(GameContainer gc, StateBasedGame game) throws SlickException
	{
		gc.getInput().clearMousePressedRecord();
		gc.getInput().clearKeyPressedRecord();
	}

	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException 
	{
		mouseLocation[0] = input.getMouseX();
		mouseLocation[1] = input.getMouseY();	
		
		if(input.isMousePressed(0) && isMouseInMenu())
		{
			if(elements[selected].equals("START GAME"))
			{
				game.enterState(0);
			}
			else
			if(elements[selected].equals("EDITOR"))
			{
				game.enterState(3);
			}
			else
			if(elements[selected].equals("EXIT"))
				gc.exit();
		}
		
	}

	public int getID() 
	{		
		return 2;
	}
}
