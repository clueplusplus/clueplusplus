package mainPackage;

public class Card
{
	public String name;
	public String filePath;
	public String alias;
	
	public int type;
	public static int CharacterType = 0;
	public static int LocationType = 1;
	public static int WeaponType = 2;
	
	public Card(String name, int type, String filePath, String alias)
	{
		this.name = name;
		this.type = type;
		this.filePath = filePath;
		this.alias = alias;
	}
}
