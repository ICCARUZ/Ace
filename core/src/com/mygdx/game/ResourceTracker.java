package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ResourceTracker extends HUDElement
{
	void init() 
	{
		this.x = 2;
		this.y = Gdx.graphics.getHeight() - 2;
	}

	void update() 
	{
		
	}

	void render(SpriteBatch batch) 
	{
		batch.begin();
		Ace.font.draw(batch, "" + Skirmish.getCurrentPlayer().resourceCount, x, y);
		batch.end();
	}	
}
