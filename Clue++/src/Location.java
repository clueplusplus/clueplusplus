import java.util.ArrayList;

public class Location
{
	// Name of location
	String name = "";
	String userGeneratedName; // null if not used.
	
	// Grid Locations
	int row;
	int col;
	
	// Neighbors you can move to, null if no option.
	Location up;
	Location down;
	Location left;
	Location right;
	Location tunnel;

	// Capacity
	int capacity;
	
	// People in the location
	ArrayList<Character> occupants = new ArrayList<Character>(0);
	
	Location(int row, int col, String name, int capacity)
	{
		this.row = row;
		this.col = col;
		this.name = name;
		this.capacity = capacity;		
	}
	
	public String printMovementOptions()
	{
		StringBuilder s = new StringBuilder("From " + name + ":\n");
		
		if(up != null) s.append("Up: " + up.name + "\n");
		if(down != null) s.append("Down: " + down.name + "\n");
		if(left != null) s.append("Left: " + left.name + "\n");
		if(right != null) s.append("Right: " + right.name + "\n");
		if(tunnel != null) s.append("Tunnel: " + tunnel.name + "\n");
		
		return s.toString();
	}

}
