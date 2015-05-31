package Units;
import java.util.LinkedList;
import java.util.Queue;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import Core.Map;
import Core.Player;
import Core.Terrain;

public abstract class Unit 
{
	public Map board;
	public int[][] reachable;
	public Image image;
	public Player owner;
	float scale;
	public boolean canMove = true;
	public boolean canAct = true;
	public static SpriteSheet sheet;
	public String name;
	
	public int x, y;
	
	public int speed = 4, range = 2, attack = 2;
	public int[] hp = {3, 3}; // HP STORED AS {CURRENT, MAX}
	double distance;
	
	Rectangle tile = new Rectangle(1,1,1,1);
	public Type unitType;
	
	enum Type
	{
		LAND,
		AIR,
		NAVAL
	}
	
	public void act(int[] pos)
	{
		if(board.get(pos[0], pos[1]) == null)
			move(pos);
		else
		if(board.get(pos[0], pos[1]).owner != owner)
			attack(pos);
	}
	
	public void updateReachable()
	{				
		for(int i = 0; i < board.getHeight(); i++)
		{
			for(int j = 0; j < board.getWidth(); j++)
			{
				reachable[j][i] = -1;
			}
		}		
		
		Queue<int[]> bounds = new LinkedList<int[]>();
		bounds.add(new int[] { x, y, 0 });		
		reachable[x][y] = 0;
		int[] current;
		int distance = 0;		
		
		while(!bounds.isEmpty())
		{
			current = bounds.poll();
			
			distance = current[2];
									
			// N 
			if(canAccess(current[0], current[1] - 1) && board.get(current[0], current[1] - 1) == null && distance + board.cost[current[0]][current[1] - 1] <= speed)
			{
				bounds.add(new int[] { current[0], current[1] - 1, distance + board.cost[current[0]][current[1] - 1] });
				reachable[current[0]][current[1] - 1] = distance;
			}
			
			// W
			if(canAccess(current[0] - 1, current[1]) && board.get(current[0] - 1, current[1]) == null && distance + board.cost[current[0] - 1][current[1]] <= speed)
			{
				bounds.add(new int[] { current[0] - 1, current[1], distance + board.cost[current[0] - 1][current[1]] });
				reachable[current[0] - 1][current[1]] = distance;
			}
			
			// S
			if(canAccess(current[0], current[1] + 1) && board.get(current[0], current[1] + 1) == null && distance + board.cost[current[0]][current[1] + 1] <= speed)
			{
				bounds.add(new int[] { current[0], current[1] + 1, distance + board.cost[current[0]][current[1] + 1] });
				reachable[current[0]][current[1] + 1] = distance;
			}
			
			// E
			if(canAccess(current[0] + 1, current[1]) && board.get(current[0] + 1, current[1]) == null && distance + board.cost[current[0] + 1][current[1]] <= speed)
			{
				bounds.add(new int[] { current[0] + 1, current[1], distance + board.cost[current[0] + 1][current[1]] });
				reachable[current[0] + 1][current[1]] = distance;
			}
		}		
	}	
	
	public boolean canReach(int x, int y)
	{
		return reachable[x][y] >= 0 && reachable[x][y] <= speed && canAccess(x,y);
	}
	
	private boolean canAccess(int x, int y) 
	{
		if(!board.contains(x, y))
			return false;
		
		Terrain t = board.terrains[board.tiles[x][y]];
		
		switch(unitType)
		{
			case LAND:
				return (!t.water);
						
			case AIR:
				return true;
				
			case NAVAL:
				return (t.water || t.beach);
				
			default:
				return false;
		}
	}

	public void move(int[] pos)
	{
		if(canMove)
		{			
			if(canReach(pos[0], pos[1]))
			{
				board.move(this, pos);
				canMove = false;
			}
		}
	}
	
