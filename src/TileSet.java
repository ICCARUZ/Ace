import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class TileSet 
{
	public String filename;
	public String name;
	
	private Image tileSet;
	
	public int width, height; // width and height of the tileset in tiles
	public int tileWidth, tileHeight; // width and height in pixels of the tiles on the tileset
	public int tileSpacing; // pixel spacing between tiles on the tileset image
	public Image[] tileList;
	
	public TileSet(Image tileSet)
	{
		this.tileSet = tileSet;
		this.width = 32;
		this.height = 32;
		this.tileWidth = 32;
		this.tileHeight = 32;
		this.tileSpacing = 0;
		generateTileList();
	}
	
	public TileSet(String filename) throws SlickException
	{
		Image tileSet = new Image(filename);
		this.filename = filename.substring(4, filename.length());
		this.tileSet = tileSet;
		this.width = 32;
		this.height = 32;
		this.tileWidth = 32;
		this.tileHeight = 32;
		this.tileSpacing = 0;
		generateTileList();
	}
	
	public TileSet(String filename, int tileWidth, int tileBuffer) throws SlickException
	{
		Image tileSet = new Image(filename);
		this.filename = filename.substring(4, filename.length());
		this.tileSet = tileSet;
		this.width = 32;
		this.height = 32;
		this.tileWidth = tileWidth;
		this.tileHeight = tileWidth;
		this.tileSpacing = tileBuffer;
		generateTileList();
	}
	
	public int getTileNumber(int x, int y)
	{
		return x + y * tileWidth; // get the id number of a tile, distributed from 0
	}
	
	public int[] getTile(int tileNumber)
	{		
		int x = tileNumber%width;
		int y = tileNumber/width;
		return new int[] { x, y };// returns the location of a tile, used for drawing, etc...
	}
	
	public void generateTileList()
	{
		int count = 0;
		int size = (tileSet.getWidth()/(tileWidth + tileSpacing)) * (tileSet.getHeight()/(tileHeight + tileSpacing));
		tileList = new Image[size];
		for(int y = 0; y < tileSet.getHeight(); y += tileSpacing + tileHeight)
		{
			for(int x = 0; x < tileSet.getWidth(); x += tileSpacing + tileWidth)
			{
				tileList[count] = tileSet.getSubImage(x, y, tileWidth, tileHeight);
				count++;
			}
		}
	}
	
	public Image getImage()
	{
		return this.tileSet;
	}
}
