

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import beans.Institution;
import beans.Lien;
import beans.Personne;
import beans.PieceJointe;




public class parseur{

	// ********************** lien sory ******************************************//
	//private static final String dossierAttachement = "/home/diallo/Documents/projetTER/corpus/president_2010/DATA/attachments/";

	// ********************** lien Marwane ******************************************//
	private static final String dossierAttachement = "/home/etudiant/M1/S2/TER/Projet/TER_M1/Projet_TER/Data/Attachement/";

	// ********************** lien Mm Touria ******************************************//
	//private static final String dossierAttachement = "/home/touria/Bureau/Corpus/Data/Attachement/";
		
		
	private MimeMessage message; 
	
	public parseur(String messagePath) throws FileNotFoundException, MessagingException {
		super();
	    InputStream mailFileInputStream = new FileInputStream(messagePath);    
	    Properties props = System.getProperties(); 
	    props.setProperty("mail.mime.address.strict", "false");
	    Session session = Session.getInstance(props, null);
	    this.message = new MimeMessage(session, mailFileInputStream);		
	}
	
	/**
	 * fonction qui retourne l'id du message 
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
 
	public ArrayList<Personne> getDestinataire(Message.RecipientType type) throws UnsupportedEncodingException, MessagingException  {
		ArrayList<Personne> destinataire  = new ArrayList<>(); 
		Address[] destinataireBrut ; 
		if( this.message.getRecipients(type)!=null) {
			destinataireBrut = this.message.getRecipients(type);
	
		    //recuperation de tous les destinataire 
		    for(int num = 0; num < destinataireBrut.length ; num++) {
		    	String element = destinataireBrut[num].toString(); 
		    		Personne a = stringToPersonne(element);
			    	destinataire.add(a);
		    }
		}
		else {
			Personne a = new Personne();
			Institution b = new Institution(); 
			a.setEmailPersonne("Pas de Destinaire");
			a.setNomPersonne("Pas de Destinaire");
			a.setPrenomPersonne("Pas de Destinaire");
			a.setInstitutionPersonne(b);
			destinataire.add(a); 
		}
		return destinataire;
	}
	
	/**
	 * fonction qui retourne le type de message  simple pour les message sans fil de discution 
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public String getTypeMessage() throws MessagingException, IOException {
		
		String a = this.GetMailContenu();
		String Multi = "Multi"; 
		String Simple = "Simple";
		if(a.contains("-----Message d'origine-----")) {
			return Multi; 
		}
		else if( a.contains("De :") && a.contains("Envoyé ") && a.contains("Objet :")) {
			return Multi; 
		}
		else if(a.contains(">") && a.contains("a écrit :") ) {
			return Multi; 
		}
		else if(a.contains(">") && a.contains("a ?crit?:") ) {
			return Multi; 
		}
		
		return Simple;
		
	}
	/**
	 * fonction qui retourne le sujet du message 
	 * @return
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException 
	 */
	
