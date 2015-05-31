package Structures;

import Core.Player;

public class Headquarters extends Structure
{
	public Headquarters()
	{
		
	}
	
	public Headquarters(int player)
	{
		this.ownerIndex = player;
	}
	
	public Headquarters(Player player)
	{
		this.owner = player;
		this.ownerIndex = player.playerNumber;
		
		
	}
}
