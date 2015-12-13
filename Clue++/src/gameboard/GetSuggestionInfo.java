package gameboard;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import mainPackage.Character;
import mainPackage.Game;
import mainPackage.Location;
import mainPackage.Weapon;

public class GetSuggestionInfo
{
	public String location;
	public String character;
	public String weapon;	
	public int choice;
	
	public void getInfo(boolean getLocation)
	{
		String[] characters = Character.getValues();
		String[] weapons = Weapon.getValues();
		String[] locations = Location.getValues();
		
		JRadioButton[] characterButtons = new JRadioButton[characters.length];
		JRadioButton[] weaponButtons = new JRadioButton[weapons.length];
		JRadioButton[] locationButtons = new JRadioButton[locations.length];		
		
		JPanel selectionPanel = new JPanel();		
		
		GridLayout layout = new GridLayout(10, 3);
		
		selectionPanel.setLayout(layout);
		
		ButtonGroup characterButtonGroup = new ButtonGroup();
		ButtonGroup weaponButtonGroup = new ButtonGroup();
		ButtonGroup locationButtonGroup = new ButtonGroup();
		
		for(int i=0; i<10; i++)
		{		
			if(i < characters.length)
			{
				characterButtons[i] = new JRadioButton(characters[i], false);
				characterButtonGroup.add(characterButtons[i]);
				selectionPanel.add(characterButtons[i]);
			}
			else
				selectionPanel.add(new JLabel(""));
			
			if(i < weapons.length)
			{
				weaponButtons[i] = new JRadioButton(weapons[i], false);
				weaponButtonGroup.add(weaponButtons[i]);
				selectionPanel.add(weaponButtons[i]);			
			}
			else
				selectionPanel.add(new JLabel(""));
			
			if(getLocation)
			{				
				if(i < locations.length)
				{
					locationButtons[i] = new JRadioButton(locations[i], false);
					locationButtonGroup.add(locationButtons[i]);
					selectionPanel.add(locationButtons[i]);
				}
				else
					selectionPanel.add(new JLabel(""));				
			}
			else
				selectionPanel.add(new JLabel(""));
		}

		Object[] option = {"Okay"};
		choice = JOptionPane.showOptionDialog(Game.getInstance().frame, selectionPanel, "Select One of Each", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option[0]);
		
		//Fill in based on selection.
		for(int x=0; x<characterButtons.length; x++){
			
			if(characterButtons[x].isSelected())
			{
				character = characterButtons[x].getText();
			}			
		}
		
		for(int x=0; x<weaponButtons.length; x++){
			
			if(weaponButtons[x].isSelected())
			{
				weapon = weaponButtons[x].getText();
			}			
		}
		
		if(getLocation)
		{
			for(int x=0; x<locationButtons.length; x++){
				
				if(locationButtons[x].isSelected())
				{
					location = locationButtons[x].getText();
				}			
			}
		}
		
	}

}
