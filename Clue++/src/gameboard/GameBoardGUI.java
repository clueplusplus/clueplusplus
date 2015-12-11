package gameboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import mainPackage.Game;
import mainPackage.Location;
import mainPackage.Map;

/**
 * GUI for the game.
 */
public class GameBoardGUI {

    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private final JComponent[][] locations = new JComponent[5][5];
    private final JLabel[][] labels = new JLabel[5][5];
    private final Image[][] locationImages = new Image[5][5];
    private final Image[] gamePieceImages = new Image[6];
    private JPanel gameBoard;
    private final List<String> roomImagePaths = new ArrayList<String>(Arrays.
            asList("Study", "Hall", "Lounge", "Library", "BilliardRoom",
                    "DiningRoom", "Conservatory", "Ballroom", "Kitchen"));
    private final List<String> pieceImagePaths = new ArrayList<String>(Arrays.
            asList("RedPiece.png", "PurplePiece.png", "BluePiece.png", "GreenPiece.png", "YellowPiece.png", "WhitePiece.png"));
        
    private static final int imageSize = 150;

    /**
     * Creates an instance of the class.
     */
    public GameBoardGUI() {
        initializeGui();
    }

    /**
     * Initializes the GUI at start up.
     */
    public final void initializeGui() {
        createDefaultGameBoardImages();
        createGameBoardPanel();
        gui.setBorder(new EmptyBorder(4, 4, 4, 4));
        gameBoard.setBorder(new CompoundBorder(new EmptyBorder(1, 1, 1, 1), new LineBorder(Color.BLACK)));
        setGameBoardScope();
        setupGameBoard();
        createPhysicalGameBoard();
        updateGameBoardPieces();
    }

    /**
     * Adds the components of each location to the game board.
     */
    private void createPhysicalGameBoard() {
        /*
    	for (JComponent[] clueLocation : locations) {
            for (JComponent clueLocation1 : clueLocation) {
                gameBoard.add(clueLocation1);
            }
        }
        */
    	for(int x=0; x<locations.length; x++)
    	{
    		for(int y=0; y<locations[x].length; y++)
    		{
    			gameBoard.add(locations[x][y]);
    		}
    	}
    }

