package com.mygdx.game;

public class Barracks extends Structure
{	
	public static boolean loaded = false;
	
	public static Animation[] animations;
	public static Terrain terrain;
	public static Unit[] buildableUnits;
	
	public Barracks(int ownerIndex)
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
		this.buildingType = Structure.BuildingType.PRODUCTION;
		this.income = 1000;		
		this.currentHealth = 10;
	}
	
	public static void load()
	{
		if(!Barracks.loaded)
		{					
			Barracks.terrain = new Terrain();
			terrain.defense = 1;
			terrain.mountain = false;
			terrain.moveCost = 1;
			terrain.name = "BARRACK";
			animations = new Animation[Skirmish.playerCount + 1];
			for(int i = 0; i < Skirmish.playerCount + 1; i++)
			{
				animations[i] = new Animation("./res/art/structures/barracks.png", i, 16, 16);
			}
			Structure.structures.add(new Barracks(-1));
			
			Barracks.buildableUnits = new Unit[4];
			buildableUnits[0] = new Infantry(0);
			buildableUnits[1] = new Copter(0);
			buildableUnits[2] = new Tank(0);
			buildableUnits[3] = new Frog(0);
			
			Barracks.loaded = true;
		}
	}
	
	public Terrain getTerrain()
	{
		return Barracks.terrain;
	}
	
	public Barracks createInstance(int i)
	{
		return new Barracks(i);
	}
	
	public Unit[] getBuildableUnits()
	{	
		return Barracks.buildableUnits;
	}
}


