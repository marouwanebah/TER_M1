

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;


import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import beans.Personne;
import beans.PieceJointe;




public class parseur{


	//private static final String LIEN_FICHIER = "/home/etudiant/M1/S2/TER/Projet/TER_M1/Projet_TER/Data/president/";  
	
	private MimeMessage message; 
	
	
	public parseur(String messagePath) throws FileNotFoundException, MessagingException {
		super();
	    InputStream mailFileInputStream = new FileInputStream(messagePath);    
	    Properties props = System.getProperties(); 
	    Session session = Session.getInstance(props, null);
	    this.message = new MimeMessage(session, mailFileInputStream);
		
	}
	
	/**
	 * 
	 * @return
	 * @throws MessagingException
	 */
	
	public String GetMessageId() throws MessagingException {
		String id = this.message.getMessageID();
		String finalID = id.replace("<", "").replace(">", "");	
		return finalID;
	}
	
	
	/**
	 * Fonction qui retourne les information nom, prenom, email 
	 * dans un object de type personne(String nom,String Prenomn, String Mail)  
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public  Personne getExpediteur() throws MessagingException, UnsupportedEncodingException {
		Personne expediteur= new Personne (); 
		
		Address[] expediteurBrut = this.message.getFrom();
		String element = expediteurBrut[0].toString();
    	if(element.startsWith("=?ISO-8859-1")) {
	    	System.out.println("ISO-8859-1 enconterted");
	    	//decodage des ISO-....
    		String decodedElement = MimeUtility.decodeText(element);
    		expediteur = stringToPersonne(decodedElement);
   
    	}
		expediteur = stringToPersonne(element); 
	    return expediteur;
		
	}
	/**
	 * fonction qui prend en parametre type Message.RecipientType.TO pour les destinataires 
	 * et  Message.RecipientType.CC  pour destinataires en copies 
	 * @param type
	 * @return
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
 
	public ArrayList<Personne> getDestinataire(Message.RecipientType type) throws MessagingException, UnsupportedEncodingException{
		ArrayList<Personne> destinataire  = new ArrayList<>(); 
		Address[] destinataireBrut = this.message.getRecipients(type);
	    //recuperation de tous les destinataire 
	    for(int num = 0; num < destinataireBrut.length ; num++) {
	    	String element = destinataireBrut[num].toString(); 
	    	if(element.startsWith("=?ISO-8859-1")) {
		    	System.out.println("ISO-8859-1 enconterted");
		    	//decodage 
	    		String decodedElement = MimeUtility.decodeText(element);
	    		Personne a = stringToPersonne(decodedElement);
	    		destinataire.add(a);
	    	}else {
	    		//le replace  est utiliser pour enlever le ' qui reste sur certain nom et prenom de destinataire 
	    		Personne a = stringToPersonne(element);
		    	destinataire.add(a);
	    	}
	    }
		
		return destinataire;
		
	}
	/**
	 * fonction qui retourne le sujet du message 
	 * @return
	 * @throws MessagingException
	 */
	
	public String GetSubject() throws MessagingException {
		String a = this.message.getSubject();  
		return a; 
	}
	
