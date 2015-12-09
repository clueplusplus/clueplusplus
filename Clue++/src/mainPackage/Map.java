package mainPackage;
import java.util.ArrayList;

public class Map
{
	ArrayList<Location> locations;
	ArrayList<Character> characters;
	
	public synchronized Location getLocation(String name)
	{
		for(Location l : locations)
		{
			if(l.name.compareTo(name) == 0)
				return l;
		}
		
		return null;
	}
	
	public synchronized Location getRoom(int row, int col)
	{
		for(Location l : locations)
		{
			if(l.row == row && l.col == col)
				return l;
		}
		
		return null;
	}
	
	public synchronized void printMapMovementOptions()
	{
		for(Location l : locations)
		{
			System.out.print(l.printMovementOptions());
		}
	}
	
	public synchronized Character getCharacter(String name)
	{
		for(Character c : characters)
		{
			if(c.name.compareTo(name) == 0)
				return c;
		}
		
		return null;
	}
	
	public synchronized void moveCharacter(String characterName, String locationName)
	{
		Character c = getCharacter(characterName);
		Location l = getLocation(locationName);
		
		c.location.occupants.remove(c);
		c.location = l;
		l.occupants.add(c);		
	}
	
	Map()
	{
		// Build the starting map.
		locations = new ArrayList<Location>(0);
		
		// Grouped into rows
		Location aa = new Location(1, 1, Location.Study, 10);
		Location ab = new Location(1, 2, "Hallway (1, 2)", 1);
		Location ac = new Location(1, 3, Location.Hall, 10);
		Location ad = new Location(1, 4, "Hallway (1, 4)", 1);
		Location ae = new Location(1, 5, Location.Lounge, 10);
		
		Location ba = new Location(2, 1, "Hallway (2, 1)", 1);
		//Location bb = new Location(2, 2);
		Location bc = new Location(2, 3, "Hallway (2, 3)", 1);
		//Location bd = new Location(2, 4);
		Location be = new Location(2, 5, "Hallway (2, 5)", 1);
		
		Location ca = new Location(3, 1, Location.Library, 10);
		Location cb = new Location(3, 2, "Hallway (3, 2)", 1);
		Location cc = new Location(3, 3, Location.BilliardRoom, 10);
		Location cd = new Location(3, 4, "Hallway (3, 4)", 1);
		Location ce = new Location(3, 5, Location.DiningRoom, 10);
		
		Location da = new Location(4, 1, "Hallway (4, 1)", 1);
		//Location db = new Location(4, 2);
		Location dc = new Location(4, 3, "Hallway (4, 3)", 1);
		//Location dd = new Location(4, 4);
		Location de = new Location(4, 5, "Hallway (4, 5)", 1);
		
		Location ea = new Location(5, 1, Location.Conservatory, 10);
		Location eb = new Location(5, 2, "Hallway (5, 2)", 1);
		Location ec = new Location(5, 3, Location.Ballroom, 10);
		Location ed = new Location(5, 4, "Hallway (5, 4)", 1);
		Location ee = new Location(5, 5, Location.Kitchen, 10);
		
		aa.down = ba;
		aa.right = ab;
		aa.tunnel = ee;
		
		ab.left = aa;
		ab.right = ac;
		
		ac.left = ab;
		ac.right = ad;
		ac.down = bc;
		
		ad.left = ac;
		ad.right = ae;
		
		ae.left = ad;
		ae.down = be;
		ae.tunnel = ea;
		
		ba.up = aa;
		ba.down = ca;
		
		bc.up = ac;
		bc.down = cc;
		
		be.up = ae;
		be.down = ce;
		
		ca.up = ba;
		ca.down = da;
		ca.right = cb;
		
		cb.left = ca;
		cb.right = cc;
		
		cc.up = bc;
		cc.left = cb;
		cc.down = dc;
		cc.right = cd;
		
		cd.left = cc;
		cd.right = ce;
		
		ce.up = be;
		ce.left = cd;
		ce.down = de;
		
		da.up = ca;
		da.down = ea;
		
		dc.up = cc;
		dc.down = ec;
		
		de.up = ce;
		de.down = ee;
		
		ea.up = da;
		ea.right = eb;
		ea.tunnel = ae;
		
		eb.left = ea;
		eb.right = ec;
		
		ec.left = eb;
		ec.right = ed;
		ec.up = dc;
		
		ed.left = ec;
		ed.right = ee;
		
		ee.left = ed;
		ee.up = de;
		ee.tunnel = aa;
		
		locations.add(aa);
		locations.add(ab);
		locations.add(ac);
		locations.add(ad);
		locations.add(ae);
		
		locations.add(ba);
		locations.add(bc);
		locations.add(be);
		
		locations.add(ca);
		locations.add(cb);
		locations.add(cc);
		locations.add(cd);
		locations.add(ce);
		
		locations.add(da);
		locations.add(dc);
		locations.add(de);
		
		locations.add(ea);
		locations.add(eb);
		locations.add(ec);
		locations.add(ed);
		locations.add(ee);
		
		// -----------------------------------------------
		// Generate the characters and place on the map.
		characters = new ArrayList<Character>(0);
		
		Character a = new Character(Character.missScarlet, eb);
		eb.occupants.add(a);
		
		Character b = new Character(Character.profPlum, de);
		de.occupants.add(b);
		
		Character c = new Character(Character.mrsPeacock, be);
		be.occupants.add(c);
		
		Character d = new Character(Character.revGreen, ad);
		ad.occupants.add(d);
		
		Character e = new Character(Character.colMustard, da);
		da.occupants.add(e);
		
		Character f = new Character(Character.mrsWhite, ab);
		ab.occupants.add(f);
				
		characters.add(a);
		characters.add(b);
		characters.add(c);
		characters.add(d);
		characters.add(e);
		characters.add(f);
	}
}
