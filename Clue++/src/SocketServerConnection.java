import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class SocketServerConnection implements Runnable{

	private Socket socket;
	private Thread thread;
	
	private	SocketDecoder in;
	private	SocketEncoder out;	
	
	private SocketServer server;
		
	public boolean firstPlayer = false;
	public String character = "invalid";
	
	public SocketServerConnection(SocketServer server)
	{
		this.server = server;
	}
	
	public void startThread(Socket socket)
	{
		this.socket = socket;
		
		try
		{
			in = new SocketDecoder(new DataInputStream(new BufferedInputStream(socket.getInputStream())));
			out = new SocketEncoder(new DataOutputStream(new BufferedOutputStream(socket.getOutputStream())));
			
			thread = new Thread(this);
			thread.start();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void run()
	{
		try
		{
			if(firstPlayer)
				sendYourFirstPlayer();
			
			// Monitor the connection.
			while(socket.isConnected())
	        {
				// Parse messages
				String messageType = in.readString();
				
				System.out.println("Received: " + messageType);
				
				if(messageType.compareTo("SelectCharacter") == 0)
				{
					String characterName = in.readString();
					
					//TODO: Check that this character selection is still valid. If so save the data for this user.
					character = characterName; // Assuming it's valid.
				}
				else if(messageType.compareTo("StartGame") == 0)
				{
					//TODO: Check that this is the first character. If so start the game.
				}
				else if(messageType.compareTo("MakeMove") == 0)
				{
					String location = in.readString();
					
					//TODO: Whatever is required internally.
					
					//TODO: Update other users with this information
				}
				else if(messageType.compareTo("MakeSuggestion") == 0)
				{
					String character = in.readString();
					String room = in.readString();
					String weapon = in.readString();
					
					//TODO: Figure out who should respond and send this data out to everyone.
				}
				else if(messageType.compareTo("RespondToSuggestion") == 0)
				{
					String card = in.readString(); // May = "NoCard"
					
					//TODO: Process and tell all users.
				}
				else if(messageType.compareTo("MakeAccusation") == 0)
				{
					String character = in.readString();
					String room = in.readString();
					String weapon = in.readString();
					
					//TODO: Process. Player might win, might lose. Tell everyone.
				}
				else if(messageType.compareTo("EndTurn") == 0)
				{
					//TODO: Start the next players turn.
				}
				else
				{
					// Error somehow.
					System.out.println("Socket Message Error: " + messageType);
				}
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public synchronized void sendYourFirstPlayer()
	{
		try {
			out.writeString("YourFirstPlayer");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void sendAvailableCharacterList(ArrayList<String> characters)
	{
		try {
			out.writeString("AvailableCharacterList");
			out.writeInt(characters.size());
			for(int x=0; x<characters.size(); x++)
			{
				out.writeString(characters.get(x));
			}						
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void sendInvalidCharacter()
	{
		try {
			out.writeString("InvalidCharacter");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void sendYourCharacter()
	{
		try {
			out.writeString("YourCharacter");
			out.writeString(character);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void sendYourCards(ArrayList<String> cards)
	{
		try {
			out.writeString("YourCards");
			out.writeInt(cards.size());
			for(int x=0; x<cards.size(); x++)
			{
				out.writeString(cards.get(x));
			}						
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void sendStartGame()
	{
		try {
			out.writeString("StartGame");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void sendStartTurn(String characterWhosTurnItIs)
	{
		try {
			out.writeString("StartTurn");
			out.writeString(characterWhosTurnItIs);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void sendNotifyMove(String characterMoving, String location)
	{
		try {
			out.writeString("NotifyMove");
			out.writeString(characterMoving);
			out.writeString(location);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void sendSuggestionNotification(
			String suggestingCharacter,
			String characterToRespond,
			String suggestionCharacter,
			String suggestionRoom,
			String suggestionWeapon)
	{
		try {
			out.writeString("SuggestionNotification");
			out.writeString(suggestingCharacter);
			out.writeString(characterToRespond);
			out.writeString(suggestionCharacter);
			out.writeString(suggestionRoom);
			out.writeString(suggestionWeapon);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void sendForwardResponseToSuggestion(String characterResponding, String card)
	{
		try {
			out.writeString("ForwardResponseToSuggestion");
			out.writeString(characterResponding);
			out.writeString(card);			
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void sendSuggestionRoundComplete()
	{
		try {
			out.writeString("SuggestionRoundComplete");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void sendAccusationMade(
			String accusingCharacter,
			String accusationCharacter,
			String accusationRoom,
			String accusationWeapon,
			String accuracy)
	{
		try {
			out.writeString("AccusationMade");
			out.writeString(accusingCharacter);
			out.writeString(accusationCharacter);
			out.writeString(accusationRoom);
			out.writeString(accusationWeapon);
			out.writeString(accuracy);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void sendEndGame(String why)
	{
		try {
			out.writeString("EndGame");
			out.writeString(why);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
