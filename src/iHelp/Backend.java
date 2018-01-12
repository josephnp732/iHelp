package iHelp;

import java.io.IOException;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class Backend {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		MaxentTagger tagger = new MaxentTagger("taggers/left3words-wsj-0-18.tagger");
		
		// The sample string		 
		String sample = "Where can i find vice principal's office";
		 
		// The tagged string		 
		String tagged = tagger.tagString(sample);
		 
		// Output the result		 
		System.out.println(tagged);
	}

}
