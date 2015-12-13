package gameboard;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ChoiceSelectionGUI {
	 private final JPanel gui = new JPanel();
	 
	 // Add other buttons here as needed
	 public JButton startGameBtn = new JButton("Start Game");
	 public JButton makeSuggestionBtn = new JButton("Make Suggestion");
	 public JButton makeAccusationBtn = new JButton("Make Accusation");
	 public JButton rspToSuggestionBtn = new JButton("Respond To Suggestion");
	 

	 public ChoiceSelectionGUI() {
	        initializeGui();
	    }
	 
	 public final void initializeGui() {
		 gui.add(startGameBtn);
		 gui.add(makeSuggestionBtn);
		 gui.add(makeAccusationBtn);
		 gui.add(rspToSuggestionBtn);
	 }
	 
	 public final JComponent getGui() {
	        return gui;
	 }
	 
	 public void setInvisible(){
		 this.gui.setVisible(false);
	 }
	 
	 public void setVisible(){
		 this.gui.setVisible(true);
	 }
	 
	 public void setAllOptionsInvisible(){
		 makeSuggestionBtn.setVisible(false);
		 makeAccusationBtn.setVisible(false);
		 rspToSuggestionBtn.setVisible(false);
	 }
	 
	 public void setAllOptionsVisible(){
		 makeSuggestionBtn.setVisible(true);
		 makeAccusationBtn.setVisible(true);
		 rspToSuggestionBtn.setVisible(true);
	 }
	 
	 public void setRoomOptionsInvisible(){
		 makeSuggestionBtn.setVisible(false);
		 makeAccusationBtn.setVisible(false);
		 rspToSuggestionBtn.setVisible(false);
	 }

	 public void setRoomOptionsVisible(){
		 makeSuggestionBtn.setVisible(true);
		 makeAccusationBtn.setVisible(true);
		 rspToSuggestionBtn.setVisible(true);
	 }
	 
	 
	 
	 public void setStartInvisible(){
		 this.startGameBtn.setVisible(false);
	 }
	 
	 public void setStartVisible(){
		 this.startGameBtn.setVisible(true);
	 }
}
