import java.util.ArrayList;

public class Map
{
	ArrayList<Location> locations = Location.getEmptyMap();

	
	public Location getRoom(String name)
	{
		for(Location l : locations)
		{
			if(l.name.compareTo(name) == 0)
				return l;
		}
		
		return null;
	}
	
	public Location getRoom(int row, int col)
	{
		for(Location l : locations)
		{
			if(l.row == row && l.col == col)
				return l;
		}
		
		return null;
	}
	
	public void printMapMovementOptions()
	{
		for(Location l : locations)
		{
			System.out.print(l.printMovementOptions());
		}
	}
}
