package mainPackage;

public class PlayerSeat
{
	boolean seatTaken = false;
	boolean playerIsStillInGame = true;
	SocketServerConnection playerConnection;
	String characterName;
		
	PlayerSeat(String characterName)
	{
		this.characterName = characterName;
	}
}
