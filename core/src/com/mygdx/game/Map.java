package com.mygdx.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map 
{
	private Unit[][] mapUnits;	
	private Structure[][] mapStructures;
	private int[][] tiles;
	private int[][] moveCost;
	
	public int width, height;
	private int tileSize = 16;
	private String mapName = "";
	public boolean fogEnabled;
	
	private int playerCount = 2;
	
	private Texture highlight;
	
	float timer = 0;
		
	public Map(String filename) throws FileNotFoundException
	{
		highlight = new Texture("./res/art/ui/highlight.png");
		File mapSource = new File(filename);
		buildMap(mapSource);
	}
	
	public void addUnit(Unit u, int x, int y)
	{
		if(!this.contains(x, y))
			return;
		
		mapUnits[x][y] = u;
		u.x = x;
		u.y = y;
	}
	
	public void addUnit(Unit u, int x, int y, boolean canAct)
	{
		if(!this.contains(x, y))
			return;
		
		mapUnits[x][y] = u;
		u.x = x;
		u.y = y;
		u.canMove = canAct;
		u.canAct = canAct;
	}
	
	public Unit getUnit(int x, int y)
	{
		if(x >=0 && x < width && y >= 0 && y < height)
			return this.mapUnits[x][y];
		return null;
	}
	
	public Structure getStructure(int x, int y)
	{
		if(!this.contains(x, y))
			return null;
		return mapStructures[x][y];
	}
	
	public Terrain getTerrain(int x, int y)
	{
		if(this.contains(x, y))
		{
			int i = tiles[x][y];
			if(i < Terrain.terrainList.length)
				return Terrain.terrainList[i];
			else
				return mapStructures[x][y].getTerrain();
		}
		return null;
	}
	
	public void buildMap(File mapSource) throws FileNotFoundException
	{
		Terrain.loadTerrains();
		Structure.loadStructures();
		
		Scanner s = new Scanner(mapSource);		
		buildProperties(s);
		
		this.tiles = new int[width][height];
		this.moveCost = new int[width][height];
		this.mapUnits = new Unit[width][height];
		this.mapStructures = new Structure[width][height];
		buildTerrain(s);
		s.close();		
	}
	
	public void buildProperties(Scanner s)
	{
		String property;
		int set = 0;
		
		while(s.hasNext() && set < 4)
		{
			property = s.next();
			
			switch(property)
			{
				case "name:" : this.mapName = s.nextLine().trim().replace("\"", ""); set++; break;					
				case "width:" :  this.width = s.nextInt(); set++; break;					
				case "height:" :  this.height = s.nextInt(); set++; break;					
				case "fog:" :  this.fogEnabled = (s.nextInt() == 1); set++; break;
				default: break;
			}
		}
	}
	
	public void buildTerrain(Scanner s)
	{
		while(!s.hasNextInt() && s.hasNext())
		{
			s.next();
		}
		
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				if(s.hasNextInt())
				{
					tiles[x][y] = s.nextInt();
					if(tiles[x][y] < Terrain.terrainList.length)
					{
						moveCost[x][y] = Terrain.terrainList[tiles[x][y]].moveCost;
					}
					else
					{						
						int i = tiles[x][y] - Terrain.terrainList.length;
						mapStructures[x][y] = Structure.structures.get(i/(playerCount + 1)).createInstance(i%(playerCount + 1) - 1);
						moveCost[x][y] = 0; // TODO: CHANGE
					}
				}
			}
		}
	}
	
	public void highlightTiles(SpriteBatch batch, float rx, float ry, int[][] toHighlight)
	{
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				if(toHighlight[x][y] >= 0)
				{
					batch.draw(this.highlight, rx + x * tileSize, ry - (y+1) * tileSize);
				}
			}
		}
	}
	
	public void render(SpriteBatch batch, float rx, float ry, int sx, int sy, int width, int height)
	{
		batch.begin();
		
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				if(x >= this.width || y >= this.height || x < 0 || y < 0)
					continue;
				if(tiles[x][y] < 0)
					continue;
				
				// Draw terrains/structures
				if(mapStructures[x][y] == null)
				{
					Terrain.terrainList[tiles[x][y]].render(batch, rx + x * tileSize, ry - (y+1) * tileSize, tileSize, tileSize);
				}
				else
				{
					mapStructures[x][y].render(batch, rx + x * tileSize, ry - (y+1) * tileSize);
				}
			}
			
			// Draw units
			for(int xi = 0; xi < width; xi++)
			{
				if(xi >= this.width || y >= this.height || xi < 0 || y < 0)
					continue;
				
				if(mapUnits[xi][y] != null)
				{
					mapUnits[xi][y].render(batch, rx + xi * tileSize, ry - (y+1) * tileSize);
				}		
			}
		}
		
		// Draw tile highlights and move arrows
		if(Skirmish.getCurrentPlayer().getSelection() != null)
		{
			timer += Gdx.graphics.getDeltaTime() * 5f;
			timer = timer%(2f*3.14f);
			batch.setColor(1f, 1f, 1f, .6f + (float)Math.sin(timer)/10f);
					
			if(Skirmish.getCurrentPlayer().getSelection().canMove)
			{
				highlightTiles(batch, rx, ry, Skirmish.getCurrentPlayer().getSelection().reachable);
			}
			else
			if(Skirmish.getCurrentPlayer().getSelection().canAct)
			{
				highlightTiles(batch, rx, ry, Skirmish.getCurrentPlayer().getSelection().getAttackable());
			}
					
			batch.setColor(Color.WHITE);
		}
		
		batch.end();
	}
	
	public int getTileSize()
	{
		return this.tileSize;
	}
	
	public String getName()
	{
		return this.mapName;
	}
	
	public void removeUnit(int x, int y)
	{
		this.mapUnits[x][y] = null;
	}
	
	public int getDistance(int x1, int y1, int x2, int y2)
	{
		return Math.abs(x1 - x2 + y1 - y2);
	}
	
	public boolean contains(int x, int y)
	{
		return x >=0 && x < width && y >= 0 && y < height;
	}
	
	public void moveUnit(int x, int y, int targetX, int targetY)
	{
		Unit u = mapUnits[x][y];
		mapUnits[x][y] = null;
		mapUnits[targetX][targetY] = u;		
	}
}
