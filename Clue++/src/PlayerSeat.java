
public class PlayerSeat
{
	boolean seatTaken = false;
	SocketServerConnection client;
	String characterName;
	
	PlayerSeat(String characterName)
	{
		this.characterName = characterName;
	}
}
