import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Ace extends StateBasedGame
{	
	public Map activeMap; 
	
	public static void main(String[] args) throws SlickException
	{
		AppGameContainer app = new AppGameContainer(new Ace());
		app.setDisplayMode(640, 360, false);
		app.setShowFPS(false);
		app.setAlwaysRender(true);
		app.start();
		app.setVSync(false);
	}
	
	public Ace()
	{
		super("Ace");
	}
	
	public void initStatesList(GameContainer gc) throws SlickException
	{		
		addState(new MainMenu());
		addState(new Editor());
		addState(new Gameplay());
		addState(new TurnTransition());		
	}	
}