	/**
	 * fonction qui retour le contenu du mail 
	 *  //** piste pour traiter les mails avec reponse https://github.com/edlio/EmailReplyParser
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public String GetMailContenu() throws MessagingException, IOException {
		String body =""; 
		
		String contentType = this.message.getContentType();
		     
		//recuperation du contenu si le contain type=text/plain
		if(contentType.contains("text/plain"))
		{  
			System.out.println("text plain detected--1");
		    Object content = this.message.getContent();
		    if(content != null)
		                body += content.toString();
		}

		//recuperation du contenu si le contain type=text/html utilisation de la librairie JSOUP pour gerer les partie html 	    
		if(contentType.contains("text/html")){
			System.out.println("text/html detected ");
		      	
			Object content = this.message.getContent();
		    if(content != null)
		       		body += Jsoup.parse((String)content).text();
		}
		//pour les mails de type multupart avec ou sans piéce joint piéce joint
		if(contentType.contains("multipart")){  
			System.out.println("mutipart");
		    Multipart mp = (Multipart)this.message.getContent();
		    int numParts = mp.getCount();
		           
		    for(int count = 0; count < numParts; count++)
		    {	    
		    	MimeBodyPart part = (MimeBodyPart)mp.getBodyPart(count);
		        String content = part.getContent().toString();
		        //pour les parts qui sont de type content text.html on utlise la libraire jsoup  
		        if(part.getContentType().contains("text/html")) {
		        	body += Jsoup.parse(content).text();
		        	
		        }    
		        else if(part.getContent() instanceof MimeMultipart )
		            	body += getTextFromMimeMultipart((MimeMultipart) part.getContent()); 
		        else 
		        	body += content;
		    }
		}
		body = MimeUtility.decodeText(body);
		return body; 
	}
	/**
	 * recuperation de la signature  il y'aura un partie a gerer pour les mail qui ont des réponses car on recupere plusieur signature 
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public String getSignature() throws MessagingException, IOException {
		String signature =""; 
		String contentType = this.message.getContentType();

		String content = this.message.getContent().toString(); 
		if(contentType.contains("text/html" )) {
			System.out.println("signature ");
			org.jsoup.nodes.Document doc=  Jsoup.parse(content);
			Elements a = doc.getElementsByClass("moz-signature"); 
			signature = a.text();
		}
		else {
			Multipart mp = (Multipart)this.message.getContent();
			int numParts = mp.getCount();
			           
			for(int count = 0; count < numParts; count++)
			{	    
				MimeBodyPart part = (MimeBodyPart)mp.getBodyPart(count);
			    //pour les parts qui sont de type content text.html on utlise la libraire jsoup  
			    if(part.isMimeType("text/html")) {
					System.out.println("tedddddddddddddddddddddddd ");
					org.jsoup.nodes.Document doc=  Jsoup.parse(content);
					Elements a = doc.getElementsByClass("moz-signature"); 
					signature = a.text();
			        	
			      }    
			    if(part.getContent() instanceof MimeMultipart){
			    	signature +=getSignatureFromMimeMultipart((MimeMultipart) part.getContent());
			    	
			    }
			}
		}	
		return signature;
		
	}
	/**
	 * recuperation des piéce jointe 
	 * il reste les contenu la piece a décoder 
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	//il reste a gere le contenue de la piéce jointe  recuperation du nom et de l'id de la piece joint fait 
	public ArrayList<PieceJointe> getPieceJointe() throws MessagingException, IOException{
		String contentType = this.message.getContentType();
		ArrayList<PieceJointe> attachments = new ArrayList<PieceJointe>(); 
		 //pour les mails de type multupart avec ou sans piéce joint piéce joint
	    if(contentType.contains("multipart")){  
	    	System.out.println("mutipart");
	    	Multipart mp = (Multipart)this.message.getContent();
	    	int numParts = mp.getCount();   
	        for(int count = 0; count < numParts; count++)
	        {	    
	        	MimeBodyPart part = (MimeBodyPart)mp.getBodyPart(count);
	            //pour les piéce jointe on te recupere juste pour l'instant quel traiment faire ? 
	            if(MimeBodyPart.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
	            	PieceJointe piece = new PieceJointe(); 
	            	String nomPiceJointe = MimeUtility.decodeText(part.getFileName());
	            	String messageID = GetMessageId(); 

	            	//Base64.Decoder decodeur = Base64.getMimeDecoder(); 
	            	//byte[] decryptes = decodeur.decode(part.getContent().toString().getBytes());
	            	//System.out.println(new String(decryptes))
	            	//MimeUtility.re
	            	//System.out.println(part.get);
	            	//MimeUtility.decode(new ByteArrayInputStream(part.getContent().toString() ), "UTF-8"); 
	            	System.out.println(nomPiceJointe); 
//	System.out.println(part.getParent().g);
	            	//System.out.println(part.getDescription()); 
	            	piece.setNomPieceJointe(nomPiceJointe);
	            	piece.setMailID(messageID);
	            	
	            }
	            	
	        }
	    }
		
		return attachments;
		
	}
	
	
	
	
	
	/*
	 * Fonction qui recupere toutes les infos  du mail et qui crée un object 
	 * Maillist qui a toutes des informations du mail 
	 */
	

	
	public static MailList ParseMail(String messagePath) throws MessagingException, IOException {
		
	    InputStream mailFileInputStream = new FileInputStream(messagePath);    
	    Properties props = System.getProperties(); 
	    Session session = Session.getInstance(props, null);
	    MimeMessage message = new MimeMessage(session, mailFileInputStream);
	    
	  //  Address[] cc= message.
	    //System.out.println("replay to : "+cc);
		
	    
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
	        	System.out.println("text plain detected +++1");
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
	    //creation de l'object 
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


	    
	   
	
	
	private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart)  throws MessagingException, IOException{
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

	/**
	 * fonction qui recuperer les signature dans les mail de type multipart 
	 * 
	 * @param mimeMultipart
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	private static String getSignatureFromMimeMultipart(MimeMultipart mimeMultipart)  throws MessagingException, IOException{
	    String result = "";
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        if (bodyPart.isMimeType("text/html")) {
				org.jsoup.nodes.Document doc=  Jsoup.parse(bodyPart.getContent().toString());
				Elements a = doc.getElementsByClass("moz-signature"); 
	            result = a.text();
	        } else if (bodyPart.getContent() instanceof MimeMultipart){
	            result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
	        }
	    }
	    return result;
	}

	public  Personne stringToPersonne(String element)  {
		Personne pers = new Personne(); 
		//ca ou le mail a des destinataires non divulgués
		if (element.contains("undisclosed-recipients:")) {
		    pers.setNomPersonne("destinataires non divulgués");
		    pers.setPrenomPersonne("destinataires non divulgués");
		    pers.setEmailPersonne("destinataires non divulgués");
		    System.out.println("pre:"+ "UR"+" nom : "+"UR"+" email :  "+"UR");
		}
		else {
			String elementNew= element.replace("<", "").replace(">", "").replace("'", "");
		    String[] textSplited= elementNew.split(" ");
		    //si le text est =3 on a un format prenom, non et email 
		    if(textSplited.length==3) {
			    String prenom = textSplited[0];
			    String nom = textSplited[1];
			    String email = textSplited[2];
			    System.out.println("pre:"+ prenom+" nom : "+nom+" email :  "+email);
			    pers.setNomPersonne(nom);
			    pers.setPrenomPersonne(prenom);
			    pers.setEmailPersonne(email);
		    }
		    //on a juste le mail  gestion du des case nom et prénom a determinier avec touria 
		    if(textSplited.length==1) {
			    String email = textSplited[0];
			    System.out.println("email :  "+email);
			    pers.setEmailPersonne(email);
		    }
		    //pour les destinataire qui on un second prenom 
		    if(textSplited.length >=4) {
		    	 String prenom = textSplited[0]+" "+textSplited[1];
		    	 String nom = textSplited[2];
		    	 String email = textSplited[3];
		    	 System.out.println("pre:"+ prenom+" nom : "+nom+" email :  "+email);
				 pers.setNomPersonne(nom);
				 pers.setPrenomPersonne(prenom);
				 pers.setEmailPersonne(email);
		    	
		    }

		}

		
	    return pers;
		
	}
	
}
