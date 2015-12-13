package gameboard;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import mainPackage.Game;

public class SuggestionResponseGUI
{
	Game game = Game.getInstance();
	
	public void popup(String suggestionCharacter, String suggestionRoom, String suggestionWeapon)
	{
		JRadioButton characterButton = new JRadioButton(suggestionCharacter);
		JRadioButton roomButton = new JRadioButton(suggestionRoom);
		JRadioButton weaponButton = new JRadioButton(suggestionWeapon);
		
		JPanel selectionPanel = new JPanel();		
		ButtonGroup characterButtonGroup = new ButtonGroup();
		
		int count = 0;
		if(game.haveCard(suggestionCharacter))
		{
			count++;
			characterButtonGroup.add(characterButton);
			selectionPanel.add(characterButton);
			characterButton.setSelected(true);
		}
		if(game.haveCard(suggestionRoom))
		{
			count++;
			characterButtonGroup.add(roomButton);
			selectionPanel.add(roomButton);
			if(count == 1) roomButton.setSelected(true);
		}
		if(game.haveCard(suggestionWeapon))
		{
			count++;
			characterButtonGroup.add(weaponButton);
			selectionPanel.add(weaponButton);
			if(count == 1) weaponButton.setSelected(true);
		}
		
		if(count > 0)
		{
			// Popup
			Object[] option = {"Okay"};
			int choice = JOptionPane.showOptionDialog(Game.getInstance().frame, selectionPanel, "Disprove the suggestion with a card.", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option[0]);
			
			String response = "Error";
			
			if(characterButton.isSelected())
				response = suggestionCharacter;
			else if(roomButton.isSelected())
				response = suggestionRoom;
			else if(characterButton.isSelected())
				response = suggestionWeapon;
			
			game.clientConnection.sendRespondToSuggestion(response);
		}
		else
		{
			game.clientConnection.sendRespondToSuggestion("NoCard");
		}
	}

}
