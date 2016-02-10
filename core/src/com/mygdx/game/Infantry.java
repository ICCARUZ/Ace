package com.mygdx.game;

public class Infantry extends Unit
{	
	private static Animation[] idle;
	private static Animation[] runningUp;
	private static Animation[] runningDown;
	private static Animation[] runningRight;
	private static Animation[] runningLeft;
	
	private static boolean loaded = false;
	
	public Infantry(int ownerIndex)
	{		
		Infantry.load();
		this.cost = 1000;
		this.entityType = Entity.type.UNIT;
		this.ownerIndex = ownerIndex;
		this.name = "INFANTRY";
		this.unitType = Unit.UnitType.LAND;
		this.baseDamage = 55;
		this.speed = 4;
		this.currentHealth = 100;		
		this.animation = idle[ownerIndex];
		this.unitClass = Unit.UnitClass.INFANTRY;
	}
	
	public Infantry createInstance(int ownerIndex)
	{
		Infantry i = new Infantry(ownerIndex);
		return i;
	}
	
	public static void load()
	{
		if(!Infantry.loaded)
		{			
			idle = new Animation[Skirmish.playerCount];
			runningUp = new Animation[Skirmish.playerCount];
			runningDown = new Animation[Skirmish.playerCount];
			runningRight = new Animation[Skirmish.playerCount];
			runningLeft = new Animation[Skirmish.playerCount];
			
			for(int i = 0; i < Skirmish.playerCount; i++)
			{
				Infantry.idle[i] = new Animation("./res/art/units/infantry_idle.png", i, 24, 24, 5, 0);
				Infantry.runningUp[i] = new Animation("./res/art/units/infantry_run_up.png", i, 24, 24, 5, 0);
				Infantry.runningDown[i] = new Animation("./res/art/units/infantry_run_down.png", i, 24, 24, 5, 0);
				Infantry.runningRight[i] = new Animation("./res/art/units/infantry_run_right.png", i, 24, 24, 5, 0);
				Infantry.runningLeft[i] = new Animation("./res/art/units/infantry_run_left.png", i, 24, 24, 5, 0);
				
			}
			Infantry.loaded = true;
		}
	}
	
	public void setAnimation(Unit.Animations anim)
	{
		switch(anim)
		{
			case IDLE: this.animation = Infantry.idle[this.ownerIndex]; break;
			case RUN: this.animation = this.ownerIndex%2 == 0 ? Infantry.runningRight[this.ownerIndex] : Infantry.runningLeft[this.ownerIndex]; break;
			case RUNUP: this.animation = Infantry.runningUp[this.ownerIndex];break;
			case RUNDOWN: this.animation = Infantry.runningDown[this.ownerIndex];break;
			case RUNLEFT: this.animation = Infantry.runningLeft[this.ownerIndex]; break;
			case RUNRIGHT: this.animation = Infantry.runningRight[this.ownerIndex]; break;
		}
	}
}