	public String GetSubject() throws MessagingException, UnsupportedEncodingException {
		String element = this.message.getSubject();  
		String decodage = MimeUtility.decodeText(element).replace("[president]", "");
		//System.out.println(element.replace("[president]", ""));
		return decodage;
		
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
		return body; 
	}
	/**
	 * recuperation de la signature  il y'aura un partie a gerer pour les mail qui ont des réponses car on recupere plusieur signature 
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public String getSignature() throws MessagingException, IOException {
		
		String withCharacters  = this.GetMailContenu().replace('\u0092','\'').replace('\u0095','-').replace('\u0096','à'); 
		String contenueBrut = StringEscapeUtils.unescapeHtml(withCharacters);
		
		if(this.getTypeMessage()=="Simple") {
			
			if(contenueBrut.contains("salutations")){
				String[] splitContenut = contenueBrut.split("salutations");
				return splitContenut[1].replace(",", "").replace(".", "");
			}
			else if(contenueBrut.contains("Cordialement")){
				String[] splitContenut = contenueBrut.split("Cordialement");
				if(splitContenut.length==2) {
					return splitContenut[1].replace(",", "").replace(".", "");
				}
			}
			else if(contenueBrut.contains("cordialement")){
				String[] splitContenut = contenueBrut.split("cordialement");
				if(splitContenut.length==2) {
					return splitContenut[1].replace(",", "").replace(".", "");
				}
			} 
			else if(contenueBrut.contains("Sincèrement")){
				String[] splitContenut = contenueBrut.split("Sincèrement");
				return splitContenut[1].replace(",", "").replace(".", "");
			}
			else if(contenueBrut.contains("Bonne réception")){
				String[] splitContenut = contenueBrut.split("Bonne réception");
				return splitContenut[1].replace(",", "").replace(".", "");
			}
			else if(contenueBrut.contains("Bien sincèrement")){
				String[] splitContenut = contenueBrut.split("Bien sincèrement");
				return splitContenut[1].replace(",", "").replace(".", "");
			}
			else if(contenueBrut.contains("Respectueusement")){
				String[] splitContenut = contenueBrut.split("Respectueusement");
				return splitContenut[1].replace(",", "").replace(".", "");
			}
			else if(contenueBrut.contains("--")){
				String[] splitContenut = contenueBrut.split("--",2);
				if(splitContenut.length==2) {
					return splitContenut[1].replace(",", "").replace(".", "");
				}
			}
			
		//cas particulier 
			else if(contenueBrut.contains("lunam")){
				String[] splitContenut = contenueBrut.split("lunam",2);
				if(splitContenut.length==2) {
					return splitContenut[1];
				}
			}
		}else {
			if(contenueBrut.contains("-----Message d'origine-----")) {
				String[] splitContenut = contenueBrut.split("-----Message d'origine-----",2);
				if(splitContenut.length==2) {
					if (splitContenut[0].contains("Francis YGUEL")) {
						//System.out.println("test "+  splitContenut[0]);
						String[] SplitFinale = splitContenut[0].split("Francis YGUEL");
						if(SplitFinale.length==2) {
							return SplitFinale[1].replace(",", "").replace(".", "");
						}	
					}
					if (splitContenut[0].contains("Cordialement")) {
						String[] SplitFinale = splitContenut[0].split("Cordialement");
						if(SplitFinale.length==2) {
							return SplitFinale[1].replace(",", "").replace(".", "");
						}	
					}
				}
			}
			if(contenueBrut.contains(">") && contenueBrut.contains("a écrit :") ) {
				String[] splitContenut = contenueBrut.split(">",2);

				if(splitContenut[0].contains("cordialement")) {
					String[] splitSignatureEtSuite = splitContenut[0].split("cordialement",2); 
					String[] SplitFinale = splitSignatureEtSuite[1].split("Le"); 
					
					return SplitFinale[0];
				}
				else if(splitContenut[0].contains("Cordialement")) {

					String[] splitSignatureEtSuite = splitContenut[0].split("Cordialement",2); 
					return splitSignatureEtSuite[1];
				}
			}
			if(contenueBrut.contains("Envoyé ") && contenueBrut.contains("Objet :")  && contenueBrut.contains("De :") ) {
				String[] splitContenut = contenueBrut.split("De :",2);
				if(splitContenut[0].contains("cordialement")) {
					String[] splitSignatureEtSuite = splitContenut[0].split("cordialement",2); 
					String[] SplitFinale = splitSignatureEtSuite[1].split("Le"); 
					return SplitFinale[0];
				}
				if(splitContenut[0].contains("lunam")) {
					String[] splitSignatureEtSuite = splitContenut[0].split("lunam",2); 
					return splitSignatureEtSuite[1];
				}
			}

		}
			
	
		return null;
		
		
	}
	/**
	 * recuperation des piéce jointe 
	 * recreation des piéce joint  
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */

	public ArrayList<PieceJointe> getPieceJointe() throws MessagingException, IOException{
		ArrayList<PieceJointe> attachments = new ArrayList<PieceJointe>(); 
		//pour les mails de type multupart avec ou sans piéce joint piéce joint
	    if(this.message.isMimeType("multipart/*")){  
	    	Multipart mp = (Multipart)this.message.getContent();
	    	int numParts = mp.getCount();   
	        for(int count = 0; count < numParts; count++)
	        {	    
	        	MimeBodyPart part = (MimeBodyPart)mp.getBodyPart(count);
	            //pour les piéce jointe on te recupere juste pour l'instant quel traiment faire ? 
	            if(MimeBodyPart.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
	            	PieceJointe piece = new PieceJointe(); 
	            	//Mail mail = new Mail();
	            	String nomPiceJointe="sansNom";
	            	
	            	if (part.getFileName()!=null) {
	            		nomPiceJointe="";
	            		nomPiceJointe = MimeUtility.decodeText(part.getFileName());
	            		
	            	}
						            	
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
	                FileInputStream inputStream = new FileInputStream(dossierAttachement+nomPiceJointe);
	                
	            	piece.setNomPieceJointe(nomPiceJointe);
	            	piece.setMailId(messageID);
	            	piece.setContenuJointe(inputStream); 
	            	attachments.add(piece); 
	            }	            	
	        }
	    }
	    
		return attachments;
	}
	
