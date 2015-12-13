package mainPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck
{
	ArrayList<Card> cards = new ArrayList<Card>();
	
	
	Deck()
	{
		cards.addAll(fillDeck());
	}
	
	public static List<Card> fillDeck() {
		List<Card> cards = new ArrayList<Card>();
		// Create all the cards
				cards.add(new Card(Character.colMustard, Card.CharacterType, "Characters/ColonelMustard", Character.colMustard));
				cards.add(new Card(Character.missScarlet, Card.CharacterType, "Characters/MissScarlet", Character.missScarlet));
				cards.add(new Card(Character.mrsPeacock, Card.CharacterType, "Characters/MrsPeacock", Character.mrsPeacock));
				cards.add(new Card(Character.mrsWhite, Card.CharacterType, "Characters/MrsWhite", Character.mrsWhite));
				cards.add(new Card(Character.profPlum, Card.CharacterType, "Characters/ProfessorPlum", Character.profPlum));
				cards.add(new Card(Character.revGreen, Card.CharacterType, "Characters/MrGreen", Character.revGreen));
				cards.add(new Card(Location.Ballroom, Card.LocationType, "Rooms/Ballroom", Location.Ballroom));
				cards.add(new Card(Location.BilliardRoom, Card.LocationType, "Rooms/BilliardRoom", Location.BilliardRoom));
				cards.add(new Card(Location.Conservatory, Card.LocationType, "Rooms/Conservatory", Location.Conservatory));
				cards.add(new Card(Location.DiningRoom, Card.LocationType, "Rooms/DiningRoom", Location.DiningRoom));
				cards.add(new Card(Location.Hall, Card.LocationType, "Rooms/Hall", Location.Hall));
				cards.add(new Card(Location.Kitchen, Card.LocationType, "Rooms/Kitchen", Location.Kitchen));
				cards.add(new Card(Location.Library, Card.LocationType, "Rooms/Library", Location.Library));
				cards.add(new Card(Location.Lounge, Card.LocationType, "Rooms/Lounge", Location.Lounge));
				cards.add(new Card(Location.Study, Card.LocationType, "Rooms/Study", Location.Study));
				cards.add(new Card(Weapon.Candlestick, Card.WeaponType, "Weapons/CandleStick", Weapon.Candlestick));
				cards.add(new Card(Weapon.Revolver, Card.WeaponType, "Weapons/Revolver", Weapon.Revolver));
				cards.add(new Card(Weapon.Knife, Card.WeaponType, "Weapons/Knife", Weapon.Knife));
				cards.add(new Card(Weapon.LeadPipe, Card.WeaponType, "Weapons/LeadPipe", Weapon.LeadPipe));
				cards.add(new Card(Weapon.Wrench, Card.WeaponType, "Weapons/Wrench", Weapon.Wrench));
				cards.add(new Card(Weapon.Rope, Card.WeaponType, "Weapons/Rope", Weapon.Rope));
		return cards;
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
	
	public Card getRandomCard()
	{
		shuffle();
		
		Card card = cards.get(0);
		
		return card;
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
