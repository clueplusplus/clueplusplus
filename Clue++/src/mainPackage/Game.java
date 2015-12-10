package mainPackage;

import gameboard.CardGUI;
import gameboard.ChecklistGUI;
import gameboard.ChoiceSelectionGUI;
import gameboard.GameBoardGUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
    ChoiceSelectionGUI choiceGui;
    
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
		this.choiceGui = new ChoiceSelectionGUI();
		this.frame = new JFrame("Clue...Less");
		
		return this;
	}
	
    public static void main(String[] args) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
            	Game game = Game.getInstance().initializeGUIComponents();
				JFrame frame = createGameJFrame(game);
                
                // TODO Check if a game server already exists, what happens if two players choose to be servers?                
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
				//JOptionPane.showConfirmDialog(frame, "When you click OK I will move pieces on the board as a test.");
				//game.map.moveCharacter(Character.missScarlet, Location.BilliardRoom);
				//game.map.moveCharacter(Character.colMustard, Location.BilliardRoom);
				
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
		ChoiceSelectionGUI choiceGui = game.choiceGui;
		
		JFrame frame = game.frame;

		frame.add(gameBoardGui.getGui(), BorderLayout.WEST);
		frame.add(cardGui.getGui(), BorderLayout.SOUTH);
		
		// East panel has to contain the checkbox as well as the choice selection panel
		JPanel eastPanel = new JPanel(new GridLayout(4,1));
		eastPanel.add(checklistGui.getGui());
		eastPanel.add(choiceGui.getGui());
		
		
		frame.add(eastPanel);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Need Exit to kill all the socket threads. Not going for graceful
		frame.setLocationByPlatform(true);
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		frame.setVisible(true);
		return frame;
	}
	
	
}