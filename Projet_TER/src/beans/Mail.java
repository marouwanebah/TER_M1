package beans;


public class Mail {
	private String idMail;
	private Email expediteurPrincipal;
	private String sujetMail;
	private String dateEnvoiMail;
	private String contenuMail; 
	private Mail mailPere;
	private String typeMail;
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
	public Email getExpediteur() {
		return expediteurPrincipal;
	}
	public void setExpediteur(Email expediteur) {
		this.expediteurPrincipal = expediteur;
	}
	public Mail getMailPere() {
		return mailPere;
	}
	public void setMailPere(Mail mailPère) {
		this.mailPere = mailPère;
	}
	public String getTypeMail() {
		return typeMail;
	}
	public void setTypeMail(String typeMail) {
		this.typeMail = typeMail;
	}
	
}
