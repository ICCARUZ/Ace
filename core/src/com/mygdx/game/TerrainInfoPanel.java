package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class TerrainInfoPanel extends HUDElement
{	
	public float width = 100;
	public float height = 50;
	public Terrain currentTerrain;
	public Structure currentStructure;
	
	private Texture defenseTexture;
	private Texture visionTexture;
	
	private int[] hoverLocation;
	
	void init() 
	{
		this.defenseTexture = new Texture("./res/art/ui/defense.png");
		this.visionTexture = new Texture("./res/art/ui/vision.png");
		
		this.x = Gdx.graphics.getWidth() - 3 - width;	
		this.y = 3;
	}

	void update() 
	{
		this.hoverLocation = Skirmish.getCurrentPlayer().camera.screenToBoard(UserInput.getMouseX(), UserInput.getMouseY());
		if(this.hoverLocation == null)
			return;
		
		this.currentStructure = Skirmish.map.getStructure(hoverLocation[0], hoverLocation[1]);
		if(this.currentStructure == null)
		{
			this.currentTerrain = Skirmish.map.getTerrain(hoverLocation[0], hoverLocation[1]);
			this.height = 50;
		}
		else
		{
			this.currentTerrain = this.currentStructure.getTerrain();
			this.height = 75;
		}
	}

	void render(SpriteBatch batch) 
	{
		if(this.currentTerrain == null)
			return;
		
		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		
		HUD.shaper.begin();
		
		// Draw panel
		HUD.shaper.set(ShapeType.Filled);
		HUD.shaper.setColor(0, 0, 0, .5f);		
		HUD.shaper.rect(x, y, width, height);
		
		// Draw HP bar for building if present
		if(currentStructure != null)
		{
			// Draw unit HP bar
			HUD.shaper.setColor(Color.RED);
			HUD.shaper.rect(x + 3, y + height - 48, width - 6, 20);
			HUD.shaper.setColor(Color.GREEN);
			HUD.shaper.rect(x + 3, y + height - 48, (width - 6) * currentStructure.currentHealth/Structure.maxHealth, 20);
		}
		
		HUD.shaper.end();
		
		// Draw images (terrain image, defense and vision images)
		batch.begin();
		if(this.currentStructure == null)
		{
			batch.draw(currentTerrain.texture, x + 3, y + height - 22);
		}
		else
		{
			// Draw image and HP text for structure
			this.currentStructure.animation.render(batch, x + 3, y + height - 22);
			Ace.font.draw(batch, "HP: " + (int)(currentStructure.currentHealth), x + 24, y + height - 32);
		}
		batch.draw(defenseTexture, x + 3, y + 3);
		batch.draw(visionTexture, x + 50, y + 3);
		
		// Draw text (terrain name, defense and vision values)
		Ace.font.draw(batch, currentTerrain.name.toUpperCase(), x + 24, y + height - 9);
		Ace.font.draw(batch, ": " + currentTerrain.defense, x + 22, y + 6 + Ace.font.getCapHeight());
		Ace.font.draw(batch, ": +" + currentTerrain.bonusVision, x + 70, y + 6 + Ace.font.getCapHeight());
		
		batch.end();
	}	
}
