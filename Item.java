
public class Item {

	public String loc, time, food;
	
	public Item(String l, String t, String f) {
		loc = l;
		time = t;
		food = f;
	}

	
	public boolean at(String t) {
		if(t.length() == 0)
			return false;
		return t.toLowerCase().charAt(0) == time.toLowerCase().charAt(0);
	}
	
	public boolean in(String l) {
		if(l.length() == 0)
			return false;
		return loc.toLowerCase().contains(l.toLowerCase());
	}
	
	public boolean has(String ingrediant) {
		if(ingrediant.length() == 0) {
			return false;	
		}
		return food.toLowerCase().contains(ingrediant.toLowerCase());
	}
	
	public String toString() {
		return food + " in " + loc + " at " + time;
	}
 }
