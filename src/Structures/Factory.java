package Structures;

import Core.Player;

public class Factory extends Structure
{
	public Factory()
	{
		
	}
	
	public Factory(int player)
	{
		this.ownerIndex = player;
	}
	
	public Factory(Player player)
	{
		this.owner = player;
		this.ownerIndex = player.playerNumber;
	}
}