	public ArrayList<Lien>  getLiens() throws IOException, MessagingException {
		ArrayList<Lien> liens = new ArrayList<Lien>(); 
		String content = this.message.getContent().toString(); 
		if(this.message.isMimeType("text/html" )) {
			org.jsoup.nodes.Document doc=  Jsoup.parse(content);
			Elements links = doc.select("a[href]");
			for (Element a : links) {
				Lien lien = new Lien(); 
				lien.setNomLien(a.text());  
				lien.setContenuLien(a.attr("href"));
				lien.setIdMail(this.GetMessageId());
				
				liens.add(lien); 
			}
	
		}
		else if(this.message.isMimeType("multipart/*" )) {
			Multipart mp = (Multipart)this.message.getContent();
			int numParts = mp.getCount();
			           
			for(int count = 0; count < numParts; count++)
			{	    
				MimeBodyPart part = (MimeBodyPart)mp.getBodyPart(count);
			    //pour les parts qui sont de type content text.html on utlise la libraire jsoup  
			    if(part.isMimeType("text/html")) {
			    	
					org.jsoup.nodes.Document doc=  Jsoup.parse(part.getContent().toString());
					Elements links = doc.select("a[href]");
					for (Element a : links) {
						Lien lien = new Lien(); 
						lien.setNomLien(a.text());  
						lien.setContenuLien(a.attr("href"));
						lien.setIdMail(this.GetMessageId());
						
						liens.add(lien); 
					}
			    }    
			}
		}
		return liens ; 
	}

	
	public void getMailTest() throws MessagingException, IOException {
		System.out.println("************************Type Message********************");
		System.out.println(this.getTypeMessage()); 
		System.out.println("************************MessageID********************");
		System.out.println(this.GetMessageId()); 	
		System.out.println("************************expéditeur********************");		
		System.out.println(this.getExpediteur().toString());
		System.out.println("************************Destinataire********************");		
		ArrayList<Personne>  destinatire = this.getDestinataire(Message.RecipientType.TO);
		for (Personne a : destinatire) {
			System.out.println(a.toString());
		}
		System.out.println("************************en copie********************");
		Address[] test = this.message.getRecipients(Message.RecipientType.CC); 
		if(test==null) {
			System.out.println("pas de personne en copie ");
		}else 
		{
			System.out.println(test.length);
			ArrayList<Personne>  destinatireCC = this.getDestinataire(Message.RecipientType.CC);
			for (Personne a : destinatireCC) {
				System.out.println(a.toString());
			}
		}
		System.out.println("************************subject********************");
		System.out.println("sujet: "+this.GetSubject());
		System.out.println("************************BODY********************");
		System.out.println(this.GetMailContenu()); 
		System.out.println("************************attachement********************");
		ArrayList<PieceJointe> arrayPiece=  this.getPieceJointe();
		for(PieceJointe p : arrayPiece) {
			System.out.println(p.toString());
		}
		System.out.println("************************Signature********************");
		System.out.println(this.getSignature());
		System.out.println("************************Liens ********************");
		ArrayList<Lien> liens=  this.getLiens();
		for(Lien p : liens) {
			System.out.println(p.toString());
		}
		System.out.println("************************ BODY cleanned ********************");
		System.out.println(this.getContenueCleaned());
	}
	
