
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import org.jsoup.Jsoup;




public class parseur {

	private static final String LIEN_FICHIER = "/home/etudiant/M1/S2/TER/Projet/TER_M1/Data/president/";  
	
	
	
	/*
	 * Fonction qui recupere toutes les infos  du mail et qui crée un object 
	 * Maillist qui a toutes des informations du mail 
	 */
	
	public static MailList ParseMail(String messagePath) throws MessagingException, IOException {
		
	    InputStream mailFileInputStream = new FileInputStream(messagePath);    
	    Properties props = System.getProperties(); 
	    Session session = Session.getInstance(props, null);
	    MimeMessage message = new MimeMessage(session, mailFileInputStream);

	    String body = "";
	    String from = "";
	    ArrayList<String> destinataire = new ArrayList<String>(); 
	    ArrayList<MimeBodyPart> attachments = new ArrayList<MimeBodyPart>();         
	    String contentType = message.getContentType();
	        Address[] addresses = message.getFrom();
	        Address[] AdresDestinatire = message.getAllRecipients(); 
	        
	        for(int num = 0; num < AdresDestinatire.length ; num++) {
	        	destinataire.add(AdresDestinatire[num].toString()); 
	        }

	        
	        
	        if(addresses.length == 1)
	            from = addresses[0].toString();
	        else
	        {
	            for(int num = 0; num < addresses.length - 1; num++)
	                from += addresses[num].toString() + ", ";
	            from += addresses[addresses.length].toString();
	        }
	        if(contentType.contains("text/plain"))
	        {  
	        	System.out.println("text plain detected ");
	            Object content = message.getContent();
	            if(content != null)
	                body += content.toString();
	        }
	        if(contentType.contains("text/html"))
	        {
	        	System.out.println("text/html detected ");
	        	
	            Object content = message.getContent();
	            if(content != null)
	                body += Jsoup.parse((String)content).text();
	        }
	        if(contentType.contains("multipart"))
	        {  
	        	System.out.println("mutipart");
	            Multipart mp = (Multipart)message.getContent();
	            int numParts = mp.getCount();
	           
	            for(int count = 0; count < numParts; count++)
	            {
	    
	                MimeBodyPart part = (MimeBodyPart)mp.getBodyPart(count);
	                String content = part.getContent().toString();
	                if(MimeBodyPart.ATTACHMENT.equalsIgnoreCase(part.getDisposition()))
	                    attachments.add(part);
	                else if(part.getContentType().contains("text/html"))
	                    body += Jsoup.parse(content).text();
	                else
	                    body += content;
	            }
	        }
	        MailList a = new MailList(from, destinataire, message.getSubject(), body,
	                message.getSentDate().toString(), attachments);
	    
/*
	        System.out.println(a.toString());
	        for (MimeBodyPart part : a.getAttachments() ) {
	        	System.out.println(part.getContent());
	        }
*/        
	        return a; 
		
	}
	
	public static void main (String[] args) throws MessagingException, IOException {
		
		ArrayList<MailList> listeMail = new ArrayList<MailList>(); 
		
		for(int i = 1; i<32; ++i) {
			listeMail.add(ParseMail(LIEN_FICHIER+i)); 
		}
		int i=1; 
		for(MailList a : listeMail) {
			System.out.println(a.getBody());
			System.out.println("-------------"+(i++)+"--------------");
		}
		
	}
	
}
