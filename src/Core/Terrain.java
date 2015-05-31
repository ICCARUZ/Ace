package Core;



import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Terrain
{
	public String name;
	public boolean solid = false;
	public boolean water = false;
	public boolean beach = false;
	public boolean mountain = false;
	public int vision = 0;
	public int movepenalty = 0;
	public int movecost = 1;
	public int defense = 0;
	public static Image def;
	public static Image vis;
	
	public Terrain(String name) throws SlickException
	{
		// Load images for defense and vision if not yet loaded
		if(def == null)
			def = new Image("res/art/ui/defense.png");
		if(vis == null)
			vis = new Image("res/art/ui//vision.png");
		
		this.name = name;
		buildTerrain(name);
	}
	
	public void buildTerrain(String name)
	{		
		// Open terrain data file to scan for properties
		File dataFile = new File("./res/terrain/" + name +  ".txt");
		Scanner s;
		try
		{
			s = new Scanner(dataFile);
		}
		catch(FileNotFoundException e)
		{
			return;
		}
		
		// Determine which property to set
		String property;
		int value;
		
		while(s.hasNext())
		{
			property = s.next();
			if(property.equalsIgnoreCase("solid"))
			{
				solid = true;
			}
			else
			if(property.equalsIgnoreCase("water"))
			{				
				water = true;
			}
			else
			if(property.equalsIgnoreCase("beach"))
			{
				beach = true;
			}
			else
			if(property.equalsIgnoreCase("mountain"))
			{
				mountain = true;
			}
			else
			if(property.equalsIgnoreCase("vision"))
			{
				value = s.nextInt();
				vision = value;
			}
			else
			if(property.equalsIgnoreCase("movepenalty"))
			{
				value = s.nextInt();
				movepenalty = value;
			}
			else
			if(property.equalsIgnoreCase("movecost"))
			{
				value = s.nextInt();
				movecost = value;
			}
			else
			if(property.equalsIgnoreCase("defense"))
			{
				value = s.nextInt();
				defense = value;
			}
		}
		
		s.close();
	}	
}
