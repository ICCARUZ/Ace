package Core;



import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import Structures.*;
import Units.*;

public class Map 
{	
	public Unit[][] mapObjects;
	public Structure[][] mapBuildings;
	
	public int[][] tiles;
	public int[][] structures;
	
	public int[][] cost;
	
	public Terrain[] terrains;
	public ArrayList<Structure> buildings;
	public ArrayList<Unit> units;
	
	protected SpriteSheet terrainSheet;
	
	Image error;
	
	public int width, height;
	public int tileSize = 16;

	public String name;
	public String filename;
	
	File mapSource;	
	
	public boolean fogEnabled;

	public Map(String filename) throws FileNotFoundException, SlickException
	{
		this.mapSource = new File(filename);
		this.filename = filename.substring(10, filename.length());
		buildMap();
	}	
	
	public void buildProperties(Scanner s)
	{
		String property;
		int value;
		int set = 0;
		
		while(s.hasNext() && set < 4)
		{
			property = s.next();
			if(property.equalsIgnoreCase("name:"))
			{
				this.name = s.nextLine().trim().replace("\"", "");
				set++;
			}
			else
			if(property.equalsIgnoreCase("width:"))
			{
				value = s.nextInt();
				this.width = value;
				set++;
			}
			else
			if(property.equalsIgnoreCase("height:"))
			{
				value = s.nextInt();
				this.height = value;
				set++;
			}
			else
			if(property.equalsIgnoreCase("fog:"))
			{
				value = s.nextInt();
				this.fogEnabled = (value == 1);
				set++;
			}
		}
	}
	
