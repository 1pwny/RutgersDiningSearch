import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Analyzer {

	public static void main(String[] args) throws IOException {
		Scanner s = new Scanner(System.in);
		System.out.print("Re-read?  ");
		if(s.nextLine().charAt(0)=='y')
			ReaderWriter.read();
		
		
		File f = new File("info.txt");
		s = new Scanner(f);
		
		String data = "";
		while(s.hasNextLine()) {
			data += s.nextLine();
		}
		
		//System.out.println(data);
		
		String location = "", time = "", food = "";
		
		int layer = 0;
		/* 0 = nothing important
		 * 
		 * 1 = location name
		 * 2 = meal name
		 * 
		 * 3 = nothing important
		 * 
		 * 4 = food!
		 */
		
		//inquotes - if you're in quotes
		//nextup - if you need multiple points of data at the same layer, helps you differentiate
		//ignore - whether to ignore some data
		boolean inquotes = false, nextup = false, ignore = false;
		String temp = "";
		
		ArrayList<Item> foods = new ArrayList<Item>();
		
		for(int x = 0; x < data.length(); x++) {
			
			if(!inquotes) {
				if(data.charAt(x) == '\"')
					inquotes = true;
				else if(data.charAt(x) == '[')
					layer++;
				else if(data.charAt(x) == ']') {
					if(layer == 4 && ignore)
						ignore = false;
					layer--;
				}
				continue;
			}
			
			switch(data.charAt(x)) {
			
			
			case '\"':
			    //System.out.println(temp + " [" + layer + "]");
				
				if(layer == 1 && temp.equals("location_name")) {
					nextup = true;
					//System.out.print(" - now finding location");
				} else if(layer == 1 && nextup) {
					//System.out.println(" is our current location");
					location = temp;
					nextup = false;
				}
				else if(layer == 2 && temp.equals("meal_name")) {
					nextup = true;
				}
				else if(layer == 2 && nextup) {
					time = temp;
					nextup = false;
				}
				else if(layer == 3 && temp.equals("genre_name")) {
					nextup = true;
				}
				else if(layer == 3 && nextup) {
					/*if(temp.equals("Dessert") || temp.equals("Accompaniments") || temp.equals("Cook To Order Bar")
							|| temp.equals("Pizza/ Pasta") || temp.equals("Lunch To Go")
							|| temp.contains("Bakery") || temp.equals("Wok/ Mongolian Grill")
							|| temp.equals("Sauces & Gravies") || temp.equals("Salad Bar")
							|| temp.equals("Desserts") || temp.equals("Knight Room")
							|| temp.equals("Pub") || temp.contains("Order")) {*/
					if(temp.contains("Entrees") || temp.contains("Soup")) {
						ignore = false;
						nextup = false;
					} else {
						ignore = true;
						nextup = false;
					}
				}
				else if(layer == 4 && !ignore) {
					food = temp;
					foods.add(new Item(location, time, food));
					//System.out.println(foods.get(foods.size()-1));
				}
				
				//System.out.println();
				
				temp = "";
				inquotes = false;
				break;
				
			default:
				temp += data.charAt(x);
				break;
			}
		}
		
		//System.out.println("we made it?");
		
		s = new Scanner(System.in);
		
		String in = "a";
		boolean exclude = false;
		ArrayList<Item> sorted = foods;
		
		while(in.charAt(0) != 'q') {
			System.out.println("======\nI have " + sorted.size() + " items that fit these requirements.");
			System.out.print("(R)estart, (P)rint, or sort by (L)ocation/(T)ime/(F)ood: "); //location/time/food/
			in = s.nextLine().toLowerCase();
			
			if(in.equals("quit"))
				break;
			
			if(in.charAt(0) == 'l') {
				System.out.print("Place (add X to exclude): ");
				in = s.nextLine();
				exclude = in.charAt(in.length()-1) == 'X';
				if(exclude)
					in = in.substring(0, in.length()-1);
				sorted = sort(sorted, 0, in, !exclude);
				
			} else if(in.charAt(0) == 't') {
				System.out.print("Time (add X to exclude): ");
				in = s.nextLine();
				exclude = in.charAt(in.length()-1) == 'X';
				if(exclude)
					in = in.substring(0, in.length()-1);
				sorted = sort(sorted, 1, in, !exclude);
				
			} else if(in.charAt(0) == 'f') {
				System.out.print("Food (add X to exclude): ");
				in = s.nextLine();
				exclude = in.charAt(in.length()-1) == 'X';
				if(exclude)
					in = in.substring(0, in.length()-1);
				sorted = sort(sorted, 2, in, !exclude);
				
			} else if(in.charAt(0) == 'n') {
				System.out.println("Normal filters: neilsonX, breakfastX, knight roomX, kosher");
				sorted = sort(sorted, 0, "neilson", false);
				sorted = sort(sorted, 1, "breakfast", false);
				sorted = sort(sorted, 1, "knight roomX", false);
				sorted = sort(sorted, 2, "chicken", false);
				sorted = sort(sorted, 2, "turkey", false);
				sorted = sort(sorted, 2, "beef", false);
				sorted = sort(sorted, 2, "pork", false);
				sorted = sort(sorted, 2, "ham", false);
				sorted = sort(sorted, 2, "lobster", false);
				sorted = sort(sorted, 2, "shrimp", false);
				sorted = sort(sorted, 2, "sausage", false);
				sorted = sort(sorted, 2, "rib", false);
				sorted = sort(sorted, 2, "steak", false);
			} else if(in.charAt(0) == 'r') {
				sorted = foods;
			} else if(in.charAt(0) == 'p') {
					for(Item i: sorted)
						System.out.println(i);
			}
		}
	}

	
	/* Takes certain items from the list, returns the new list
	 * 
	 * list - input food list
	 * var - 0/1/2 for loc/time/food
	 * term - term to check on
	 * include - true: include everything with term, false: exclude everything with term
	 */
	public static ArrayList<Item> sort(ArrayList<Item> list, int var, String term, boolean include) {
		ArrayList<Item> ret = new ArrayList<Item>();
		
		for(Item i: list)
			switch(var) {
			case 0:
				if(i.in(term) == include)
					ret.add(i);
				break;
			case 1:
				if(i.at(term) == include)
					ret.add(i);
				break;
			case 2:
				if(i.has(term) == include)
					ret.add(i);
				break;
			}
		
		return ret;
	}
	
}