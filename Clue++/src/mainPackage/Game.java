package mainPackage;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import gameboard.CardGUI;
import gameboard.ChecklistGUI;
import gameboard.ChoiceSelectionGUI;
import gameboard.GameBoardGUI;

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
	public boolean isSetupComplete = false;
	
	// This is probably not especially useful after startup. We should ignore it most of the time.
	public boolean iAmServer = false;
	
	// Game state info
	public boolean iAmFirstPlayer = false;
	public boolean myTurn = false;
	public int connectedPlayers = 0;	
	
	// Traits assigned to my person
	public Character myCharacter;
	public ArrayList<Card> myCards;
	
	// A reference deck with all possible cards.
	public Deck deck = new Deck();
	
	// The mapping of rooms and players.
	public Map map = new Map();	
	
	// GUI Stuff
	GameBoardGUI gameBoardGui;
    CardGUI cardGui;
    ChecklistGUI checklistGui;
    public ChoiceSelectionGUI choiceGui;
    
    public JFrame frame;
    
	private Game() {
		
	}

	public static Game getInstance() {
		if(instance == null) {
			instance = new Game();
		}

		return instance;
	}
	
	public boolean haveCard(String name)
	{
		for(Card c : myCards)
		{
			if(c.name.compareTo(name) == 0)
				return true;
		}
		
		return false;
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
				choice = JOptionPane.showConfirmDialog(frame, "Do you want to upload custom names or images?", "Customize", JOptionPane.YES_NO_OPTION);
				ArrayList<Card> myCards = new ArrayList<Card>();
				int count = 0;
				if (choice == JOptionPane.YES_OPTION) {
					while (choice == JOptionPane.YES_OPTION) {
						JPanel customChoice = new JPanel();
						String[] items = new String[]{"Character", "Weapon", "Room"};
						customChoice.add(new JLabel("Choose an item to customize"));
						JComboBox<String> itemBox = new JComboBox<String>(items);
						customChoice.add(itemBox);
						Object[] option = {"Okay", "Cancel"};
						int result = JOptionPane.showOptionDialog(frame, customChoice, "Choose an item to customize", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option[0]);
						if (result == 0) {
							if ("Character".equals((String) itemBox.getSelectedItem())) {
								uploadCustomGraphics(frame, "Character");
							} else if ("Weapon".equals((String) itemBox.getSelectedItem())) {
								uploadCustomGraphics(frame, "Weapon");
							} else {
								uploadCustomGraphics(frame, "Room");
							}
						} else {
							choice = JOptionPane.NO_OPTION;
						}
						
					}
				} 
				
				game.isSetupComplete = true;
            }
        };
        SwingUtilities.invokeLater(run);
    }
    
    protected static void uploadCustomGraphics(JFrame frame, String itemType) {
    	final JPanel choicePanel = new JPanel();
    	choicePanel.add(new JLabel("Choose an item to customize"));
    	DefaultComboBoxModel<String> model = null;
    	if ("Character".equals(itemType)) {
    		model = new DefaultComboBoxModel<String>(Character.getValues());
    	} else if ("Weapon".equals(itemType)) {
    		model = new DefaultComboBoxModel<String>(Weapon.getValues());
    	} else {
    		model = new DefaultComboBoxModel<String>(Location.getValues());
    	}
    	JComboBox<String> nameList = new JComboBox<>(model);
    	choicePanel.add(nameList);
    	choicePanel.add(new JLabel("Item's new name"));
    	JTextField newName = new JTextField();
    	choicePanel.add(newName);
    	JButton button = new JButton("Choose an image to upload");
    	final JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
		chooser.setFileFilter(filter);
		final JTextField pathField = new JTextField();
    	button.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			chooser.showOpenDialog(choicePanel);
    			pathField.setText(chooser.getSelectedFile().getAbsolutePath());
    		}
    	});
    	choicePanel.add(pathField);
    	choicePanel.add(button);
    	choicePanel.setLayout(new GridLayout(3, 2));
    	Object[] option = {"Okay"};
    	JOptionPane.showOptionDialog(frame, choicePanel, "Customize", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option[0]);
    	String characterChosen = (String) nameList.getSelectedItem();
    	File imageChosen = chooser.getSelectedFile();
    	Card card = Game.getInstance().deck.findCard(characterChosen);
		card.filePath = imageChosen.getAbsolutePath();
		card.alias = newName.getText();
		Game.getInstance().checklistGui = new ChecklistGUI();
		Game.getInstance().checklistGui.getGui().revalidate();
		Game.getInstance().checklistGui.getGui().repaint();
		frame.repaint();
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
		
		// Set all buttons to disabled and make start game invisible
		game.choiceGui.setInvisible();
		game.choiceGui.setStartInvisible();
		
		frame.add(eastPanel);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Need Exit to kill all the socket threads. Not going for graceful
		frame.setLocationByPlatform(true);
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		frame.setVisible(true);
		return frame;
	}
	
	public Location selectOnBoard(List<Location> availableMoves) {
		return gameBoardGui.getMovementChoice(availableMoves, map);
	}
}