	/**
	 * Returns a list with all links contained in the input
	 */
	public static ArrayList<String> extractUrls(String text)
	{
		ArrayList<String> containedUrls = new ArrayList<String>();
	    String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
	    Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
	    Matcher urlMatcher = pattern.matcher(text);

	    while (urlMatcher.find())
	    {
	        containedUrls.add(text.substring(urlMatcher.start(0),
	                urlMatcher.end(0)));
	    }

	    return containedUrls;
	}
	public String getContenueCleaned() throws MessagingException, IOException {
		
		String withCharacters  = this.GetMailContenu().replace('\u0092','\'').replace('\u0095','-').replace('\u0096','à'); 
		String contenueBruttt = StringEscapeUtils.unescapeHtml(withCharacters).replace(">", "").replace("<", "");
		//String contenueBruttt = StringEscapeUtils.
		String contenueBrut = Jsoup.parse(contenueBruttt).text();
		
		
		String contenuAnglais= "Contenu en anglais"; 

		
		ArrayList<String>  liens =  extractUrls(contenueBrut) ;  
		
		//suprresion des liens 
		for (String lien : liens) {
			//System.out.println(a.getContenuLien().substring(0, 40));
			if(contenueBrut.contains(lien)) {
	
				contenueBrut= contenueBrut.replace(lien, "");
			}		 
		}
		
		if(this.getTypeMessage()=="Simple") {
			if(contenueBrut.contains("Cordialement")){
				String[] splitContenut = contenueBrut.split("Cordialement");
				if (contenueAnglais(splitContenut[0])) {
					return contenuAnglais; 
				}
				else 
					return splitContenut[0];
			}
			else if(contenueBrut.contains("cordialement")){
				String[] splitContenut = contenueBrut.split("cordialement");
				if (contenueAnglais(splitContenut[0])) {
					return contenuAnglais; 
				}
				else 
					return splitContenut[0];
			}
			else if(contenueBrut.contains("Bien sincèrement")){
				String[] splitContenut = contenueBrut.split("Bien sincèrement");
				if (contenueAnglais(splitContenut[0])) {
					return contenuAnglais; 
				}
				else 
					return splitContenut[0];
			}
			else if(contenueBrut.contains("Sincèrement")){
				String[] splitContenut = contenueBrut.split("Sincèrement");
				if (contenueAnglais(splitContenut[0])) {
					return contenuAnglais; 
				}
				else 
					return splitContenut[0];
			}
			else if(contenueBrut.contains("Bonne réception")){
				String[] splitContenut = contenueBrut.split("Bonne réception");
				if (contenueAnglais(splitContenut[0])) {
					return contenuAnglais; 
				}
				else 
					return splitContenut[0].replace(",", "");
			}
			else if(contenueBrut.contains("salutations")){
				String[] splitContenut = contenueBrut.split("salutations");
				if (contenueAnglais(splitContenut[0])) {
					return contenuAnglais; 
				}
				else 
					return splitContenut[0].replace(",", "");
			}
			else if(contenueBrut.contains("Respectueusement")){
				String[] splitContenut = contenueBrut.split("Respectueusement");
				if (contenueAnglais(splitContenut[0])) {
					return contenuAnglais; 
				}
				else 
					return splitContenut[0];
			}
			else if (contenueBrut.contains("--")){
				String[] splitContenut = contenueBrut.split("--", 2 );
				if (contenueAnglais(splitContenut[0])) {
					return contenuAnglais; 
				}
				else 
					return splitContenut[0];
			}
			//cas particulier  de lunam 
			else if(contenueBrut.contains("lunam")){
				String[] splitContenut = contenueBrut.split("lunam",2);
				if(splitContenut.length==2) {
					if (contenueAnglais(splitContenut[0])) {
						return contenuAnglais; 
					}
					else 
						return splitContenut[0];
				}
			}
			else if (contenueBrut.contains("Evelyne PUGLIA")){
				String[] splitContenut = contenueBrut.split("Evelyne PUGLIA", 2 );
				if (contenueAnglais(splitContenut[0])) {
					return contenuAnglais; 
				}
				else 
					return splitContenut[0];
			}
			if (contenueAnglais(contenueBrut)) {
				return contenuAnglais; 
			}
			else 
				return contenueBrut;
		
			//contenur multi 
		}else {
			if(contenueBrut.contains("-----Message d'origine-----")) {
				String[] splitContenut = contenueBrut.split("-----Message d'origine-----",2);
				if(splitContenut.length==2) {
					if (splitContenut[0].contains("Francis YGUEL")) {
						//System.out.println("test "+  splitContenut[0]);
						String[] SplitFinale = splitContenut[0].split("Francis YGUEL");
						if(SplitFinale.length==2) {
							if (contenueAnglais(SplitFinale[0])) {
								return contenuAnglais; 
							}
							else 
								return SplitFinale[0];
						}	
					}
					if (splitContenut[0].contains("Evelyne PUGLIA")) {
						//System.out.println("test "+  splitContenut[0]);
						String[] SplitFinale = splitContenut[0].split("Evelyne PUGLIA");
						if(SplitFinale.length==2) {
							if (contenueAnglais(SplitFinale[0])) {
								return contenuAnglais; 
							}
							else 
								return SplitFinale[0];
						}	
					}
					if (splitContenut[0].contains("Cordialement")) {
						String[] SplitFinale = splitContenut[0].split("Cordialement");
						if(SplitFinale.length==2) {
							if (contenueAnglais(SplitFinale[0])) {
								return contenuAnglais; 
							}
							else 
								return SplitFinale[0];
						}	
					}
				}
			}
			if(contenueBrut.contains(">") && contenueBrut.contains("a écrit :") ) {
				String[] splitContenut = contenueBrut.split(">",2);
				if(splitContenut[0].contains("cordialement")) {
					String[] splitSignatureEtSuite = splitContenut[0].split("cordialement",2);
					if (contenueAnglais(splitSignatureEtSuite[0])) {
						return contenuAnglais; 
					}
					else 
						return splitSignatureEtSuite[0];  
				}
				else if(splitContenut[0].contains("Cordialement")) {
					String[] splitSignatureEtSuite = splitContenut[0].split("Cordialement",2); 
					if (contenueAnglais(splitSignatureEtSuite[0])) {
						return contenuAnglais; 
					}
					else 
						return splitSignatureEtSuite[0]; 
				}
			}
			if(contenueBrut.contains("Envoyé ") && contenueBrut.contains("Objet :")  && contenueBrut.contains("De :") ) {
				String[] splitContenut = contenueBrut.split("De :",2);
				if(splitContenut[0].contains("cordialement")) {
					String[] splitSignatureEtSuite = splitContenut[0].split("cordialement",2); 
					if (contenueAnglais(splitSignatureEtSuite[0])) {
						return contenuAnglais; 
					}
					else 
						return splitSignatureEtSuite[0]; 
				}
				if(splitContenut[0].contains("lunam")) {
					String[] splitSignatureEtSuite = splitContenut[0].split("lunam",2); 
					if (contenueAnglais(splitSignatureEtSuite[0])) {
						return contenuAnglais; 
					}
					else 
						return splitSignatureEtSuite[0]; 
				}
			}
			
		}
		
		return null;
	}
	
