package gameboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
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
	
	JLabel[] labels = new JLabel[9];
	
	private JPanel cardBoard;

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
		
		// Load dummy placeholders for cards. Changed to 9 to account for worst case of 2 players.
		ArrayList<Card> myCards = new ArrayList<Card>();
		for(int x=0; x<labels.length; x++)
			myCards.add(new Card("Question Mark", 3, "Characters/Question", "Question Mark"));		
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
		
		if(cards.size() > labels.length)
		{
			System.out.println("Error in loadCardImages. To many cards.");
			return;
		}
		
		int x = 0;
		for(; x<cards.size(); x++) {
			// Create a button with the correct image.
			labels[x].setIcon(new ImageIcon(getImage(cards.get(x)).getScaledInstance(160, 200, BufferedImage.TYPE_INT_ARGB)));
		}
		
		for(; x<labels.length; x++) {
			// Create a button with the correct image.
			labels[x].setIcon(new ImageIcon(getImage(new Card("Blank", 3, "Characters/Blank", "Blank")).getScaledInstance(160, 200, BufferedImage.TYPE_INT_ARGB)));
		}
		
	}

	/**
	 * Gets the correct image for the card to display.
	 * 
	 * @param card The card to display.
	 * @return The image.
	 */
	private Image getImage(Card card) {
		InputStream cardStream = null;
		// Load the correct input stream.
        BufferedImage cardImage = null;
        try {
        	if (card.filePath.contains(".")) {
    			cardStream = new FileInputStream(new File(card.filePath));
    		} else {
    		 cardStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/Cards/" + card.filePath + ".jpg");
    		}
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