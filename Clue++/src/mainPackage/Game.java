package mainPackage;

import gameboard.CardGUI;
import gameboard.ChecklistGUI;
import gameboard.GameBoardGUI;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;

/**
 * Kicks off the start of the game. Holds important data.
 */
public class Game {

    /**
     * @param args
     */
	// To make sure we can get access to this whenever. Probably not the cleanest idea.
	static private Game instance = null;
	
	// Connection information.
	public String ipAddr = "localhost";
	public int port = 1234;
	public SocketServer socketServer;
	public SocketClientConnection clientConnection;
	
	// This is probably not especially usefull after startup. We should ignore it most of the time.
	public boolean iAmServer = false;
	
	// Traits assigned to my person
	Character myCharacter;
	ArrayList<Card> myCards;
	
	// A reference deck with all possible cards.
	public Deck deck = new Deck();
	
	// The mapping of rooms and players.
	public Map map = new Map();	
	
	// GUI Stuff
	GameBoardGUI gameBoardGui;
    CardGUI cardGui;
    ChecklistGUI checklistGui;
    JFrame frame;
    
	private Game() {
		
	}

	public static Game getInstance() {
		if(instance == null) {
			instance = new Game();
		}

		return instance;
	}

	private Game initializeGUIComponents() {
		this.gameBoardGui = new GameBoardGUI();
		this.cardGui = new CardGUI();
		this.checklistGui = new ChecklistGUI();
		this.frame = new JFrame("Clue...Less");
		
		return this;
	}
	
    public static void main(String[] args) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
            	Game game = Game.getInstance().initializeGUIComponents();
				JFrame frame = createGameJFrame(game);
                
                // Is this going to run as the server?                
                Object[] options = {"Server", "Client"};
				int choice = JOptionPane.showOptionDialog(frame,
						"Is the server or a client?",
						"?",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,     //do not use a custom Icon
				options,  //the titles of buttons
				options[0]); //default button title
								
				if(choice == 1)
				{
					game.iAmServer = false;
					startClientConnection(game, frame);
				}
				else
				{
					game.iAmServer = true;
					startServerConnection(game);
					startClientConnection(game, frame);
				}

				// Just some test code. Delete Eventually.
				JOptionPane.showConfirmDialog(frame, "When you click OK I will move pieces on the board as a test.");
				game.map.moveCharacter(Character.missScarlet, Location.BilliardRoom);
				game.map.moveCharacter(Character.colMustard, Location.BilliardRoom);
				
				// Load some random cards to test the GUI.
				JOptionPane.showConfirmDialog(frame, "When you click OK I will load some cards as a test.");
				ArrayList<Card> myCards = new ArrayList<Card>();				
				myCards.add(Game.getInstance().deck.getRandomCard(Card.CharacterType));
				myCards.add(Game.getInstance().deck.getRandomCard(Card.CharacterType));
				myCards.add(Game.getInstance().deck.getRandomCard(Card.LocationType));
				myCards.add(Game.getInstance().deck.getRandomCard(Card.LocationType));
				myCards.add(Game.getInstance().deck.getRandomCard(Card.WeaponType));
				myCards.add(Game.getInstance().deck.getRandomCard(Card.WeaponType));
				game.cardGui.loadCardImages(myCards);
				
