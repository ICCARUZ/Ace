package Structures;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import Core.Map;
import Core.Player;

public class Structure 
{
	public Map board;
	
	public int x, y;
	
	public boolean canAct;
	
	public String name;
	public Player owner;
	public int ownerIndex = -1;
	public static SpriteSheet sheet; // Sheet of all building sprites
	public Image image;
	
	public static ArrayList<Structure> allStructures;
	
	Type structureType;
	
	enum Type
	{
		CITY,
		PRODUCTION,
		WEAPON
	}	
	
	public static void initStructures()
	{
		if(allStructures != null)
			return;
		
		allStructures = new ArrayList<Structure>();		
		allStructures.add(new City());		
	}
	
	public void act(int x, int y)
	{
		if(canAct)
		{
			
		}
	}
	
	public void render(int rx, int ry, int sx, int sy)
	{
		if(canAct)
		{
			image.draw(rx + (x - sx) * board.tileSize, ry + (y - sy) * board.tileSize, board.tileSize, board.tileSize);
		}
		else
		{
			image.draw(rx + (x - sx) * board.tileSize, ry + (y - sy) * board.tileSize, board.tileSize, board.tileSize, Color.gray);
		}
	}
}
