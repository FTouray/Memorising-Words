package ca4;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*Write a program that represents memorizing words from the English language that start with the letter A.*/

public class MemorisingWords{
	
	static ArrayList<String>memWords = new ArrayList<String>(); //creating an ArrayList to keep words from duplicating
	
	public static void main(String[] args) throws IOException {
		
			
			//Initialize thread pool with fixed size
			ExecutorService exService = Executors.newFixedThreadPool(5);
			
			//read data from the words.txt file
			FileInputStream fIn = new FileInputStream("words.txt");
			BufferedInputStream bIn = new BufferedInputStream(fIn);
			BufferedReader bReader = new BufferedReader(new InputStreamReader(bIn));//reads text from character-input-stream
			
			String words;
			while ((words = bReader.readLine()) != null) { //this reads one line at a time until the end of the file is reached
	           if(words.charAt(0) == 'A' || words.charAt(0) == 'a') { //only if the first letter is A or a
				Memorizer memorize = new Memorizer(words);
	            exService.submit(memorize); //submit the task to the executor service
	           }
	        }
			
			bReader.close();//close the bufferedReader
			
			// Wait for all the threads to finish
	        exService.shutdown();
		
	}	
	
	//had to add this method because words kept infinitely  duplicating in the memory.txt file
	public synchronized static boolean containMemorizedWord(String word) {
		//method that returns true or false if word is in ArrayList or not
        if (memWords.contains(word)) {
            return false; //if the ArrayList contains the word false is returned
        } else { //if it doesn't contain the word
        	memWords.add(word);//add the word to ArrayList to store and keep not of it
            return true;//return true if word is not in ArrayList
        }
    }

}

class Memorizer implements Runnable {
	String word;	
	
	
	public Memorizer(String word) {
		this.word = word;
	}
	
	public void run() {
        boolean success = false;
        while (!success) {
            try {
                synchronized ("memory.txt") { //only 1 thread can access the file at a time
                    if (MemorisingWords.containMemorizedWord(word)) { //add the word only if it hasn't already been memorized
                        FileOutputStream fOut = new FileOutputStream("memory.txt", true); //the true tells the FileOutputStream to append to the file instead of overwriting it
                        BufferedWriter bOut = new BufferedWriter(new OutputStreamWriter(fOut));
                        Thread.sleep(1000); //sleeps for a second
                        bOut.write(word); //writes the word into the memory file
                        bOut.newLine(); //so it prints on a new line
                        
                        //important to close because they will take up memory
                        bOut.close();
                        fOut.close();
                    }
                    success = true; // If we reach this point without exception, we succeeded
                }
            } catch (FileNotFoundException e) {
                //if the file is already being used, wait for a short period of time and retry
                try {
                    Thread.sleep(1000); // Wait for 1 second before retrying
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
