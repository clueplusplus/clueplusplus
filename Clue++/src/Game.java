
import gameboard.CardGUI;
import gameboard.ChecklistGUI;
import gameboard.GameBoardGUI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Kicks of the start of the game.
 */
public class Game {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                GameBoardGUI gameBoardGui = new GameBoardGUI();
                CardGUI cardGui = new CardGUI();
                ChecklistGUI checklistGui = new ChecklistGUI();
                JFrame frame = new JFrame("Clue...Less");
                frame.add(gameBoardGui.getGui(), BorderLayout.WEST);
                frame.add(cardGui.getGui(), BorderLayout.SOUTH);
                frame.add(checklistGui.getGui(), BorderLayout.EAST);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocationByPlatform(true);
                frame.pack();
                frame.setMinimumSize(frame.getSize());
                frame.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(run);
    }
}