	public void printTerrain()
	{
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				System.out.print( tiles[x][y] + " ");
			}
			System.out.print("\n");
		}
		System.out.println();
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
					cost[x][y] = terrains[tiles[x][y]].movecost + terrains[tiles[x][y]].movepenalty;
				}
			}
		}
	}
	
	public void buildStructures(Scanner s)
	{
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				structures[x][y] = -1;
			}
		}
		
		while(!s.hasNextInt() && s.hasNext())
			s.next();
		for(int x = 0; x < width; x++)
		{
			for(int y= 0; y < height; y++)
			{
				if(s.hasNextInt())
					structures[x][y] = s.nextInt();
			}
		}
	}
	
	public void buildObjects(Scanner s)
	{
	}	
	
	public void loadTerrainData() throws FileNotFoundException, SlickException
	{
		File terrainFile = new File("res/terrain/terrains_active.txt");
		ArrayList<String> terrainNames = new ArrayList<String>();
		Scanner s = new Scanner(terrainFile);
		int count = 0;
		while(s.hasNextLine())
		{
			terrainNames.add(s.nextLine());
			count++;
		}
		s.close();
		
		error = new Image("res/art/terrain/error.png");		
		terrainSheet = new SpriteSheet("res/art/terrain/terrains.png", tileSize, tileSize);
		terrains = new Terrain[count];			
		
		for(int i = 0; i < terrainNames.size(); i++)
		{
			terrains[i] = new Terrain(terrainNames.get(i));
		}		
	}
	
	public void loadBuildingData()
	{		
		Structure.initStructures();
		buildings = new ArrayList<Structure>();
		buildings.add(new City());
	}
	
	public void loadUnitData()
	{
		
	}
	
	public void buildMap() throws FileNotFoundException, SlickException
	{		
		loadTerrainData();
		loadBuildingData();
		Scanner s = new Scanner(mapSource);
		
		buildProperties(s);
		
		this.tiles = new int[width][height];
		this.cost = new int[width][height];
		this.mapObjects = new Unit[width][height];
		structures = new int[width][height];	
		buildTerrain(s);
		buildStructures(s);
		buildObjects(s);
		
		s.close();
	}
		
	public void buildMapDisplay(Scanner scanner)
	{
		int current;
		int x = 0;
		int y = 0;
		while(scanner.hasNextInt() && y < tiles[0].length)
		{
			current = scanner.nextInt();
			tiles[x][y] = current;
						
			if(x < tiles.length - 1)
			{
				x++;
			}
			else
			{
				x = 0;
				y++;
			}
		}
	}
	
	// Is the provided location a valid map location?
	public boolean contains(int x, int y)
	{
		return (x >= 0 && y >= 0 && x < width && y < height);
	}
	
	public Unit get(int x, int y)
	{
		if(!this.contains(x, y))
			return null;
		if(mapObjects[x][y] != null)
			return this.mapObjects[x][y];
		else
			return null;
	}
	
	public void add(Unit obj, int x, int y)
	{
		mapObjects[x][y] = obj;
		obj.board = this;
		obj.x = x;
		obj.y = y;
	}
	
	public int[] find(Object obj)
	{
		for(int x = 0; x < mapObjects.length; x++)
		{
			for(int y = 0; y < mapObjects[0].length; y++)
			{
				if(mapObjects[x][y] == obj)
					return new int[] {x, y};
			}
		}
		return null;
	}
	
	public void drawUnits(GameContainer gc, Graphics g, int rx, int ry, int width, int height)
	{
		for(int x = 0; x < tiles.length; x++)
		{
			for(int y = 0; y < tiles[0].length; y++)
			{											
				// Draw units				
				if(mapObjects[x][y] != null)
				{					
					mapObjects[x][y].render(rx, ry, 0, 0);
				}					
			}
		}
	}
	
	public void render(int rx, int ry, int sx, int sy, int width, int height)
	{		
		terrainSheet.startUse();
		
		for(int x = sx; x < sx + width; x++)
		{
			for(int y = sy; y < sy + height; y++)
			{				
				if(x >= this.width || y >= this.height || x < 0 || y < 0)
					continue;
				
				if(tiles[x][y] != -1)
				{
					terrainSheet.getSprite(tiles[x][y]%16, tiles[x][y]/16).drawEmbedded(rx + x * tileSize, ry + y * tileSize, tileSize, tileSize);
				}
				else
				{
					error.drawEmbedded(rx + x * tileSize, ry + y * tileSize, tileSize, tileSize);
				}
				
				// TODO: Draw structures
								
			}
		}
		
		terrainSheet.endUse();
	}
	
	public void renderOld(GameContainer gc, Graphics g, int rx, int ry, int width, int height)
	{		
		terrainSheet.startUse();
		
		for(int x = 0; x < this.width; x++)
		{
			for(int y = 0; y < this.height; y++)
			{
				// Draw tiles
				if(tiles[x][y] != -1)
				{
					terrainSheet.getSprite(tiles[x][y]%16, tiles[x][y]/16).drawEmbedded(rx + x * tileSize, ry + y * tileSize, tileSize, tileSize);
				}
				else
				{
					g.drawImage(error, rx + x * tileSize, ry + y * tileSize);
				}
				
				// TODO: Draw structures
								
			}
		}
		
		terrainSheet.endUse();
	}
	
	public void highlightTile(int[] tile, Graphics g, Color color, int rx, int ry)
	{
		g.setColor(color);
		g.fillRect(rx + tile[0]*tileSize, ry + tile[1]*tileSize, tileSize, tileSize);
	}
	
	public void highlightTiles(ArrayList<int[]> spaces, Graphics g, Color color, int rx, int ry)
	{
		for(int[] tile : spaces)
		{
			highlightTile(tile, g, color, rx, ry);
		}
	}
	

	public void drawTerrainInfoWindow(Graphics g, int x, int y)
	{
		//Graphics g = new Graphics();		
		
		//DRAW WINDOW
		g.setColor(new Color(50, 50, 50, 150));
		g.fillRect(Gameplay.gc.getWidth() - 96, Gameplay.gc.getHeight() - 44, 95, 43);	
						
		// Draw terrain sprite
		terrainSheet.getSprite(tiles[x][y]%16, tiles[x][y]/16).draw(Gameplay.gc.getWidth()-93, Gameplay.gc.getHeight() - 40, tileSize, tileSize);
		
		// Draw terrain name		
		g.setColor(Color.white);
		g.drawString(terrains[tiles[x][y]].name, Gameplay.gc.getWidth() - 75, Gameplay.gc.getHeight() - 40);
		
		// Defense
		g.drawImage(Terrain.def, Gameplay.gc.getWidth() - 93, Gameplay.gc.getHeight() - 21);
		g.drawString(":" + terrains[tiles[x][y]].defense, Gameplay.gc.getWidth() - 80, Gameplay.gc.getHeight() - 21);
		
		// Vision
		g.drawImage(Terrain.vis, Gameplay.gc.getWidth() - 50, Gameplay.gc.getHeight() - 21);
		g.drawString(":" + terrains[tiles[x][y]].vision, Gameplay.gc.getWidth() - 37, Gameplay.gc.getHeight() - 21);
	}
		
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}

	public void move(Unit u, int[] pos) 
	{
		if(pos[0] >= 0 && pos[0] < width && pos[1] >= 0 && pos[1] < height)
		{
			if(mapObjects[pos[0]][pos[1]] == null)				
			{
				int oldx = u.getX();
				int oldy = u.getY();
				mapObjects[pos[0]][pos[1]] = u;
				mapObjects[oldx][oldy] = null;
				u.x = pos[0];
				u.y = pos[1];
			}
		}
	}
	
	public void remove(Unit u)
	{
		mapObjects[u.x][u.y] = null;
	}
}
