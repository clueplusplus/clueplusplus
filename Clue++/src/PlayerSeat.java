
public class PlayerSeat
{
	boolean seatTaken = false;
	boolean playerIsStillInGame = true;
	SocketServerConnection client;
	String characterName;
	
	
	PlayerSeat(String characterName)
	{
		this.characterName = characterName;
	}
}
