package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class UserInput 
{
	public static boolean isMouseButtonPressed(int keyCode)
	{
		return Gdx.input.justTouched() && Gdx.input.isButtonPressed(keyCode);
	}
	
	public static boolean isMouseButtonDown(int keyCode)
	{
		return Gdx.input.isButtonPressed(keyCode);
	}
	
	public static boolean isKeyPressed(int keyCode)
	{
		return Gdx.input.isKeyJustPressed(keyCode);
	}
	
	public static boolean isKeyDown(int keyCode)
	{
		return Gdx.input.isKeyPressed(keyCode);
	}
	
	public static int getMouseX()
	{
		return Gdx.input.getX();
	}
	
	public static int getMouseY()
	{
		return Gdx.input.getY();
	}
}
