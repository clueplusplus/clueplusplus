public class Character
{
	String name;
	String userSuppliedName; // null if not supplied
	
	Location location;
	
	Character(String name, Location location)
	{
		this.name = name;
		this.location = location;
	}
}
