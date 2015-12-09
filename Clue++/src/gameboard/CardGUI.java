package gameboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mainPackage.Card;
import mainPackage.Game;

/**
 * GUI to display the cards.
 */
public class CardGUI {
	private final JPanel gui = new JPanel();
	
	JLabel[] labels = new JLabel[8];
	
	private JPanel cardBoard;
	
	/* This is no longer used. Just saved as a reference.
	private final List<String> cardImagePaths = new ArrayList<String>(
			Arrays.asList("Characters/ColonelMustard", "Characters/MissScarlet", "Characters/MrGreen",
					"Characters/MrsPeacock", "Characters/MrsWhite", "Characters/ProfessorPlum", "Rooms/Ballroom",
					"Rooms/BilliardRoom", "Rooms/Conservatory", "Rooms/DiningRoom", "Rooms/Hall", "Rooms/Kitchen",
					"Rooms/Library", "Rooms/Lounge", "Rooms/Study", "Weapons/CandleStick", "Weapons/Knife",
					"Weapons/LeadPipe", "Weapons/Revolver", "Weapons/Rope", "Weapons/Wrench"));
	*/

	/**
	 * Creates instance of the Card GUI.
	 */
	public CardGUI() {
		initializeGui();
	}
	
    /**
     * @return The GUI component.
     */
    public final JComponent getGui() {
        return gui;
    }

    /**
     * Initializes the GUI.
     */
	private void initializeGui() {
				
		createCardBoard();
		setCardBoardScope();
		setupCardBoard();
		
		// Load dummy placeholders for cards.
		ArrayList<Card> myCards = new ArrayList<Card>();				
		myCards.add(new Card("Question Mark", 3, "Characters/Question"));
		myCards.add(new Card("Question Mark", 3, "Characters/Question"));
		myCards.add(new Card("Question Mark", 3, "Characters/Question"));
		myCards.add(new Card("Question Mark", 3, "Characters/Question"));
		myCards.add(new Card("Question Mark", 3, "Characters/Question"));
		myCards.add(new Card("Question Mark", 3, "Characters/Question"));
		myCards.add(new Card("Question Mark", 3, "Characters/Question"));
		myCards.add(new Card("Question Mark", 3, "Characters/Question"));
		loadCardImages(myCards);
		
	}
	
	 /**
     * Sets the scope of the cards.
     */
    private void setCardBoardScope() {
        JPanel boardScope = new JPanel(new GridBagLayout());
        boardScope.add(cardBoard);
        gui.add(boardScope);
    }

    /**
     * Creates the card board.
     */
	private void createCardBoard() {
		cardBoard = new JPanel(new GridLayout(1, 10));
	}

	/**
	 * Sets up the card board to have the proper images.
	 */
	private void setupCardBoard() {
		for(int x=0; x<labels.length; x++) {
			// Create a button with the correct image.
			labels[x] = new JLabel();
			cardBoard.add(labels[x]);
		}
	}
	
	public void loadCardImages(List<Card> cards) {
		for(int x=0; x<cards.size(); x++) {
			// Create a button with the correct image.
			labels[x].setIcon(new ImageIcon(getImage(cards.get(x)).getScaledInstance(160, 200, BufferedImage.TYPE_INT_ARGB)));
		}
	}

	/**
	 * Gets the correct image for the card to display.
	 * 
	 * @param card The card to display.
	 * @return The image.
	 */
	private Image getImage(Card card) {
		// Load the correct input stream.
		InputStream cardStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/Cards/" + card.filePath + ".jpg");
        BufferedImage cardImage = null;
        try {
        	// Read the image.
            cardImage = ImageIO.read(cardStream);
        } catch (IOException ex) {
            Logger.getLogger(GameBoardGUI.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cardStream.close();
            } catch (IOException ex) {
                Logger.getLogger(GameBoardGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
		return cardImage;
	}
}