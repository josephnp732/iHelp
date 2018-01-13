

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GoogleResponse;
import com.darkprograms.speech.recognizer.Recognizer;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import javaFlacEncoder.FLACFileWriter;




public class Backend {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		//START - SPEECH RECOGNITION
		String Gresponse="";
		Microphone mic = new Microphone(FLACFileWriter.FLAC);
		File file = new File("..//iHelp.flac");//Name your file audio file
		try {
			mic.captureAudioToFile(file);
		} catch (Exception ex) {//Microphone not available or some other error.
			System.out.println("ERROR: Microphone is not availible.");
			ex.printStackTrace();
			//TODO Add your error Handling Here
		}
		/* User records the voice here. Microphone starts a separate thread so do whatever you want
		 * in the mean time. Show a recording icon or whatever.
		 */
		try {
			System.out.println("Recording...");
			Thread.sleep(5000);//In our case, we'll just wait 5 seconds.
	                    mic.close();
		} catch (InterruptedException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		
		mic.close();//Ends recording and frees the resources
		System.out.println("Recording stopped.");
		
		Recognizer recognizer = new Recognizer(Recognizer.Languages.ENGLISH_US, "AIzaSyDdUqUPUtaItfEEYTm6sB3XHT4IMsT_GpQ"); //Specify your language here.
		//Although auto-detect is avalible, it is recommended you select your region for added accuracy.
		try {
			//int maxNumOfResponses = 4;
			int maxNumOfResponses = 1;
			GoogleResponse response = recognizer.getRecognizedDataForFlac(file, maxNumOfResponses,(int)mic.getAudioFormat().getSampleRate());
			Gresponse = response.getResponse();
			System.out.println("Google Response: " + Gresponse);
			System.out.println("Google is " + Double.parseDouble(response.getConfidence())*100 + "% confident in"
					+ " the reply");
			/* --use this for alternate answers--
			System.out.println("Other Possible responses are: ");
			for(String s: response.getOtherPossibleResponses()){
				System.out.println("\t" + s);
			}
			*/
		} catch (Exception ex) {
			// TODO Handle how to respond if Google cannot be contacted
			System.out.println("ERROR: Google cannot be contacted");
			ex.printStackTrace();
		}
		
		file.deleteOnExit();//Deletes the file as it is no longer necessary.
		
		//END - SPEECH RECOGNITION
		
		
		
		//START - POS TAGGING
		
		MaxentTagger tagger = new MaxentTagger("taggers/left3words-wsj-0-18.tagger");
		
		// The sample string		 
		//String sample = "Where is information science department";
		 
		// The tagged string		 
		String tagged = tagger.tagString(Gresponse);
		 
		// Output the result		 
		System.out.println(tagged);
		
		//END - POS TAGGING
		
		
		//START - TEXT-2-SPEECH
		
		try
        {
            // set property as Kevin Dictionary
            System.setProperty("freetts.voices","com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory"); 
                 
            // Register Engine
            Central.registerEngineCentral
                ("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
 
            // Create a Synthesizer
            Synthesizer synthesizer =                                         
                Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));     
     
            // Allocate synthesizer
            synthesizer.allocate();        
             
            // Resume Synthesizer
            synthesizer.resume();    
             
            // speaks the given text until queue is empty.
            //Gresponse a String variable storing the text to be spoken. 
            synthesizer.speakPlainText(Gresponse, null);         
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
             
            // Deallocate the Synthesizer.
            synthesizer.deallocate();                                 
        } 
 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
		
		//END - TEXT-2-SPEECH
		
		
	}

}
