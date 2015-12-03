import java.util.ArrayList;
import java.util.Random;

public class Card
{
	String name;
	
	int type;
	static int CharacterType = 0;
	static int LocationType = 1;
	static int WeaponType = 2;
	
	Card(String name, int type)
	{
		this.name = name;
		this.type = type;
	}
	
	static ArrayList<Card> referenceDeck = getFullDeck();
	
	public static ArrayList<Card> getFullDeck()
	{
		ArrayList<Card> deck = new ArrayList<Card>();
		
		// Build a deck
		deck.add(new Card(Character.colMustard, CharacterType));
		deck.add(new Card(Character.missScarlet, CharacterType));
		deck.add(new Card(Character.mrsPeacock, CharacterType));
		deck.add(new Card(Character.mrsWhite, CharacterType));
		deck.add(new Card(Character.profPlum, CharacterType));
		deck.add(new Card(Character.revGreen, CharacterType));
		deck.add(new Card(Location.Ballroom, LocationType));
		deck.add(new Card(Location.BilliardRoom, LocationType));
		deck.add(new Card(Location.Conservatory, LocationType));
		deck.add(new Card(Location.DiningRoom, LocationType));
		deck.add(new Card(Location.Hall, LocationType));
		deck.add(new Card(Location.Kitchen, LocationType));
		deck.add(new Card(Location.Library, LocationType));
		deck.add(new Card(Location.Lounge, LocationType));
		deck.add(new Card(Location.Study, LocationType));
		deck.add(new Card(Weapon.Candlestick, WeaponType));
		deck.add(new Card(Weapon.Gloves, WeaponType));
		deck.add(new Card(Weapon.Horseshoe, WeaponType));
		deck.add(new Card(Weapon.Knife, WeaponType));
		deck.add(new Card(Weapon.LeadPipe, WeaponType));
		deck.add(new Card(Weapon.Poison, WeaponType));
		deck.add(new Card(Weapon.Rope, WeaponType));
	
		// Shuffle the deck
		Random random = new Random();
		for(int x=0; x<deck.size(); x++)
		{
			// Get a random index between x and the end of the array (including x).
			int swapIndex = random.nextInt(deck.size() - x) + x;
			
			// Swap x with index.
			Card temp = deck.get(swapIndex);
			deck.set(swapIndex, deck.get(x));
			deck.set(x, temp);
		}
		
		return deck;		
	}
	
	public static Card removeTypeOfCard(ArrayList<Card> deck, int type)
	{
		for(int x=0; x<deck.size(); x++)
		{
			if(deck.get(x).type == type)
				return deck.remove(x);				
		}
		
		// Don't let this happen.
		System.out.println("Error in removeTypeOfCard");
		return null;
	}
	
	public static Card findCardInReferenceDeck(String name)
	{
		for(Card c: referenceDeck)
		{
			if(c.name.compareTo(name) == 0)
				return c;
		}
		
		// Don't let this happen.
		System.out.println("Error in findCard()");
		return null;
	}
}
