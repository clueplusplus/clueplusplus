import java.util.ArrayList;

public class Location
{
	// Name of location
	String name = "";
	String userGeneratedName; // null if not used.
	
	// Grid Locations
	int row;
	int col;
	
	// Neighbors you can move to, null if no option.
	Location up;
	Location down;
	Location left;
	Location right;
	Location tunnel;

	// Capacity
	int capacity;
	
	// People in the location
	// TODO Change the object type from String.
	ArrayList<String> occupants = new ArrayList<String>(0);
	
	Location(int row, int col, String name, int capacity)
	{
		this.row = row;
		this.col = col;
		this.name = name;
		this.capacity = capacity;		
	}
	
	public String printMovementOptions()
	{
		StringBuilder s = new StringBuilder("From " + name + ":\n");
		
		if(up != null) s.append("Up: " + up.name + "\n");
		if(down != null) s.append("Down: " + down.name + "\n");
		if(left != null) s.append("Left: " + left.name + "\n");
		if(right != null) s.append("Right: " + right.name + "\n");
		if(tunnel != null) s.append("Tunnel: " + tunnel.name + "\n");
		
		return s.toString();
	}
	
	// This is probably the least efficient way to do this. But hey, it's done...	
	static ArrayList<Location> getEmptyMap()
	{
		ArrayList<Location> map = new ArrayList<Location>(0);
		
		// Grouped into rows
		
		Location aa = new Location(1, 1, "Study", 10);
		Location ab = new Location(1, 2, "Hallway (1, 2)", 1);
		Location ac = new Location(1, 3, "Hall", 10);
		Location ad = new Location(1, 4, "Hallway (1, 4)", 1);
		Location ae = new Location(1, 5, "Lounge", 10);
		
		Location ba = new Location(2, 1, "Hallway (2, 1)", 1);
		//Location bb = new Location(2, 2);
		Location bc = new Location(2, 3, "Hallway (2, 3)", 1);
		//Location bd = new Location(2, 4);
		Location be = new Location(2, 5, "Hallway (2, 5)", 1);
		
		Location ca = new Location(3, 1, "Library", 10);
		Location cb = new Location(3, 2, "Hallway (3, 2)", 1);
		Location cc = new Location(3, 3, "Billiard Room", 10);
		Location cd = new Location(3, 4, "Hallway (3, 4)", 1);
		Location ce = new Location(3, 5, "Dining Room", 10);
		
		Location da = new Location(4, 1, "Hallway (4, 1)", 1);
		//Location db = new Location(4, 2);
		Location dc = new Location(4, 3, "Hallway (4, 3)", 1);
		//Location dd = new Location(4, 4);
		Location de = new Location(4, 5, "Hallway (4, 5)", 1);
		
		Location ea = new Location(5, 1, "Conservatory", 10);
		Location eb = new Location(5, 2, "Hallway (5, 2)", 1);
		Location ec = new Location(5, 3, "Ballroom", 10);
		Location ed = new Location(5, 4, "Hallway (5, 4)", 1);
		Location ee = new Location(5, 5, "Kitchen", 10);
		
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
		
		map.add(aa);
		map.add(ab);
		map.add(ac);
		map.add(ad);
		map.add(ae);
		
		map.add(ba);
		map.add(bc);
		map.add(be);
		
		map.add(ca);
		map.add(cb);
		map.add(cc);
		map.add(cd);
		map.add(ce);
		
		map.add(da);
		map.add(dc);
		map.add(de);
		
		map.add(ea);
		map.add(eb);
		map.add(ec);
		map.add(ed);
		map.add(ee);
		
		return map;		
	}
}
