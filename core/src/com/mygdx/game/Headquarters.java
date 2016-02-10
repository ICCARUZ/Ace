package com.mygdx.game;

public class Headquarters extends Structure
{
	public static Terrain terrain;
	public static Animation[] animations;
	public static boolean loaded;
	
	public Headquarters(int ownerIndex)
	{
		this.init(ownerIndex);
	}
	
	public static void load()
	{
		if(!Headquarters.loaded)
		{				
			Headquarters.terrain = new Terrain();
			terrain.defense = 2;
			terrain.mountain = false;
			terrain.moveCost = 1;
			terrain.name = "HQ";
			animations = new Animation[Skirmish.playerCount + 1];
			for(int i = 0; i < Skirmish.playerCount + 1; i++)
			{
				animations[i] = new Animation("./res/art/structures/hq.png", i, 16, 32);
			}
			Structure.structures.add(new Headquarters(-1));
			Headquarters.loaded = true;			
		}
	}
	
	public Terrain getTerrain() 
	{
		return Headquarters.terrain;
	}

	public Structure createInstance(int ownerIndex) 
	{
		return new Headquarters(ownerIndex);
	}

	public Unit[] getBuildableUnits() 
	{
		return null;
	}

	public void init(int ownerIndex) 
	{
		this.entityType = Entity.type.STRUCTURE;
		this.ownerIndex = ownerIndex;
		this.animation = animations[ownerIndex + 1]; // offset by 1 so that ownerIndex -1 fetches the neutral image
		this.cost = 0;
		this.buildingAura = null;
		this.buildingType = Structure.BuildingType.RESOURCE;
		this.income = 2000;		
		this.currentHealth = 10;
	}

	public void getCaptured(Unit u)
	{
		if(u.ownerIndex == capturingTeam)
		{
			this.currentHealth -= ((int) (u.currentHealth/10) + 1)/2;
		}
		else
		{
			this.currentHealth = Structure.maxHealth - ((int) (u.currentHealth/10) + 1)/2;
			this.capturingTeam = u.ownerIndex;
		}
		
		if(this.currentHealth <= 0)
		{
			this.init(u.ownerIndex);
			Skirmish.setWinner(u.ownerIndex);
		}
	}
}