	public void render(int rx, int ry, int sx, int sy)
	{			
		if(canAct)
		{
			image.draw(rx + (x - sx) * board.tileSize, ry + (y - sy) * board.tileSize, board.tileSize, board.tileSize);
		}
		else
		{
			image.draw(rx + (x - sx) * board.tileSize, ry + (y - sy) * board.tileSize, board.tileSize, board.tileSize, Color.gray);
		}
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void attack(int[] loc)
	{
		if(canAct)
		{
			Unit other = board.get(loc[0], loc[1]);
			if (other == null || other.owner == owner)
				return;
			
			distance = Math.abs(loc[1] - this.y) + Math.abs(loc[0] - this.x);
			if(distance <= range)
			{
				other.hp[0] -= this.attack;
				if(other.hp[0] <= 0)
					other.destroy();
				canAct = false;
				canMove = false;
			}
		}			
	}
	
	public void drawMoves(Graphics g, int rx, int ry, int sx, int sy)
	{
		
		for(int x = Math.max(0, this.x - (this.range + this.speed)); x <= Math.min(board.getWidth() - 1, this.x + this.range + this.speed); x++)
		{
			for(int y = Math.max(0, this.y - (this.range + this.speed)); y <= Math.min(board.getHeight() - 1, this.y + this.range + this.speed); y++)
			{								
				tile.setBounds(2 + rx + (x - sx) * board.tileSize, 2 + ry + (y - sx) * board.tileSize, board.tileSize-3, board.tileSize-3);
				
				if(canReach(x,y))
				{
					g.setColor(new Color(0, 0, 255, 120));
					g.fill(tile);
				}
			}
		}
		
	}
	
	public void drawRangeOnly(Graphics g, int rx, int ry, int sx, int sy)
	{
		for(int x = Math.max(0, this.x - (this.range)); x <= Math.min(board.getWidth() - 1, this.x + this.range); x++)
		{
			for(int y = Math.max(0, this.y - (this.range)); y <= Math.min(board.getHeight() - 1, this.y + this.range); y++)
			{
				//double distance = Math.sqrt(Math.pow(Math.abs(y - this.y), 2) + Math.pow(Math.abs(x - this.x), 2)); //ACTUAL DISTANCE WHO USES THIS SHIT LAMO
				distance = Math.abs(y - this.y) + Math.abs(x - this.x);
				
				tile.setBounds(2 + rx + (x - sx) * board.tileSize, 2 + ry + (y - sx) * board.tileSize, board.tileSize-3, board.tileSize-3);
				
								
				if(distance <= this.range)
				{
					g.setColor(new Color(255, 0, 0, 120));
					g.fill(tile);
				}
			}
		}
	}
	
	public void drawRangeEnemy(Graphics g, int rx, int ry, int sx, int sy)
	{		
		for(int x = Math.max(0, this.x - (this.range + this.speed)); x <= Math.min(board.getWidth() - 1, this.x + this.range + this.speed); x++)
		{
			for(int y = Math.max(0, this.y - (this.range + this.speed)); y <= Math.min(board.getHeight() - 1, this.y + this.range + this.speed); y++)
			{
				//double distance = Math.sqrt(Math.pow(Math.abs(y - this.y), 2) + Math.pow(Math.abs(x - this.x), 2)); //ACTUAL DISTANCE WHO USES THIS SHIT LAMO
				distance = Math.abs(y - this.y) + Math.abs(x - this.x);
				
				tile.setBounds(2 + rx + (x - sx) * board.tileSize, 2 + ry + (y - sx) * board.tileSize, board.tileSize-3, board.tileSize-3);
				
				if(distance <= this.speed)
				{
					g.setColor(new Color(255, 0, 0, 120));
					g.fill(tile);
				}
				else
				if(distance <= this.range + this.speed)
				{
					g.setColor(new Color(255, 0, 0, 120));
					g.fill(tile);
				}
			}
		}
	}
	
	public void drawHP()
	{
	}
	
	public void drawStatusWindow()
	{
		Graphics g = new Graphics();		
		
		//DRAW WINDOW
		g.setColor(new Color(50, 50, 50, 150));
		g.fillRect(owner.gc.getWidth() - 96, 1, 95, 45);	// 95 height	
		
		//DRAW HEALTH BAR
		g.setColor(Color.red);
		g.fillRect(owner.gc.getWidth() - 93, 4, 89, 18);
		g.setColor(Color.green);
		g.fillRect(owner.gc.getWidth() - 93, 4, 89 * (float)hp[0]/hp[1], 18);
		
		//DRAW HEALTH TEXT
		g.setColor(Color.white);		
		g.resetFont();
		g.drawString("HP: " + hp[0] + "/" + hp[1], owner.gc.getWidth() - 91, 4);
		
		// Draw unit sprite
		image.draw(owner.gc.getWidth() - 93, 26);
		
		// Draw unit name
		g.drawString(name, owner.gc.getWidth() - 77, 26);
		
		g.destroy();
	}
	
	public void destroy()
	{
		owner.units.remove(this);
		board.remove(this);
	}
	
	public abstract void update(int delta);
}
