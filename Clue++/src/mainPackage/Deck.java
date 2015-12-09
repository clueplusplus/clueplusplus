package mainPackage;

import java.util.ArrayList;
import java.util.Random;

public class Deck
{
	ArrayList<Card> cards = new ArrayList<Card>();
	
	Deck()
	{
		// Create all the cards
		cards.add(new Card(Character.colMustard, Card.CharacterType, "Characters/ColonelMustard"));
		cards.add(new Card(Character.missScarlet, Card.CharacterType, "Characters/MissScarlet"));
		cards.add(new Card(Character.mrsPeacock, Card.CharacterType, "Characters/MrsPeacock"));
		cards.add(new Card(Character.mrsWhite, Card.CharacterType, "Characters/MrsWhite"));
		cards.add(new Card(Character.profPlum, Card.CharacterType, "Characters/ProfessorPlum"));
		cards.add(new Card(Character.revGreen, Card.CharacterType, "Characters/MrGreen"));
		cards.add(new Card(Location.Ballroom, Card.LocationType, "Rooms/Ballroom"));
		cards.add(new Card(Location.BilliardRoom, Card.LocationType, "Rooms/BilliardRoom"));
		cards.add(new Card(Location.Conservatory, Card.LocationType, "Rooms/Conservatory"));
		cards.add(new Card(Location.DiningRoom, Card.LocationType, "Rooms/DiningRoom"));
		cards.add(new Card(Location.Hall, Card.LocationType, "Rooms/Hall"));
		cards.add(new Card(Location.Kitchen, Card.LocationType, "Rooms/Kitchen"));
		cards.add(new Card(Location.Library, Card.LocationType, "Rooms/Library"));
		cards.add(new Card(Location.Lounge, Card.LocationType, "Rooms/Lounge"));
		cards.add(new Card(Location.Study, Card.LocationType, "Rooms/Study"));
		cards.add(new Card(Weapon.Candlestick, Card.WeaponType, "Weapons/CandleStick"));
		cards.add(new Card(Weapon.Revolver, Card.WeaponType, "Weapons/Revolver"));
		cards.add(new Card(Weapon.Knife, Card.WeaponType, "Weapons/Knife"));
		cards.add(new Card(Weapon.LeadPipe, Card.WeaponType, "Weapons/LeadPipe"));
		cards.add(new Card(Weapon.Wrench, Card.WeaponType, "Weapons/Wrench"));
		cards.add(new Card(Weapon.Rope, Card.WeaponType, "Weapons/Rope"));
	}
	
	public void shuffle()
	{
		// Shuffle the deck
		Random random = new Random();
		for(int x=0; x<cards.size(); x++)
		{
			// Get a random index between x and the end of the array (including x).
			int swapIndex = random.nextInt(cards.size() - x) + x;
			
			// Swap x with index.
			Card temp = cards.get(swapIndex);
			cards.set(swapIndex, cards.get(x));
			cards.set(x, temp);
		}
	}
	
	public Card removeRandomCard(int type)
	{
		shuffle();
		
		for(int x=0; x<cards.size(); x++)
		{
			if(cards.get(x).type == type)
				return cards.remove(x);				
		}
		
		// Don't let this happen.
		System.out.println("Error in removeRandomCard");
		return null;
	}
	
	public Card getRandomCard(int type)
	{
		shuffle();
		
		for(int x=0; x<cards.size(); x++)
		{
			if(cards.get(x).type == type)
				return cards.get(x);				
		}
		
		// Don't let this happen.
		System.out.println("Error in getRandomCard");
		return null;
	}
	
	public Card removeNextCard()
	{
		if(cards.size() > 0)
			return cards.remove(cards.size() - 1);				
		
		return null;
	}
	
	public Card findCard(String name)
	{
		for(Card c: cards)
		{
			if(c.name.compareTo(name) == 0)
				return c;
		}
		
		// Don't let this happen.
		System.out.println("Error in findCard()");
		return null;
	}

}
