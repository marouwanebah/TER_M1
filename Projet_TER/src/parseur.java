
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jsoup.Jsoup;

import beans.Mail;
import beans.MailDestinataire;
import beans.Personne;
import beans.TypePersonne;
import dao.MailDao;
import dao.MailDestinataireDao;
import dao.PersonneDAO;




public class parseur {


	private static final String LIEN_FICHIER = "/home/etudiant/M1/S2/TER/Projet/TER_M1/Projet_TER/Data/president/";  
	
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
	    ArrayList<lien> liens = new ArrayList<lien>(); 
	    ArrayList<MimeBodyPart> attachments = new ArrayList<MimeBodyPart>();         
	    String contentType = message.getContentType();
	    Address[] addresses = message.getFrom();
	    Address[] AdresDestinatire = message.getAllRecipients(); 
	        
	    //recuperation de tous les destinataire 
	    for(int num = 0; num < AdresDestinatire.length ; num++) {
	    	destinataire.add(AdresDestinatire[num].toString()); 
	    }
  
	    //recuperation de l'expéditeur vu que la fonction  getFrom() retourne un tableau je gere le cas ou il y'en a plusieur 
	    if(addresses.length == 1)
	        from = addresses[0].toString();
	    else
	    {
	    	for(int num = 0; num < addresses.length; num++)
	    		from += addresses[num].toString() + ", ";
	            from += addresses[addresses.length].toString();
	    }
	    
	    //recuperation du contenu si le contain type=text/plain
	    if(contentType.contains("text/plain"))
	    {  
	        	System.out.println("text plain detected ");
	            Object content = message.getContent();
	            if(content != null)
	                body += content.toString();
	    }

	    //recuperation du contenu si le contain type=text/html utilisation de la librairie JSOUP pour gerer les partie html 	    
	    if(contentType.contains("text/html")){
	       	System.out.println("text/html detected ");
	        	
	       	Object content = message.getContent();
	       	if(content != null)
	       		body += Jsoup.parse((String)content).text();
	    }
	    //pour les mails de type multupart avec ou sans piéce joint piéce joint
	    if(contentType.contains("multipart")){  
	    	System.out.println("mutipart");
	    	Multipart mp = (Multipart)message.getContent();
	    	int numParts = mp.getCount();
	           
	        for(int count = 0; count < numParts; count++)
	        {	    
	        	MimeBodyPart part = (MimeBodyPart)mp.getBodyPart(count);
	            String content = part.getContent().toString();
	            //pour les piéce jointe on te recupere juste pour l'instant quel traiment faire ? 
	            if(MimeBodyPart.ATTACHMENT.equalsIgnoreCase(part.getDisposition()))
	            	attachments.add(part);
	            //pour les parts qui sont de type content text.html on utlise la libraire jsoup  
	            else if(part.getContentType().contains("text/html")) {
	            	body += Jsoup.parse(content).text();
	            	//exemple de recuperation des liens
	            	for (org.jsoup.nodes.Element a: Jsoup.parse(content).select("a")) {
	            			lien link= new lien(a.attr("abs:href"), a.text() );
	            			liens.add(link);
	            		}
	            }    
	            else if(part.getContent() instanceof MimeMultipart )
	            	body += getTextFromMimeMultipart((MimeMultipart) part.getContent()); 
	            else 
	                	body +=content;
	            }

	        }
	    //creation 
	    MailList a = new MailList(message.getMessageID(),from, destinataire, message.getSubject(), body,
	                message.getSentDate().toString(), attachments, liens);
	    
/*
	        System.out.println(a.toString());
	        for (MimeBodyPart part : a.getAttachments() ) {
	        	System.out.println(part.getContent());
	        }
*/        
	        return a; 
		
	       
	    }


	    
	   
	
	
	private static String getTextFromMimeMultipart(
	        MimeMultipart mimeMultipart)  throws MessagingException, IOException{
	    String result = "";
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        if (bodyPart.isMimeType("text/plain")) {
	            result = result + "\n" + bodyPart.getContent();
	            break; // sans break le même texte apparaît deux fois dans mes tests
	        } else if (bodyPart.isMimeType("text/html")) {
	            String html = (String) bodyPart.getContent();
	            result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
	        } else if (bodyPart.getContent() instanceof MimeMultipart){
	            result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
	        }
	    }
	    return result;
	}
	
	public static void main (String[] args) throws MessagingException, IOException {
		PersonneDAO personneDao;
		MailDao mailDao;
		MailDestinataireDao malDestinataireDao;
		dao.DaoFactory daoFactory = dao.DaoFactory.getInstance();
		personneDao = daoFactory.getPersonneDao();
		mailDao = daoFactory.getMailDao();
		malDestinataireDao = daoFactory.getMailDestinataireDao();
		
		
		//MailList a= ParseMail(LIEN_FICHIER+17); 
		//System.out.println(a.toString());
		
		ArrayList<MailList> listeMail = new ArrayList<MailList>(); 
		
		for(int i = 1; i<32; ++i) {
			listeMail.add(ParseMail(LIEN_FICHIER+i)); 
		}
		/*
					System.out.println("--------------"+"message numero : "+(i)+"-------------------");
			System.out.println(a.toString());
			System.out.println("-------------"+" fin message numero : "+(i)+"--------------");
			++i;
		*/
		for(MailList a : listeMail) {

			//insert Personne
			TypePersonne typePersonne = new TypePersonne();
			typePersonne.setCodePersonne("PH");
			typePersonne.setLibellePersonne("Physique");
			Personne personne= new Personne();
			personne.setEmailPersonne(a.getFrom());
			personne.setNomPersonne("test");
			personne.setPrenomPersonne("test");
			personne.setRolePersonne("test");
			personne.setTypePersonne(typePersonne);
			if(personneDao.getPersonne(personne.getEmailPersonne())== null )
				personneDao.ajouterPersonne(personne);
			//insert Mail
			Mail mail = new Mail();
			Mail mailPere = new Mail();
			mail.setIdMail(a.getIdMail());
			mail.setContenuMail(a.getBody());
			mail.setSujetMail(a.getSujet());
			mail.setDateEnvoiMail(a.getDate());
			mail.setExpediteur(personne);
			mail.setMailPère(mailPere);
			if(mailDao.getMail(mail.getIdMail()) == null)
				mailDao.ajouterMail(mail);
			//insert Destinataire
			MailDestinataire mailDestinataire = new MailDestinataire();
			mailDestinataire.setMail(mail);
			for(String dest : a.getDestinataire()) {
				personne = personneDao.getPersonne(dest);
				if(personne == null ) {
					Personne destinat= new Personne();
					destinat.setEmailPersonne(dest);
					destinat.setNomPersonne("test");
					destinat.setPrenomPersonne("test");
					destinat.setRolePersonne("test");
					destinat.setTypePersonne(typePersonne);
					personneDao.ajouterPersonne(destinat);
					personne = destinat;
				}
				mailDestinataire.setPersonne(personne);
				if(malDestinataireDao.getMailDestinataire(mail, personne) == null)
					malDestinataireDao.ajouterDestinataire(mailDestinataire);
			}

		}
		
	
	}
}
