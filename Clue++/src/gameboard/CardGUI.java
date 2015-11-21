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

public class CardGUI {
	private final JPanel gui = new JPanel();
	private JPanel cardBoard;
	private final List<String> cardImagePaths = new ArrayList<String>(
			Arrays.asList("Characters/ColonelMustard", "Characters/MissScarlet", "Characters/MrGreen",
					"Characters/MrsPeacock", "Characters/MrsWhite", "Characters/ProfessorPlum", "Rooms/Ballroom",
					"Rooms/BilliardRoom", "Rooms/Conservatory", "Rooms/DiningRoom", "Rooms/Hall", "Rooms/Kitchen",
					"Rooms/Library", "Rooms/Lounge", "Rooms/Study", "Weapons/CandleStick", "Weapons/Knife",
					"Weapons/LeadPipe", "Weapons/Revolver", "Weapons/Rope", "Weapons/Wrench"));

	public CardGUI() {
		initializeGui();
	}
	
    /**
     * @return The GUI component.
     */
    public final JComponent getGui() {
        return gui;
    }

	private void initializeGui() {
		List<String> myCards = getRandom10();
		displayCards(myCards);
	}

	private void displayCards(List<String> myCards) {
		createCardBoard();
		setCardBoardScope(new Color(204, 119, 34));
		setupCardBoard(myCards);
	}
	
	 /**
     * Sets the scope of the game board with the color.
     * 
     * @param backgroundColor The color to set.
     */
    private void setCardBoardScope(Color backgroundColor) {
        JPanel boardScope = new JPanel(new GridBagLayout());
        boardScope.setBackground(backgroundColor);
        boardScope.add(cardBoard);
        gui.add(boardScope);
    }

	private void createCardBoard() {
		cardBoard = new JPanel(new GridLayout(1, 10)); 
	}

	/**
	 * Sets up the game board to have the proper images at each location.
	 */
	private void setupCardBoard(List<String> cards) {
		for (String card : cards) {
			// Create a button with the correct image.
			JLabel label = new JLabel();
			label.setIcon(new ImageIcon(getImage(card).getScaledInstance(160, 200, BufferedImage.TYPE_INT_ARGB)));
			cardBoard.add(label);
		}
	}

	private Image getImage(String card) {
		InputStream cardStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/Cards/" + card + ".jpg");
        BufferedImage cardImage = null;
        try {
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

	private List<String> getRandom10() {
		List<String> shuffledCardPaths = cardImagePaths;
		Collections.shuffle(shuffledCardPaths);
		return shuffledCardPaths.subList(0, 7);
	}
}
