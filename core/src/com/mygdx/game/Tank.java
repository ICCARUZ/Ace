package com.mygdx.game;

public class Tank extends Unit 
{
private static Animation[] idle;
	
	private static boolean loaded = false;
	
	public Tank(int ownerIndex)
	{		
		Tank.load();
		this.cost = 3000;
		this.entityType = Entity.type.UNIT;
		this.ownerIndex = ownerIndex;
		this.name = "TANK";
		this.unitType = Unit.UnitType.LAND;
		this.baseDamage = 85;
		this.speed = 6;
		this.currentHealth = 100;		
		this.animation = idle[ownerIndex];
	}
	
	public Tank createInstance(int ownerIndex)
	{
		Tank c = new Tank(ownerIndex);
		return c;
	}
	
	public static void load()
	{
		if(!Tank.loaded)
		{			
			idle = new Animation[Skirmish.playerCount];
						
			for(int i = 0; i < Skirmish.playerCount; i++)
			{
				Tank.idle[i] = new Animation("./res/art/units/Tank.png", i, 24, 24, 0, 0, 0);							
			}
			Tank.loaded = true;
		}
	}
	
	public void setAnimation(Unit.Animations anim)
	{
		switch(anim)
		{
			default: this.animation = Tank.idle[this.ownerIndex]; break;
		}
	}
}
