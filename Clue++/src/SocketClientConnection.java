import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class SocketClientConnection implements Runnable
{
	private	Thread thread;
	private	Socket socket;
	
	private String ipaddr = "localhost";
	private int port = 1234;
	
	private Game game;
	
	private	SocketDecoder in;
	public SocketEncoder out;	
	
	public volatile boolean connected = false;	
	public volatile boolean attemptToConnect = true;
	
	public SocketClientConnection()
	{
		game = Game.getInstance();
	}
	
	public void connect(String ipaddr, int port)
	{
		this.ipaddr = ipaddr;
		this.port = port;
		
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run()
	{
		// Note this will keep trying to connect if the connection is broken or never established.
		while(attemptToConnect)
		{
			try
			{
				Thread.sleep(1000); // This is here to space out repeat connect attempts.
				
				socket = new Socket(ipaddr, port);
				
				in = new SocketDecoder(new DataInputStream(new BufferedInputStream(socket.getInputStream())));
				out = new SocketEncoder(new DataOutputStream(new BufferedOutputStream(socket.getOutputStream())));
								
				// Do the communication.
				connected = true;
				while(socket.isConnected())
		        {					
					// Parse messages
					String messageType = in.readString();
					
					System.out.println("Received: " + messageType);
					
					if(messageType.compareTo("YourFirstPlayer") == 0)
					{
						//TODO: Record this. I will need to start the game.
					}
					else if(messageType.compareTo("AvailableCharacterList") == 0)
					{
						// Get additional message data.
						ArrayList<String> availableCharacters = new ArrayList<String>();
						int count = in.readInt();
						for(int x=0; x<count; x++)
						{
							availableCharacters.add(in.readString());
						}
						
						//TODO: Process available characters. Probably display the options for user to choose.
						
					}
					else if(messageType.compareTo("InvalidCharacter") == 0)
					{
						// No more reading of message required.
						
						//TODO: Notify user the selection was invalid. Show the list again without this option.
					}
					else if(messageType.compareTo("YourCharacter") == 0)
					{
						String myCharacter = in.readString();
						
						//TODO: Save this data and move on. Save in Game object?
					}
					else if(messageType.compareTo("YourCards") == 0)
					{
						// Get additional message data.
						ArrayList<String> myCards = new ArrayList<String>();
						int count = in.readInt();
						for(int x=0; x<count; x++)
						{
							myCards.add(in.readString());
						}
						
						//TODO: Save these cards somewhere? Move to next step of game?						
					}
					else if(messageType.compareTo("StartGame") == 0)
					{
						//TODO: Start the game!
					}
					else if(messageType.compareTo("StartTurn") == 0)
					{
						String character = in.readString();
						
						//TODO: Check if character is me, if so start my turn.
					}
					else if(messageType.compareTo("NotifyMove") == 0)
					{
						String character = in.readString();
						String location = in.readString();
						
						//TODO: Update the records.
					}
					else if(messageType.compareTo("SuggestionNotification") == 0)
					{
						String suggestingCharacter = in.readString();
						String characterToRespond = in.readString();
						
						String suggestionCharacter = in.readString();
						String suggestionRoom = in.readString();
						String suggestionWeapon = in.readString();
						
						//TODO: Log the event. Respond if I am the characterToRespond.
					}
					else if(messageType.compareTo("ForwardResponseToSuggestion") == 0)
					{
						String respondingCharacter = in.readString();
						String card = in.readString(); // May be "NoCard"
						
						//TODO: Log the event. May required special handling if I was the suggester.
					}
					else if(messageType.compareTo("SuggestionRoundComplete") == 0)
					{
						//TODO: Log the event.
					}
					else if(messageType.compareTo("AccusationMade") == 0)
					{
						String accusingCharacter = in.readString();
						
						String accusationCharacter = in.readString();
						String accusationRoom = in.readString();
						String accusationWeapon = in.readString();
						String accuracy = in.readString(); // "Correct" or "Incorrect"
												
						//TODO: Log the event.
					}
					else if(messageType.compareTo("EndGame") == 0)
					{
						String reason = in.readString();
						
						//TODO: The game is over. Display the reason.
					}
					else
					{
						System.out.println("Socket Message Error: " + messageType);
					}
		        }
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			connected = false;
		}
	}
	
	public boolean waitForConnection(int timeout)
	{
		int seconds = 0;
		
		while( (game.clientConnection.connected == false) && (seconds < timeout) )
		{
			System.out.println("Not connected yet...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			seconds++;
		}
		
		return game.clientConnection.connected;
	}
	
	public synchronized void sendSelectCharacter(String characterName)
	{
		try {
			out.writeString("SelectCharacter");
			out.writeString(characterName);
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
	
	public synchronized void sendMakeMove(String location)
	{
		try {
			out.writeString("MakeMove");
			out.writeString(location);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void sendMakeSuggestion(String character, String room, String weapon)
	{
		try {
			out.writeString("MakeSuggestion");
			out.writeString(character);
			out.writeString(room);
			out.writeString(weapon);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void sendRespondToSuggestion(String card)
	{
		try {
			out.writeString("RespondToSuggestion");
			out.writeString(card);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void sendMakeAccusation(String character, String room, String weapon)
	{
		try {
			out.writeString("MakeAccusation");
			out.writeString(character);
			out.writeString(room);
			out.writeString(weapon);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void sendEndTurn()
	{
		try {
			out.writeString("EndTurn");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}

