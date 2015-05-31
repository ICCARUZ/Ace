package Structures;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import Units.TestUnit;
import Core.Player;

public class City extends Structure
{	
	public City()
	{
		if(Structure.sheet == null)
		try
		{
			City.sheet = new SpriteSheet("res/art/structures/city.png", 16, 16);
		}catch(SlickException e){System.err.println("ERROR FINDING IMAGE FOR CITY");}	
		
		if(this.owner != null)
			this.image = sheet.getSubImage(owner.playerNumber + 1, 0);
	}
	
	public City(int player)
	{
		this.ownerIndex = player;
	}
	
	public City(Player player)
	{
		this.owner = player;
		this.ownerIndex = player.playerNumber;
	}
}
