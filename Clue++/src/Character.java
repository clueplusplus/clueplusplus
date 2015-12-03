public class Character
{
	String name;
	String userSuppliedName; // null if not supplied
	
	Location location;
	
	static String missScarlet =	"Miss Scarlet";
	static String profPlum = "Professor Plum";
	static String mrsPeacock = "Mrs. Peacock";
	static String revGreen = "Reverend Green";
	static String colMustard = "Colonel Mustard";
	static String mrsWhite = "Mrs. White";	
	
	Character(String name, Location location)
	{
		this.name = name;
		this.location = location;
	}
}
