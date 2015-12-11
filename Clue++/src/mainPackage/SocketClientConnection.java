package mainPackage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;


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
	public boolean comLock = false; // Added this communication lock to prevent flooding the server with the same query while waiting for a response
	
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
		// Note this will keep trying to connect until cancelled.
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
				attemptToConnect = false; // Do not reconnect if we lose connection.
				while(socket.isConnected())
		        {	
					// Parse messages
					String messageType = in.readString();
					
					System.out.println("Client Received: " + messageType);
					
					// Changing the order here. First pick character
					if(messageType.compareTo("AvailableCharacterList") == 0 )
					{
						
						// Get additional message data. -- this needs to be called to empty the input data stream
						ArrayList<String> availableCharacters = new ArrayList<String>();
						int count = in.readInt();
						for(int x=0; x<count; x++)
						{
							availableCharacters.add(in.readString());
						}
						
						if (!comLock)
						{
							// Lock this until we get a response whether valid or invalid -- should probably add a timeout for the lock
							comLock = true;
						
							
							JRadioButton[] buttons = new JRadioButton[availableCharacters.size()];
							
							int idx=0;
							for (String character : availableCharacters) {
								//System.out.println(character);
								buttons[idx] = new JRadioButton(character, idx==0);
								idx++;
							} 
							
							ButtonGroup buttonGroup = new ButtonGroup();
							for(JRadioButton button : buttons) {
								buttonGroup.add(button);
							}
							
							JPanel characterSelectionPanel = new JPanel();
							for(JRadioButton button : buttons) {
								characterSelectionPanel.add(button);
							}
	
							Object[] option = {"Okay"};
							int choice = JOptionPane.showOptionDialog(game.frame, characterSelectionPanel, "Select Your Character", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option[0]);
							
							idx = 0;
							for (JRadioButton radioButton : buttons) {
								if (radioButton.isSelected()) {
									this.sendSelectCharacter(availableCharacters.get(idx));
								}
								idx++;
							}

							//TODO: Update list of connected players based on missing characters.
							
							//TODO: If I don't already have a character selected give me the option to choose one.
							// There is a small chance we could be in the process of waiting for validation
							// of a character selection when we get this message. If that happens we don't
							// want to select another character. The server will not figure this problem out
							// and we will get 2 seats.
							}
					}
					
					
					else if(messageType.compareTo("YouAreFirstPlayer") == 0)
					{
						//TODO: Record this. I will need to start the game when ready. I will need a button for this.
						
					}
					else if(messageType.compareTo("InvalidCharacter") == 0)
					{
						// No more reading of message required.
					    comLock = false;	
						//TODO: Notify user the selection was invalid. Allow user to choose another one.
					}
					else if(messageType.compareTo("YourCharacter") == 0)
					{
						// This means the server has validated out choice.
						String myCharacterName = in.readString();
						System.out.println("Your Character: "+ myCharacterName);
						// Look up my character on the map and save it for reference.
						game.myCharacter = game.map.getCharacter(myCharacterName);
					}
					else if(messageType.compareTo("YourCards") == 0)
					{
						// Get the list of cards
						game.myCards = new ArrayList<Card>();
						int count = in.readInt();
						for(int x=0; x<count; x++)
						{
							// Look up the card in a reference deck, this will load the type of card and any other data in there.
							game.myCards.add(game.deck.findCard(in.readString()));
						}
						
						//TODO: Save these cards somewhere? Move to next step of game?						
					}
					else if(messageType.compareTo("StartGame") == 0)
					{
						//TODO: Start the game! Make sure gui functions are in gui thread.
					}
					else if(messageType.compareTo("StartTurn") == 0)
					{
						// Read the character who's turn it is.
						String currentCharacterName = in.readString();
						
						//TODO: Check if character is me, if so start my turn.
						String myCharacterName = game.myCharacter.name;
						if(myCharacterName.equals(currentCharacterName))
						{
							// Start my turn. Make sure to call gui functions in gui thread.
							System.out.println("It is my turn.  I am " + myCharacterName + " and I am in " + game.map.getCharacter(myCharacterName).location.name);

							//TODO option to stay/make suggestion if automatically in room from some else's suggestion

							//TODO make a move on the board
							List<Location> moveOptions = game.map.getCharacter(myCharacterName).location.getAvailableMovementOptions();
							Location moveChoice = game.selectOnBoard(moveOptions);
							game.map.moveCharacter(myCharacterName, moveChoice.name);
							sendMakeMove(moveChoice.name);

							//TODO make suggestion
							//TODO end turn
							sendEndTurn();
						}
					}
					else if(messageType.compareTo("NotifyMove") == 0)
					{
						// Get the information about whose turn it is.
						String character = in.readString();
						String location = in.readString();
						
						// Update the map.
						game.map.moveCharacter(character, location);
					}
					else if(messageType.compareTo("SuggestionNotification") == 0)
					{
						// Additional message data
						String suggestingCharacter = in.readString();
						String characterToRespond = in.readString();						
						String suggestionCharacter = in.readString();
						String suggestionRoom = in.readString();
						String suggestionWeapon = in.readString();
						
						//TODO: Log the event. Respond if I am the characterToRespond. Gui functions in gui thread.
						
						// Update the map since a character gets moved for this.
						game.map.moveCharacter(suggestionCharacter, suggestionRoom);
					}
					else if(messageType.compareTo("ForwardResponseToSuggestion") == 0)
					{
						String respondingCharacter = in.readString();
						String card = in.readString(); // May be "NoCard"
						
						//TODO: Log the event. May required special handling if I was the suggester.
						// Gui actions in gui thread.
					}
					else if(messageType.compareTo("SuggestionRoundComplete") == 0)
					{
						//TODO: Log the event. May required special handling if I was the suggester. Remember I need to end my own turn.
						// Make sure gui actions in gui thread.
					}
					else if(messageType.compareTo("AccusationMade") == 0)
					{
						String accusingCharacter = in.readString();						
						String accusationCharacter = in.readString();
						String accusationRoom = in.readString();
						String accusationWeapon = in.readString();
						String accuracy = in.readString(); // "Correct" or "Incorrect"
												
						//TODO: Log the event. Gui actions in gui thread.
					}
					else if(messageType.compareTo("EndGame") == 0)
					{
						String reason = in.readString();
						
						//TODO: The game is over. Display the reason. Gui actions in gui thread.
					}
					else
					{
						System.out.println("Client - Received Message Error: " + messageType);
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
			e.printStackTrace();
		}		
	}
	
	public synchronized void sendStartGame()
	{
		try {
			out.writeString("StartGame");
			out.flush();
		} catch (IOException e) {
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
			e.printStackTrace();
		}
	}
	
	public synchronized void sendEndTurn()
	{
		try {
			out.writeString("EndTurn");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}

