package iHelp;



import java.io.IOException;

import com.darkprograms.speech.synthesiser.SynthesiserV2;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 * This is where all begins .
 * 
 * @author GOXR3PLUS
 *
 */
@SuppressWarnings("all")
public class TTSout {
	
	//Create a Synthesizer instance
	//SynthesiserV2 synthesizer = new SynthesiserV2("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");
	SynthesiserV2 synthesizer = new SynthesiserV2("AIzaSyDdUqUPUtaItfEEYTm6sB3XHT4IMsT_GpQ");
	
	/**
	 * Constructor
	 */
	public TTSout(String tts) {
		
		//Let's speak in English
		speak(tts);
		
		
	}
	
	/**
	 * Calls the MaryTTS to say the given text
	 * 
	 * @param text
	 */
	public void speak(String text) {
		System.out.println(text);
		
		//Create a new Thread because JLayer is running on the current Thread and will make the application to lag
		Thread thread = new Thread(() -> {
			try {
				
				//Create a JLayer instance
				AdvancedPlayer player = new AdvancedPlayer(synthesizer.getMP3Data(text));
				player.play();
				
				System.out.println("TTS Accomplished");
				
			} catch (IOException | JavaLayerException e) {
				
				e.printStackTrace(); //Print the exception ( we want to know , not hide below our finger , like many developers do...)
				
			}
		});
		
		//We don't want the application to terminate before this Thread terminates
		thread.setDaemon(false);
		
		//Start the Thread
		thread.start();
		
	}
	/*
	public static void main(String[] args) {
		new TTSout();
	}*/
	
}
