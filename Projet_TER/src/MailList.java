import java.util.ArrayList;


import beans.Personne;
import beans.PieceJointe;

public class MailList {
	private String idMail;
	private Personne from; 
	private ArrayList<Personne> destinataire;  
	private ArrayList<Personne> destinataireEnCopie;
	private String sujet; 
	private String Body; 
	private String date; 
	private ArrayList<PieceJointe> attachments;
	//private ArrayList<Lien> liens;
	private String signature;

	public MailList() {
		
	}
	
	public String getIdMail() {
		return idMail;
	}
	public void setIdMail(String idMail) {
		this.idMail = idMail;
	}
	public Personne getFrom() {
		return from;
	}
	public void setFrom(Personne from) {
		this.from = from;
	}
	public ArrayList<Personne> getDestinataire() {
		return destinataire;
	}
	public void setDestinataire(ArrayList<Personne> destinataire) {
		this.destinataire = destinataire;
	}
	public ArrayList<Personne> getDestinataireEnCopie() {
		return destinataireEnCopie;
	}
	public void setDestinataireEnCopie(ArrayList<Personne> destinataireEnCopie) {
		this.destinataireEnCopie = destinataireEnCopie;
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
	public ArrayList<PieceJointe> getAttachments() {
		return attachments;
	}
	public void setAttachments(ArrayList<PieceJointe> attachments) {
		this.attachments = attachments;
	}
	/*
	public ArrayList<Lien> getLiens() {
		return liens;
	}
	public void setLiens(ArrayList<Lien> liens) {
		this.liens = liens;
	}
	*/
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	@Override
	public String toString() {
		return "MailList [idMail=" + idMail + ", from=" + from + ", destinataire=" + destinataire
				+ ", destinataireEnCopie=" + destinataireEnCopie + ", sujet=" + sujet + ", Body=" + Body + ", date="
				+ date + ", attachments=" + attachments + ", liens=" + "" + ", signature=" + signature + "]";
	} 
	
	
	
	
	


}
