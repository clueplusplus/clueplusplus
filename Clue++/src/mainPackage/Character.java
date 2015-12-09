package mainPackage;
public class Character
{
	public String name;
	public String userSuppliedName;
	
	public Location location;
	public int gamePieceImageIndex = 0; // This maps to the gamePeices images loaded in GameBoardGUI.java 
	
	static String missScarlet =	"Miss Scarlet";
	static String profPlum = "Professor Plum";
	static String mrsPeacock = "Mrs. Peacock";
	static String revGreen = "Reverend Green";
	static String colMustard = "Colonel Mustard";
	static String mrsWhite = "Mrs. White";
	
	Character(String name, Location location, int gamePieceImageIndex)
	{
		this.name = name;
		this.userSuppliedName = name;
		this.location = location;
		this.gamePieceImageIndex = gamePieceImageIndex;
	}
}
