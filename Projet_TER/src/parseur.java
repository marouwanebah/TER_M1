

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;


import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import beans.Mail;
import beans.Personne;
import beans.PieceJointe;




public class parseur{


	//private static final String LIEN_FICHIER = "/home/etudiant/M1/S2/TER/Projet/TER_M1/Projet_TER/Data/president/";  
	private static final String dossierAttachement = "/home/etudiant/M1/S2/TER/Projet/TER_M1/Projet_TER/Data/Attachement/";

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
		    Object content = this.message.getContent();
		    if(content != null)
		                body += content.toString();
		}

		//recuperation du contenu si le contain type=text/html utilisation de la librairie JSOUP pour gerer les partie html 	    
		if(contentType.contains("text/html")){
			Object content = this.message.getContent();
		    if(content != null)
		       		body += Jsoup.parse((String)content).text();
		}
		//pour les mails de type multupart avec ou sans piéce joint piéce joint
		if(contentType.contains("multipart")){  
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
			org.jsoup.nodes.Document doc=  Jsoup.parse(content);
			Elements a = doc.getElementsByClass("moz-signature"); 
			signature = a.text();
		}
		if(contentType.contains("multipart" )) {
			Multipart mp = (Multipart)this.message.getContent();
			int numParts = mp.getCount();
			           
			for(int count = 0; count < numParts; count++)
			{	    
				MimeBodyPart part = (MimeBodyPart)mp.getBodyPart(count);
			    //pour les parts qui sont de type content text.html on utlise la libraire jsoup  
			    if(part.isMimeType("text/html")) {
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
	 * recreation des piéce joint  
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */

	public ArrayList<PieceJointe> getPieceJointe() throws MessagingException, IOException{
		String contentType = this.message.getContentType();
		ArrayList<PieceJointe> attachments = new ArrayList<PieceJointe>(); 
		//pour les mails de type multupart avec ou sans piéce joint piéce joint
	    if(contentType.contains("multipart")){  
	    	Multipart mp = (Multipart)this.message.getContent();
	    	int numParts = mp.getCount();   
	        for(int count = 0; count < numParts; count++)
	        {	    
	        	MimeBodyPart part = (MimeBodyPart)mp.getBodyPart(count);
	            //pour les piéce jointe on te recupere juste pour l'instant quel traiment faire ? 
	            if(MimeBodyPart.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
	            	PieceJointe piece = new PieceJointe(); 
	            	//Mail mail = new Mail();
	            	String nomPiceJointe = MimeUtility.decodeText(part.getFileName());
	            	String messageID = GetMessageId(); 

					File f = new File(dossierAttachement + nomPiceJointe);
	            	InputStream is = part.getInputStream();
	            	FileOutputStream fos = new FileOutputStream(f);
	                byte[] buf = new byte[4096];
	                int bytesRead;
	                while((bytesRead = is.read(buf))!=-1) {
	                    fos.write(buf, 0, bytesRead);
	                }
	                fos.close();

	            	piece.setNomPieceJointe(nomPiceJointe);
	            	piece.setMailId(messageID);
	            	piece.setContenuJointe(nomPiceJointe); //dans le contenu de la piece joint pour l'instant on met le nom en attendant 
	            	
	            	attachments.add(piece); 
	            }	            	
	        }
	    }
		return attachments;
	}
	public void getMailTest() throws MessagingException, IOException {
		System.out.println("************************MessageID********************");
		System.out.println(this.GetMessageId()); 	
		System.out.println("************************expéditeur********************");		
		this.getExpediteur();
		System.out.println("************************Destinataire********************");		
		ArrayList<Personne>  destinatire = this.getDestinataire(Message.RecipientType.TO);
		System.out.println("************************en copie********************");
		Address[] test = this.message.getRecipients(Message.RecipientType.CC); 
		if(test==null) {
			System.out.println("pas de personne en copie ");
		}else 
		{
			System.out.println(test.length);
			ArrayList<Personne>  destinatireCC = this.getDestinataire(Message.RecipientType.CC);
		}
		System.out.println("************************subject********************");
		System.out.println(this.GetSubject());
		System.out.println("************************BODY********************");
		System.out.println(this.GetMailContenu()); 
		System.out.println("************************attachement********************");
		this.getPieceJointe();
		System.out.println("************************Signature********************");
		System.out.println(this.getSignature());
		
		System.out.println("************************fin Signature********************");
		
		
		
	}
	
	
	
	
	/*
	 * Fonction qui recupere toutes les infos  du mail et qui crée un object 
	  Maillist qui a toutes des informations du mail 
	 */
	public String getDate() throws MessagingException {
		return this.message.getSentDate().toString(); 
	}

	/**
	 * fonction qui créee un Object de type maillist qui recupere tous les monposant d'un mail  sauf les liens pour l'instant 
	 * @param messagePath
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	
	public  MailList mailToObject() throws MessagingException, IOException {
		MailList mailObject = new MailList(); 
		
		mailObject.setIdMail(this.GetMessageId());
		mailObject.setFrom(this.getExpediteur());
		mailObject.setDestinataire(this.getDestinataire(RecipientType.TO)); 
		Address[] test = this.message.getRecipients(Message.RecipientType.CC); 
		if(test!=null) {
			mailObject.setDestinataireEnCopie(this.getDestinataire(Message.RecipientType.CC));
		}
		mailObject.setAttachments(this.getPieceJointe());
		mailObject.setSignature(this.getSignature());
		mailObject.setDate(this.getDate());
		mailObject.setSignature(this.getSignature());
		mailObject.setBody(this.GetMailContenu());
	 //   MailList a = new MailList(message.getMessageID(),from, destinataire, message.getSubject(), body, message.getSentDate().toString(), attachments, liens);
	     return mailObject;   
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

	public  Personne stringToPersonne(String e) throws UnsupportedEncodingException  {
		Personne pers = new Personne(); 
		
		String element = MimeUtility.decodeText(e);
		
		//ca ou le mail a des destinataires non divulgués
		if (element.contains("undisclosed-recipients:")) {
		    pers.setNomPersonne("destinataires non divulgués");
		    pers.setPrenomPersonne("destinataires non divulgués");
		    pers.setEmailPersonne("destinataires non divulgués");
		}
		else {
			String elementNew= element.replace("<", "").replace(">", "").replace("'", "");
		    String[] textSplited= elementNew.split(" ");
		    //si le text est =3 on a un format prenom, non et email 
		    if(textSplited.length==3) {
			    String prenom = textSplited[0];
			    String nom = textSplited[1];
			    String email = textSplited[2];
			    pers.setNomPersonne(nom);
			    pers.setPrenomPersonne(prenom);
			    pers.setEmailPersonne(email);
		    }
		    //on a juste le mail  gestion du des case nom et prénom a determinier avec touria 
		    else if(textSplited.length==1) {
			    String email = textSplited[0];
			    pers.setNomPersonne("");
			    pers.setPrenomPersonne("");
			    pers.setEmailPersonne(email);
		    }
		    //pour les destinataire qui on un second prenom 
		    else if(textSplited.length >=4) {

		    	 String prenom = textSplited[0]+" "+textSplited[1];
		    	 //System.out.println(textSplited[textSplited.length-1]+" kk");
		    	 String nom = textSplited[2];
		    	 String email = textSplited[textSplited.length-1];
		    	// System.out.println(prenom);
				 pers.setNomPersonne(nom);
				 pers.setPrenomPersonne(prenom);
				 pers.setEmailPersonne(email);
		    	
		    }else {
		    	//cas exeptionel  de taile 2 mais on sais que le dernier element est toujour le mail 
		    	 pers.setPrenomPersonne(textSplited[0]);
				 pers.setEmailPersonne(textSplited[textSplited.length-1]);
		    	
		    }

		}

	    return pers;
		
	}
	
}
