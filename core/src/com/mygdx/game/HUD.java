package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HUD 
{
	public static ArrayList<HUDElement> elements;	
	public static ShapeRenderer shaper;
	
	public static void init()
	{
		// Instantiate shape renderer
		HUD.shaper = new ShapeRenderer();
		HUD.shaper.setAutoShapeType(true);
		
		elements = new ArrayList<HUDElement>();	
		elements.add(new ResourceTracker());
		elements.add(new UnitInfoPanel());
		elements.add(new TerrainInfoPanel());
		elements.add(new FieldMenu());
		elements.add(new BuildMenu());
		
		for(HUDElement element : elements)
		{
			element.init();
		}
	}	
	
	public static void add(HUDElement elem)
	{
		elements.add(elem);
	}
	
	public static void remove(HUDElement elem)
	{
		elements.remove(elem);
	}
	
	public static void update()
	{				
		for(HUDElement element : elements)
		{
			element.update();
		}
	}
	
	public static void render(SpriteBatch batch)
	{		
		for(HUDElement element : elements)
		{
			element.render(batch);
		}		
	}	
}
