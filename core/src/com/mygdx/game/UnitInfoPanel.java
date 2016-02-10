package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class UnitInfoPanel extends HUDElement
{
	private Unit currentUnit;
	private float width = 100, height = 50;
	private int[] hoverLocation;
	
	void init() 
	{
		this.x = Gdx.graphics.getWidth() - 3 - width;
		this.y = Gdx.graphics.getHeight() - 3 - height;
	}

	void update() 
	{		
		this.hoverLocation = Skirmish.getCurrentPlayer().camera.screenToBoard(UserInput.getMouseX(), UserInput.getMouseY());
		if(hoverLocation != null)
			this.currentUnit = Skirmish.map.getUnit(hoverLocation[0], hoverLocation[1]);		
		else
			this.currentUnit = null;
	}

	void render(SpriteBatch batch) 
	{
		if(this.currentUnit == null)
			return;
		
		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		
		HUD.shaper.begin();
		
		// Draw panel
		HUD.shaper.set(ShapeType.Filled);
		HUD.shaper.setColor(0, 0, 0, .5f);
		HUD.shaper.rect(x, y, width, height);
		
		// Draw unit HP bar
		HUD.shaper.setColor(Color.RED);
		HUD.shaper.rect(x + 3, y + height - 23, width - 6, 20);
		HUD.shaper.setColor(Color.GREEN);
		HUD.shaper.rect(x + 3, y + height - 23, (width - 6) * currentUnit.currentHealth/Unit.maxHealth, 20);
		
		HUD.shaper.end();		
		
		batch.begin();
		
		// Draw unit sprite
		batch.draw(this.currentUnit.animation.getCurrentFrame(), x + 4 - this.currentUnit.animation.anchorX, y + 6 - this.currentUnit.animation.anchorY);
		
		// Draw text (unit name, hp value)
		Ace.font.draw(batch, currentUnit.name, x + 24, y + 8 + Ace.font.getCapHeight());
		Ace.font.draw(batch, "HP: " + (int)(currentUnit.currentHealth/10), x + 24, y + height - 7);
		
		batch.end();
	}	
}
