package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public abstract class Menu extends HUDElement
{
	public ArrayList<String> menuItems;
	public int selectedMenuItem;
	
	protected int padding = 4;
	protected int buttonPadding = 2;	
	protected int menuWidth = 100;
	protected int menuHeight;
	protected int buttonWidth = 92;
	protected int buttonHeight = 40;	
	
	public boolean open = false;
	public boolean isMouseOnMenu = true;
			
	public abstract void selectItem(int index);
	
	public String title;
	
	public int getMouseIndex(int mouseX, int mouseY)
	{		
		for(int i = 0; i < menuItems.size(); i++)
		{
			if(mouseX >= x && mouseX <= (x + menuWidth) && mouseY <= (y + menuHeight - padding) - (buttonHeight + buttonPadding)*i && mouseY >= (y + menuHeight - padding) - (buttonHeight + buttonPadding)*i - buttonHeight)
				return i;
		}
		
		return -1;
	}
	
	public void update()
	{						
		// Update the current selected menu item
		isMouseOnMenu = Gdx.input.getX() >= x + padding && Gdx.graphics.getHeight() - Gdx.input.getY() >= y && Gdx.input.getX() <= x + menuWidth - padding && Gdx.graphics.getHeight() - Gdx.input.getY() <= y + menuHeight;
				
		if(isMouseOnMenu)
			selectedMenuItem = getMouseIndex(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		else
			selectedMenuItem = -1;
				
		// If a menu option is selected, perform the selected action
		if(UserInput.isMouseButtonPressed(Buttons.LEFT) && isMouseOnMenu)
		{
			selectItem(selectedMenuItem);
		}
	}	
	
	public void render(SpriteBatch batch)
	{
		// Only draw the field menu if prompted
		if(!open)
			return;
		
		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);		
		HUD.shaper.begin();		
		HUD.shaper.set(ShapeType.Filled); // We draw filled shapes, no outline bullshit
		
		// Draw menu box
		HUD.shaper.setColor(0, 0, 0, .5f);
		HUD.shaper.rect(x, y, menuWidth, menuHeight);
		
		// Draw buttons 
		HUD.shaper.setColor(1, 1, 1, .5f);
		batch.setColor(Color.WHITE);
				
		for(int i = 0; i < menuItems.size(); i++)
		{
			HUD.shaper.rect(x + padding, (y+padding) + (buttonHeight + buttonPadding)*i, buttonWidth, buttonHeight);
		}		
		
		// Highlight selected element
		if(isMouseOnMenu)
		{
			if(selectedMenuItem >= 0)
			{
				HUD.shaper.setColor(1, 1, 1, .25f);
				HUD.shaper.rect(x + padding, y + menuHeight - padding - (buttonHeight + buttonPadding)*(selectedMenuItem) - buttonHeight, buttonWidth, buttonHeight);
			}
		}		
		
		// Draw title bg
		if(this.title != null)
		{
			HUD.shaper.setColor(0, 0, 0, .5f);
			HUD.shaper.rect(x, y + menuHeight, menuWidth, buttonHeight + buttonPadding + padding);
		}
		
		HUD.shaper.end();
		Gdx.gl.glDisable(GL30.GL_BLEND);
		
		// Draw button text labels
		batch.begin();		
		for(int i = 0; i < menuItems.size(); i++)
		{
			Ace.font.draw(batch, menuItems.get(i), x+padding*3, y + menuHeight - padding - (buttonHeight + padding)*i - buttonHeight/2 + Ace.font.getCapHeight()/2);
		}		
		
		// Draw title
		if(this.title != null)
		{
			Ace.font.draw(batch, this.title, x + padding*3, y + menuHeight - padding + (buttonHeight + padding) - buttonHeight/2 + Ace.font.getCapHeight()/2);
		}
		
		batch.end();
	}
}
