
import gameboard.CardGUI;
import gameboard.ChecklistGUI;
import gameboard.GameBoardGUI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Kicks off the start of the game. Holds important data.
 */
public class Game {

    /**
     * @param args
     */
	static private Game instance = null;
	
	public String ipAddr = "localhost";
	public int port = 1234;
	
	public boolean iAmServer = false;
	public SocketServer socketServer;
	public SocketClientConnection clientConnection;
	
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
                Object[] options = {"Server", "Client (Player)"};
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
					startClientConnection(game, frame);
				}
				else
				{
					startServerConnection(game);
				}
				
            }
        };
        SwingUtilities.invokeLater(run);
    }

	private static void startServerConnection(Game game) {
		// Initialize the server connection
		game.iAmServer = true;

		game.socketServer = new SocketServer();
		game.socketServer.startServer(game.port);
	}

	private static void startClientConnection(Game game, JFrame frame) {
		// Initialize client connection.
		game.iAmServer = false;

		String s = (String)JOptionPane.showInputDialog(
                            frame,
                            "What is the server IP?",
                            "IP Address?",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            game.ipAddr);

		// Attempt to connect to the server. Note there is no error checking.
		game.ipAddr = s;
		game.clientConnection = new SocketClientConnection();
		game.clientConnection.connect(s, game.port);

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