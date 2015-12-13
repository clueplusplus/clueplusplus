package mainPackage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;

import mainPackage.utility.CustomUtility;


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
					if (messageType.compareTo("Customize") == 0) {
						while (!game.isSetupComplete) {
							Thread.sleep(1000);
						}
					} else if(messageType.compareTo("AvailableCharacterList") == 0 )
					{
						
						// Get additional message data. -- this needs to be called to empty the input data stream
						java.util.Map<String, String> availableCharacters = new HashMap<String, String>();
						int count = in.readInt();
						for(int x=0; x<count; x++)
						{
							String character = in.readString();
							availableCharacters.put(character, getAlias(character));
						}
						
						// Show how many players have joined so far.
						if(count != 6)
							game.choiceGui.addTextLine("There are " + (6-count) + " players in the game.");
						
						if (!comLock)
						{
							// Lock this until we get a response whether valid or invalid -- should probably add a timeout for the lock
							comLock = true;
						
							
							JRadioButton[] buttons = new JRadioButton[availableCharacters.size()];
							
							int idx=0;
							for (String character : availableCharacters.keySet()) {
								//System.out.println(character);
								buttons[idx] = new JRadioButton(availableCharacters.get(character), idx==0);
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
									
									this.sendSelectCharacter(CustomUtility.revereseMapLookup(radioButton.getText(), availableCharacters));
								}
								idx++;
							}
							
							game.choiceGui.setVisible();

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
						game.iAmFirstPlayer = true;
						game.choiceGui.setStartVisible();			
						
						// Moved button code to ChoiceSelectionGui.
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
						game.choiceGui.addTextLine("My Character: "+ myCharacterName);
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
						
						game.cardGui.loadCardImages(game.myCards);						
												
					}
					else if(messageType.compareTo("StartGame") == 0)
					{
						//TODO: Start the game! Make sure gui functions are in gui thread.
						game.choiceGui.addTextLine("Game has started!");
					}
					else if(messageType.compareTo("StartTurn") == 0)
					{
						// Read the character who's turn it is.
						String currentCharacterName = in.readString();
						
						//TODO: Check if character is me, if so start my turn.
						String myCharacterName = game.myCharacter.name;
						if(myCharacterName.equals(currentCharacterName))
						{
							game.myTurn = true;
							
							// Start my turn. Make sure to call gui functions in gui thread.
							System.out.println("It is my turn.  I am " + myCharacterName + " and I am in " + game.map.getCharacter(myCharacterName).location.name);
							game.choiceGui.addTextLine("It is my turn.  I am " + myCharacterName + " and I am in " + game.map.getCharacter(myCharacterName).location.name + " and I need to make a move.");
							
							//TODO option to stay/make suggestion if automatically in room from some else's suggestion

							//TODO make a move on the board
							List<Location> moveOptions = game.map.getCharacter(myCharacterName).location.getAvailableMovementOptions();
							Location moveChoice = game.selectOnBoard(moveOptions);
							game.map.moveCharacter(myCharacterName, moveChoice.name);
							sendMakeMove(moveChoice.name);
							game.choiceGui.addTextLine("I moved to " + moveChoice.name);
							
							//TODO make suggestion if in a room.
							if(game.myCharacter.location.isRoom())
							{
								game.choiceGui.addTextLine("I now need to make a suggestion.");
								game.choiceGui.makeSuggestionBtn.setEnabled(true);
							}
							else
							{
								game.choiceGui.addTextLine("I cannot make a suggestion. I need to make an accusation or end my turn.");
								game.choiceGui.makeSuggestionBtn.setEnabled(false);
								game.choiceGui.makeAccusationBtn.setEnabled(true);
								game.choiceGui.endTurnBtn.setEnabled(true);
							}
							
							
							// Game flow follows from state machine actions.
						}
					}
					else if(messageType.compareTo("NotifyMove") == 0)
					{
						// Get the information about whose turn it is.
						String character = in.readString();
						String location = in.readString();
						
						game.choiceGui.addTextLine(character + " moved to " + location);
						
						// Update the map.
						game.map.moveCharacter(character, location);
						
						System.out.println(game.myCharacter.location.name);
						// Update Choice Options
						if(!game.myCharacter.location.name.contains("Hallway")){
							game.choiceGui.setAllOptionsVisible();
							
						}
						else{
							game.choiceGui.setAllOptionsInvisible();
						}
							
						
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
						game.choiceGui.addTextLine(suggestingCharacter + " suggested that it was " + suggestionCharacter + " in the " + suggestionRoom + " with the " + suggestionWeapon + ". It is " + characterToRespond + "'s turn to respond.");
						
						// Update the map since a character gets moved for this.
						game.map.moveCharacter(suggestionCharacter, suggestionRoom);
					}
					else if(messageType.compareTo("ForwardResponseToSuggestion") == 0)
					{
						String respondingCharacter = in.readString();
						String card = in.readString(); // May be "NoCard"
						
						//Log the event.
						
						// If I was suggesting.
						if(card.compareTo("NoCard") == 0)
						{
							game.choiceGui.addTextLine(respondingCharacter + " could not show a card." );
						}
						else if(true) // If I suggested then I get to see which card it was.
						{
							game.choiceGui.addTextLine(respondingCharacter + " showed: " + card);
							
							// TODO: Special handling if I was the suggester.
						}
						else // If I was not suggesting I just know there was a card shown.
						{
							game.choiceGui.addTextLine(respondingCharacter + " showed a card");
						}
						
					}
					else if(messageType.compareTo("SuggestionRoundComplete") == 0)
					{
						// TODO: special handling if i was the suggestor. Allow me to accuse or end turn.
						game.choiceGui.addTextLine("The suggestion round is complete.");
					}
					else if(messageType.compareTo("AccusationMade") == 0)
					{
						String accusingCharacter = in.readString();						
						String accusationCharacter = in.readString();
						String accusationRoom = in.readString();
						String accusationWeapon = in.readString();
						String accuracy = in.readString(); // "Correct" or "Incorrect"
												
						//TODO: Log the event. Gui actions in gui thread.
						game.choiceGui.addTextLine(accusingCharacter + " accused " + accusationCharacter + " in the " + accusationRoom + " with the " + accusationWeapon + ".");
						
						if(accuracy.compareTo("Correct") == 0)
						{
							game.choiceGui.addTextLine("They were correct!");
						}
						else
						{
							game.choiceGui.addTextLine("They were incorrect :(");
						}
						
					}
					else if(messageType.compareTo("EndGame") == 0)
					{
						String reason = in.readString();
						
						game.choiceGui.addTextLine("The game is over because: " + reason);
						
						//TODO: Clean up the game.
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
	
	private String getAlias(String character) {
		for (Card card: game.deck.cards) {
			if (character.equals(card.name)) {
				return card.alias;
			}
		}
		return null;
	}
	
	private String getStandard(String alias) {
		for (Card card: game.deck.cards) {
			if (alias.equals(card.alias)) {
				return card.name;
			}
		}
		return null;
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

