import java.util.ArrayList;

import javax.mail.internet.MimeBodyPart;

public class MailList {
	private String idMail;
	private String from; 
	private ArrayList<String> destinataire;  
	private String sujet; 
	private String Body; 
	private String date; 
	ArrayList<MimeBodyPart> attachments;
	
	public MailList(String idMail, String from, ArrayList<String> destinataire, String sujet, String body, String date, ArrayList<MimeBodyPart> attachments) {
		super();
		this.idMail = idMail;
		this.destinataire=destinataire; 
		this.from = from;
		this.sujet = sujet;
		this.Body = body;
		this.date = date;
		this.attachments = attachments;
	}
	
	public String getIdMail() {
		return idMail;
	}

	public void setIdMail(String idMail) {
		this.idMail = idMail;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public ArrayList<String> getDestinataire() {
		return destinataire;
	}

	public void setDestinataire(ArrayList<String> destinataire) {
		this.destinataire = destinataire;
	}

	public String getSujet() {
		return sujet;
	}

	public void setSujet(String sujet) {
		this.sujet = sujet;
	}

	public String getBody() {
		return Body;
	}

	public void setBody(String body) {
		Body = body;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ArrayList<MimeBodyPart> getAttachments() {
		return attachments;
	}

	public void setAttachments(ArrayList<MimeBodyPart> attachments) {
		this.attachments = attachments;
	}

	@Override
	public String toString() {
		return "MailList [from=" + from + ", destinataire=" + destinataire + ", sujet=" + sujet + ", Body=" + Body
				+ ", date=" + date + ", attachments=" + attachments + "]";
	}


	


}
