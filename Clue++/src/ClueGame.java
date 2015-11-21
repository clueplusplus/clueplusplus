
import gameboard.ClueGUI;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Kicks of the start of the game.
 */
public class ClueGame {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                ClueGUI clue = new ClueGUI();
                JFrame frame = new JFrame("Clue...Less");
                frame.add(clue.getGui());
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