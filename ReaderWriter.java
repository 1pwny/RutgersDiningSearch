import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ReaderWriter {

    public static void main(String[] args) throws IOException {
    	read();
    }
    
    public static String read() throws IOException {
    	
        URL url = new URL("https://rumobile.rutgers.edu/1/rutgers-dining.txt");

        
        URLConnection con = url.openConnection();
        InputStream is =con.getInputStream();



        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = null;

        // read each line and write to a file
        try {
			File f = new File("info.txt");
			PrintWriter pw = new PrintWriter(f);

			String full = "";
			
	        while ((line = br.readLine()) != null) {
	        	full += line;
	        }
	        
	        int tabs = 0;
	        
	        for(int x = 0; x < full.length(); x++) {
	        	if(full.charAt(x) == '{') {
	        		full = full.substring(0,x+1) + space(++tabs) + full.substring(x+1);
	        		x += 1 + tabs;
	        	}
	        	if(full.charAt(x) == '}')
	        		tabs--;
	        }
	        
	        while(full.contains("\\")) {
	        	int pos = full.indexOf('\\');
	        	full = full.substring(0, pos) + "\'" + full.substring(pos+2);
	        }
	        
	        pw.println(full);
	        
	        pw.flush();
	        pw.close();
	        
	        return full;
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return "";
    }

    public static String space(int i) {
    	String ret = "\n";
    	for(int x = 0; x < i; x++)
    		ret += "\t";
    	return ret;
    }
}
