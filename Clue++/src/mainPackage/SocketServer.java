package mainPackage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class SocketServer implements Runnable
{
	// This holds the list of people connected to the server.
	private ArrayList<SocketServerConnection> connections = new ArrayList<SocketServerConnection>(0);
	
	private Thread thread;
	private int port = 1234;
	
	private ServerSocket listener;
	
	private boolean running = false;
	
	// Game Management Things
	int playersTurn = -1;	// An index into the array below to keep track of turns.
	ArrayList<PlayerSeat> seats = new ArrayList<PlayerSeat>(); 
	
	// Murder Scenario
	ArrayList<Card> murderCards;
	
	// Suggestion Round Info
	int suggestingPlayerSeat;
	int respondingPlayerSeat;	
	String suggestionCharacter;
	String suggestionRoom;
	String suggestionWeapon;
	
	public SocketServer()
	{
		seats.add(new PlayerSeat(Character.colMustard));
		seats.add(new PlayerSeat(Character.mrsWhite));
		seats.add(new PlayerSeat(Character.revGreen));
		seats.add(new PlayerSeat(Character.mrsPeacock));
		seats.add(new PlayerSeat(Character.profPlum));
		seats.add(new PlayerSeat(Character.missScarlet));		
	}
	
	public void startServer(int port)
	{
		this.port = port;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run()
	{
		try 
		{
			listener = new ServerSocket(port);
		
			System.out.println("Socket Listener Running");
			
			while(!listener.isClosed())
			{
				running = true;
				Socket socket = listener.accept();
				
				System.out.println("Connection From " + socket.getRemoteSocketAddress());
				
				SocketServerConnection c = new SocketServerConnection(this);
				synchronized(connections)
				{
					connections.add(c);
					
					if(connections.size() == 1)
						c.firstPlayer = true;
				}		
				c.startThread(socket);
			}
			
            listener.close();
        }
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		running = false;
	}
	
	public synchronized boolean isRunning()
	{
		return running;
	}
	
	public void stop()
	{
		if(!running) return;
		
		try {
			listener.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getAvailableCharacters()
	{
		ArrayList<String> list = new ArrayList<String>();
	
		synchronized(seats)
		{
			for(PlayerSeat s : seats)
			{
				if(!s.seatTaken) list.add(s.characterName);
			}
		}
		
		return list;
	}
	
	public boolean selectCharacter(SocketServerConnection c, String characterName)
	{
		boolean status = false;

		synchronized(seats)
		{
			for(PlayerSeat s : seats)
			{
				// Find the seat that matches that character.
				if(s.characterName.compareTo(characterName) == 0)
				{
					// Check if another player took that seat already.
					if(!s.seatTaken)
					{
						// Assign this person to this character.
						s.playerConnection = c;
						s.seatTaken = true;
						status = true;
						
						// Update everyone about the new character selection.
						sendAvailableCharacterList();
					}
					
					// End the loop
					break;
				}
			}
		}
		
		return status;
	}
	
	private synchronized int getPlayerSeat(SocketServerConnection c)
	{
		for(int x=0; x<seats.size(); x++)
		{
			if(seats.get(x).playerConnection == c) return x; 
		}
		
		// This better not happen.
		System.out.println("Error in getPlayerSeat");
		return -1;
	}
	
	public synchronized void processSuggestion(SocketServerConnection c, String character, String room, String weapon)
	{
		suggestingPlayerSeat = getPlayerSeat(c);
		respondingPlayerSeat = suggestingPlayerSeat + 1;
		suggestionCharacter = character; 
		suggestionRoom = room;
		suggestionWeapon = weapon;
		
		sendNextSuggestionResponseRequest();
	}
	
	public synchronized void sendNextSuggestionResponseRequest()
	{
		while(respondingPlayerSeat != suggestingPlayerSeat)
		{
			if(respondingPlayerSeat == seats.size())
			{
				suggestingPlayerSeat = 0;
			}
			else 
			{
				if(seats.get(respondingPlayerSeat).seatTaken)
				{
					sendSuggestionNotification(	seats.get(suggestingPlayerSeat).characterName, 
												seats.get(respondingPlayerSeat).characterName);				
				}
				else
				{
					respondingPlayerSeat++;
				}
			}
		}
		
		if(respondingPlayerSeat == suggestingPlayerSeat)
		{
			sendSuggestionRoundComplete();
		}
	}
	
	public synchronized void processSuggestionResponse(SocketServerConnection c, String card)
	{
		sendForwardResponseToSuggestion(c.characterName, card);
		
		if(card.compareTo("NoCard") == 0)
		{
			respondingPlayerSeat++;
			sendNextSuggestionResponseRequest();
		}
		else
		{
			sendSuggestionRoundComplete();
		}			
	}
	
	public synchronized void processAccusation(SocketServerConnection c, String character, String room, String weapon)
	{
		int accusingPlayerSeat = getPlayerSeat(c);
		
		// TODO: Figure out if cards are correct
		boolean correct = false;

		sendAccusationMade(c.characterName, character, room, weapon, correct);
		
		if(correct)
		{
			sendEndGame(c.characterName + " guessed correctly!");
			
			// TODO: Close down gracefully.
		}
		else
		{
			seats.get(accusingPlayerSeat).playerIsStillInGame = false;
		}
	}
	
	public synchronized void processStartGame(SocketServerConnection c)
	{
		// Create a new deck to distribute the cards from.
		Deck deck = new Deck();
		
		// Get the 3 cards for the murder. This also shuffles the deck
		murderCards = new ArrayList<Card>();
		murderCards.add(deck.removeRandomCard(Card.CharacterType));
		murderCards.add(deck.removeRandomCard(Card.LocationType));
		murderCards.add(deck.removeRandomCard(Card.WeaponType));
		
		// Assign cards to the players.
		int x = -1;
		Card nextCard = deck.removeNextCard();
		while(nextCard != null)
		{
			x++;
			if(x == seats.size())
				x = 0;
			
			if(seats.get(x).seatTaken)
			{
				seats.get(x).playerConnection.cards.add(nextCard);
				nextCard = deck.removeNextCard();
			}
		}
		
		// Send out the cards to the clients
		sendCardList();		
		
		// Start the game! Perhaps this step is redundant.
		sendStartGame();
		
		// Start the next turn.
		startNextTurn();
	}
	
	public synchronized void startNextTurn()
	{
		playersTurn++;
		
		if(playersTurn == seats.size()) playersTurn = 0;
		
		if(seats.get(playersTurn).seatTaken && seats.get(playersTurn).playerIsStillInGame)
		{
			sendStartTurn(seats.get(playersTurn).characterName);
		}
		else
		{
			// Easier than a loop, better not all lose. Make that check when someone loses.
			startNextTurn();
		}
		
	}
	
	public synchronized void processEndTurn(SocketServerConnection c)
	{
		startNextTurn();
	}
	
	// -----------------------Messages-----------------------------
	
	public synchronized void sendStartGame()
	{
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendStartGame();
			}
		}
	}
	public synchronized void sendAvailableCharacterList()
	{
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendAvailableCharacterList();
			}
		}
	}
	public synchronized void sendCardList()
	{
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendYourCards();
			}
		}
	}
	public synchronized void sendStartTurn(String characterWhosTurnItIs)
	{
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendStartTurn(characterWhosTurnItIs);
			}
		}
	}
	public synchronized void sendNotifyMove(String characterMoving, String location)
	{
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendNotifyMove(characterMoving, location);
			}
		}
	}
	public synchronized void sendSuggestionNotification(
			String suggestingCharacter,
			String characterToRespond)
	{
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendSuggestionNotification(	suggestingCharacter,
												characterToRespond,
												suggestionCharacter,
												suggestionRoom,
												suggestionWeapon);
			}
		}
	}
	public synchronized void sendForwardResponseToSuggestion(String characterResponding, String card)
	{
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendForwardResponseToSuggestion(characterResponding, card);
			}
		}
	}
	public synchronized void sendSuggestionRoundComplete()
	{
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendSuggestionRoundComplete();
			}
		}
	}
	public synchronized void sendAccusationMade(
			String accusingCharacter,
			String accusationCharacter,
			String accusationRoom,
			String accusationWeapon,
			boolean correct)
	{
		String accuracy;
		if(correct)
			accuracy = "correct";
		else
			accuracy = "incorrect";
		
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendAccusationMade(	accusingCharacter,
										accusationCharacter,
										accusationRoom,
										accusationWeapon,
										accuracy);
			}
		}
	}
	public synchronized void sendEndGame(String why)
	{
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendEndGame(why);
			}
		}
	}
	
	
	

}
