package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class HUDElement
{
	float x, y;
	
	abstract void init();
	abstract void update();
	abstract void render(SpriteBatch batch);
}
