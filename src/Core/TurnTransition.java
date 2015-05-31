package Core;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class TurnTransition extends BasicGameState
{
	int width, height;
	int nextPlayer;
	Input input;
	
	public void init(GameContainer gc, StateBasedGame game) throws SlickException
	{
		input = gc.getInput();
		width = gc.getScreenWidth();
		height = gc.getScreenHeight();
	}
	
	public void enter(GameContainer gc, StateBasedGame game)
	{
		input.clearKeyPressedRecord();
		advancePlayer();		
	}
	
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException
	{
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.white);
		g.drawString("  PLAYER " + (nextPlayer + 1) + " TURN\nCLICK TO CONTINUE", gc.getWidth()/3, gc.getHeight()/3);
	}
	
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException
	{
		if(input.isMousePressed(0))
			game.enterState(0, new FadeOutTransition(), null);
	}
	
	public int getID()
	{
		return 1;
	}
	
	public void advancePlayer()
	{
		if(Gameplay.activePlayer + 1 >= Gameplay.players.length)
			Gameplay.activePlayer = 0;
		else
			Gameplay.activePlayer++;
		nextPlayer = Gameplay.activePlayer;		
		for(Player playa : Gameplay.players)
			playa.startTurn();
	}
}
