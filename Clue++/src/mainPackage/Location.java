package mainPackage;
import java.util.ArrayList;
import java.util.List;

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
	public ArrayList<Character> occupants = new ArrayList<Character>(0);
	
	static String Study = "Study";
	static String Hall = "Hall";
	static String Lounge= "Lounge";
	static String Library = "Library";
	static String BilliardRoom = "Billiard Room";
	static String DiningRoom = "Dining Room";
	static String Conservatory = "Conservatory";
	static String Ballroom = "Ballroom";
	static String Kitchen = "Kitchen";
	
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

	public List<Location> getAvailableMovementOptions() {
		List<Location> openLocations = new ArrayList<>();
		if (canMoveToLocation(up)) {
			openLocations.add(up);
		}
		if (canMoveToLocation(down)) {
			openLocations.add(down);
		}
		if (canMoveToLocation(right)) {
			openLocations.add(right);
		}
		if (canMoveToLocation(left)) {
			openLocations.add(left);
		}
		if (canMoveToLocation(tunnel)) {
			openLocations.add(tunnel);
		}
		return openLocations;
	}

	public boolean canMoveToLocation(Location location) {
		return location != null && occupants.size() < location.capacity;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

}
