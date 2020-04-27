import java.util.ArrayList;

import javax.mail.internet.MimeBodyPart;

public class MailList {
	private String idMail;
	private String from; 
	private ArrayList<String> destinataire;  
	private String sujet; 
	private String Body; 
	private String date; 
	private ArrayList<MimeBodyPart> attachments;
	private ArrayList<lien> liens;
	private String signature; 
	
	public MailList(String idMail,String from, ArrayList<String> destinataire, String sujet, String body, String date,
			ArrayList<MimeBodyPart> attachments, ArrayList<lien> liens) {
    this.idMail = idMail;
		this.from = from;
		this.destinataire = destinataire;
		this.sujet = sujet;
		Body = body;
		this.date = date;
		this.attachments = attachments;
		this.liens = liens;
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
	public ArrayList<lien> getLiens() {
		return liens;
	}
	public void setLiens(ArrayList<lien> liens) {
		this.liens = liens;
	}
	
	
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	@Override
	public String toString() {
		return "MailList [from=" + from + ", destinataire=" + destinataire + ", sujet=" + sujet + ", Body=" + Body
				+ ", date=" + date + ", attachments=" + attachments + ", liens=" + liens + "]";
	}
	
	
	
	


}
