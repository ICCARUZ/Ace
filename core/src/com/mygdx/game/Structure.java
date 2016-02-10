package com.mygdx.game;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public abstract class Structure extends Entity
{
	public static ArrayList<Structure> structures;
	public Aura buildingAura;
	public int income;
	public int cost;
	public BuildingType buildingType;
	
	public int currentHealth = 10;
	public int capturingTeam = -1;	
	public static int maxHealth = 10;
	
	public static boolean loaded = false;
	
	public static enum BuildingType 
	{
		PRODUCTION,
		RESOURCE,
		INSTALLMENT,
		AURA
	}
		
	public static void loadStructures() throws FileNotFoundException
	{
		if(!loaded)
		{
			structures = new ArrayList<Structure>();
			Headquarters.load();
			Barracks.load();
			City.load();
			loaded = true;
		}
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
		}
	}
	
	public abstract Terrain getTerrain();

	public abstract Structure createInstance(int i);
	
	public abstract Unit[] getBuildableUnits();
	
	public abstract void init(int ownerIndex);

	public void startTurn() 
	{
		if(Skirmish.currentPlayerID == this.ownerIndex)
		{
			Skirmish.getCurrentPlayer().resourceCount += this.income;
		}
	}
}
