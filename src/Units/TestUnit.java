package Units;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import Core.Gameplay;
import Core.Player;

public class TestUnit extends Unit
{	
	public TestUnit(Player owner)
	{
		this.hp = new int[] {3, 3};		
		this.owner = owner;
		owner.units.add(this);
		this.unitType = Unit.Type.LAND;
		reachable = new int[Gameplay.board.getWidth()][Gameplay.board.getHeight()];
		this.name = "Infantry";
		
		if(TestUnit.sheet == null)
		try
		{
			TestUnit.sheet = new SpriteSheet("res/art/units/infantry.png", 16, 16);
		}catch(SlickException e){System.err.println("ERROR FINDING IMAGE FOR TESTUNIT");}	
		
		this.image = sheet.getSubImage(owner.playerNumber, 0);
	}

	public void update(int delta)
	{		
		
	}
}
