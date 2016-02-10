package com.mygdx.game;

public class City extends Structure
{
	public static boolean loaded = false;
	
	public static Animation[] animations;
	public static Terrain terrain;
	
	public City(int ownerIndex)
	{
		this.init(ownerIndex);
	}
	
	public void init(int ownerIndex)
	{
		this.entityType = Entity.type.STRUCTURE;
		this.ownerIndex = ownerIndex;
		this.animation = animations[ownerIndex + 1]; // offset by 1 so that ownerIndex -1 fetches the neutral image
		this.cost = 0;
		this.buildingAura = null;
		this.buildingType = Structure.BuildingType.RESOURCE;
		this.income = 1000;		
		this.currentHealth = 10;
	}
	
	public static void load()
	{
		if(!City.loaded)
		{				
			City.terrain = new Terrain();
			terrain.defense = 1;
			terrain.mountain = false;
			terrain.moveCost = 1;
			terrain.name = "CITY";
			animations = new Animation[Skirmish.playerCount + 1];
			for(int i = 0; i < Skirmish.playerCount + 1; i++)
			{
				animations[i] = new Animation("./res/art/structures/city.png", i, 16, 16);
			}
			Structure.structures.add(new City(-1));
			City.loaded = true;			
		}
	}
	
	public Terrain getTerrain()
	{
		return City.terrain;
	}
	
	public City createInstance(int i)
	{
		return new City(i);
	}

	public Unit[] getBuildableUnits() 
	{
		return null;
	}
}
