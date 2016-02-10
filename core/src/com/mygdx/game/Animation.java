package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation 
{
	public static ArrayList<Animation> animations;
	public float frameProgress = 0;
	public TextureRegion[][] spriteSheet;
	
	public int frameWidth = 16;
	public int frameHeight = 16;
	public int frameCount = 1;
	public float fps = 4;
	public int anchorX = 0;
	public int anchorY = 0;
	
	public Animation(String filepath)
	{
		if(animations == null)
		{
			animations = new ArrayList<Animation>();
		}
		this.spriteSheet = TextureRegion.split(new Texture(filepath), frameWidth, frameHeight);
		this.frameCount = this.spriteSheet[0].length * this.spriteSheet.length;		
		animations.add(this);
	}
	
	public Animation(String filepath, int row, int width, int height)
	{
		if(animations == null)
		{
			animations = new ArrayList<Animation>();
		}
		this.frameWidth = width;
		this.frameHeight = height;		
		TextureRegion t = new TextureRegion(new Texture(filepath));
		t.setRegion(0, row*height, t.getRegionWidth(), height);
		this.spriteSheet = t.split(width, height);
		this.frameCount = this.spriteSheet[0].length * this.spriteSheet.length;		
		animations.add(this);
	}
	
	public Animation(String filepath, int row, int width, int height, int anchorX, int anchorY)
	{
		if(animations == null)
		{
			animations = new ArrayList<Animation>();
		}
		this.frameWidth = width;
		this.frameHeight = height;		
		TextureRegion t = new TextureRegion(new Texture(filepath));
		t.setRegion(0, row*height, t.getRegionWidth(), height);
		this.spriteSheet = t.split(width, height);
		this.frameCount = this.spriteSheet[0].length * this.spriteSheet.length;		
		this.anchorX = anchorX;
		this.anchorY = anchorY;
		animations.add(this);
	}
	
	public Animation(String filepath, int row, int width, int height, int anchorX, int anchorY, int fps)
	{
		if(animations == null)
		{
			animations = new ArrayList<Animation>();
		}
		this.frameWidth = width;
		this.frameHeight = height;		
		TextureRegion t = new TextureRegion(new Texture(filepath));
		t.setRegion(0, row*height, t.getRegionWidth(), height);
		this.spriteSheet = t.split(width, height);
		this.frameCount = this.spriteSheet[0].length * this.spriteSheet.length;		
		this.anchorX = anchorX;
		this.anchorY = anchorY;
		this.fps = fps;
		animations.add(this);
	}
	
	public Animation(TextureRegion sheet)
	{
		if(animations == null)
		{
			animations = new ArrayList<Animation>();
		}
		this.spriteSheet = sheet.split(frameWidth, frameHeight);
		this.frameCount = this.spriteSheet[0].length * this.spriteSheet.length;
		animations.add(this);
	}
	
	public TextureRegion getCurrentFrame()
	{
		return this.spriteSheet[(int) ((this.frameProgress/(this.spriteSheet[0].length)))][(int) (this.frameProgress%(this.spriteSheet[0].length))];
	}
	
	public static void updateAll()
	{
		if(animations == null)
			return;
		
		for(Animation a : animations)
			a.update();
	}
	
	public void update()
	{
		this.frameProgress += Gdx.graphics.getRawDeltaTime() * fps;
		this.frameProgress %= this.frameCount;
	}
	
	public void render(SpriteBatch batch, float x, float y)
	{
		batch.draw(this.getCurrentFrame(), x-anchorX, y-anchorY);
	}
	
	public void render(SpriteBatch batch, float x, float y, float width, float height)
	{
		batch.draw(this.getCurrentFrame(), x-anchorX, y-anchorY, width, height);
	}
	
	public void setSpeed(float fps)
	{
		this.fps = fps;
	}
	
	public void setFrameSize(int x, int y)
	{
		this.frameWidth = x;
		this.frameHeight = y;
	}
}
