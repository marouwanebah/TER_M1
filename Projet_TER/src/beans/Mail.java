package beans;

import java.util.ArrayList;

public class Mail {
	private String idMail;
	private Personne expediteur;
	private ArrayList<Personne> destinaire;
	private String sujetMail;
	private String dateEnvoiMail;
	private String contenuMail;
	private ArrayList<PieceJointe> pieceJoints; 
	
	
	private Mail mailPère;
	public Mail() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getIdMail() {
		return idMail;
	}
	public void setIdMail(String idMail) {
		this.idMail = idMail;
	}
	public String getDateEnvoiMail() {
		return dateEnvoiMail;
	}
	public void setDateEnvoiMail(String dateEnvoiMail) {
		this.dateEnvoiMail = dateEnvoiMail;
	}
	public String getContenuMail() {
		return contenuMail;
	}
	public void setContenuMail(String contenuMail) {
		this.contenuMail = contenuMail;
	}
	public String getSujetMail() {
		return sujetMail;
	}
	public void setSujetMail(String sujetMail) {
		this.sujetMail = sujetMail;
	}
	public Personne getExpediteur() {
		return expediteur;
	}
	public void setExpediteur(Personne expediteur) {
		this.expediteur = expediteur;
	}
	public Mail getMailPère() {
		return mailPère;
	}
	public void setMailPère(Mail mailPère) {
		this.mailPère = mailPère;
	}
	
}
