package mainPackage;

public class Weapon
{
	static String Candlestick = "Candlestick";
	static String Wrench = "Wrench";
	static String Rope = "Rope";
	static String Revolver = "Revolver";
	static String Knife = "Knife";
	static String LeadPipe = "Lead Pipe";
	
	public static String[] getValues() {
		String[] weapons = new String[6];
		weapons[0] = Candlestick;
		weapons[1] = Wrench;
		weapons[2] = Rope;
		weapons[3] = Revolver;
		weapons[4] = Knife;
		weapons[5] = LeadPipe;
		return weapons;
	}
}