	public boolean contenueAnglais(String contenu) throws UnsupportedEncodingException, MessagingException {
		//verification si le contenu est en anglais 
		if ( contenu.contains("Universities") || contenu.contains("Dear") || contenu.contains("I am")  || contenu.contains("will") || contenu.contains("your") || contenu.contains("that")  ) {
			return true;
		}
		else
			return false ; 
	}
	/**
	 * fonction qui retourne la date d'envoie 
	 * @throws MessagingException 
	 */
	public String getDate() throws MessagingException {
		String date ="no date"; 
		if(this.message.getSentDate()!=null) {
			date= this.message.getSentDate().toString();
		}
	
		return date;

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
		
		mailObject.setTypeemail(this.getTypeMessage());
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
		mailObject.setContenuePropre(this.getContenueCleaned());
		mailObject.setLiens(this.getLiens());
	 //   MailList a = new MailList(message.getMessageID(),from, destinataire, message.getSubject(), body, message.getSentDate().toString(), attachments, liens);
	     return mailObject;   
	}  
	
	private static  String getTextFromMessage(Message message) throws MessagingException, IOException {
	    String result = "";
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

	private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException  {
	    String result = "";
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        try {
				if (bodyPart.isMimeType("text/plain")) {			
				    result = result + "\n" + bodyPart.getContent().toString();
				    break; //Sans break affiche deux fois 
				} else if (bodyPart.isMimeType("text/html")) {
				    String html =  MimeUtility.decodeText(bodyPart.getContent().toString());
					result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
				} else if (bodyPart.getContent() instanceof MimeMultipart){
				    result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
				}
			} catch (MessagingException | IOException e) {
				// TODO Auto-generated catch block
				//System.out.println("Exception dans getTextFromMimeMultipart du a la gesti des iso 8859-16");
				e.getSuppressed();
				//e.printStackTrace();
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
	/*
	private static String getSignatureFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException  {
	    String result = "";
	  
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	
	        try {
				if (bodyPart.isMimeType("text/html")) {
					org.jsoup.nodes.Document doc=  Jsoup.parse(bodyPart.getContent().toString());
					Elements a =doc.getElementsByTag("pre");
					Elements b = doc.getElementsByClass("moz-signature");

					//System.out.println("je suis null"+  b);
				    result = a.text()+b.text();
				}		

				else if (bodyPart.getContent() instanceof MimeMultipart){
				    result =  getSignatureFromMimeMultipart((MimeMultipart)bodyPart.getContent());
				}
			} catch (MessagingException | IOException e) {
				// TODO Auto-generated catch block
				e.getSuppressed(); 
				//e.printStackTrace();
			}

	    }
	    return result;
	}
	*/
	/**
	 * fontion qui prend en parametre un chaine de caratére ou juste un mail et qui 
	 * retourne une personne (nom, prenom , mail) 
	 * @param e
	 * @return
	 * @throws UnsupportedEncodingException
	 */

	public  Personne stringToPersonne(String e) throws UnsupportedEncodingException  {
		Personne pers = new Personne(); 
		Institution institution = new Institution(); 
		
		//decodage systematique des ISO-8859-1
		String element = MimeUtility.decodeText(e);
		//les cas ou le mail a des destinataires non divulgués  destinataires inconnus:
		if (element.contains("Undisclosed") || element.contains("undisclosed")  || element.contains("destinataires inconnus:")  || element.contains("recipient list not shown:")  ) {
		    pers.setNomPersonne("destinataires non divulgués");
		    pers.setPrenomPersonne("destinataires non divulgués");
		    pers.setEmailPersonne("destinataires non divulgués");
		    pers.setInstitutionPersonne(institution);
		}
		else {
			//supressiont de tous les caractère 
			String elementNew= element.replace("<", "").replace(">", "").replace("'", "").replace("[", "");
			element.replace("]", "").replace("\\", "").replace("//", "").replace("\"", ""); 
			String[] textSplited= elementNew.split(" ");
		    //si le text est =3 on a un format prenom, non et email 
		    if(textSplited.length==3) {
			    String prenom = textSplited[0];
			    String nom = textSplited[1].toUpperCase();
			    String email = textSplited[2];
			    String nomInsitution = InsitutionEmailSplit(email); 
			    institution.setNomInstitution(nomInsitution);
			    
			    pers.setNomPersonne(nom);
			    pers.setPrenomPersonne(prenom);
			    pers.setEmailPersonne(email);
			    pers.setInstitutionPersonne(institution);
		    }
		    //on a juste le mail  gestion des case nom et prénom 
		    else {
	    		String email = textSplited[textSplited.length-1];
			    String nomInsitution = InsitutionEmailSplit(email); 
			    institution.setNomInstitution(nomInsitution);
			    
		    	pers.setEmailPersonne(email);
		    	pers.setNomPersonne(nomEmailSplit(email));
		    	pers.setPrenomPersonne(prenomEmailSplit(email));
			    pers.setInstitutionPersonne(institution);
		    	
		    	
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
	
	/**
	 * fonction qui retourne l'instution  et qui prend en paramtre en email 
	 * @param email
	 * @return
	 */
	public String InsitutionEmailSplit(String email) {
		String institution=""; 
		if (email.contains("@")) {
	    	String[] emailSplited = email.split("@"); // exe francoi.calsel @ up.fr
	    	if(emailSplited.length>0) {
		    	String[] partie2 = emailSplited[1].split("\\.");     // separation du nom et prenom ex francois.cassel 
		    	if(partie2.length>1){
			    	if(partie2.length==2) {
			    		institution= partie2[0]; 
			    	}
			    	if(partie2.length==3) {
			    		institution =  partie2[0]+"."+partie2[1]; 
			    	}
		    	}
	    	}
		}
    	return institution; 
	}
}
