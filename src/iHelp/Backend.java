package iHelp;


import java.io.File;
import java.io.IOException;
import java.net.ConnectException;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GoogleResponse;
import com.darkprograms.speech.recognizer.Recognizer;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import javaFlacEncoder.FLACFileWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;



public class Backend {
	
	
	
	public static int Dest() {
	//public static void main(String[] args) {
		
		int flag;
		String fromClause = "";
		ArrayList <String> whereClause = new ArrayList <String>();
		
		//START - SPEECH RECOGNITION
		String Gresponse="";
		Microphone mic = new Microphone(FLACFileWriter.FLAC);
		File file = new File("..//iHelp.flac");//Audio file name
		try {
			mic.captureAudioToFile(file);
		} catch (Exception ex) {
			System.out.println("ERROR: Microphone is not availible.");
			ex.printStackTrace();
		}
		// User records the voice here. Microphone starts a separate thread so do whatever you want
		// in the mean time. Show a recording icon or whatever.
		try {
			System.out.println("Recording...");
			Thread.sleep(5000);//In our case, we'll just wait 5 seconds.
	                    mic.close();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		
		mic.close();//Ends recording and frees the resources
		System.out.println("Recording stopped.");
		
		Recognizer recognizer = new Recognizer(Recognizer.Languages.ENGLISH_US, "AIzaSyDdUqUPUtaItfEEYTm6sB3XHT4IMsT_GpQ");
		
		try {
			//int maxNumOfResponses = 4;
			int maxNumOfResponses = 1;
			GoogleResponse response = recognizer.getRecognizedDataForFlac(file, maxNumOfResponses,(int)mic.getAudioFormat().getSampleRate());
			Gresponse = response.getResponse();
			System.out.println("Google Response: " + Gresponse);
			System.out.println("Google is " + Double.parseDouble(response.getConfidence())*100 + "% confident in"
					+ " the reply");
			// --use this for alternate answers--
			//System.out.println("Other Possible responses are: ");
			//for(String s: response.getOtherPossibleResponses()){
			//	System.out.println("\t" + s);
			//}
			
		} catch (Exception ex) {
			System.out.println("ERROR: Google cannot be contacted");
			ex.printStackTrace();
		}
		
		file.deleteOnExit();//Deletes the file as it is no longer necessary.
		
		
		
		//END - SPEECH RECOGNITION
		
		
		
		//START - POS TAGGING
		
		MaxentTagger tagger;
		String tagged = "";
		try {
			tagger = new MaxentTagger("taggers/left3words-wsj-0-18.tagger");
			// The sample string		 
			//String sample = "Where is information science department";
			 
			// The tagged string		 
			tagged = tagger.tagString(Gresponse);
			 
			// Output the result		 
			System.out.println(tagged);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		
		
		
		//END - POS TAGGING
	
		
		
		
		
		/// START - TEXT PRE-PROCESSING
		ArrayList<String> nouns = new ArrayList<String>();
		ArrayList<String> whs = new ArrayList<String>();
		ArrayList<String> adjectives = new ArrayList<String>();
		ArrayList<String> numbers = new ArrayList<String>();
		for(String w : tagged.split(" "))
		{
			int index = w.indexOf('/');
			char[] word = w.toCharArray();
			if(word[index+1] == 'W' || word[index+1] == 'N' || word[index+1] == 'J' ||
					(word[index+1] == 'C' && word[index+2] == 'D'))
			{
				if(word[index+1] == 'W')
				{
					whs.add(w.substring(0, w.indexOf('/')));
					//System.out.println("Wh word:");
					fromClause = w.substring(0, w.indexOf('/'));
				}
				else if(word[index+1] == 'N')
				{
					//System.out.println("Noun");
					nouns.add(w.substring(0, w.indexOf('/')));
					whereClause.add(w.substring(0, w.indexOf('/')));				
				}
				else if(word[index+1] == 'J')
				{
					//System.out.println("adjective");
					adjectives.add(w.substring(0, w.indexOf('/')));
					whereClause.add(w.substring(0, w.indexOf('/')));
				}
				else if(word[index+1] == 'C' && word[index+2] == 'D')
				{
					//System.out.println("Number");
					numbers.add(w.substring(0, w.indexOf('/')));
					whereClause.add(w.substring(0, w.indexOf('/')));
				}
				
			}
			
		}
		
		
		//------------------------- FOR DEBUGGING PURPOSE -------------------------------		
		
		try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	
				// root elements
				Document doc = docBuilder.newDocument();
				Element rootElement = doc.createElement("CMRIT");
				doc.appendChild(rootElement);
				
				for(String wh : whs)
				{
					Element w = doc.createElement("WH");
					w.appendChild(doc.createTextNode(wh));
					rootElement.appendChild(w);
				}
				
				for(String noun : nouns)
				{
					Element n = doc.createElement("NN");
					n.appendChild(doc.createTextNode(noun));
					rootElement.appendChild(n);
				}
	
				for(String adjective : adjectives)
				{
					Element a = doc.createElement("ADJ");
					a.appendChild(doc.createTextNode(adjective));
					rootElement.appendChild(a);
				}
	
				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File("trainer\\ixq.xml"));
	
				transformer.transform(source, result);
	
				System.out.println("XML File updated!");

		  	} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  	} catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }	
		///END - TEXT PRE-PRCESSING
		   
		
		// START - DATABASE CONNECTION
		
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		String DB_URL = "jdbc:mysql://localhost/ihelp";
		String USER = "root";
		String PASS = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs;
		
		String tablename = "";
		Boolean switchStatus = true;
		String description = "Sorry! No results found";
		String destination = "";
        
        
        try {
        	if(whereClause.size()>=3) 
    		{
    			switch(fromClause)
    			{
    				case "where": tablename = "LL_LOCATION";	break;
    				case "when": tablename = "TT_TIME";	break;
    				default: tablename = "RR_REST";
    			}
    		outer1: for(String key1: whereClause)
    		        {
    		        
    			        for(String key2: whereClause)
    					{
    				inner1:	for(String key3: whereClause)
    						{
    							System.out.println(key1);
    							System.out.println(key2);
    							if(key1.equals(key2))
    								continue inner1;
    							try{
    						      //STEP 2: Register JDBC driver
    						      Class.forName("com.mysql.jdbc.Driver");
    				
    						      //STEP 3: Open a connection
    						      System.out.println("Connecting to a single database...");
    						      conn = DriverManager.getConnection(DB_URL, USER, PASS);
    							
    						      System.out.println("Connected database successfully...");
    						      
    						      //STEP 4: Execute a query
    						      System.out.println("Creating statement...");
    						      stmt = conn.createStatement();
    				
    						      rs = stmt.executeQuery("SELECT * FROM "+tablename+" WHERE SEARCH_KEY1='"+key1+"' AND SEARCH_KEY2='"+key2+"' AND SEARCH_KEY3='"+key3+"'");
    						      //STEP 5: Extract data from result set
    						      if(!rs.next())
    						      {
    					    		  rs = stmt.executeQuery("SELECT * FROM "+tablename+" WHERE SEARCH_KEY1='"+key2+"' AND SEARCH_KEY2='"+key3+"' AND SEARCH_KEY3='"+key1+"'");
    						    	  if(!rs.next())
    							      {
    						    		  rs = stmt.executeQuery("SELECT * FROM "+tablename+" WHERE SEARCH_KEY1='"+key3+"' AND SEARCH_KEY2='"+key1+"' AND SEARCH_KEY3='"+key2+"'");
    							    	  if(!rs.next())
    								      {
    							    		  switchStatus = true;
    								    	  continue inner1;
    									  }
    										  
    							      }
    							    	  
    						      }
    						      do{
    						         //Retrieve by column name
    						         description = rs.getString("DESCRIPTION");
    						         destination = rs.getString("DESTINATION");
    				
    						         //Display values
    						         System.out.println("Description: " + description);
    						         System.out.println("Destination: " + destination);
    						         switchStatus = false;
    						      }while(rs.next());
    						      rs.close();
    						      break outer1;
    						   }catch(SQLException se){
    						      //Handle errors for JDBC
    						      se.printStackTrace();
    						   }catch(Exception e){
    						      //Handle errors for Class.forName
    						      e.printStackTrace();
    						   }finally{
    						      //finally block used to close resources
    						      try{
    						         if(stmt!=null)
    						            conn.close();
    						      }catch(SQLException se){
    						      }// do nothing
    						      try{
    						         if(conn!=null)
    						            conn.close();
    						      }catch(SQLException se){
    						         se.printStackTrace();
    						      }//end finally try
    						   }
    					
    						}
    					}
    		        }
    		        
    		}       
            
            if (switchStatus && whereClause.size()>=2)
            {
            
    			switch(fromClause)
    			{
    				case "where": tablename = "L_LOCATION";	break;
    				case "when": tablename = "T_TIME";	break;
    				default: tablename = "R_REST";
    			}
    	
    	outer:	for(String key1: whereClause)
    			{
    		inner:	for(String key2: whereClause)
    				{
    					System.out.println(key1);
    					System.out.println(key2);
    					if(key1.equals(key2))
    						continue inner;
    					try{
    				      //STEP 2: Register JDBC driver
    				      Class.forName("com.mysql.jdbc.Driver");
    		
    				      //STEP 3: Open a connection
    				      System.out.println("Connecting to a single database...");
    				      conn = DriverManager.getConnection(DB_URL, USER, PASS);
    				      System.out.println("Connected database successfully...");
    				      
    				      //STEP 4: Execute a query
    				      System.out.println("Creating statement...");
    				      stmt = conn.createStatement();
    		
    				      rs = stmt.executeQuery("SELECT * FROM "+tablename+" WHERE SEARCH_KEY1='"+key1+"' AND SEARCH_KEY2='"+key2+"'");
    				      //STEP 5: Extract data from result set
    				      if(!rs.next())
    				      {
    				    	  switchStatus = true;
    				    	  continue;
    				      }
    				      do{
    				         //Retrieve by column name
    				         description = rs.getString("DESCRIPTION");
    				         destination = rs.getString("DESTINATION");
    		
    				         //Display values
    				         System.out.println("Description: " + description);
    				         System.out.println("Destination: " + destination);
    				         switchStatus = false;
    				      }while(rs.next());
    				      rs.close();
    				      break outer;
    				   }catch(SQLException se){
    				      //Handle errors for JDBC
    				      se.printStackTrace();
    				   }catch(Exception e){
    				      //Handle errors for Class.forName
    				      e.printStackTrace();
    				   }finally{
    				      //finally block used to close resources
    				      try{
    				         if(stmt!=null)
    				            conn.close();
    				      }catch(SQLException se){
    				      }// do nothing
    				      try{
    				         if(conn!=null)
    				            conn.close();
    				      }catch(SQLException se){
    				         se.printStackTrace();
    				      }//end finally try
    				   }
    			
    				}
    			}
            }
    		
    		
    		if(switchStatus && whereClause.size()>=1)
    		{
    			switch(fromClause)
    			{
    				case "where": tablename = "LOCATION";	break;
    				case "when": tablename = "TIME";	break;
    				default: tablename = "REST";
    			}
    			
    			for(String key: whereClause)
    			{
    				try{
    			      //STEP 2: Register JDBC driver
    			      Class.forName("com.mysql.jdbc.Driver");
    	
    			      //STEP 3: Open a connection
    			      System.out.println("Connecting to a selected database...");
    			      conn = DriverManager.getConnection(DB_URL, USER, PASS);
    			      System.out.println("Connected database successfully...");
    			      
    			      //STEP 4: Execute a query
    			      System.out.println("Creating statement...");
    			      stmt = conn.createStatement();
    	
    			      rs = stmt.executeQuery("SELECT * FROM "+tablename+" WHERE SEARCH_KEY='"+key+"'");
    			      //STEP 5: Extract data from result set
    			      if(!rs.next())
    			    	  continue;
    			      do{
    			         //Retrieve by column name
    			         description = rs.getString("DESCRIPTION");
    			         destination = rs.getString("DESTINATION");
    	
    			         //Display values
    			         System.out.println("Description: " + description);
    			         System.out.println("Destination: " + destination);
    			      }while(rs.next());
    			      rs.close();
    			      break;
    			   }catch(SQLException se){
    			      //Handle errors for JDBC
    			      se.printStackTrace();
    			   }catch(Exception e){
    			      //Handle errors for Class.forName
    			      e.printStackTrace();
    			   }finally{
    			      //finally block used to close resources
    			      try{
    			         if(stmt!=null)
    			            conn.close();
    			      }catch(SQLException se){
    			      }// do nothing
    			      try{
    			         if(conn!=null)
    			            conn.close();
    			      }catch(SQLException se){
    			         se.printStackTrace();
    			      }//end finally try
    			   }

    			}
    		}
        } catch (Exception e) {
        	System.out.println("Connection to DB failed");
        }
		
		
	    
		
		// END - DATABASE CONNECTION
		
	    
	    
	    
		//START - TEXT-2-SPEECH
        
        TTSout ttsout = new TTSout(description);
        
		
		//Create TextToSpeech
		//TextToSpeech tts = new TextToSpeech();

				// Setting the Voice
		//tts.setVoice("dfki-poppy-hsmm");//Other voices: dfki-poppy-hsmm, cmu-slt-hsmm, cmu-rms-hsmm
				
				// Gresponse is a String variable containing text
		//tts.speak(description, 2.0f, true, true);
		
		//END - TEXT-2-SPEECH
		flag = Integer.parseInt(destination);
		return Integer.parseInt(destination);
        
        
	    //return 1;
	}

}
