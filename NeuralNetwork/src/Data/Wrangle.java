/**
 * @author Kevin Mc Inerney
 * @version Version 1.2
 * @since 22/05/2015
 * 
 * */

package Data;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Wrangle {
	
	
	private File dataFilename;
    private BufferedReader br;
    

    public Wrangle(String dataFileName){
    	this.dataFilename = new File(Wrangle.class.getResource("/Data/"+dataFileName).getFile());
    	resetBuffer();
    }
    
    public void getAverage(){
    	
    	String trial;
    	
    	try {
    		double total = 0;
    		double lineTotal = 0;
			while ((trial = br.readLine()) != null) {
				char[] row = trial.toCharArray();
				total += row.length; // Double.parseDouble(row[row.length-1]);
				System.out.println(row.length);
			}
			System.out.println();
			System.out.println(lineTotal/70);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	public void resetBuffer(){
		this.br = new BufferedReader(new InputStreamReader(this.getClass().
				getResourceAsStream("/Data/" + dataFilename.getName()), Charset.forName("UTF-8")));
	
	}
	
	public static void main(String args[]){
		
		Wrangle w = new Wrangle("full_dataset.txt");
		w.getAverage();
	}
}
