package mainPackage;
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
	
	public String characterName = null;
	
	public ArrayList<Card> cards = new ArrayList<Card>();
	
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
			// If this is the first person to connect they decide when to start the game.
			// AM: The server side assigns the player who starts the server as the first player
			// AM: Send message to client that it is first player 
			if(firstPlayer)
				sendYouAreFirstPlayer();
			
			// Tell this person the available characters to choose from
			sendAvailableCharacterList();
			
			// Monitor the connection.
			while(socket.isConnected())
	        {
				// Parse messages
				String messageType = in.readString();
				
				System.out.println("Server Received: " + messageType);
				
				if(messageType.compareTo("SelectCharacter") == 0)
				{
					String characterName = in.readString();
					
					if(server.selectCharacter(this, characterName))
					{
						this.characterName = characterName;
						
						// Send the message back to the client that this is a valid choice.
						sendYourCharacter();
						sendStartTurn(characterName);
					}
					else
					{
						// This is no longer a valid choice.
						sendInvalidCharacter();
					}
				}
				else if(messageType.compareTo("StartGame") == 0)
				{
					//Check that this is the first character. If so start the game.
					// Skipping the check.
					server.processStartGame(this);
				}
				else if(messageType.compareTo("MakeMove") == 0)
				{
					String location = in.readString();
					
					// Since the server doesn't care about player location this data should just be forwarded on.
					
					//Update other users with this information
					server.sendNotifyMove(characterName, location);
				}
				else if(messageType.compareTo("MakeSuggestion") == 0)
				{
					String character = in.readString();
					String room = in.readString();
					String weapon = in.readString();
					
					server.processSuggestion(this, character, room, weapon);
				}
				else if(messageType.compareTo("RespondToSuggestion") == 0)
				{
					String card = in.readString(); // May equal "NoCard"
					server.processSuggestionResponse(this, card);
				}
				else if(messageType.compareTo("MakeAccusation") == 0)
				{
					String character = in.readString();
					String room = in.readString();
					String weapon = in.readString();
					
					server.processAccusation(this, character, room, weapon);
				}
				else if(messageType.compareTo("EndTurn") == 0)
				{
					server.processEndTurn(this);
				}
				else
				{
					// Error somehow.
					System.out.println("Server - Received Message Error: " + messageType);
				}
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendYouAreFirstPlayer()
	{
		try {
			synchronized(out)
			{
				out.writeString("YouAreFirstPlayer");
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendAvailableCharacterList()
	{
		try {
			ArrayList<String> characters = server.getAvailableCharacters();
			
			synchronized(out)
			{
				out.writeString("AvailableCharacterList");
				out.writeInt(characters.size());
				for(int x=0; x<characters.size(); x++)
				{
					out.writeString(characters.get(x));
				}						
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendInvalidCharacter()
	{
		try {
			synchronized(out)
			{
				out.writeString("InvalidCharacter");
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendYourCharacter()
	{
		try {
			synchronized(out)
			{
				out.writeString("YourCharacter");
				out.writeString(characterName);
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendYourCards()
	{
		try {
			synchronized(out)
			{
				out.writeString("YourCards");
				out.writeInt(cards.size());
			
				for(int x=0; x<cards.size(); x++)
				{
					out.writeString(cards.get(x).name);
				}						
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendStartGame()
	{
		try {
			synchronized(out)
			{
				out.writeString("StartGame");
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendStartTurn(String characterWhosTurnItIs)
	{
		try {
			synchronized(out)
			{
				out.writeString("StartTurn");
				out.writeString(characterWhosTurnItIs);
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendNotifyMove(String characterMoving, String location)
	{
		try {
			synchronized(out)
			{
				out.writeString("NotifyMove");
				out.writeString(characterMoving);
				out.writeString(location);
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendSuggestionNotification(
			String suggestingCharacter,
			String characterToRespond,
			String suggestionCharacter,
			String suggestionRoom,
			String suggestionWeapon)
	{
		try {
			synchronized(out)
			{
				out.writeString("SuggestionNotification");
				out.writeString(suggestingCharacter);
				out.writeString(characterToRespond);
				out.writeString(suggestionCharacter);
				out.writeString(suggestionRoom);
				out.writeString(suggestionWeapon);
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendForwardResponseToSuggestion(String characterResponding, String card)
	{
		try {
			synchronized(out)
			{
				out.writeString("ForwardResponseToSuggestion");
				out.writeString(characterResponding);
				out.writeString(card);			
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendSuggestionRoundComplete()
	{
		try {
			synchronized(out)
			{
				out.writeString("SuggestionRoundComplete");
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendAccusationMade(
			String accusingCharacter,
			String accusationCharacter,
			String accusationRoom,
			String accusationWeapon,
			String accuracy)
	{
		try {
			synchronized(out)
			{
				out.writeString("AccusationMade");
				out.writeString(accusingCharacter);
				out.writeString(accusationCharacter);
				out.writeString(accusationRoom);
				out.writeString(accusationWeapon);
				out.writeString(accuracy);
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendEndGame(String why)
	{
		try {
			synchronized(out)
			{
				out.writeString("EndGame");
				out.writeString(why);
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
