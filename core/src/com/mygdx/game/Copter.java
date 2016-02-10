package com.mygdx.game;

public class Copter extends Unit
{
	private static Animation[] idle;
	
	private static boolean loaded = false;
	
	public Copter(int ownerIndex)
	{		
		Copter.load();
		this.cost = 3000;
		this.entityType = Entity.type.UNIT;
		this.ownerIndex = ownerIndex;
		this.name = "COPTER";
		this.unitType = Unit.UnitType.AIR;
		this.baseDamage = 85;
		this.speed = 6;
		this.currentHealth = 100;		
		this.animation = idle[ownerIndex];
	}
	
	public Copter createInstance(int ownerIndex)
	{
		Copter c = new Copter(ownerIndex);
		return c;
	}
	
	public static void load()
	{
		if(!Copter.loaded)
		{			
			idle = new Animation[Skirmish.playerCount];
						
			for(int i = 0; i < Skirmish.playerCount; i++)
			{
				Copter.idle[i] = new Animation("./res/art/units/copter.png", i, 24, 24, 0, 0, 12);							
			}
			Copter.loaded = true;
		}
	}
	
	public void setAnimation(Unit.Animations anim)
	{
		switch(anim)
		{
			default: this.animation = Copter.idle[this.ownerIndex]; break;
		}
	}
}
