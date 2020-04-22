import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.ContentHandler;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;




public class parseur {

	private static final String LIEN_FICHIER = "/home/etudiant/M1/S2/TER/Projet/TER_M1/Data/president/";  
	
	public static void main (String[] args) throws IOException, MimeException, MessagingException{
		/*
		BufferedReader fichierEntre = new BufferedReader(new FileReader(LIEN_FICHIER+17));
		String line;
		
		while ((line = fichierEntre.readLine()) != null)
		{
			// Afficher le contenu du fichier
			System.out.println (line);

		}
		fichierEntre.close();
		
		
		File input = new File(LIEN_FICHIER+17);

		Document doc =Jsoup.parse(input,"UTF-8");
		String body = doc.body().text();
		String title = doc.title(); 
		//String text = doc.body().text();
		org.jsoup.nodes.Element content =doc.body();
 
		System.out.print(title);
		System.out.print(body);
		
		 
		//MimeMessage a = Mime;  
		

		MimeStreamParser parser = new MimeStreamParser();
		InputStream instream = new FileInputStream(LIEN_FICHIER+17);
	        
		parser.parse(instream);	     
		instream.close();
	      
	    	
		 StringBuffer fileData = new StringBuffer(1000);
		    BufferedReader reader = new BufferedReader(new FileReader(new File(LIEN_FICHIER+17)));
		    char[] buf = new char[1024];
		    int numRead = 0;
		    while ((numRead = reader.read(buf)) != -1) {
		        fileData.append(buf, 0, numRead);
		    }
		    reader.close();

  */

		// Create a MimeMessage

		    InputStream mailFileInputStream = new FileInputStream(LIEN_FICHIER+14);    
		    Properties props = System.getProperties(); 
		    Session session = Session.getInstance(props, null);
		    MimeMessage message = new MimeMessage(session, mailFileInputStream);

		    Address[] destinataire = message.getAllRecipients(); 
		    Date DateEnvoie = message.getSentDate(); 
		    Address[] sender = message.getFrom(); 
		    String sujet = message.getSubject();
		    Object contenu  = message.getDataHandler().getContent(); 
		    
		    
		    for( int i=0; i<destinataire.length; ++i) {
		    	System.out.println( "Destinataire : "+ destinataire[i]);
		    }
		    for( int i=0; i<sender.length; ++i) {
		    	System.out.println("sender :"+ sender[i]);
		    }
		    
		    System.out.println(DateEnvoie);
		    System.out.println("sujet :" + sujet);
		    
			File input = new File(LIEN_FICHIER+14);

			Document doc =Jsoup.parse(input,"UTF-8");
			//exemple de recuperation de lien 
			//Elements tex = doc.select("a"); 
			for (org.jsoup.nodes.Element a: doc.select("a")) {
				System.out.println(a.attr("href"));
			}

		  //  System.out.println(doc.ti);
	}
	
}
