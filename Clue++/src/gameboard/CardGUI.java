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

/**
 * GUI to display the cards.
 */
public class CardGUI {
	private final JPanel gui = new JPanel();
	private JPanel cardBoard;
	private final List<String> cardImagePaths = new ArrayList<String>(
			Arrays.asList("Characters/ColonelMustard", "Characters/MissScarlet", "Characters/MrGreen",
					"Characters/MrsPeacock", "Characters/MrsWhite", "Characters/ProfessorPlum", "Rooms/Ballroom",
					"Rooms/BilliardRoom", "Rooms/Conservatory", "Rooms/DiningRoom", "Rooms/Hall", "Rooms/Kitchen",
					"Rooms/Library", "Rooms/Lounge", "Rooms/Study", "Weapons/CandleStick", "Weapons/Knife",
					"Weapons/LeadPipe", "Weapons/Revolver", "Weapons/Rope", "Weapons/Wrench"));

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
		// TODO: pull correct cards.
		List<String> myCards = getRandom10();
		displayCards(myCards);
	}

	/**
	 * Displays the cards on the board.
	 * 
	 * @param myCards The cards to display.
	 */
	private void displayCards(List<String> myCards) {
		createCardBoard();
		setCardBoardScope();
		setupCardBoard(myCards);
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
	private void setupCardBoard(List<String> cards) {
		for (String card : cards) {
			// Create a button with the correct image.
			JLabel label = new JLabel();
			label.setIcon(new ImageIcon(getImage(card).getScaledInstance(160, 200, BufferedImage.TYPE_INT_ARGB)));
			cardBoard.add(label);
		}
	}

	/**
	 * Gets the correct image for the card to display.
	 * 
	 * @param card The card to display.
	 * @return The image.
	 */
	private Image getImage(String card) {
		// Load the correct input stream.
		InputStream cardStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/Cards/" + card + ".jpg");
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

	/**
	 * Temp method to choose cards.
	 * 
	 * @return The list of card names.
	 */
	private List<String> getRandom10() {
		List<String> shuffledCardPaths = cardImagePaths;
		Collections.shuffle(shuffledCardPaths);
		return shuffledCardPaths.subList(0, 7);
	}
}