package com.mygdx.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Terrain 
{	
	public static Terrain terrainList[];
	public static Texture terrainSheet;
	public static int tileSize = 16;
	
	public String name;
	
	public TextureRegion texture;	
	public boolean solid = false;
	public boolean water = false;
	public boolean shallow = false;
	public boolean mountain = false;
	
	public int bonusVision = 0;
	public int moveCost = 1;
	public int defense = 0;	
	
	public Terrain(String name)
	{
		this.name = name;
		buildTerrain(name);
	}
	
	public Terrain() 
	{
		this.texture = null;
	}

	public static void loadTerrains() throws FileNotFoundException
	{
		if(terrainList != null)
			return;
		
		File terrainFile = new File("res/terrain/terrains_active.txt");
		ArrayList<String> terrainNames = new ArrayList<String>();
		
		Scanner s = new Scanner(terrainFile);		
		while(s.hasNextLine())
			terrainNames.add(s.nextLine());		
		s.close();
		
		terrainSheet = new Texture("./res/art/terrain/terrains.png");
		
		terrainList = new Terrain[terrainNames.size()];				
		for(int i = 0; i < terrainNames.size(); i++)
		{
			terrainList[i] = new Terrain(terrainNames.get(i));
			terrainList[i].texture = TextureRegion.split(terrainSheet, tileSize, tileSize)[i/(terrainSheet.getWidth()/tileSize)][i%(terrainSheet.getWidth()/tileSize)];
		}
	}
	
	public void buildTerrain(String name)
	{
		File dataFile = new File("./res/terrain/" + name + ".txt");
		Scanner s;
		
		try
		{
			s = new Scanner(dataFile);
		}
		catch( FileNotFoundException e ){ return; }
		
		String property;
		
		while(s.hasNext())
		{
			property = s.next();
			property.toLowerCase();
			switch(property)
			{
				case "solid" : solid = true; break;
				case "water" : water = true; break;
				case "shallow" : shallow = true; break;
				case "mountain" : mountain = true; break;
				case "vision" : bonusVision = s.nextInt(); break;
				case "movepenalty" : moveCost += s.nextInt(); break;
				case "movecost" : moveCost = s.nextInt(); break;
				case "defense" : defense = s.nextInt(); break;
				default: break;
			}
		}
		
		s.close();
	}

	public void render(SpriteBatch batch, float x, float y, float width, float height) 
	{
		batch.draw(texture, x, y, width, height);
	}
}
