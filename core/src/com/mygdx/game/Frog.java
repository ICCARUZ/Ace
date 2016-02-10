package com.mygdx.game;

public class Frog extends Unit
{
	private static Animation[] idle;
	private static boolean loaded = false;
	
	public Frog(int ownerIndex)
	{		
		Frog.load();
		this.cost = 1000;
		this.entityType = Entity.type.UNIT;
		this.ownerIndex = ownerIndex;
		this.name = "FROG";
		this.unitType = Unit.UnitType.LAND;
		this.baseDamage = 55;
		this.speed = 4;
		this.currentHealth = 100;		
		this.animation = idle[ownerIndex];
		this.unitClass = Unit.UnitClass.INFANTRY;
	}
	
	public Frog createInstance(int ownerIndex)
	{
		Frog i = new Frog(ownerIndex);
		return i;
	}
	
	public static void load()
	{
		if(!Frog.loaded)
		{			
			idle = new Animation[Skirmish.playerCount];
			
			for(int i = 0; i < Skirmish.playerCount; i++)
			{
				Frog.idle[i] = new Animation("./res/art/units/frog.png", i, 24, 24, 6, 0);				
			}
			Frog.loaded = true;
		}
	}
	
	public void setAnimation(Unit.Animations anim)
	{
		this.animation = Frog.idle[this.ownerIndex];
	}
}