				// TODO: Start selecting characters and whatnot.
				/*
				if (game.iAmServer) {
					playGameAsPlayerOne(game, frame);
				} else {
					playGameAsPlayerTwo(game);
				}
				*/
            }
        };
        SwingUtilities.invokeLater(run);
    }
    
    public void updateGameBoard()
    {
    	SwingUtilities.invokeLater(new Runnable() {

            public void run() {

            	gameBoardGui.updateGameBoardPieces();

            }

        });
    	
    }

    // TODO: Marina to delete or modify?
	private static void playGameAsPlayerOne(Game game, JFrame frame) {
		JRadioButton mustard = new JRadioButton("Colonel Mustard", true);
		JRadioButton scarlet = new JRadioButton("Miss Scarlet", false);
		JRadioButton green = new JRadioButton("Mr. Green", false);
		JRadioButton peacock = new JRadioButton("Mrs. Peacock", false);
		JRadioButton white = new JRadioButton("Mrs. White", false);
		JRadioButton plum = new JRadioButton("Professor Plum", false);
		
		List<JRadioButton> characters = Arrays.asList(mustard, scarlet, green, peacock, white, plum);

		ButtonGroup buttonGroup = new ButtonGroup();
		for(JRadioButton radioButton : characters) {
			buttonGroup.add(radioButton);
		}

		JPanel characterSelectionPanel = new JPanel();
		for(JRadioButton radioButton : characters) {
			characterSelectionPanel.add(radioButton);
		}

		
		//ImageIcon characterPieceIcon = new ImageIcon(game.getClass().getResource("resources/misc_images/clue_game_piece.png"));

		Object[] option = {"Okay"};
		//int choice = JOptionPane.showOptionDialog(frame, characterSelectionPanel, "Select Your Character", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, characterPieceIcon, option, option[0]);
		int choice = 1;
		
		String selectedCharacter = "Colonel Mustard";

		if (choice == 0) {
			for (JRadioButton radioButton : characters) {
				if (radioButton.isSelected()) {
					selectedCharacter = radioButton.getText();
				}
			}
		}

		System.out.println("Selected: " + selectedCharacter);
		
		boolean gameOver = false;
		while (!gameOver) {
			gameOver = takeMyTurn(game);
			if (gameOver) {
				break;
			}
			gameOver = partnerTakeTurn(game);
		}

		//TODO DELETE CODE WITHIN STARS and end game properly
		/***/
		System.out.println("Game over!");
		System.exit(0);
		/***/
	}

	private static void playGameAsPlayerTwo(Game game) {
		//TODO choose a character
		//TODO distribute cards to players
		//TODO distribute cards to solution folder

		boolean gameOver = false;
		while (!gameOver) {
			gameOver = partnerTakeTurn(game);
			if (gameOver) {
				break;
			}
			gameOver = takeMyTurn(game);
		}

		//TODO DELETE CODE WITHIN STARS and end game properly
		/***/
		System.out.println("Game over!");
		System.exit(0);
		/***/
	}

	private static boolean takeMyTurn(Game game) {
		//TODO DELETE CODE WITHIN STARS and fill out what you do during a turn here
		/***/
		System.out.println("Continue for you?");
		Scanner scanner = new Scanner(System.in);
		String in = scanner.nextLine();
		if (in.trim().equals("0")) {
			return true;
		}
		/***/

		return false; //return false if game should continue
	}

	private static boolean partnerTakeTurn(Game game) {
		//TODO DELETE CODE WITHIN STARS and fill out receiving partner's turn information here
		/***/
		System.out.println("Continue for partner?");
		Scanner scanner = new Scanner(System.in);
		String in = scanner.nextLine();
		if (in.trim().equals("0")) {
			return true;
		}
		/***/

		return false; //return false if game should continue
	}

	private static void startServerConnection(Game game) {
		// Initialize the server connection
		game.socketServer = new SocketServer();
		game.socketServer.startServer(game.port);
	}

	private static void startClientConnection(Game game, JFrame frame) {
		// Initialize client connection.
		if(!game.iAmServer)
		{
			String s = (String)JOptionPane.showInputDialog(
	                            frame,
	                            "What is the server IP?",
	                            "IP Address?",
	                            JOptionPane.PLAIN_MESSAGE,
	                            null,
	                            null,
	                            game.ipAddr);
			
			game.ipAddr = s;
		}
		
		// Attempt to connect to the server. Note there is no error checking.
		game.clientConnection = new SocketClientConnection();
		game.clientConnection.connect(game.ipAddr, game.port);

		// My test to see if the connection works or not.
		if(game.clientConnection.waitForConnection(10))
        {
            System.out.println("Connected!");
        }
        else
        {
            System.out.println("The IP Address is probably wrong...");
            game.clientConnection.attemptToConnect = false;
        }
	}

	private static JFrame createGameJFrame(Game game) {
		GameBoardGUI gameBoardGui = game.gameBoardGui;
		CardGUI cardGui = game.cardGui;
		ChecklistGUI checklistGui = game.checklistGui;

		JFrame frame = game.frame;

		frame.add(gameBoardGui.getGui(), BorderLayout.WEST);
		frame.add(cardGui.getGui(), BorderLayout.SOUTH);
		frame.add(checklistGui.getGui(), BorderLayout.EAST);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Need Exit to kill all the socket threads. Not going for graceful
		frame.setLocationByPlatform(true);
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		frame.setVisible(true);
		return frame;
	}
}