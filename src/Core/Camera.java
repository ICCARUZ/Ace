package Core;



import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

public class Camera 
{
	Map map;

	float x; // The x center of the camera (on the map)
	float y; // The y center of the camera (on the map)
	float speed = .256f; // Speed at which the camera is moved (Arrow Keys)
	
	int viewportWidth; // Window width
	int viewportHeight; // Window height
	
	int mapWidth; // The map's width in pixels
	int mapHeight; // The map's height in pixels
	
	int mapWidthTiles; // The map's width in tiles
	int mapHeightTiles; // The map's height in tiles
	
	int mapTileSize = 16; // The size of a tile in pixels (Ex: 32 => 32 * 32)
	
	int rx; 
	int ry;
	int sx = 0;
	int sy = 0;
	int tileRenderWidth =  mapWidthTiles; 
	int tileRenderHeight =  mapHeightTiles;
	
	Input input;
	
	int[] mousePos;
	int[] mousePosLast;
	
	public int[] cursorPos;
	float cursorSleep = 150;
	float cursorAsleep = 0;
	
	Player owner;
	
	public Camera(GameContainer gc, Map map)
	{
		this.map = map;
		
		this.viewportWidth = gc.getWidth();
		this.viewportHeight = gc.getHeight();
		
		this.mapTileSize = map.tileSize;		
		this.mapWidthTiles = map.width;
		this.mapHeightTiles = map.height;		
		this.mapWidth = mapWidthTiles * mapTileSize;
		this.mapHeight = mapHeightTiles * mapTileSize;
				
		this.x = mapWidth / 2;
		this.y = mapHeight /2;
		
		this.input = gc.getInput();
		
		mousePos = new int[2];
		mousePosLast = new int[2];
		
		cursorPos = new int[2];
		cursorPos = screenToBoard((int)x, (int)y);
		cursorAsleep = cursorSleep + 1;
	}	
	
	public Camera(GameContainer gc, Map board, Player player) 
	{
		this.map = board;
		this.mapWidthTiles = map.width;
		this.mapHeightTiles = map.height;		
		this.mapWidth = mapWidthTiles * mapTileSize;
		this.mapHeight = mapHeightTiles * mapTileSize;
				
		this.x = mapWidth / 2;
		this.y = mapHeight /2;
		
		this.input = gc.getInput();
		
		mousePos = new int[2];
		mousePosLast = new int[2];
		
		cursorPos = new int[2];
		cursorPos = screenToBoard((int)x, (int)y);
		cursorAsleep = cursorSleep + 1;
		
		this.owner = player;
	}

	public void update(GameContainer gc, int delta)
	{			
		processKBMInput(delta);
	}	
		
	public void processKBMInput(int delta)
	{
		mousePosLast[0] = mousePos[0];
		mousePosLast[1] = mousePos[1];
		
		mousePos[0] = input.getMouseX();
		mousePos[1] = input.getMouseY();
				
		cursorPos = screenToBoard(mousePos);	
		
		if(input.isMouseButtonDown(2))
		{
			x -= mousePos[0] - mousePosLast[0];
			y -= mousePos[1] - mousePosLast[1];
		}		
	}
	
	public boolean isMouseOnMap()
	{
		return mousePos != null && mousePos[0] >= rx && mousePos[1] >= ry && mousePos[0] < rx + mapWidth && mousePos[1] < ry + mapHeight;
	}

	public int[] screenToBoard(int x, int y)
	{		
		int[] result = {sx + (x - rx)/mapTileSize, sy + (y - ry)/mapTileSize};
		if(result[0] >= 0 && result[1] >= 0 && result[0] < mapWidthTiles && result[1] < mapHeightTiles)
			return result;
		else
			return null;
	}
	
	public int[] screenToBoard(int[] loc)
	{		
		int[] result = {sx + (loc[0] - rx)/mapTileSize, sy + (loc[1] - ry)/mapTileSize};
		
		if(result[0] >= 0 && result[1] >= 0 && result[0] < mapWidthTiles && result[1] < mapHeightTiles)
			return result;
		return null;
	}	
	
	public void drawOverCursor(Image image, Graphics g)
	{
		g.drawImage(image,  rx + cursorPos[0] * map.tileSize,  ry + cursorPos[1] * map.tileSize);
	}
	
	public void render(GameContainer gc, Graphics g)
	{
		/*      
    	 *	Parameters:
	     *   	rx - The x location to render at ("ON" SCREEN)
	     *   	ry - The y location to render at ("ON" SCREEN)
	     *   	sx - The x tile location to start rendering (in tiles) (CALCULATE)
	     *   	sy - The y tile location to start rendering (in tiles) (CALCULATE)
	     *   	width - The width of the section to render (in tiles) (CALCULATE)
	     *   	height - The height of the secton to render (in tiles) (CALCULATE)
		 */
		g.setColor(Color.gray);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		
		int[] start = {-rx / mapTileSize, -ry / mapTileSize};
		rx = (int)((gc.getWidth()/2) - x); 
		ry = (int)((gc.getHeight()/2) - y);
		tileRenderWidth =  gc.getWidth() / mapTileSize + 1; 
		tileRenderHeight =  gc.getHeight() / mapTileSize + 1;
		
		map.render(rx, ry, start[0], start[1], tileRenderWidth, tileRenderHeight);		
		
		// Draw night overlay
		/*
		g.setColor(new Color(80, 30, 255, 150));
		g.fillRect(rx, ry, map.width * mapTileSize, map.height * mapTileSize);
		*/
		
		if(isMouseOnMap())
		{
			g.setColor(Color.white);
			g.drawString(cursorPos[0] + ", " + cursorPos[1], 4, gc.getHeight() - 20);
		}
	}	
}
