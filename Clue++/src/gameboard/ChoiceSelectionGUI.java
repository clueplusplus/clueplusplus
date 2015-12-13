package gameboard;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import mainPackage.Game;

public class ChoiceSelectionGUI implements ActionListener {
	 private final JPanel gui = new JPanel();
	 
	 // Add other buttons here as needed
	 public JButton startGameBtn = new JButton("Start Game");
	 public JButton makeSuggestionBtn = new JButton("Make Suggestion");
	 public JButton makeAccusationBtn = new JButton("Make Accusation");
	 public JButton rspToSuggestionBtn = new JButton("Respond To Suggestion");
	 public JButton endTurnBtn = new JButton("End Turn");
	 public JTextArea textArea = new JTextArea(20, 40);
	 public JScrollPane scrollPane = new JScrollPane(textArea); 
	 
	 Game game = Game.getInstance();
	 
	 public ChoiceSelectionGUI() {
	        initializeGui();
	    }
	 
	 public final void initializeGui() {
		 
		 textArea.setEditable(false);
		 
		 startGameBtn.addActionListener(this);
		 makeSuggestionBtn.addActionListener(this);
		 makeAccusationBtn.addActionListener(this);
		 rspToSuggestionBtn.addActionListener(this);
		 endTurnBtn.addActionListener(this);		 
		 
		 gui.add(startGameBtn, BorderLayout.NORTH);
		 gui.add(makeSuggestionBtn, BorderLayout.CENTER);
		 gui.add(makeAccusationBtn, BorderLayout.CENTER);
		 gui.add(rspToSuggestionBtn, BorderLayout.CENTER);
		 gui.add(endTurnBtn, BorderLayout.CENTER);
		 gui.add(scrollPane, BorderLayout.SOUTH);
		 
		 gui.setVisible(true);
		 
		 setNoActionConfiguration();

	 }
	 
	 public void addTextLine(final String text)
	 {
		 SwingUtilities.invokeLater(new Runnable() {

	            public void run() {

	            	textArea.setText(text + "\n" + textArea.getText());

	            }

	        });
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
		 endTurnBtn.setVisible(false);
	 }
	 
	 public void setAllOptionsVisible(){
		 makeSuggestionBtn.setVisible(true);
		 makeAccusationBtn.setVisible(true);
		 rspToSuggestionBtn.setVisible(true);
		 endTurnBtn.setVisible(true);
	 }
	 
	 public void setRoomOptionsInvisible(){
		 makeSuggestionBtn.setVisible(false);
		 makeAccusationBtn.setVisible(false);
		 rspToSuggestionBtn.setVisible(false);
		 endTurnBtn.setVisible(false);
	 }

	 public void setRoomOptionsVisible(){
		 makeSuggestionBtn.setVisible(true);
		 makeAccusationBtn.setVisible(true);
		 rspToSuggestionBtn.setVisible(true);
		 endTurnBtn.setVisible(true);
	 }
	 	 
	 public void setStartInvisible()
	 {
		 SwingUtilities.invokeLater(new Runnable() {

	            public void run() {

	            	startGameBtn.setVisible(false);

	            }

	        });
	 }
	
	 public void setStartVisible()
	 {
		 SwingUtilities.invokeLater(new Runnable() {

	            public void run() {

	            	startGameBtn.setVisible(true);

	            }

	        });
	 }
	 
	 public void setNoActionConfiguration()
	 {
		 SwingUtilities.invokeLater(new Runnable() {

	            public void run() {

	            	makeSuggestionBtn.setEnabled(false);
	    			makeAccusationBtn.setEnabled(false);
	    			rspToSuggestionBtn.setEnabled(false);
	    			endTurnBtn.setEnabled(false);

	            }

	        });
	 }
	 
	 public void setSuggestionConfiguration()
	 {
		 SwingUtilities.invokeLater(new Runnable() {

	            public void run() {

	            	makeSuggestionBtn.setEnabled(true);
	    			makeAccusationBtn.setEnabled(false);
	    			rspToSuggestionBtn.setEnabled(false);
	    			endTurnBtn.setEnabled(false);

	            }

	        });
	 }
	 
	 public void setAccusationConfiguration()
	 {
		 SwingUtilities.invokeLater(new Runnable() {

	            public void run() {

	            	makeSuggestionBtn.setEnabled(false);
	    			makeAccusationBtn.setEnabled(true);
	    			rspToSuggestionBtn.setEnabled(false);
	    			endTurnBtn.setEnabled(true);

	            }

	        });
	 }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(arg0.getSource() == startGameBtn)
		{
			if(game.iAmFirstPlayer)
			{
				game.clientConnection.sendStartGame();
				startGameBtn.setVisible(false);
			}
		}
		else if(arg0.getSource() == makeSuggestionBtn)
		{
			
		}
		else if(arg0.getSource() == makeAccusationBtn)
		{
			
		}
		else if(arg0.getSource() == rspToSuggestionBtn)
		{
			
		}
		else if(arg0.getSource() == endTurnBtn)
		{
			game.clientConnection.sendEndTurn();
			
			game.myTurn = false;
			game.mySuggestionRound = false;
			
			setNoActionConfiguration();
			
			addTextLine("My turn is over.");
		}
	}
}
