package gameboard;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ChoiceSelectionGUI {
	 private final JPanel gui = new JPanel();
	 
	 // Add other buttons here as needed
	 JButton startGameBtn = new JButton("Start Game");
	 JButton makeSuggestionBtn = new JButton("Make Suggestion");
	 JButton rspToSuggestionBtn = new JButton("Respond To Suggestion");

	 
	 public ChoiceSelectionGUI() {
	        initializeGui();
	    }
	 
	 public final void initializeGui() {
		 gui.add(startGameBtn);
		 gui.add(makeSuggestionBtn);
		 gui.add(rspToSuggestionBtn);
	 }
	 
	 public final JComponent getGui() {
	        return gui;
	 }
}
