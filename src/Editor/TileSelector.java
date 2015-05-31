package Editor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class TileSelector 
{
	// On-screen tool for selecting the tile to place on the map when drawing
	public boolean isVisible = true;
	Input input;
	int mousex, mousey;
	int mousepos;
	
	int rx, ry; // render x and y
	int rw, rh; // render width and height
	
	ArrayList<String> terrains;
	Image[] terrainImages;
	Image[] buildingImages;
	Image[] unitImages;
	
	public int selection;
	public Image selectionImage;
	
	int padding = 12;
	int tilesize = 16;
	
	enum ent 
	{
		terrain,
		unit,
		building
	}
	
	ent selectionType; // Tells us which type of item has been selected for placement
	int windowStatus; // Tells us which set of items to display on the selector
	
	public TileSelector(GameContainer gc) throws SlickException
	{
		try {loadTiles();} catch (FileNotFoundException e) {e.printStackTrace();}
		windowStatus = 0;
		selection = 0;
		selectionImage = terrainImages[0];
		rx = 0;
		ry = 0;
		rw = gc.getWidth() / 4;
		rh = gc.getHeight();		
	}
	
	public boolean isMouseOnWindow()
	{
		return mousex <= rw;
	}
	
	public void select(int target)
	{
		this.selection = target;
		this.selectionImage = terrainImages[selection];
	}
	
	public void loadTiles() throws FileNotFoundException, SlickException
	{
		File current;
		
		// Import terrain images
		current = new File("res/terrain/terrains_active.txt");
		terrains = new ArrayList<String>();
		Scanner s = new Scanner(current);
		int count = 0;
		while(s.hasNextLine())
		{
			terrains.add(s.nextLine());
			count++;
		}
		s.close();
		terrainImages = new Image[count];		
				
		for(int i = 0; i < terrains.size(); i++)
		{
			try
			{
				terrainImages[i] = new Image("res/art/terrain/" + terrains.get(i) + ".png");
			} 
			catch (RuntimeException e)
			{
				terrainImages[i] = new Image("res/art/terrain/error.png");
			}
		}
		
		// TODO: Import building images
		/*
		for(int i = 0; i < Structure.allStructures.size(); i++)
		{
			
		}
		*/
		buildingImages = new Image[0];
		
		
		// TODO: Import unit images
		/*
		for(int i = 0; i < Unit.allUnits.size(); i++)
		{
			
		}
		*/
		unitImages = new Image[0];
	}
	
	public void drawBoxContents(Graphics g)
	{
		float x = padding;
		float y = padding + 20;
		int i = 0;
		
		Image[] toDraw = {};
		
		switch(windowStatus)
		{
			case 0: toDraw = terrainImages; break;
			case 1: toDraw = buildingImages; break;
			case 2: toDraw = unitImages; break;
			default: break;
		}
		
		while(i < toDraw.length)
		{
			if(x < rw - tilesize)
			{
				if(selection == i)
				{
					g.setColor(Color.gray);
					g.fillRect(x - padding/2, y - padding/2, tilesize + padding, tilesize + padding);
				}
				
				g.drawImage(toDraw[i], x, y);
				i++;
				x += tilesize + padding;
			}
			else
			{
				x = padding;
				y += tilesize + padding;
			}
		}
	}
	
	public void render(GameContainer gc, Graphics g)
	{
		// Draw window
		g.setColor(new Color(0, 0, 0, 128));
		g.fillRect(rx, ry, rw, rh);
		
		// Draw selected element title
		String title = "";
		if(windowStatus == 0)
			title = "TERRAIN";
		else
		if(windowStatus == 1)
			title = "STRUCTURES";
		else
		if(windowStatus == 2)
			title = "UNITS";
		
		g.setColor(Color.white);
		g.drawString(title, 32, 2);
		
		// Draw proper box for selected element type
		drawBoxContents(g);
	}
	
	public void update(GameContainer gc, int delta)
	{
		input = gc.getInput();
		if(input.isMousePressed(0))
		{
			mousex = input.getMouseX();
			mousey = input.getMouseY();
			if(mousex >= 0 && mousex <= rw && mousey >= 20 && mousey <= rh)
			{
				// Mouse1 clicked within selector, see if we've clicked on anything
				mousepos = (mousex - padding/2)/(tilesize + padding) + (mousey - padding/2 - 20)/(tilesize + padding) * ((rw - padding) / (tilesize + padding));
				if(mousepos < terrains.size())
				{
					selection = mousepos;
					selectionImage = terrainImages[selection];
				}
			}
		}
		
		// TOGGLE DISPLAYED ELEMENTS
		if(input.isKeyPressed(Input.KEY_TAB))
		{
			windowStatus++;
			windowStatus %= 3;
		}
	}
}
