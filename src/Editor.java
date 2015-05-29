

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import UI.TileSelector;

public class Editor extends BasicGameState
{
	public static Map activeMap; 
	Camera camera;
	ArrayList<int[]> selectedTiles;	
	String filename;
	String mapname;
	Tool activeTool = Tool.COLLISION;
	Image toolImage;
	TileSelector selector;
	int[] mouse;
	
	public enum Tool
	{
		COLLISION, SELECT;
		
		public Tool getNext()
		{
			return values()[(ordinal() + 1) % values().length];
		}
	}

	public void init(GameContainer gc, StateBasedGame game) throws SlickException
	{
		openMap("testmap.txt");
		this.camera = new Camera(gc, activeMap);
		this.selectedTiles = new ArrayList<int[]>();
		this.selector = new TileSelector(gc);
	}
	
	public void enter(GameContainer gc, StateBasedGame game) throws SlickException
	{
		gc.getInput().clearMousePressedRecord();
		gc.getInput().clearKeyPressedRecord();
	}

	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException
	{
		getMouseInput(gc, gc.getInput());
		getKeyboardInput(gc, gc.getInput(), game);
		camera.update(gc, delta);
		if(selector.isVisible)
			selector.update(gc,  delta);
	}
	
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException
	{
		camera.render(gc, g);		
		
		if(selector.selection >= 0 && camera.isMouseOnMap())
		{
			g.setColor(Color.green);
			camera.drawOverCursor(selector.selectionImage, g);
		}		

		if(selector.isVisible)
			selector.render(gc,  g);
		
		if(camera.isMouseOnMap())
		{
			g.setColor(Color.white);
			g.drawString(camera.cursorPos[0] + ", " + camera.cursorPos[1], 5, gc.getHeight() - 20);
		}
	}	
	
	public void getMouseInput(GameContainer gc, Input input)
	{
		if(input.isMouseButtonDown(0) && (!selector.isVisible || !selector.isMouseOnWindow()) && camera.isMouseOnMap())
		{
			activeMap.tiles[camera.cursorPos[0]][camera.cursorPos[1]] = selector.selection;
		}
		
		if(input.isMouseButtonDown(1) && camera.isMouseOnMap())
		{
			selector.select(activeMap.tiles[camera.cursorPos[0]][camera.cursorPos[1]]);
		}
	}
	
	public void getKeyboardInput(GameContainer gc, Input input, StateBasedGame game)
	{		
		// SAVE MAP
		if(input.isKeyPressed(Input.KEY_S))
		{
			if(input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL))
			{
				saveMap();
			}
		}
		
		if(input.isKeyPressed(Input.KEY_ESCAPE))
		{
			game.enterState(2);
		}
	}
		
	public void openMap(String filename) throws SlickException
	{
		try 
		{
			activeMap = new Map("res/maps/" + filename);
			this.filename = filename;
			this.mapname = activeMap.name;
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void saveMap()
	{
		try 
		{
			Writer fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("res/maps/" + filename), "utf-8"));
			fileWriter.write("[properties]\n");
			fileWriter.write("name: \"" + mapname + "\"\n");
			fileWriter.write("width: " + activeMap.width + "\n");
			fileWriter.write("height: " + activeMap.height + "\n");
			fileWriter.write("fog: " + (activeMap.fogEnabled ? "1\n" : "0\n"));
			
			fileWriter.write("\n[terrain]\n");
			for(int y = 0; y < activeMap.height; y++)
			{
				for(int x = 0; x < activeMap.width; x++)
				{
					fileWriter.write( activeMap.tiles[x][y] + " ");
				}
				fileWriter.write("\n");
			}
			
			fileWriter.write("\n[structures]\n");
			for(int y = 0; y < activeMap.height; y++)
			{
				for(int x = 0; x < activeMap.width; x++)
				{
					fileWriter.write(activeMap.structures[x][y] + " ");
				}
				fileWriter.write("\n");
			}
			
			fileWriter.write("\n[objects]\n");
			for(int y = 0; y < activeMap.height; y++)
			{
				for(int x = 0; x < activeMap.width; x++)
				{
					if(activeMap.mapObjects[x][y] == null)
					{
						fileWriter.write("-1 ");
					}					
				}
				fileWriter.write("\n");
			}
			
			fileWriter.close();
		} 
		catch (UnsupportedEncodingException | FileNotFoundException e){e.printStackTrace();} catch (IOException e){e.printStackTrace();}		
	}	

	public int getID() 
	{
		return 3;
	}	
}
