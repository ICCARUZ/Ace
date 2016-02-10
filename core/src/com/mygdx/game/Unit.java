package com.mygdx.game;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Unit extends Entity
{
	public static float maxHealth = 100; 
	public float currentHealth = 100;
	public float rx, ry;
	public String name = "";
	public float baseDamage = 55;
	public int speed = 1;
	public int minRange = 1, maxRange = 1;
	
	public boolean canMove = true;
	public boolean canAct = true;
	
	public int[][] reachable;
	
	public UnitClass unitClass;
	public UnitType unitType;
	public int cost;
	
	public static enum Animations
	{
		IDLE, 
		RUN,
		RUNUP,
		RUNDOWN,
		RUNRIGHT,
		RUNLEFT
	}
	
	public static enum UnitClass
	{
		INFANTRY,
		VEHICLE,
		SETUP,
		TRANSPORT
	}
	
	public static enum AttackType
	{
		SMALLARMS,
		EXPLOSIVE,
		PIERCING
	}
	
	public static enum DefenseType
	{
		FOOTSOLDIER,
		MECHANICAL,
		ARMORED,
		INSTALLATION
	}
	
	public static enum UnitType
	{
		LAND,
		AIR,
		NAVAL
	}
		
	public void move(int targetX, int targetY)
	{
		if(canMoveTo(targetX, targetY))
		{
			Skirmish.map.moveUnit(this.x, this.y, targetX, targetY);
			this.x = targetX;
			this.y = targetY;
			this.canMove = false;
			this.updateReachable();
		}		
	}
	
	public void updateReachable()
	{
		if(this.unitType == Unit.UnitType.AIR)
		{
			this.reachable = new int[Skirmish.map.width][Skirmish.map.height];
			for(int i = 0; i < Skirmish.map.height; i++)
			{
				for(int j = 0; j < Skirmish.map.width; j++)
				{
					if(Math.abs(this.x - j) + Math.abs(this.y - i) <= this.speed)
						reachable[j][i] = 0;
					else
						reachable[j][i] = -1;
				}
			}
			
			return;
		}
		
		if(this.reachable == null)
		{
			this.reachable = new int[Skirmish.map.width][Skirmish.map.height];
		}
		
		for(int i = 0; i < Skirmish.map.height; i++)
		{
			for(int j = 0; j < Skirmish.map.width; j++)
			{
				reachable[j][i] = -1;
			}
		}		
		
		Queue<int[]> bounds = new LinkedList<int[]>();
		bounds.add(new int[] { x, y, 0 });		
		reachable[x][y] = 0;
		int[] current;
		int distance = 0;		
		
		Unit otherUnit;
		Terrain currentTerrain;
		
		while(!bounds.isEmpty())
		{
			current = bounds.poll();			
			distance = current[2];								
			
			// N 
			otherUnit = Skirmish.map.getUnit(current[0], current[1] - 1);
			currentTerrain = Skirmish.map.getTerrain(current[0], current[1] - 1);
			if(canAccess(current[0], current[1] - 1) && distance + currentTerrain.moveCost <= speed)
			{
				if(otherUnit == null || otherUnit.ownerIndex == this.ownerIndex)
					bounds.add(new int[] { current[0], current[1] - 1, distance + currentTerrain.moveCost });
				reachable[current[0]][current[1] - 1] = distance;
			}
			
			// W
			otherUnit = Skirmish.map.getUnit(current[0] - 1, current[1]);
			currentTerrain = Skirmish.map.getTerrain(current[0] - 1, current[1]);
			if(canAccess(current[0] - 1, current[1]) && distance + currentTerrain.moveCost <= speed)
			{
				if(otherUnit == null || otherUnit.ownerIndex == this.ownerIndex)
					bounds.add(new int[] { current[0] - 1, current[1], distance + currentTerrain.moveCost });
				reachable[current[0] - 1][current[1]] = distance;
			}
			
			// S
			otherUnit = Skirmish.map.getUnit(current[0], current[1] + 1);
			currentTerrain = Skirmish.map.getTerrain(current[0], current[1] + 1);
			if(canAccess(current[0], current[1] + 1) && distance + currentTerrain.moveCost <= speed)
			{
				if(otherUnit == null || otherUnit.ownerIndex == this.ownerIndex)
					bounds.add(new int[] { current[0], current[1] + 1, distance + currentTerrain.moveCost });
				reachable[current[0]][current[1] + 1] = distance;
			}
			
			// E
			otherUnit = Skirmish.map.getUnit(current[0] + 1, current[1]);
			currentTerrain = Skirmish.map.getTerrain(current[0] + 1, current[1]);
			if(canAccess(current[0] + 1, current[1]) && distance + currentTerrain.moveCost <= speed)
			{
				if(otherUnit == null || otherUnit.ownerIndex == this.ownerIndex)
					bounds.add(new int[] { current[0] + 1, current[1], distance + currentTerrain.moveCost });
				reachable[current[0] + 1][current[1]] = distance;
			}
		}		
	}
	
	public boolean canAccess(int x, int y)
	{
		Terrain t = Skirmish.map.getTerrain(x, y);
		if(t == null)
			return false;
		
		switch(this.unitType)
		{
			case LAND:
				return (!t.water);
						
			case AIR:
				return true;
				
			case NAVAL:
				return (t.water || t.shallow);
				
			default:
				return false;
		}
	}
	
	public boolean canReach(int x, int y)
	{
		if(!Skirmish.map.contains(x,y))
			return false;
		return reachable[x][y] >= 0;
	}
	
	public boolean canMoveTo(int x, int y)
	{
		return canAct && canMove && canReach(x,y) && (Skirmish.map.getUnit(x,y) == null || (x == this.x && y == this.y));
	}
	
	public static void getAttackMod(UnitClass attackerClass, UnitClass targetClass)
	{
		// TODO: IMPLEMENT
	}
	
	public boolean canAttack(int x, int y)
	{
		int distance = Skirmish.map.getDistance(this.x, this.y, x, y);
		return this.canAct && Skirmish.map.getUnit(x,y) != null && distance >= this.minRange && distance <= this.maxRange && Skirmish.map.getUnit(x, y).ownerIndex != this.ownerIndex;
	}
	
	public void attack(int targetX, int targetY)
	{
		if(!this.canAttack(targetX, targetY))
			return;
		
		this.canAct = false;
		this.canMove = false;
		
		Unit target = Skirmish.getUnit(targetX, targetY);
		
		float damageToDeal = (this.baseDamage/100) * (this.currentHealth/Unit.maxHealth) * (400 - (Skirmish.getDefense(targetX, targetY) * target.currentHealth))/400;
		target.currentHealth -= damageToDeal * Unit.maxHealth;	
		if(target.currentHealth <= 0)
		{
			target.destroy();
			return;
		}
		
		float damageToTake = 0;
		if(target != null && target.canAttack(this.x, this.y))
		{
			damageToTake = (target.baseDamage/100) * (target.currentHealth/Unit.maxHealth) * (400 - (Skirmish.getDefense(this.x, this.y) * this.currentHealth))/400;
			this.currentHealth -= damageToTake * Unit.maxHealth;
		}
		
		if(this.currentHealth <= 0)		
			this.destroy();		
	}
	
	public int[][] getAttackable()
	{
		int[][] result = new int[Skirmish.map.width][Skirmish.map.height];
		
		for(int i = 0; i < Skirmish.map.height; i++)
		{
			for(int j = 0; j < Skirmish.map.width; j++)
			{
				result[j][i] = -1;
				if(Math.abs(i - this.y) + Math.abs(j - this.x) >= this.minRange && Math.abs(i - this.y) + Math.abs(j - this.x) <= this.maxRange)
					result[j][i] = 1;
			}
		}
		
		return result;
	}
	
	public boolean canCapture(int targetX, int targetY)
	{
		if(!this.canAct)
			return false;
		
		if(this.unitClass != UnitClass.INFANTRY)
			return false; 	
		
		if(this.x != targetX || this.y != targetY)
			return false;
		
		Structure other = Skirmish.map.getStructure(targetX, targetY);
		return other != null && other.ownerIndex != this.ownerIndex;
	}
	
	public void capture(int targetX, int targetY)
	{
		Structure target = Skirmish.map.getStructure(targetX, targetY);
		target.getCaptured(this);
		this.canAct = false;
		this.canMove = false;
	}
	
	public boolean canReinforce(int targetX, int targetY)
	{
		if(targetX == this.x && targetY == this.y)
			return false;
		Unit other = Skirmish.map.getUnit(targetX, targetY);
		return other != null && other.ownerIndex == this.ownerIndex && other.currentHealth < Unit.maxHealth && other.getClass() == this.getClass();
	}
	
	public void reinforce(int targetX, int targetY)
	{
		Unit other = Skirmish.map.getUnit(targetX, targetY);
		other.currentHealth += this.currentHealth;
		if(other.currentHealth > Unit.maxHealth)
			other.currentHealth = Unit.maxHealth;
		this.destroy();
	}
	
	public void destroy()
	{
		// TODO: CREATE DESTRUCTION ANIMATION HERE WHEN EFFECTS MANAGER IS IMPLEMENTED
		Skirmish.map.removeUnit(this.x, this.y);
	}			
	
	public void render(SpriteBatch batch, float rx, float ry)
	{
		if(canAct)
		{			
			//batch.draw(this.currentTexture, rx, ry);
			this.animation.render(batch, rx, ry);
		}
		else
		{
			Color c = batch.getColor();
			batch.setColor(.5f,.5f,.5f,1);
			this.animation.render(batch, rx, ry);
			batch.setColor(c);
		}
	}
	
	public abstract void setAnimation(Unit.Animations anim);
	public abstract Unit createInstance(int ownerIndex);

	public void startTurn() 
	{		
		this.canMove = true;
		this.canAct = true;
		
		if(this.ownerIndex == Skirmish.currentPlayerID && Skirmish.map.getStructure(x, y) != null && Skirmish.map.getStructure(x, y).ownerIndex == this.ownerIndex)
			this.heal(10);
	}

	public void heal(float amount) 
	{		
		if(this.currentHealth < Unit.maxHealth)
		{
			this.currentHealth += amount;
			if(this.currentHealth > Unit.maxHealth)
				this.currentHealth = Unit.maxHealth;
		}
	}
	
	public void damage(float amount)
	{
		this.currentHealth -= amount;
		if(this.currentHealth <= 0)
			this.destroy();
	}
}
