

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import java.util.Properties;
import java.util.Scanner;

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


import beans.Personne;
import beans.PieceJointe;




public class parseur{

	//ne jamais effacer le lien pour les autres mettre juste en commentaire 
	//private static final String LIEN_FICHIER = "/home/etudiant/M1/S2/TER/Projet/TER_M1/Projet_TER/Data/president/";  
	//private static final String dossierAttachement = "/home/diallo/Documents/projetTER/corpus/president_2010/president_2010/DATA/attachments/";
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
		expediteur = stringToPersonne(element); 
	    return expediteur;
		
	}
	/**
	 * fonction qui prend en parametre type Message.RecipientType.TO pour les destinataires 
	 * ou  Message.RecipientType.CC  pour destinataires en copies  et tetourne la liste des destinaires 
	 * ou des destinataire en CC  
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
	    		Personne a = stringToPersonne(element);
		    	destinataire.add(a);
	    }
		return destinataire;
	}
	/**
	 * fonction qui retourne le sujet du message 
	 * @return
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException 
	 */
	
	public String GetSubject() throws MessagingException, UnsupportedEncodingException {
		String element = this.message.getSubject();  
		return element; 
	}
	
	/**
	 * fonction qui retour le contenu du mail 
	 *  //** piste pour traiter les mails avec reponse https://github.com/edlio/EmailReplyParser
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public String GetMailContenu() throws MessagingException, IOException {

		String body = getTextFromMessage(message);     
		//body = MimeUtility.decodeText(body);
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
	                
	                //contenue piéce joint to  string 
	                InputStream inputStream = new FileInputStream(dossierAttachement+nomPiceJointe);
	                //Creating a Scanner object
	                Scanner sc = new Scanner(inputStream);
	                //Reading line by line from scanner to StringBuffer
	                StringBuffer sb = new StringBuffer();
	                while(sc.hasNext()){
	                   sb.append(sc.nextLine());
	                }
	                
	            	piece.setNomPieceJointe(nomPiceJointe);
	            	piece.setMailId(messageID);
	            	piece.setContenuJointe(sb.toString()); 
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
		mailObject.setSujet(this.GetSubject());
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
	
	private static  String getTextFromMessage(Message message) throws MessagingException, IOException {
	    String result = "";
	    System.out.println(message.getContentType());
	    if (message.isMimeType("text/plain")) {
	        result = message.getContent().toString();
	    } 
	    else if (message.isMimeType("text/html")) {
            String html = (String) message.getContent();
            result = result + "\n" + org.jsoup.Jsoup.parse(html).text(); 
	    } else if (message.isMimeType("multipart/*")) {
	        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
	        result = getTextFromMimeMultipart(mimeMultipart);
	    }
	    return result;
	}

	private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart)  throws MessagingException, IOException{
	    String result = "";
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        if (bodyPart.isMimeType("text/plain")) {
	            result = result + "\n" + bodyPart.getContent();
	            break; //Sans break affiche deux fois 
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
	 * fonction qui recuperer les signatures dans les mail de type multipart qui on une partie text/hml
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
	/**
	 * fontion qui prend en parametre un chaine de caratére ou juste un mail et qui 
	 * retourne une personne (nom, prenom , mail) 
	 * @param e
	 * @return
	 * @throws UnsupportedEncodingException
	 */

	public  Personne stringToPersonne(String e) throws UnsupportedEncodingException  {
		Personne pers = new Personne(); 
		
		//decodage systematique des ISO-8859-1
		String element = MimeUtility.decodeText(e);
		//ca ou le mail a des destinataires non divulgués
		if (element.contains("undisclosed-recipients:")) {
		    pers.setNomPersonne("destinataires non divulgués");
		    pers.setPrenomPersonne("destinataires non divulgués");
		    pers.setEmailPersonne("destinataires non divulgués");
		}
		else {
			//supressiont de tous les caractère 
			String elementNew= element.replace("<", "").replace(">", "").replace("'", "").replace("[", "").replace("]", "");
		    String[] textSplited= elementNew.split(" ");
		    //si le text est =3 on a un format prenom, non et email 
		    if(textSplited.length==3) {
			    String prenom = textSplited[0];
			    String nom = textSplited[1].toUpperCase();
			    String email = textSplited[2];
			    pers.setNomPersonne(nom);
			    pers.setPrenomPersonne(prenom);
			    pers.setEmailPersonne(email);
		    }
		    //on a juste le mail  gestion des case nom et prénom 
		    else {
	    		String email = textSplited[textSplited.length-1];
		    	pers.setEmailPersonne(email);
		    	pers.setNomPersonne(nomEmailSplit(email));
		    	pers.setPrenomPersonne(prenomEmailSplit(email));
		    }

		}
		//System.out.println(pers.getNomPersonne() +" "+ pers.getPrenomPersonne());
	    return pers;
		
	}
	/**
	 * fonction qui retourne le nom sur un email(string) de type marouwane.bah@quesquechose.fr
	 * @param email
	 * @return
	 */
	
	public String nomEmailSplit(String email) {
    	String[] emailSplited = email.split("@"); // exe francoi.calsel @ up.fr
    	String[] partie1 = emailSplited[0].split("\\.");     // separation du nom et prenom ex francois.cassel 
    	
    	String nom=""; 
    	if(partie1.length==2) {
		    nom= partie1[1]; 
    	}
    	else 
    		nom=""; 
    	return nom.toUpperCase(); 
	}
	/**
	 * fonction qui retourne le prenom sur un email(string) de type marouwane.bah@quesquechose.fr
	 * @param email
	 * @return
	 */
	public String prenomEmailSplit(String email) {
    	String[] emailSplited = email.split("@"); // exe francoi.calsel @ up.fr
    	String[] partie1 = emailSplited[0].split("\\.");     // separation du nom et prenom ex francois.cassel 
    	String prenom=""; 
    	if(partie1.length==2) {
    		prenom= partie1[0]; 
    	}
    	else 
    		prenom=""; 
    	return prenom; 
	}
}