    /**
     * Sets up the game board to have the proper images at each location.
     */
    private void setupGameBoard() {
        for (int row = 0; row < locations.length; row++) {
            for (int column = 0; column < locations[row].length; column++) {
                ImageIcon icon = new ImageIcon(new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB));
                // If one of the 4 squares in the 5x5 grid that are not part of the board, create a blank label.
                if (row % 2 == 1 && column % 2 == 1) {
                    JLabel label = new JLabel();
                    label.setIcon(icon);
                    locations[row][column] = label;
                } else {
                    // Create a button with the correct image.
                    labels[row][column] = new JLabel();
                    labels[row][column].setIcon(new ImageIcon(locationImages[row][column].getScaledInstance(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB)));
                    locations[row][column] = labels[row][column];
                    locations[row][column].setToolTipText(""+row+","+column); //TODO include location name in a better way
                }
            }
        }
        
        
    }
    
    /**
     * Update the images with the game peices on them.
     */
    public void updateGameBoardPieces() {
    	
    	Game game = Game.getInstance();
    	
        for (int row = 0; row < locations.length; row++) {
            for (int column = 0; column < locations[row].length; column++) {
            	
                // If one of the 4 squares in the 5x5 grid that are not part of the board, skip it
                if(!(row % 2 == 1 && column % 2 == 1)) {
               	
                	// Use this to find the players.
                	Location location = game.map.getRoom(row, column);
                	
                	// Don't overlap the pieces when placing them inside rooms.
                	Point[] copySpot = new Point[6];
                	copySpot[0] = new Point(imageSize/3, imageSize/3);
                	copySpot[1] = new Point(2*imageSize/3, 0);
                	copySpot[2] = new Point(imageSize/3, 0);                	
                	copySpot[3] = new Point(0, imageSize/3);
                	copySpot[4] = new Point(0, 0);
                	copySpot[5] = new Point(2*imageSize/3, 2*imageSize/3);
                	
                	// Get the image of the room we will be editing.
                	BufferedImage sourceRoom = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
                	Graphics g = sourceRoom.createGraphics();
                	g.drawImage(locationImages[row][column].getScaledInstance(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB), 0, 0, null);
                	g.dispose();
                	
                	// Use this to draw the game pieces.
                	Graphics gRoom = sourceRoom.getGraphics();
                	
                	for(int x = 0; x < location.occupants.size(); x++){
                		
                		// Figure out which game piece to draw based on the character in the location.
                		mainPackage.Character c = location.occupants.get(x);
                	
                		// Get a copy of the game piece image.
                		BufferedImage gamePiece = new BufferedImage(imageSize/3, imageSize/3, BufferedImage.TYPE_INT_ARGB);
	                	Graphics gPiece = gamePiece.createGraphics();
	                	gPiece.drawImage(gamePieceImages[c.gamePieceImageIndex].getScaledInstance(imageSize/3, imageSize/3, BufferedImage.TYPE_INT_ARGB), 0, 0, null);
	                	gPiece.dispose();
	                	
	                	// Copy the game piece into it's location on the square.
	                	gRoom.drawImage(gamePiece, copySpot[x].x, copySpot[x].y, null);	                	               		                	
                	}
                	
                	// Finalize the image.
                	gRoom.finalize();                	
                	
                    // Update the button with the new image.
                    labels[row][column].setIcon(new ImageIcon(sourceRoom));
                }
            }
        }
        
        gui.repaint();
    }

    /**
     * Sets the scope of the game board.
     */
    private void setGameBoardScope() {
        JPanel boardScope = new JPanel(new GridBagLayout());
        boardScope.add(gameBoard);
        gui.add(boardScope);
    }

    /**
     * Creates the panel that displays the game board.
     */
    private void createGameBoardPanel() {
        gameBoard = new JPanel(new GridLayout(0, 5)) {
            @Override
            public final Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                Dimension prefSize = null;
                Component com = getParent();
                if (com == null) {
                    prefSize = new Dimension((int) dim.getWidth(), (int) dim.getHeight());
                } else if (com.getWidth() > dim.getWidth() && com.getHeight() > dim.getHeight()) {
                    prefSize = com.getSize();
                } else {
                    prefSize = dim;
                }
                int width = (int) prefSize.getWidth();
                int height = (int) prefSize.getHeight();
                int small = (width > height ? height : width);
                return new Dimension(small, small);
            }
        };
    }

    /**
     * @return The GUI component.
     */
    public final JComponent getGui() {
        return gui;
    }

    /**
     * Creates the default game board images.
     */
    private void createDefaultGameBoardImages() {
        InputStream hallway = Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/Locations/Hallway.jpg");
        BufferedImage biHallway = null;
        try {
            biHallway = ImageIO.read(hallway);
        } catch (IOException ex) {
            Logger.getLogger(GameBoardGUI.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                hallway.close();
            } catch (IOException ex) {
                Logger.getLogger(GameBoardGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        int count = 0;
        for (int row = 0; row < locations.length; row++) {
            for (int column = 0; column < locations[row].length; column++) {
                // If column 0, 2, or 4 in row 0, 2, or 4 use a room.
                if (row % 2 == 0 && column % 2 == 0) {
                    InputStream room = Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/Locations/" + roomImagePaths.get(count) + ".jpg");
                    BufferedImage biRoom = null;
                    try {
                        biRoom = ImageIO.read(room);
                    } catch (IOException ex) {
                        Logger.getLogger(GameBoardGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            room.close();
                        } catch (IOException ex) {
                            Logger.getLogger(GameBoardGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    locationImages[row][column] = biRoom;
                    count++;
                    // If column 1 or 3 in row 0, 2, or 4 OR column 0, 2, or 4 in rows 1 and 3 use a hallway.
                } else if ((row % 2 == 1 && column % 2 == 0) || (row % 2 == 0 && column % 2 == 1)) {
                    locationImages[row][column] = biHallway;
                }
            }
        }
        
        // Load Game Piece Images
        for (int x = 0; x < gamePieceImages.length; x++) {

            InputStream piece = Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/GamePieces/" + pieceImagePaths.get(x));
            BufferedImage biPeice = null;
            try {
            	biPeice = ImageIO.read(piece);
            } catch (IOException ex) {
                Logger.getLogger(GameBoardGUI.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                	piece.close();
                } catch (IOException ex) {
                    Logger.getLogger(GameBoardGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            gamePieceImages[x] = biPeice;
     
        }
    }

    private void clearAllMouseListenersOnBoard() {
        for (JComponent[] locationRow : locations) {
            for(JComponent location : locationRow) {
                for (EventListener el : location.getListeners(MouseListener.class)) {
                    if (el instanceof MouseListener) {
                        MouseListener listener = (MouseListener)el;
                        location.removeMouseListener(listener);
                    }
                }
            }
        }
    }

    private Location moveChoice;
    public Location getMovementChoice(final List<Location> moveOptions, Map map) {
        clearAllMouseListenersOnBoard();

        List<JComponent> moveOptionButtons = new ArrayList<>();
        for (Location moveOption : moveOptions) {
            moveOptionButtons.add(labels[moveOption.getRow()][moveOption.getCol()]);
        }

        moveChoice = null;

        for (JComponent button : moveOptionButtons) {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    Location clickedLocation = map.getRoom(Integer.parseInt(button.getToolTipText().split(",")[0]), Integer.parseInt(button.getToolTipText().split(",")[1]));
                    moveChoice = clickedLocation;
                }
            });
        }

        while (moveChoice == null) {
            try {
                Thread.sleep(200);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return moveChoice;
    }
}
