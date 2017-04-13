import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;

public class GeneratePossibleExchanges {

	
	
	
	
	
	public static void generatePossibleExchanges(String inputFile, String outputFile, String delimiter) throws FileNotFoundException, IOException
	{
		
		
		 HashSet<String> users = new HashSet<>();
		 HashSet<String> items = new HashSet<>();
		 PrintWriter out = new PrintWriter(outputFile);
		 
		try(BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        if(line.contains(delimiter))
		        {
		        	String user = line.split(delimiter)[0];
		        	String item = line.split(delimiter)[1];
		        	users.add(user.trim());
		        	items.add(item.trim());
		        }
		    }
		    
		}
		
		for(String user:users)
		{
			for(String item:items)
			{
				out.println(user+delimiter+item);
			}
		}
		out.close();
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		generatePossibleExchanges("exchangeInput.csv","exchangeOutput.csv","\t");
	}
}
