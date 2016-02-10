package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Entity 
{
	public int x, y;
	public int ownerIndex;
	public type entityType;
	public Animation animation;
	
	public static enum team
	{
		RED,
		BLUE,
		GREEN,
		PURPLE
	}	
	
	public static enum type
	{
		STRUCTURE,
		UNIT
	}
	
	public void render(SpriteBatch batch, float rx, float ry)
	{
		this.animation.render(batch, rx, ry);
		//batch.draw(this.currentTexture, rx, ry);		
	}
}
